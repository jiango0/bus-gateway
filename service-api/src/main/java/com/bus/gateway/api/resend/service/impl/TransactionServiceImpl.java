package com.bus.gateway.api.resend.service.impl;

import com.alibaba.fastjson.JSON;
import com.bus.gateway.api.resend.dao.mongo.MessageProviderDao;
import com.bus.gateway.api.resend.mq.MessageConvertSend;
import com.bus.gateway.api.resend.service.TransactionService;
import com.bus.gateway.common.constant.MessageConstant;
import com.bus.gateway.entity.model.MessageMQParam;
import com.bus.gateway.entity.model.MessageProvider;
import com.bus.gateway.entity.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    MessageConvertSend messageConvertSend;

    @Autowired
    MessageProviderDao messageProviderDao;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Transactional
    public Transaction sendTransaction(Transaction transaction) {

        //1、本地操作
        logger.info("此处为本地事务操作!");

        try {
            if(transaction.getCanSuccess() != null && !transaction.getCanSuccess()) {
                throw new RuntimeException("canSuccess false");
            }

            rabbitTemplate.convertAndSend("TRANSACTION_DIRECT_EXCHANGE", "TRANSACTION_QUEUE", JSON.toJSONString(transaction));
        } catch (Exception e) {
            logger.error("sendTransaction 失败", e);
            MessageProvider provider = this.insertMessagePorvider(JSON.toJSONString(transaction));
            messageConvertSend.send(provider);
        }

        return transaction;
    }

    public int confirmTransaction(String providerId) {
        Update update = new Update();
        update.set("sendStatus", MessageConstant.SEND_MESSAGE_END);
        update.set("lastDate", new Date().getTime());

        return messageProviderDao.update(Query.query(Criteria.where("id").is(providerId)), update);
    }

    private MessageProvider insertMessagePorvider(String data) {
        MessageProvider provider = new MessageProvider();

        provider.setId(UUID.randomUUID().toString());
        provider.setContent(data);
        provider.setCreateDate(new Date());
        provider.setMessageType(MessageConstant.TYPE_MQ);
        provider.setSendStatus(MessageConstant.SEND_MESSAGE_RECEIVE);

        MessageMQParam mq = new MessageMQParam();
        mq.setExchange("TRANSACTION_DIRECT_EXCHANGE");
        mq.setExchangeType(ExchangeTypes.DIRECT);
        mq.setRoutingkey("TRANSACTION_QUEUE");

        provider.setMessageParam(JSON.toJSONString(mq));
        messageProviderDao.insert(provider);
        return provider;
    }

}
