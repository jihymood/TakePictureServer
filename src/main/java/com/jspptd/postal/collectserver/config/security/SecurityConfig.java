package com.jspptd.postal.collectserver.config.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

/**
 * Created by LOG on 2017/3/6.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    //PermissionEvaluator


    //DAOAuthenticationProvide userDetailsService
    @Autowired
    private SpringUserDetailsService userDetailsService;


    //密码加密对象
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }



    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                //.authenticationProvider(new SpringAuthenticationProvider())
                //.eraseCredentials(false)
                .userDetailsService(userDetailsService)
                //对password进行加密
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .sameOrigin()
                .and()
                .authorizeRequests()

                .antMatchers("/user/**").permitAll()
                .antMatchers("/download/**").permitAll()
                .anyRequest()
//                .authenticated()   //权限允许？
                .permitAll()  //权限允许所有人
                .and()
//                .formLogin().permitAll()
//                .and()
                .logout().addLogoutHandler(new SecurityContextLogoutHandler())
                //.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()); //restful登出
                //.loginPage("/login").failureUrl("/login-error");
                .and()
                .sessionManagement()
//                .invalidSessionUrl("/login")
                .maximumSessions(5)

        ;


    }
}
