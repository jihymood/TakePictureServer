package com.jspptd.postal.collectserver.config.session;

import org.springframework.context.annotation.Bean;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * Created by LOG on 2016/9/6.
 */

@EnableSpringHttpSession
//@EnableRedisHttpSession
//@EnableJdbcHttpSession
public class HttpSessionConfig {
    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
        AllHttpSessionStrategy allHttpSessionStrategy = new AllHttpSessionStrategy();
        //这里是为了和前面的没有二合一的平台进行兼容，如果是全新的系统，不需要设置HeaderName
        //有默认的HeaderName, 建议使用默认。默认为x-security-token，所以Restful client请求时需要
        //带上此头部来请求，如果会话建立成功，也会返回x-security-token的东西
        allHttpSessionStrategy.setHeaderName("sessionId");
        return allHttpSessionStrategy;

    }


    /**
     * 由于使用了springhttpsession,所以需要实现repository
     * @return
     */
    @Bean
    public MapSessionRepository sessionRepository() {
        return new MapSessionRepository();
    }






}
