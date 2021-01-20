package com.adc.da.main.config;

import com.adc.da.main.filter.MyCsrfFilter;
import com.adc.da.util.filter.*;
import com.adc.da.util.xss.XssFilter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Web配置，用于注入过滤器，拦截器等
 */
@Configuration
public class WebConfig {
    /**
     * 原过滤路径为/*，改为/api/* 不对工作流相关接口进行过滤
     * date 2018-09-05
     */
//    @Bean
//    public FilterRegistrationBean xssFilter() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(new XssFilter());
//        registration.addUrlPatterns("/api/*") ;
//        registration.addInitParameter("excludedPages","form");
//        registration.addInitParameter("excludedPages","activiti");
//        registration.setName("xssFilter");
//        registration.setOrder(10);
//        return registration;
//    }
    @Bean
    public FilterRegistrationBean xssFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns("/*");
        registration.setName("xssFilter");
        // 自定义表单请求不做过滤
        registration.addInitParameter("excludedPages", "/dictype,/form,/model,/activiti,/activiti-task,/process,/workflow");
        registration.setOrder(10);
        return registration;
    }

    @Bean
    public FilterRegistrationBean csrfFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new MyCsrfFilter());
        registration.addUrlPatterns("/*");
        registration.setName("csrfFilter");
        registration.setOrder(9);
        return registration;
    }

    @Bean
    public FilterRegistrationBean requestInfoFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RequestInfoFilter());
        registration.addUrlPatterns("/*");
        registration.setName("RequestInfoFilter");
        registration.setOrder(8);
        return registration;
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CorsFilter());
        registration.addUrlPatterns("/*");
        registration.setName("corsFilter");
        registration.setOrder(7);
        return registration;
    }

    @Bean
    public FilterRegistrationBean httpCacheFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HttpCacheFilter());
        registration.addUrlPatterns("/*");
        registration.setName("httpCacheFilter");
        registration.addInitParameter("maxAge", String.valueOf(60 * 60 * 24 * 7));
        registration.setOrder(6);
        return registration;
    }

    /**
     * 防伪造jsessionid
     */
    @Bean
    public FilterRegistrationBean fakeJSessionIdFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new FakeJSessionIdFilter());
        registration.addUrlPatterns("/*");
        registration.setName("fakeJSessionIdFilter");
        registration.setOrder(5);
        return registration;
    }

}
