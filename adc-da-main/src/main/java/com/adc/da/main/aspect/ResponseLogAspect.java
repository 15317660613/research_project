package com.adc.da.main.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于记录调用controller层返回日志
 *
 * @deprecated 切面存在转json异常，效果不理想进行废弃
 *     date 2019-07-31
 */
//@Aspect
//@Component
@Deprecated
public class ResponseLogAspect {
    private static Logger logger = LoggerFactory.getLogger(ResponseLogAspect.class);

//    /**
//     * 匹配controller层的方法
//     */
//    @Pointcut(value = "(execution(* com.adc.da.*.rest.*.*(..)))")
//    private void restPointcut() {
//        // 切面方法
//        throw new UnsupportedOperationException("controllerPointcut Error.");
//    }
//
//    /**
//     * 匹配controller层的方法
//     */
//    @Pointcut(value = "(execution(* com.adc.da.*.controller.*.*(..)))")
//    private void controllerPointcut() {
//        // 切面方法
//        throw new UnsupportedOperationException("controllerPointcut Error.");
//    }
//
//    @Around(value = "controllerPointcut()||restPointcut()")
//    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
//        Object result = joinPoint.proceed();
//        logger.info("==================== 调用Controller层返回json值 ====================");
//        logger.info(GsonUtil.toJson(result));
//        return result;
//    }
}
