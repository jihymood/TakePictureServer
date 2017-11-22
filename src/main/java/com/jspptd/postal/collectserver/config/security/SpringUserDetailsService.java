package com.jspptd.postal.collectserver.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by LOG on 2017/3/7.
 * DAOAuthencation里的一个属性需要实现的类
 */

@Service
public class SpringUserDetailsService implements UserDetailsService {

    @Autowired
    private ISpringSecurityService springSecurityService;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserDetails user = springSecurityService.getUserById(userId);
        if(user!= null) {
            return user;
        }
        throw new UsernameNotFoundException("can't found user");
    }

}
