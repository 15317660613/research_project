package com.adc.da.login.rest;

import com.adc.da.login.config.ShiroCasConfiguration;
import com.adc.da.login.entity.OnlineUserEO;
import com.adc.da.login.security.validatecode.IVerifyCodeGen;
import com.adc.da.login.security.validatecode.SimpleCharVerifyCodeGenImpl;
import com.adc.da.login.security.validatecode.VerifyCode;
import com.adc.da.login.service.OnlineUserListener;
import com.adc.da.login.util.UserUtils;
import com.adc.da.login.vo.OnlineUserVO;
import com.adc.da.sys.entity.MenuEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.sys.vo.UserVO;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.IpUtil;
import com.adc.da.util.utils.RequestUtils;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 跳转至cas server去登录（一个入口）
 */
@Controller
@RequestMapping(value = "${restPath}/")
@Api(tags = "CAS登录接口")
public class CasLoginController {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(CasLoginController.class);

    /**
     * 无缓存，用于校验
     */
    private static final String NO_CACHE = "no-cache";

    /**
     * 服务层装配
     *
     * @see UserEOService
     */
    @Autowired
    private UserEOService userService;

    @Value("${shiro.cas}")
    private String casServerUrlPrefix;

    @Value("${shiro.server}")
    private String shiroServerUrlPrefix;

    @Value("${successCallBackUrl}")
    private String successCallBackUrl;

    /**
     * eo vo 转换
     *
     * @see BeanMapper
     * @see dozer
     */
    @Autowired
    BeanMapper beanMapper;

    /**
     * 读取配置文件判断是否需要开启Base64加密
     * 默认值为false
     */
    @Value("${isPassEncrypted:false}")
    private boolean isPassEncrypted;

    //    @ApiOperation(value = "根路径跳转至登录")
    @RequestMapping("/suc")
    public String home(Model model, HttpServletRequest request) {
        return "redirect:" + successCallBackUrl;
    }

    //    @ApiOperation(value = "登录跳转")
    @GetMapping(value = "/login")
    public String login(Model model) {
        // Cas登录页面地址
        String casLoginUrl = casServerUrlPrefix + "/login";

        String loginUrl = casLoginUrl + "?service=" + shiroServerUrlPrefix + ShiroCasConfiguration.CAS_FILTER_URL_PATTERN;
        return "redirect:" + loginUrl;
    }

    /**
     * 退出登录
     */
//    @ApiOperation(value = "登出")
    @GetMapping(value = "/logout")
    public String logout() {
        try {
            SecurityUtils.getSubject().logout();
        } catch (UnavailableSecurityManagerException e) {
            logger.error("logout UnavailableSecurityManagerException", e);
        } catch (InvalidSessionException e) {
            logger.error("logout InvalidSessionException", e);
        } catch (Exception e) {
            logger.error("logout Exception", e);
        }
//		return "logout suc";
        // Cas登出页面地址
        String casLogoutUrl = casServerUrlPrefix + "/logout";
        String logoutUrl = casLogoutUrl + "?service=" + shiroServerUrlPrefix + "/api/login";
        return "redirect:" + logoutUrl;
    }

    @ApiOperation(value = "登陆成功后的跳转、业务操作")
    @GetMapping(value = "/index")
    @ResponseBody
    public ResponseMessage index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserEO userEO = (UserEO) request.getSession().getAttribute(RequestUtils.LOGIN_USER);

        UserEO returnEO = userService.getUserWithRoles(userEO.getUsid());

        request.getSession().setAttribute(RequestUtils.LOGIN_USER_ID, userEO.getUsid());
        request.getSession().setAttribute(RequestUtils.LOGIN_ROLE_ID, UserUtils.getRoleIds());

        // 记录登录在线人数
        OnlineUserEO onlineUserEO = new OnlineUserEO();
        onlineUserEO.setAccount(userEO.getUsname());
        onlineUserEO.setIp(IpUtil.getIpAddr(request));
        onlineUserEO.setLoginTime(new Date());

        final Serializable onlineUserListener = new OnlineUserListener(onlineUserEO);

