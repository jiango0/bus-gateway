package com.bus.gateway.retry.resend.consumer;

import com.alibaba.fastjson.JSON;
import com.bus.gateway.common.message.ResultMessage;
import com.bus.gateway.entity.model.MessageConsumer;
import com.bus.gateway.retry.resend.service.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumerListener {

    public static Logger logger = LoggerFactory.getLogger(MessageConsumerListener.class);

    @Autowired
    MessageService messageService;

    @RabbitListener(
            bindings = @QueueBinding(value = @Queue(value = "RETRY_MESSAGE_QUEUE", durable = "true"),
                    exchange = @Exchange(value = "RETRY_MESSAGE_EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.DIRECT),
                    key = "RETRY_MESSAGE_QUEUE")
    )
    public void handleTransactionListener(String data){
        if(StringUtils.isEmpty(data)) {
            return;
        }
        //保存重发消息
        ResultMessage<MessageConsumer> messageResult = messageService.receiveMessage(data);
        if(!messageResult.isStatus()) {
            logger.error("保存消息失败 ：" + JSON.toJSONString(messageResult));
        }
    }

}
