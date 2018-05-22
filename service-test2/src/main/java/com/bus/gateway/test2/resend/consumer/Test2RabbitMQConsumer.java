package com.bus.gateway.test2.resend.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bus.gateway.entity.resend.RabbitMQMessageConsumer;
import com.bus.gateway.entity.resend.RabbitMQMessageDetail;
import com.bus.gateway.entity.resend.Transaction;
import com.bus.gateway.test2.resend.dao.mongo.Test2MessageConsumerDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class Test2RabbitMQConsumer {

    private Logger logger = LoggerFactory.getLogger(Test2RabbitMQConsumer.class);

    @Autowired
    Test2MessageConsumerDao test2MessageConsumerDao;

    @RabbitListener(
            bindings = @QueueBinding(value = @Queue(value = "TRANSACTION_QUEUE2", durable = "true"),
                    exchange = @Exchange(value = "TRANSACTION_DIRECT_EXCHANGE2", ignoreDeclarationExceptions = "true", type = ExchangeTypes.DIRECT),
                    key = "TRANSACTION_QUEUE2")
    )
    public void handleTransaction(String data) {
        logger.info(data);

        RabbitMQMessageDetail<Transaction> detail = JSON.parseObject(data, new TypeReference<RabbitMQMessageDetail<Transaction>>(){});
        String providerId = detail.getSendId();
        if(test2MessageConsumerDao.exist(providerId)) {
            logger.error("消息已存在，不能重复执行");
            return;
        }

        RabbitMQMessageConsumer<Transaction> consumer = new RabbitMQMessageConsumer<>();
        consumer.setId(UUID.randomUUID().toString());
        consumer.setExchange(detail.getExchange());
        consumer.setRoutingKey(detail.getRoutingKey());
        consumer.setProviderId(providerId);
        consumer.setCreateDate(new Date().getTime());
        consumer.setData(detail.getData());
        test2MessageConsumerDao.insert(consumer);

        //执行业务操作
        logger.info("此处执行业务操作");
    }

}
