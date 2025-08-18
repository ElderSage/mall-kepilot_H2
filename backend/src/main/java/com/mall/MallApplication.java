package com.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 在线商城应用启动类
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableScheduling
public class MallApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallApplication.class, args);
        System.out.println("=================================");
        System.out.println("在线商城后端服务启动成功！");
        System.out.println("API文档地址: http://localhost:8080/swagger-ui.html");
        System.out.println("=================================");
    }
}