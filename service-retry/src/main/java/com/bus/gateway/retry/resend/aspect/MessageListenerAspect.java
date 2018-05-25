package com.bus.gateway.retry.resend.aspect;

import com.bus.gateway.retry.resend.service.MessageService;
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
public class MessageListenerAspect {

    public static Logger logger = LoggerFactory.getLogger(MessageListenerAspect.class);

    @Autowired
    MessageService messageService;

    @Pointcut(value = "execution( * com.bus.gateway.*.resend.consumer..*.*Listener(..))")
    public void execute() {}

    @Around(value = "execute()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object [] joinPointArgs = joinPoint.getArgs();
        if(joinPointArgs == null) {
            return;
        }
        Object joinPointArg = joinPointArgs[0];
        logger.info("listener begin :" + joinPointArg.toString());
        Object proceed = joinPoint.proceed();
        logger.info("listener end :" + (proceed == null ? "" : proceed.toString()));
    }

}
