package com.adc.da;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@ComponentScan({"com.adc.da", "org.activiti.rest.diagram"})
@EnableAutoConfiguration(exclude = {
    org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class,
    org.activiti.spring.boot.SecurityAutoConfiguration.class,
    org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration.class
})
@EnableScheduling
public class AdcDaApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(AdcDaApplication.class, args);

        /// 启动Metrics 性能监控报表
//		CsvReporter reporter = applicationContext.getBean(CsvReporter.class);
//		reporter.start(1, TimeUnit.MINUTES);
    }

    /**
     * 设定容器的session失效时间，默认30分
     */
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        //单位为S
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                container.setSessionTimeout(18000);
            }
        };
    }
}