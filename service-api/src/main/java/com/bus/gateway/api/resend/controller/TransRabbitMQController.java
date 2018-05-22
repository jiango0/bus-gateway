package com.bus.gateway.api.resend.controller;

import com.bus.gateway.api.resend.service.TransRabbitMQService;
import com.bus.gateway.common.web.ResultEntity;
import com.bus.gateway.entity.resend.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "api/rabbitmq")
public class TransRabbitMQController {

    @Autowired
    TransRabbitMQService transRabbitMQService;
    @RequestMapping(value = "send")
    public ResultEntity<Transaction> send(@RequestBody Transaction transaction) {
        transaction.setId(System.currentTimeMillis());
        transaction.setCreateDate(new Date());
        return ResultEntity.returnSuccess(transRabbitMQService.sendTransaction(transaction));
    }

}