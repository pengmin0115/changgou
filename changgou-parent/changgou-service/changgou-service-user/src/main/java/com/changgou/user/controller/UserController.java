package com.changgou.user.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.core.AbstractCoreController;
import com.changgou.user.pojo.User;
import com.changgou.user.service.UserService;
import com.changgou.user.utils.BCrypt;
import entity.JwtUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController extends AbstractCoreController<User> {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        super(userService, User.class);
        this.userService = userService;
    }

    @RequestMapping("/login")
    public Result login(String username, String password, HttpServletResponse response) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return new Result(false, StatusCode.LOGINERROR, "用户名和密码不能为空");
        }
        User user = userService.selectByPrimaryKey(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            //设置令牌信息
            Map<String, Object> info = new HashMap<>();
            info.put("username", username);
            info.put("role", "ROLE_ADMIN");
            String token = JwtUtil.createJWT(UUID.randomUUID().toString(), JSON.toJSONString(info), null);
            Cookie cookie = new Cookie("Authorization", token);
            cookie.setPath("/");
            response.addCookie(cookie);
            return new Result(true, StatusCode.OK, "登陆成功");
        }
        return new Result(false, StatusCode.LOGINERROR, "用户名或者密码错误");
    }

    @Override
    @PreAuthorize("hasAnyAuthority('admin')")
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable(name = "id") Object id) {
        String name = id.toString();
        coreService.deleteById(name);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /***
     * 根据ID查询用户信息
     * @param id
     * @return
     */
    @GetMapping("/load/{id}")
    Result<User> findById(@PathVariable(name = "id") String id) {
        User user = userService.selectByPrimaryKey(id);
        return new Result<>(true, StatusCode.OK, "ok", user);
    }

    /**
     * 加积分;
     * @param username
     * @param points
     * @return
     */
    @GetMapping("/points/add")
    public Result addPoints(@RequestParam(name = "username")String username,
                            @RequestParam(name = "points")Integer points){
        int count = userService.addPoints(username,points);
        if (count > 0){
            return new Result(true,StatusCode.OK,"ok");
        }
        return new Result(false, StatusCode.ERROR,"fail");
    }
}
