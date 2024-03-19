package com.cddr.szd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ZY
 */
@SpringBootApplication
@EnableDiscoveryClient//开启服务注册功能
@RefreshScope//自动刷新 获取配置中心的配置
@EnableFeignClients//开启远程调用
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class,args);

    }
}
