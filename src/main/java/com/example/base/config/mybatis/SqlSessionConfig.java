//package com.example.base.config.mybatis;
//
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.annotation.MapperScan;
//import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.util.ObjectUtils;
//import org.springframework.util.StringUtils;
//
//import javax.annotation.Resource;
//import javax.sql.DataSource;
//
///**
// * 数据库连接配置
// * @author benben
// * @date 2021-05-08 16:27
// */
//@Configuration
//@MapperScan(basePackages = "com.dl.bf.dao.mes", sqlSessionTemplateRef = "sqlSessionTemplateMes")
//@MapperScan(basePackages = "com.dl.bf.dao.ipes", sqlSessionTemplateRef = "sqlSessionTemplateTwo")
//public class SqlSessionConfig {
//
//    @Resource
//    DataSource dataSourceMes;
//
//    @Resource
//    DataSource dataSourceTwo;
//
//    @Resource
//    MybatisProperties mybatisProperties;
//
//    @Bean
//    @Primary
//    SqlSessionFactory sqlSessionFactoryMes() throws Exception {
//        return createSqlSessionFactory(dataSourceMes);
//    }
//
//    @Bean
//    SqlSessionFactory sqlSessionFactoryTwo() throws Exception {
//        return createSqlSessionFactory(dataSourceTwo);
//    }
//
//    @Bean
//    @Primary
//    SqlSessionTemplate sqlSessionTemplateMes() throws Exception {
//        return new SqlSessionTemplate(sqlSessionFactoryMes());
//    }
//
//    @Bean
//    SqlSessionTemplate sqlSessionTemplateTwo() throws Exception {
//        return new SqlSessionTemplate(sqlSessionFactoryTwo());
//    }
//
//    @Bean
//    @Primary
//    DataSourceTransactionManager one(){
//        return new DataSourceTransactionManager(dataSourceMes);
//    }
//
//    @Bean
//    DataSourceTransactionManager two(){
//        return new DataSourceTransactionManager(dataSourceTwo);
//    }
//
//
//    private SqlSessionFactory createSqlSessionFactory(DataSource dataSource) throws Exception{
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        //applyConfiguration(bean);
//        if (mybatisProperties.getConfigurationProperties() != null) {
//            bean.setConfigurationProperties(mybatisProperties.getConfigurationProperties());
//        }
//        if (StringUtils.hasLength(mybatisProperties.getTypeAliasesPackage())) {
//            bean.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());
//        }
//        if (mybatisProperties.getTypeAliasesSuperType() != null) {
//            bean.setTypeAliasesSuperType(mybatisProperties.getTypeAliasesSuperType());
//        }
//        if (StringUtils.hasLength(mybatisProperties.getTypeHandlersPackage())) {
//            bean.setTypeHandlersPackage(mybatisProperties.getTypeHandlersPackage());
//        }
//        if (!ObjectUtils.isEmpty(mybatisProperties.resolveMapperLocations())) {
//            bean.setMapperLocations(mybatisProperties.resolveMapperLocations());
//        }
//        return bean.getObject();
//    }
//
//    private void applyConfiguration(SqlSessionFactoryBean factory) {
//        // 使用同一个Configuration蒋只会存在一个连接池
//        org.apache.ibatis.session.Configuration configuration = mybatisProperties.getConfiguration();
//        if (configuration == null && !StringUtils.hasText(mybatisProperties.getConfigLocation())) {
//            configuration = new org.apache.ibatis.session.Configuration();
//        }
//        factory.setConfiguration(configuration);
//    }
//}
