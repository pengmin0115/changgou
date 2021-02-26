package com.changgou;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import entity.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

import java.net.UnknownHostException;

/**
 * @author pengmin
 * @date 2020/11/28 16:36
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
// 开启spring定时任务;
@EnableScheduling
@MapperScan(basePackages = {"com.changgou.seckill.dao"})
public class SecKillApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecKillApplication.class,args);
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        //key:value
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericFastJsonRedisSerializer());
        // field:value
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericFastJsonRedisSerializer());
        return template;
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }
}
