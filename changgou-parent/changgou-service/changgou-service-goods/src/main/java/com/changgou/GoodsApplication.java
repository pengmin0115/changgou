package com.changgou;
import entity.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author pengmin
 * @date 2020/11/8 17:32
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.changgou.goods.dao")
public class GoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class,args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(0,0);
    }

   /* @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        return druidDataSource;
    }*/

   /* @Primary
    @Bean("dataSourceProxy")    //此处依赖seata的包;
    public DataSourceProxy dataSourceProxy(DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }*/
}
