package com.changgou;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Base64;

/**
 * @author pengmin
 * @date 2020/11/20 17:01
 */
/*@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest*/
public class Base64Test {
    /**
     * base64为对称加密方法;
     */
    //@Test
    public void test01(){
        String header = "{\"typ\":\"JWT\",\"alg\":\"HS256\"}";
        String playLoad = "{sub\":\"1234567890\",\"name\":\"John Doe\",\"admin\":true}";
        byte[] headerBytes = Base64.getEncoder().encode(header.getBytes());
        String headers = new String(headerBytes);
        System.out.println(headers);
        byte[] bytes = Base64.getDecoder().decode("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9");
        System.out.println(new String(bytes));
    }

    /**
     * 利用bcrypt生成密码;
     */
    //@Test
    public void test02(){
        String encode = new BCryptPasswordEncoder().encode("123456");
        System.out.println(encode);
    }
}
