package com.bus.gateway.test1.resend.aspect;

import com.bus.gateway.test1.resend.service.Test1ConsumerService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ListenerAspect {

    public static Logger logger = LoggerFactory.getLogger(ListenerAspect.class);

    @Autowired
    Test1ConsumerService test1ConsumerService;

    @Pointcut(value = "execution( * com.bus.gateway.*.resend.consumer..*.*Listener(..))")
    public void execute() {}

    @Around(value = "execute()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        joinPoint.proceed();
    }


}
