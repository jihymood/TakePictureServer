package com.jspptd.postal.collectserver.config.session;


import com.jspptd.postal.collectserver.config.security.ISpringSecurityService;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Created by LOG on 2017/3/11.
 */
@Component
public class SpringSecurityService implements ISpringSecurityService {

    /**
     * 用户名登陆？？？
     * @param userId
     * @return
     */
    @Override
    public UserDetails getUserById(String userId) {
        return null;
    }
}
