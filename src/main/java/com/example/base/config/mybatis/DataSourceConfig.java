//package com.example.base.config.mybatis;
//
//import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import javax.sql.DataSource;
//
///**
// * @author benben
// * @program bf
// * @Description DateSource配置
// * @date 2021-05-08 16:01
// */
//@Configuration
//public class DataSourceConfig {
//
//    @Bean
//    @ConfigurationProperties("spring.datasource.druid.mes")
//    @Primary
//    DataSource dataSourceMes(){
//        return DruidDataSourceBuilder.create().build();
//    }
//
//    @Bean
//    @ConfigurationProperties("spring.datasource.druid.ipes")
//    DataSource dataSourceTwo(){
//        return DruidDataSourceBuilder.create().build();
//    }
//
//}
