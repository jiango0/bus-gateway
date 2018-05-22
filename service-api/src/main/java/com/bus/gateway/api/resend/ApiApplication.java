package com.bus.gateway.api.resend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages="com.bus.gateway")
public class ApiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
