package com.bus.gateway.api.resend.controller;

import com.bus.gateway.api.resend.service.TransRabbitMQService;
import com.bus.gateway.common.web.ResultEntity;
import com.bus.gateway.entity.resend.Product;
import com.bus.gateway.entity.resend.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "api")
public class TransRabbitMQController {

    @Autowired
    TransRabbitMQService transRabbitMQService;

    @RequestMapping(value = "send")
    public ResultEntity<Transaction> send(@RequestBody Transaction transaction) {
        transaction.setId(System.currentTimeMillis());
        transaction.setCreateDate(new Date());
        return ResultEntity.returnSuccess(transRabbitMQService.sendTransaction(transaction));
    }

    @RequestMapping(value = "fanout/send")
    public ResultEntity<Transaction> fanoutSend(@RequestBody Transaction transaction) {
        transaction.setId(System.currentTimeMillis());
        transaction.setCreateDate(new Date());
        return ResultEntity.returnSuccess(transRabbitMQService.fanoutSend(transaction));
    }

    @RequestMapping(value = "topic/send")
    public ResultEntity<Product> topicSend(@RequestBody Product product) {
        product.setId(System.currentTimeMillis());
        product.setCreateDate(new Date());
        return ResultEntity.returnSuccess(transRabbitMQService.topicSend(product));
    }

}