        request.getSession(false).setAttribute(userEO.getUsname(), onlineUserListener);
        return Result.success(beanMapper.map(returnEO, UserVO.class));
    }

    /**
     * 4位验证码
     *
     * @param request  请求信息
     * @param response 返回信息
     */
    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse response) {
        verifyCode(request, response, 80, 28, 4);
    }

    /**
     * 6位验证码
     *
     * @param request  请求信息
     * @param response 返回信息
     */
    @GetMapping("/verifyCode6")
    public void verifyCode6(HttpServletRequest request, HttpServletResponse response) {
        verifyCode(request, response, 100, 28, 6);
    }

    /**
     * 生成验证码
     * 由 verifyCode6 和 verifyCode 调用
     *
     * @param request  请求信息
     * @param response 返回信息
     * @param width    宽度
     * @param height   高度
     * @param number   字符数
     * @author Lee Kwanho 李坤澔
     *     date 2018-09-06
     */
    private void verifyCode(HttpServletRequest request, HttpServletResponse response, int width, int height,
        int number) {
        IVerifyCodeGen iVerifyCodeGen = new SimpleCharVerifyCodeGenImpl();
        try {
            VerifyCode verifyCode = iVerifyCodeGen.generate(width, height, number);
            request.getSession().setAttribute("VerifyCode", verifyCode.getCode());
            response.setHeader("Pragma", NO_CACHE);
            response.setHeader("Cache-Control", NO_CACHE);
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            response.getOutputStream().write(verifyCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            logger.error("verifyCode Error", e);
        }
    }

    /**
     * 获取在线用户
     *
     * @param response 请求体
     * @return 当前在线用户列表
     */
    @ApiOperation(value = "获取在线用户")
    @GetMapping("/onlineUser")
    @ResponseBody
    public ResponseMessage<OnlineUserVO> onlineUser(HttpServletResponse response) {
        Map<String, OnlineUserEO> map = OnlineUserListener.getOnlineMap();
        List<OnlineUserEO> onlineUsers = new ArrayList<>(map.values());
        OnlineUserVO onlineUserVO = new OnlineUserVO();
        onlineUserVO.setOnlineUsers(onlineUsers);
        onlineUserVO.setTotal(onlineUsers.size());
        return Result.success(onlineUserVO);
    }

    /**
     * 登录成功之后获取当前登录用户信息的接口
     *
     * @param response 请求体
     * @return 当前用户信息
     */
    @ApiOperation(value = "获取登录用户信息")
    @GetMapping("/userInfo")
    @ResponseBody
    public ResponseMessage<UserVO> userInfo(HttpServletResponse response) {

        try {
            UserEO user = UserUtils.getUser();
            if (user != null) {
                String id = user.getUsid();
                /* 获取用户信息，角色信息，组织信息 */
                UserVO userVO = beanMapper.map(userService.getUserWithRoles(id), UserVO.class);
                return Result.success(userVO);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return Result.error("401", "未授权", null);
            }
        } catch (Exception e) {
            logger.error("获取登录用户信息", e);
            return Result.error("500", "获取登录用户信息" + e.getMessage(), null);
        }

    }

    /**
     * 获取用户菜单,已用菜单管理实现
     *
     * @return 菜单权限
     * @author Lee Kwanho 李坤澔
     *     date 2018-09-06
     * @see com.adc.da.sys.controller.MenuEOController
     * @deprecated 使用菜单管理替代
     */
    @ApiOperation(value = "获取登录用户菜单权限")
    @GetMapping("/userMenu")
    @ResponseBody
    @Deprecated
    public ResponseMessage<MenuEO> userMenu() {
        return Result.success(UserUtils.getMenuTree());
    }

    /**
     * 用于修改当前用户信息，与用户管理相似，但是不能修改他人信息（会强制替换为当前用户）
     *
     * @param response 请求体，获取登录信息
     * @param userVO   获取修改信息
     * @author Lee Kwanho 李坤澔
     *     date 2018-10-17
     */
    @ApiOperation(value = "修改当前用户信息")
    @PutMapping("/updateUserInfo")
    @ResponseBody
    public ResponseMessage updateUserInfo(HttpServletResponse response, @RequestBody UserVO userVO) {
        String updateTime = DateUtils.dateToString(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS_EN);
        userVO.setUpdateTime(updateTime);

        try {
            /*
             * 替换usid 为当前登录用户
             * 不能修改ID，账户account，用户名，以及工号
             */
            UserEO user = UserUtils.getUser();
            userVO.setUsid(user.getUsid());
            userVO.setAccount(user.getAccount());
            userVO.setUsname(user.getUsname());
            userVO.setUserCode(user.getUserCode());
            /* 更新返回用户角色信息/ 不能修改角色*/
            userService.updateByPrimaryKeySelective(beanMapper.map(userVO, UserEO.class));

            /* 更新头像信息 */
            if (StringUtils.isNotEmpty(userVO.getAvatar())) {
                userService.saveUserAvatar(userVO.getUsid(), userVO.getAvatar());
            }
            /* 返回值附带角色组织等信息 */
            return Result.success(beanMapper.map(userService.getUserWithRoles(user.getUsid()), UserVO.class));

        } catch (Exception e) {
            logger.error("修改当前用户信息 error", e);
            return Result.error("修改当前用户信息 error" + e.getMessage());
        }
    }

    /**
     * 用于修改密码,当前用户
     * 会进行校验
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 若成功修改则返回成功
     */
    @ApiOperation(value = "修改密码")
    @PutMapping("/updatePassword")
    @ResponseBody
    public ResponseMessage updatePassword(@NotNull(message = "请输入旧密码") @RequestParam String oldPassword,
        @NotNull(message = "请输入新密码") @RequestParam String newPassword) {
        // 前台如果base64传输密文，则需要解码
        if (isPassEncrypted) {
            oldPassword = new String(Encodes.decodeBase64(oldPassword), StandardCharsets.UTF_8);
            newPassword = new String(Encodes.decodeBase64(newPassword), StandardCharsets.UTF_8);
        }
        if (!newPassword.matches("^(?![0-9]*$)[a-zA-Z0-9]{6,10}$")) {
            return Result.error("r0018", "新密码必须6-10位且不能纯数字");
        }
        userService.updatePassword(UserUtils.getUserId(), oldPassword, newPassword);
        return Result.success();
    }

    /**
     * 登录成功清除失败记录
     *
     * @param userName 用户名
     * @author Dingqiang
     */
//    private void delLoginErrorCount(String userName) {
//        Map<String, Integer> loginFailMap = (Map<String, Integer>) CacheUtils.getErrorCache(LOGIN_FAIL_MAP);
//        /*
//         *  非空直接清除失败次数,没必要进行初始化等操作
//         *  @author Lee Kwanho 李坤澔
//         *  date 2019-01-30
//         */
//        if (loginFailMap != null) {
//            loginFailMap.put(userName, 0);
//        }
//
//    }

//    @ApiOperation(value = "CAS未登录测试")
    @GetMapping(value = "/test")
    @ResponseBody
    public ResponseMessage test(HttpServletRequest request) {
        return Result.success("Hi");
    }
}
