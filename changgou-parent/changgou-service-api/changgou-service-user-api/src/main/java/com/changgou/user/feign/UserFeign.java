package com.changgou.user.feign;

import com.changgou.user.pojo.User;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author pengmin
 * @date 2020/11/22 20:22
 */
@FeignClient(name="user",path = "/user")
public interface UserFeign {

    /***
     * 根据ID查询用户信息
     * @param id
     * @return
     */
    @GetMapping("/load/{id}")
    Result<User> findById(@PathVariable(name="id") String id);

    /**
     * 加积分;
     * @param username
     * @param points
     * @return
     */
    @GetMapping("/points/add")
    public Result addPoints(@RequestParam(name = "username")String username,
                            @RequestParam(name = "points")Integer points);
}
