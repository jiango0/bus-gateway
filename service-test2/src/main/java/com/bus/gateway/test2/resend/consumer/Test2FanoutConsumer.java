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
public class Test2FanoutConsumer {

    private Logger logger = LoggerFactory.getLogger(Test2FanoutConsumer.class);

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "FANOUT_QUEUE2", durable = "true"),
                    exchange = @Exchange(value = "TRANSACTION_FANOUT_EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.FANOUT ),
                    key = ""
            )
    )
    public void handleTransaction(String data) {
        logger.info(data);
    }

}
