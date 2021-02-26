package com.changgou;

import com.changgou.user.config.ResourceServerConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ywh
 * @version 1.0.0
 * @date 2020/11/24 13:29
 */

public class ConfigTest {
    @Autowired
    private ResourceServerConfig resourceServerConfig;

    @Test
    public void test01(){
        String pubKey = new ResourceServerConfig().getPubKey();
        System.out.println(pubKey);
    }
}
