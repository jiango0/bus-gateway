package com.bus.gateway.api.resend.service.impl;

import com.alibaba.fastjson.JSON;
import com.bus.gateway.api.resend.constant.MessageConstant;
import com.bus.gateway.api.resend.dao.mongo.TransRabbitMQDao;
import com.bus.gateway.api.resend.mq.MessageSendProvider;
import com.bus.gateway.api.resend.service.TransRabbitMQService;
import com.bus.gateway.entity.resend.RabbitMQMessageDetail;
import com.bus.gateway.entity.resend.RabbitMQMessageProvider;
import com.bus.gateway.entity.resend.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TransRabbitMQServiceImpl implements TransRabbitMQService {

    private Logger logger = LoggerFactory.getLogger(TransRabbitMQServiceImpl.class);

    @Autowired
    TransRabbitMQDao transRabbitMQDao;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MessageSendProvider messageSendProvider;

    @Transactional
    public Transaction sendTransaction(Transaction transaction) {

        //1、本地操作
        logger.info("此处为本地事务操作!");

        //封装消息队列所需的数据对象
        RabbitMQMessageProvider provider = new RabbitMQMessageProvider();
        provider.setId(UUID.randomUUID().toString());
        provider.setCreateDate(new Date().getTime());
        List<RabbitMQMessageDetail> detailList = new ArrayList<>();
        provider.setMessageDetail(detailList);

        //2、封装test1的消息
        RabbitMQMessageDetail<Transaction> test1Detail = new RabbitMQMessageDetail<>();
        test1Detail.setSendId(UUID.randomUUID().toString());
        test1Detail.setExchange("TRANSACTION_DIRECT_EXCHANGE");
        test1Detail.setRoutingKey("TRANSACTION_QUEUE");
        test1Detail.setSendStatus(MessageConstant.SEND_MESSAGE_WAIT);
        test1Detail.setData(transaction);
        detailList.add(test1Detail);

        //3、封装test2的消息
        RabbitMQMessageDetail<Transaction> test1Detail2 = new RabbitMQMessageDetail<>();
        test1Detail2.setSendId(UUID.randomUUID().toString());
        test1Detail2.setExchange("TRANSACTION_DIRECT_EXCHANGE2");
        test1Detail2.setRoutingKey("TRANSACTION_QUEUE2");
        test1Detail2.setSendStatus(MessageConstant.SEND_MESSAGE_WAIT);
        test1Detail2.setData(transaction);
        detailList.add(test1Detail2);

        //保存数据
        transRabbitMQDao.insert(provider);
        //调起消息发送
        messageSendProvider.send(provider);

        return transaction;
    }

    public Transaction fanoutSend(Transaction transaction) {

        logger.info("fanout start begin");

        rabbitTemplate.convertAndSend("TRANSACTION_FANOUT_EXCHANGE", "", JSON.toJSONString(transaction));

        logger.info("fanout start end");

        return transaction;
    }

}
