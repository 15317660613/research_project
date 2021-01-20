package com.adc.da.onlyoffice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    /**
     * 不加这个bean将会无法处理带参数的PUT请求
     * @return
     */
    @Bean
    public HttpPutFormContentFilter httpPutFormContentFilter() {
        return new HttpPutFormContentFilter();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // addResourceLocations指的是文件放置的目录，addResourceHandler指的是对外暴露的访问路径
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/file/").addResourceLocations("file:D:/uploadfile/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        return;
    }

    /**
     * 统一处理没啥业务逻辑处理的controller请求，实现代码的简洁
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry viewResolverRegistry) {
        return;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> list) {
        return;
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> list) {
        return;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> list) {
        return;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> list) {
        return;
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {
        return;
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {
        return;
    }

    @Override
    public Validator getValidator() {
        return null;
    }

    @Override
    public MessageCodesResolver getMessageCodesResolver() {
        return null;
    }

    /**
     * SpringMVC的路径参数如果带“.”的话，“.”后面的值将被忽略 .../pathvar/xx.yy 解析得到:xx
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 通过设置为false使其可以接受"."后的但是
        configurer.setUseSuffixPatternMatch(false);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer contentNegotiationConfigurer) {
        return;
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer asyncSupportConfigurer) {
        return;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer defaultServletHandlerConfigurer) {
        return;
    }

    @Override
    public void addFormatters(FormatterRegistry formatterRegistry) {
        return;
    }

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        return;
    }
}
