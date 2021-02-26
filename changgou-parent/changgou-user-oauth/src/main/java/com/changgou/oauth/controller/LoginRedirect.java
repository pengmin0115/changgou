package com.changgou.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author pengmin
 * @date 2020/11/23 16:07
 */
@Controller
@RequestMapping("/oauth")
public class LoginRedirect {

    @RequestMapping("/login")
    public String login(String url, Model model){
        // url来源;
        model.addAttribute("url",url);
        return "login";
    }
}
