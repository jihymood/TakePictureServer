package com.jspptd.postal.collectserver.config.druid;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by LOG on 2017/6/20.
 */
@Configuration
public class DruidConfig {
//    @Bean("druid-stat-interceptor")
//    public DruidStatInterceptor interceptor() {
//        return new DruidStatInterceptor();
//    }
//
//
//    @Bean
//    public SpringIbatisBeanTypeAutoProxyCreator proxy() {
//        SpringIbatisBeanTypeAutoProxyCreator beanTypeAutoProxyCreator = new SpringIbatisBeanTypeAutoProxyCreator();
//        beanTypeAutoProxyCreator.setInterceptorNames("druid-stat-interceptor");
//        return beanTypeAutoProxyCreator;
//    }

    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }
}
