package com.bus.gateway.test1.resend.aspect;

import com.bus.gateway.common.message.ResultMessage;
import com.bus.gateway.entity.model.MessageConsumer;
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

//    public static Logger logger = LoggerFactory.getLogger(ListenerAspect.class);
//
//    @Autowired
//    Test1ConsumerService test1ConsumerService;
//
//    @Pointcut(value = "execution( * com.bus.gateway.*.resend.consumer..*.*Listener(..))")
//    public void execute() {}
//
//    @Around(value = "execute()")
//    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
//        Object [] joinPointArgs = joinPoint.getArgs();
//        if(joinPointArgs == null) {
//            return;
//        }
//        Object joinPointArg = joinPointArgs[0];
//        ResultMessage<MessageConsumer> result = test1ConsumerService.receiveMessage(joinPointArg.toString());
//        if(result.isStatus()) {
//            MessageConsumer data = result.getData();
//            if(data != null && data.getId() != null) {
//                //调用listener代码块
//                joinPoint.proceed();
//                test1ConsumerService.successMessage(data.getId(), joinPointArg.toString());
//            }
//        }
//    }


}
