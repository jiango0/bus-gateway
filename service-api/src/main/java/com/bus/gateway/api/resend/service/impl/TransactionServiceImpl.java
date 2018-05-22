package com.bus.gateway.api.resend.service.impl;

import com.bus.gateway.api.resend.service.TransactionService;
import com.bus.gateway.entity.resend.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Transactional
    public Transaction sendTransaction(Transaction transaction) {
        //1、本地操作
        logger.info("此处为本地事务操作!");


        return transaction;
    }

}
