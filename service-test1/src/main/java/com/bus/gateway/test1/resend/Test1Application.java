package com.bus.gateway.test1.resend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication(scanBasePackages="com.bus.gateway")
@MapperScan("com.bus.gateway.entity.mapper")
public class Test1Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Test1Application.class, args);
    }

}
