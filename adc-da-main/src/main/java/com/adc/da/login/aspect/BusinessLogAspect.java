package com.adc.da.login.aspect;

import cn.hutool.core.codec.Base64;
import com.adc.da.ext.sys.log.entity.LogEOExt;
import com.adc.da.ext.sys.log.service.LogEOExtService;
import com.adc.da.log.entity.LogEO;
import com.adc.da.log.service.LogEOService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.IpUtil;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 用于记录调用service层日志，
 * 日志会记录在TS_LOG中
 *
 */
@Aspect
@Component
@Slf4j
public class BusinessLogAspect {

    /**
     * @see LogEOService
     */
    @Autowired
    private LogEOExtService logEOExtService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserEODao userEODao;

    /**
     * 读取配置文件
     * 系统日志类别
     *      dev:开发模式不拦截方法记日志,
     *      custom:客户自定义需要拦截记日志的方法,
     *      sys:系统原设需要拦截记日志的方法
     */
    @Value("${sysLogType}")
    private String sysLogType;

    /**
     * 匹配Service层的save, update, delete, get, find, page等方法
     */
//    @Pointcut(value = "(execution(* com.adc.da.*.service.impl.*.*(..)) "
//            + "|| execution(* com.adc.da.*.service.*.save*(..)) "
//            + "|| execution(* com.adc.da.*.service.*.update*(..)) "
//            + "|| execution(* com.adc.da.*.service.*.delete*(..)) "
//            + "|| execution(* com.adc.da.*.service.*.get*(..)) "
//            + "|| execution(* com.adc.da.*.service.*.find*(..)) "
//            + "|| execution(* com.adc.da.*.service.*.list*(..)) "
//            + "|| execution(* com.adc.da.*.service.*.query*(..)) "
//            + "|| execution(* com.adc.da.*.service.*.page*(..))) "
//            + "&& !execution(* com.adc.da.log.service.LogEOService.*(..))")
    @Pointcut(value = "execution(* com.adc.da.*.controller.*.*(..)) "
//            + "|| execution(* com.adc.da.*.service.*.*(..)) "
            + "&& !execution(* com.adc.da.log.service.LogEOService.*(..))")
    private void servicePointcut() {
        throw new UnsupportedOperationException("servicePointcut error");
    }

    @Around(value = "servicePointcut()")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        Class cls = joinPoint.getTarget().getClass();
        String signature = joinPoint.getSignature().getName();
        Object result;
        // 调用service层开始时间
        long startTime = System.currentTimeMillis();
        result = joinPoint.proceed();
        // 调用service层结束时间
        long endTime = System.currentTimeMillis();
        // 判断是否需求添加日志 appLog
        if (StringUtils.isEmpty(this.request.getHeader("applogcontent"))) {
            return result;
        }
        // dev模式下不记系统日志
       /* if (LogConstants.LOG_TYPE_DEV.equalsIgnoreCase(sysLogType)) {
            return result;
        }*/
        UserEO userEO = UserUtils.getUser();
        // 非登录模式下不记系统日志
        if (null == userEO) {
            return result;
        }
        try {
            String description = new String(Encodes.decodeBase64(this.request.getHeader("appLogContent")));
            String pageName = new String(Encodes.decodeBase64(this.request.getHeader("pagename")));
            // 业务日志组件 sys模式
//        if (LogConstants.LOG_TYPE_SYS.equalsIgnoreCase(sysLogType)) {
            writeLog(cls.getName(), signature, description, userEO, startTime, endTime, pageName);
        }catch (Exception e){
            log.error("日志参数解析失败"+e.fillInStackTrace());
        }
//        }
        return result;
    }
    @Pointcut(value = "execution(* com.adc.da.login.rest.*.*(..)) "
//            + "|| execution(* com.adc.da.*.service.*.*(..)) "
            + "&& !execution(* com.adc.da.log.service.LogEOService.*(..))")
    private void AfterPointcut() {
        throw new UnsupportedOperationException("AfterPointcut error");
    }
    @After(value = "AfterPointcut()")
    public void doAspect(JoinPoint joinPoint) throws Throwable {
        Class cls = joinPoint.getTarget().getClass();
        String signature = joinPoint.getSignature().getName();
        // 调用service层开始时间
        long startTime = System.currentTimeMillis();
        // 调用service层结束时间
        long endTime = System.currentTimeMillis();
        // 判断是否需求添加日志 appLog
//        if (StringUtils.isEmpty(this.request.getHeader("applogcontent"))) {
//            return result;
//        }
        UserEO userEO = UserUtils.getUser();
        // 非登录模式下不记系统日志
        if (null == userEO) {
            return;
        }
        String description = "登录";
        if (StringUtils.isNotEmpty(this.request.getHeader("applogcontent"))) {
            description = new String(Encodes.decodeBase64(this.request.getHeader("appLogContent")));
        }
        String pageName="登录页面";
        if (StringUtils.isNotEmpty(this.request.getHeader("pagename"))) {
            pageName = new String(Encodes.decodeBase64(this.request.getHeader("pagename")));
        }
        writeLog(cls.getName(), signature, description, userEO, startTime, endTime, pageName);
    }

    /**
     * 写日志
     *
     * @param className     类名
     * @param methodName    方法名
     * @param logAnnotation 描述
     * @param userEO        用户名
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param pageName      页面名称
     * @throws Exception
     */
    private void writeLog(String className, String methodName, String logAnnotation, UserEO userEO,
            long startTime, long endTime, String pageName) throws Exception {
        LogEOExt logEO = new LogEOExt();
        logEO.setId(UUID.randomUUID10());

        logEO.setClassName(className);
        logEO.setMethod(methodName);
        if (logAnnotation != null && !"".equals(logAnnotation)) {
            logEO.setDescription(logAnnotation);
        }
        // 写入ip信息
        logEO.setIpAddress(IpUtil.getIpAddr(request));
        if (userEO != null) {
            logEO.setUsid(userEO.getUsid());
            // 大中心项目，这里账号改存工号userCode
            logEO.setAccount(userEO.getAccount());
            logEO.setUserName(userEO.getUsname());
        }
        logEO.setStartTime(new Date(startTime));
        logEO.setEndTime(new Date(endTime));
        if (StringUtils.isNotEmpty(pageName)) {
            logEO.setPageName(pageName);
        }
        logEOExtService.insertSelective(logEO);
    }

    public static void main(String[] args) {
        String str = "哈哈哈";
        String strBase64 = Base64.encode(str);
        System.out.println(strBase64);
        String strBase64UrlEncode = URLEncoder.encode(strBase64);
        String strBase64UrlDecode = URLDecoder.decode(strBase64UrlEncode);
        System.out.println(strBase64UrlDecode);

        System.out.println(new String(Base64.decode(strBase64UrlDecode)));
    }


}
