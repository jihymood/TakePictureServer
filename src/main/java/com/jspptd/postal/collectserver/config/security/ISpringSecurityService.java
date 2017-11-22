package com.jspptd.postal.collectserver.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Created by LOG on 2017/3/11.
 * 使用权限框架必须要实现的接口
 */
@Component
public interface ISpringSecurityService {

    /**
     * 根据userId获得User实体
     *
     * @param userId
     * @return
     */

    UserDetails getUserById(String userId);

}
