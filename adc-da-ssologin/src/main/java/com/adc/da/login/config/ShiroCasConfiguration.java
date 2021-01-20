package com.adc.da.login.config;

import com.adc.da.login.security.SystemCasRealm;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * Shiro 配置
 *
 * Apache Shiro 核心通过 Filter 来实现，就好像SpringMvc 通过DispachServlet 来主控制一样。 既然是使用
 * Filter 一般也就能猜到，是通过URL规则来进行过滤和权限校验，所以我们需要定义一系列关于URL的规则和访问权限。
 */
@Configuration
public class ShiroCasConfiguration {

    // casFilter UrlPattern
    public static final String CAS_FILTER_URL_PATTERN = "/cassuc";

    /**
     * 实例化SecurityManager，该类是shiro的核心类
     */
    @Bean
    public DefaultWebSecurityManager securityManager(@Value("${shiro.cas}") String casServerUrlPrefix,
        @Value("${shiro.server}") String shiroServerUrlPrefix) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(systemCasRealm(casServerUrlPrefix, shiroServerUrlPrefix));
        // 用户授权/认证信息Cache, 采用EhCache 缓存
        securityManager.setCacheManager(getEhCacheManager());
        // 指定 SubjectFactory,如果要实现cas的remember me的功能，需要用到下面这个CasSubjectFactory，并设置到securityManager的subjectFactory中
        return securityManager;
    }

    /**
     * 配置缓存
     */
    @Bean
    public EhCacheManager getEhCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:cache/ehcache-local.xml");
        return em;
    }

    /**
     * 配置Realm，由于我们使用的是CasRealm，所以已经集成了单点登录的功能
     */
    @Bean
    public SystemCasRealm systemCasRealm(@Value("${shiro.cas}") String casServerUrlPrefix,
        @Value("${shiro.server}") String shiroServerUrlPrefix) {
        SystemCasRealm realm = new SystemCasRealm();
        // cas登录服务器地址前缀
        realm.setCasServerUrlPrefix(casServerUrlPrefix);
        // 客户端回调地址，登录成功后的跳转地址(自己的服务地址)
        realm.setCasService(shiroServerUrlPrefix + ShiroCasConfiguration.CAS_FILTER_URL_PATTERN);
        // 登录成功后的默认角色，此处默认为user角色
        realm.setDefaultRoles("user");
        return realm;
    }

    /**
     * 注册单点登出的listener
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)// 优先级需要高于Cas的Filter
    public ServletListenerRegistrationBean singleSignOutHttpSessionListener() {
        ServletListenerRegistrationBean bean = new ServletListenerRegistrationBean();
        bean.setListener(new SingleSignOutHttpSessionListener());
        bean.setEnabled(true);
        return bean;
    }

    /**
     * 注册单点登出filter
     */
    @Bean
    public FilterRegistrationBean singleSignOutFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setName("singleSignOutFilter");
        bean.setFilter(new SingleSignOutFilter());
        bean.addUrlPatterns("/*");
        bean.setEnabled(true);
        return bean;
    }

    /**
     * 注册DelegatingFilterProxy（Shiro）
     */
    @Bean
    public FilterRegistrationBean delegatingFilterProxy() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        //  该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setOrder(1);
        return filterRegistration;
    }

    /**
     * 该类可以保证实现了org.apache.shiro.util.Initializable接口的shiro对象的init或者是destory方法被自动调用，
     * 而不用手动指定init-method或者是destory-method方法
     * 注意：如果使用了该类，则不需要手动指定初始化方法和销毁方法，否则会出错
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 下面两个配置主要用来开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(
        DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor
            = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
