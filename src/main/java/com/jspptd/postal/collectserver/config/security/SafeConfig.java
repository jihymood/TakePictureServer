package com.jspptd.postal.collectserver.config.security;

import org.apache.catalina.Context;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

/**
 * Created by LOG on 2017/6/22.
 */
@Configuration
public class SafeConfig {
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                if (container.getClass().isAssignableFrom(TomcatEmbeddedServletContainerFactory.class)) {
                    TomcatEmbeddedServletContainerFactory tomcatContainer = (TomcatEmbeddedServletContainerFactory) container;
                    tomcatContainer.addContextCustomizers(new ContextSecurityCustomizer());
                }
            }
        };
    }

    private static class ContextSecurityCustomizer implements TomcatContextCustomizer {
        @Override
        public void customize(Context context) {
            SecurityConstraint constraint = new SecurityConstraint();

            SecurityCollection securityCollection = new SecurityCollection();
            securityCollection.setName("restricted_methods");
            securityCollection.addPattern("/*");
            securityCollection.addMethod(HttpMethod.DELETE.toString());
            securityCollection.addMethod(HttpMethod.PUT.toString());
            securityCollection.addMethod(HttpMethod.HEAD.toString());
            securityCollection.addMethod(HttpMethod.TRACE.toString());
            securityCollection.addMethod(HttpMethod.OPTIONS.toString());

            constraint.addCollection(securityCollection);
            context.addConstraint(constraint);
        }
    }
}
