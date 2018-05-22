package com.bus.gateway.test1.resend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages="com.bus.gateway")
public class Test1Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Test1Application.class, args);
    }

}
