package com.changgou.user.service;

import com.changgou.core.service.CoreService;
import com.changgou.user.pojo.User;

/****
 * @Author:admin
 * @Description:User业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface UserService extends CoreService<User> {
    /**
     * 根据主键删除用户;
     * @param name
     */
    void deleteUserById(String name);

    /**
     * 增加积分;
     * @param username
     * @param points
     * @return
     */
    int addPoints(String username, Integer points);
}
