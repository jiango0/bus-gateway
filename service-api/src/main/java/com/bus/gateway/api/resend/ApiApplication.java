package com.bus.gateway.api.resend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication(scanBasePackages="com.bus.gateway")
@MapperScan("com.bus.gateway.entity.mapper")
@EnableAsync
public class ApiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
