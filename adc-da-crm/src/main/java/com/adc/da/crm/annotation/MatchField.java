package com.adc.da.crm.annotation;


import java.lang.annotation.*;

/**
 * 参数类型匹配注解
 * created by chenhaidong 2018/11/26
 */
@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MatchField {
    String value() default "";
}