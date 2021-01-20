package com.adc.da.login.security;

import com.adc.da.login.entity.MyPrincipal;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.utils.RequestUtils;
import com.adc.da.util.utils.SpringContextHolder;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cas.CasAuthenticationException;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.cas.CasToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.StringUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 权限校验核心类; 由于使用了单点登录，所以无需再进行身份认证 只需要授权即可
 */
public class SystemCasRealm extends CasRealm {

    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(SystemCasRealm.class);

    private UserEOService userService;

    /**
     * 若userService为空，获取userService
     *
     * @return
     */
    public UserEOService getUserService() {
        if (userService == null) {
            userService = SpringContextHolder.getBean(UserEOService.class);
        }
        return userService;
    }

    /**
     * 1、CAS认证 ,验证用户身份
     * 2、将用户基本信息设置到会话中,方便获取
     * 3、该方法可以直接使用CasRealm中的认证方法，此处仅用作测试
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        CasToken casToken = (CasToken) token;
        if (token == null) {
            return null;
        }

        String ticket = (String) casToken.getCredentials();
        if (!StringUtils.hasText(ticket)) {
            return null;
        }

        TicketValidator ticketValidator = ensureTicketValidator();

        try {
            // contact CAS server to validate service ticket
            Assertion casAssertion = ticketValidator.validate(ticket, getCasService());
            // get principal, user id and attributes
            AttributePrincipal casPrincipal = casAssertion.getPrincipal();
            String userId = casPrincipal.getName();
            LOG.debug(
                "Validate ticket : {} in CAS server : {} to retrieve user : {}",
                ticket,
                getCasServerUrlPrefix(),
                userId);

            Map<String, Object> attributes = casPrincipal.getAttributes();
            // refresh authentication token (user id + remember me)
            casToken.setUserId(userId);
            String rememberMeAttributeName = getRememberMeAttributeName();
            String rememberMeStringValue = (String) attributes.get(rememberMeAttributeName);
            boolean isRemembered = rememberMeStringValue != null && Boolean.parseBoolean(rememberMeStringValue);
            if (isRemembered) {
                casToken.setRememberMe(true);
            }
            UserEO user = this.getUserService().getUserByLoginNameNotDeleted(userId);
            if (user == null) {
                return null;
            }
            SecurityUtils.getSubject().getSession().setAttribute(RequestUtils.LOGIN_USER, user);
            // create simple authentication info
            return new SimpleAuthenticationInfo(new MyPrincipal(user), ticket, getName());
        } catch (TicketValidationException e) {
            throw new CasAuthenticationException("Unable to validate ticket [" + ticket + "]", e);
        }
    }

    /**
     * 此方法调用 hasRole,hasPermission的时候才会进行回调.
     * <p>
     * 权限信息.(授权): 1、如果用户正常退出，缓存自动清空； 2、如果用户非正常退出，缓存自动清空；
     * 3、如果我们修改了用户的权限，而用户不退出系统，修改的权限无法立即生效。 （需要手动编程进行实现；放在service进行调用）
     * 在权限修改后调用realm中的方法，realm已经由spring管理，所以从spring中获取realm实例， 调用clearCached方法；
     * :Authorization 是授权访问控制，用于对用户进行的操作授权，证明该用户是否允许进行当前操作，如访问某个链接，某个资源文件等。
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        MyPrincipal principal = (MyPrincipal) getAvailablePrincipal(principals);

        // 根据用户名获取该用户的角色和权限信息
        // 将用户对应的角色和权限信息打包放到AuthorizationInfo中
        UserEO user = getUserService().getUserByLoginNameNotDeleted(principal.getLoginName());
        if (user != null) {
            try {
                return UserUtils.getAuthInfo();
            } catch (NumberFormatException e) {
                LOG.error("AuthorizationInfo NumberFormatException", e);
            } catch (Exception e) {
                LOG.error("AuthorizationInfo Exception", e);
            }
        } else {
            return null;
        }
        return null;
    }
}