package com.changgou.oauth.config;

import com.changgou.user.feign.UserFeign;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/***
 * 描述
 * @author ljh
 * @packagename com.itheima.config
 * @version 1.0
 * @date 2020/1/10
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 此处进行用户登陆校验以及授权;
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("获取到的用户名是：" + username);
        //设置权限
        String permission = "ROLE_ADMIN,ROLE_USER";
        Result<com.changgou.user.pojo.User> result = userFeign.findById(username);
        com.changgou.user.pojo.User user = result.getData();
        // todo
        return new User(username, user.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(permission));
    }
}
