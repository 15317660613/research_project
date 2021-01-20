package com.adc.da.main.config;

import com.adc.da.hit.MyInterceptor;
import com.adc.da.hit.service.HitInfoEOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: qichunxu
 * @create: 2019-03-27 10:41
 **/
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {
    @Autowired
    private HitInfoEOService hitInfoEOService;
    /**
     * 增加拦截器
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry){
//        //指定拦截器类
//        registry.addInterceptor(new MyInterceptor(hitInfoEOService))
//                //指定该类拦截的url
//                .addPathPatterns("/api/**");
    }
}
