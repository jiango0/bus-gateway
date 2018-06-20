package com.bus.gateway.test2.resend.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class Test2TopicConsumer {

    private Logger logger = LoggerFactory.getLogger(Test2TopicConsumer.class);

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "TOPIC_QUEUE_2", durable = "true"),
                    exchange = @Exchange(value = "TRANSACTION_TOPIC_EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC ),
                    key = "product.0.0"
            )
    )
    public void handleTransaction(String data) {
        logger.info(data);
    }

}
