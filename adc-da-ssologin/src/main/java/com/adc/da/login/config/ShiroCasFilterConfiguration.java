package com.adc.da.login.config;

import com.adc.da.login.MyCasFilter;
import com.adc.da.login.ShiroUserFilter;
import com.adc.da.util.constant.GlobalConfig;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro 权限配置
 * 设置各连接的权限需求
 */
@Configuration
public class ShiroCasFilterConfiguration {

//	private static final String casFilterUrlPattern = "/shiro-cas";

    /**
     * 权限认证失败跳转地址
     */
    private static final String UNAUTHORIZED_URL = "/api/login";

    private static final String LOGIN = "/login";

    /**
     * 认证用户，需要登录才能使用的接口
     */
    private static final String AUTHC = "authc";

    /**
     * 匿名用户，无需登录
     */
    private static final String ANON = "anon";

    @Autowired
    private Environment env;

    /**
     * 初始化，设置adminPath和restPath
     * 参数在 application.properties 下
     * 预设 adminPath = /a
     * 预设 restPath =/api
     */
    @PostConstruct
    public void init() {
        GlobalConfig.setAdminPath(env.getProperty("adminPath"));
        GlobalConfig.setRestApiPath(env.getProperty("restPath"));
    }

    /* 登录成功地址 */
    public static final String LOGIN_SUCCESS_URL = "/api/suc";

    /**
     * CAS过滤器
     */
    @Bean(name = "casFilter")
    public CasFilter getCasFilter(@Value("${shiro.cas}") String casServerUrlPrefix,
        @Value("${shiro.server}") String shiroServerUrlPrefix) {
        CasFilter casFilter = new MyCasFilter();
        casFilter.setName("casFilter");
        casFilter.setEnabled(true);
        // 登录失败后跳转的URL，也就是 Shiro 执行 CasRealm 的 doGetAuthenticationInfo 方法向CasServer验证tiket
        String loginUrl =
            casServerUrlPrefix + "/login?service=" + shiroServerUrlPrefix + ShiroCasConfiguration.CAS_FILTER_URL_PATTERN;
        casFilter.setFailureUrl(loginUrl);
        // 我们选择认证失败后再打开登录页面
        casFilter.setLoginUrl(loginUrl);
        return casFilter;
    }

    /**
     * 使用工厂模式，创建并初始化ShiroFilter
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager,
        CasFilter casFilter,
        @Value("${shiro.cas}") String casServerUrlPrefix,
        @Value("${shiro.server}") String shiroServerUrlPrefix) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        String loginUrl =
            casServerUrlPrefix + "/login?service=" + shiroServerUrlPrefix + ShiroCasConfiguration.CAS_FILTER_URL_PATTERN;
        shiroFilterFactoryBean.setLoginUrl(loginUrl);
        /*
         *  登录成功后要跳转的连接，不设置的时候，会默认跳转到前一步的url
         *  比如先在浏览器中输入了http://localhost:8080/userlist,但是现在用户却没有登录，于是会跳转到登录页面，等登录认证通过后，
         *  页面会再次自动跳转到http://localhost:8080/userlist页面而不是登录成功后的index页面
         *  建议不要设置这个字段
         */
        shiroFilterFactoryBean.setSuccessUrl(LOGIN_SUCCESS_URL);

        // 设置无权限访问页面
        shiroFilterFactoryBean.setUnauthorizedUrl(UNAUTHORIZED_URL);
        /*
         *  添加casFilter到shiroFilter中，注意，casFilter需要放到shiroFilter的前面，
         *  从而保证程序在进入shiro的login登录之前就会进入单点认证
         */
        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("userFilter", new ShiroUserFilter());
        filters.put("cassuc", casFilter);

        // logout已经被单点登录的logout取代
        shiroFilterFactoryBean.setFilters(filters);

        loadShiroFilterChain(shiroFilterFactoryBean);
        return shiroFilterFactoryBean;
    }

    /**
     * 加载shiroFilter权限控制规则（从数据库读取然后配置）,角色/权限信息由MyShiroCasRealm对象提供doGetAuthorizationInfo实现获取来的
     * 生产中会将这部分规则放到数据库中
     */
    private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean) {

        String adminPath = GlobalConfig.getAdminPath();
        String restPath = GlobalConfig.getRestApiPath();

        /////////////////////// 注意，此处加入的filter需要保证有序，所以用的LinkedHashMap ///////////////////////
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        filterChainDefinitionMap.put(ShiroCasConfiguration.CAS_FILTER_URL_PATTERN, "cassuc");

        // 不拦截的请求
        filterChainDefinitionMap.put(restPath + LOGIN, ANON);

        // 此处将logout页面设置为anon，而不是logout，因为logout被单点处理，而不需要再被shiro的logoutFilter进行拦截
        /* 登出不需认证 */
        filterChainDefinitionMap.put(restPath + "/logout", ANON);

        /* 验证码，不需要认证 */
        filterChainDefinitionMap.put(restPath + "/verifyCode", ANON);

        /* 验证码预校验，不需要认证 */
        filterChainDefinitionMap.put(restPath + "/judgeVerify", ANON);

        /* 用户信息不用认证 */
        filterChainDefinitionMap.put(restPath + "/userInfo", ANON);

        /* 在线用户列表不用验证 */
        filterChainDefinitionMap.put(restPath + "/onlineUser", ANON);

        /* */
        filterChainDefinitionMap.put(restPath + "/sys/sync/**", ANON);
//        filterChainDefinitionMap.put(restPath + "/sys/org/getOrgByName/**", ANON);
//        filterChainDefinitionMap.put(restPath + "/sys/user", ANON);

        /* 用户所属菜单信息不需认证 */
        filterChainDefinitionMap.put(restPath + "/userMenu", ANON);
        filterChainDefinitionMap.put(restPath + "/updateUserInfo", ANON);
        filterChainDefinitionMap.put(restPath + "/updatePassword", ANON);
        filterChainDefinitionMap.put(restPath + "/user/supplierRegistry/*", ANON);

        filterChainDefinitionMap.put(restPath + "/**", "userFilter");
//        filterChainDefinitionMap.put(ShiroCasConfiguration.casFilterUrlPattern, "cassuc");
//        filterChainDefinitionMap.put(restPath+"/**", AUTHC);

        /* static 不需登录 */
        filterChainDefinitionMap.put("/static/**", ANON);

        /* userfiles 用户文件 不需登录 */
        filterChainDefinitionMap.put("/userfiles/**", ANON);
        filterChainDefinitionMap.put(adminPath + LOGIN, AUTHC);
        filterChainDefinitionMap.put(adminPath + "/logout", "logout");
        filterChainDefinitionMap.put(adminPath + "/**", "user");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    }
}
