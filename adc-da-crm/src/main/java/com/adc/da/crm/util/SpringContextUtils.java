package com.adc.da.crm.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * //TODO 添加类/接口功能描述
 *
 * @author 李志伟
 * @date 2018-12-03
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtils.applicationContext = applicationContext;
    }

    /**
     * 获得spring上下文
     *
     * @return ApplicationContext spring上下文
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取bean
     *
     * @param name service注解方式name为小驼峰格式
     * @return Object bean的实例对象
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    public static String getPageClassName(String beanName) throws ClassNotFoundException {
        // 方案修改，由于Repository命名方式修改了$EntityName+Repository，下方代码不执行
        beanName = beanName.replace("VO", "EO");
        if (!beanName.matches("\\w+Service$")) {
            beanName = "com.adc.da.crm.page." + toFirstLetterUpperCase(beanName) + "Page";
        }
        return beanName;
    }

    public static String getServiceBeanName(String beanName) throws ClassNotFoundException {
        // 方案修改，由于Repository命名方式修改了$EntityName+Repository，下方代码不执行
        beanName = beanName.replace("VO", "EO");
        if (!beanName.matches("\\w+Service$")) {
            beanName = toFirstLetterLowerCase(beanName) + "Service";
        }
        return beanName;
    }

    public static String getEntityClassName(String beanName) throws ClassNotFoundException {
        // 方案修改，由于Repository命名方式修改了$EntityName+Repository，下方代码不执行
        if (beanName.matches("\\w+EO")) {
            return "com.adc.da.crm.entity." + toFirstLetterUpperCase(beanName);
        }
        if (beanName.matches("\\w+VO")) {
            return "com.adc.da.crm.vo." + toFirstLetterUpperCase(beanName);
        }
        throw new ClassNotFoundException("找不到类");
    }

    public static String getEntityName(String beanName) throws ClassNotFoundException {
        // 方案修改，由于Repository命名方式修改了$EntityName+Repository，下方代码不执行
        beanName = toFirstLetterUpperCase(beanName);
        return beanName;
    }

    /**
     * 将字符串首字母小写
     *
     * @param columnName
     * @return
     */
    public static String toFirstLetterLowerCase(String columnName) {
        return Character.toLowerCase(columnName.charAt(0)) + columnName.substring(1);
    }

    /**
     * 将字符串首字母小写
     *
     * @param columnName
     * @return
     */
    public static String toFirstLetterUpperCase(String columnName) {
        return Character.toUpperCase(columnName.charAt(0)) + columnName.substring(1);
    }
}
