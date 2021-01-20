package com.adc.da.main.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan("com.adc.da.**.dao")
public class MybatisConfig {

//    @Autowired
//    @Qualifier("dataSource")
//    private DataSource dataSource;

//    @Bean
//    public SqlSessionFactory sqlSessionFactory() throws Exception {
//        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//        factoryBean.setDataSource(dataSource);
//        factoryBean.setMapperLocations(
//                new PathMatchingResourcePatternResolver().getResources("classpath*:mybatis/mapper/**/*.xml"));
//        factoryBean.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
//        return factoryBean.getObject();
//    }

//    @Bean
//    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
//        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory());
//        return template;
//    }
}
