package com.bus.gateway.api.resend.service.impl;

import com.alibaba.fastjson.JSON;
import com.bus.gateway.api.resend.mq.MessageConvertSend;
import com.bus.gateway.api.resend.service.TransactionService;
import com.bus.gateway.common.constant.MessageConstant;
import com.bus.gateway.entity.mapper.MessageProviderMapper;
import com.bus.gateway.entity.model.MessageProvider;
import com.bus.gateway.entity.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    MessageConvertSend messageConvertSend;

    @Autowired
    MessageProviderMapper messageProviderMapper;

    @Transactional
    public Transaction sendTransaction(Transaction transaction) {
        //1、本地操作
        logger.info("此处为本地事务操作!");

        Date now = new Date();
        List<MessageProvider> providerList = new ArrayList<>();

        //2、封装test1的消息
        MessageProvider provider = new MessageProvider();
        provider.setProviderId(UUID.randomUUID().toString());
        provider.setExchangeType(ExchangeTypes.DIRECT);
        provider.setExchange("TRANSACTION_DIRECT_EXCHANGE");
        provider.setQueue("");
        provider.setRoutingkey("TRANSACTION_QUEUE");
        provider.setCreateDate(now);
        provider.setContent(JSON.toJSONString(transaction));
        provider.setSendStatus(MessageConstant.SEND_MESSAGE_WAIT);
        providerList.add(provider);

        //3、封装test2的消息
        MessageProvider provider2 = new MessageProvider();
        provider2.setProviderId(UUID.randomUUID().toString());
        provider2.setExchangeType(ExchangeTypes.DIRECT);
        provider2.setExchange("TRANSACTION_DIRECT_EXCHANGE2");
        provider2.setQueue("");
        provider2.setRoutingkey("TRANSACTION_QUEUE2");
        provider2.setCreateDate(now);
        provider2.setContent(JSON.toJSONString(transaction));
        provider2.setSendStatus(MessageConstant.SEND_MESSAGE_WAIT);
        providerList.add(provider2);
        //保存数据
        messageProviderMapper.insertBatch(providerList);

        //4、调动外部http接口
        logger.info("此处为调动外部http接口!");

        //调起消息发送
        messageConvertSend.send(providerList);

        return transaction;
    }

}
