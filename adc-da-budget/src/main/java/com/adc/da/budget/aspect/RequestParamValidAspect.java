package com.adc.da.budget.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

/**
 * 校验表单参数
 * @author qichunxu
 */
@Aspect
@Component
public class RequestParamValidAspect {

    @Before("(execution(* com.adc.da.budget.controller.*.*(..)))")
    public void paramValid(JoinPoint point) {
        Object[] args = point.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof BindingResult) {
                BindingResult result = (BindingResult) args[i];
                if (result.hasErrors()) {
                    throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
                }
            }
        }
    }
}
