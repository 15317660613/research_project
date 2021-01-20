package com.adc.da.budget.annotation;

import java.lang.annotation.*;

/**
 * 字段自动匹配装载注解
 * created by chenhaidong 2018/11/23
 */
@Documented
@Inherited
@Target({ ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoMatch {
    String value() default "";
    boolean checkId() default false;
    
}
