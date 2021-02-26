package com.changgou.user.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/11/22 15:56
 * @description 标题
 * @package com.changgou.order.config
 */
@Component
public class TokenDecode {

    private static final String PUBLIC_KEY = "public.key";

    private String getPubKey() {
        Resource resource = new ClassPathResource(PUBLIC_KEY);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            BufferedReader br = new BufferedReader(inputStreamReader);
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException ioe) {
            return null;
        }
    }

    //获取令牌的数据的MAP对象
    public Map<String,String> getInfo() {
        //先获取令牌
        OAuth2AuthenticationDetails auth2AuthenticationDetails = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        String tokenValue = auth2AuthenticationDetails.getTokenValue();
        //获取Jwt原始内容
        String claims = JwtHelper.decodeAndVerify(tokenValue, new RsaVerifier(getPubKey())).getClaims();//JSON
        //将JSON字符串转换成MAP
        Map<String, String> map = JSON.parseObject(claims, Map.class);
        return map;
    }

    //直接获取用户名
    public String getUserName(){
        return getInfo().get("user_name");
    }

}
