package com.changgou;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * @author pengmin
 * @version 1.0.0
 * @date 2020/11/25 10:06
 */

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
public class PayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class,args);
    }

    @Autowired
    private Environment environment;

    @Bean
    public DirectExchange createDirectExchange(){
        return new DirectExchange(environment.getProperty("mq.pay.exchange.order"));
    }

    @Bean
    public Queue createQueue(){
        return new Queue(environment.getProperty("mq.pay.queue.order"));
    }

    @Bean
    public Binding createBinding(){
        return BindingBuilder.bind(createQueue()).to(createDirectExchange()).with(environment.getProperty("mq.pay.routing.key"));
    }
}
