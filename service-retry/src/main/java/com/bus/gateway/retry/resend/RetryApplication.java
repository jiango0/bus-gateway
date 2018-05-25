package com.bus.gateway.retry.resend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication(scanBasePackages="com.bus.gateway")
public class RetryApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(RetryApplication.class, args);
    }

}
