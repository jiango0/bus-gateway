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
public class Test1Consumer {

    private Logger logger = LoggerFactory.getLogger(Test1Consumer.class);

    @RabbitListener(
            bindings = @QueueBinding(value = @Queue(value = "TRANSACTION_QUEUE", durable = "true"),
                    exchange = @Exchange(value = "TRANSACTION_DIRECT_EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.DIRECT),
                    key = "TRANSACTION_QUEUE")
    )
    public void handleTransactionListener(String data) {

        logger.info(data);

    }

}
