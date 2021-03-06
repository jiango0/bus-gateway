package com.bus.gateway.test1.resend.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class Test1TopicConsumer {

    private Logger logger = LoggerFactory.getLogger(Test1FanoutConsumer.class);

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "TOPIC_QUEUE_1", durable = "true"),
                    exchange = @Exchange(value = "TRANSACTION_TOPIC_EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC ),
                    key = "product.*.*"
            )
    )
    public void handleTransaction(String data) {
        logger.info(data);
    }

}
