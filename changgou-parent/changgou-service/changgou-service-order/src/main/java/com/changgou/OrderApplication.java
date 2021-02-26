package com.changgou;

import com.changgou.order.util.IdWorker;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author pengmin
 * @date 2020/11/22 21:20
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "com.changgou.order.dao")
@EnableFeignClients(basePackages = {"com.changgou.goods.feign",
                                    "com.changgou.user.feign"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
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

   /* @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        return druidDataSource;
    }*/

    /*@Primary
    @Bean("dataSourceProxy")
    public DataSourceProxy dataSourceProxy(DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }
*/
    /**
     * 使用广播(发布订阅)模式投递消息;
     * @return
     */
    @Bean
    public FanoutExchange createCartExchange(){
        return (FanoutExchange) new ExchangeBuilder("cart_exchange", ExchangeTypes.FANOUT).build();
    }

    @Bean
    public Queue createCartQueue(){
        return new Queue("cart_queue");
    }

    @Bean
    public Binding createCartBinding(){
        return BindingBuilder.bind(createCartQueue()).to(createCartExchange());
    }
}
