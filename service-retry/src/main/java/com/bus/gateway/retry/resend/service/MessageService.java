package com.bus.gateway.retry.resend.service;

import com.bus.gateway.common.message.ResultMessage;
import com.bus.gateway.entity.model.MessageConsumer;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

public interface MessageService {

    ResultMessage<MessageConsumer> receiveMessage(String data);

    List<MessageConsumer> selectWaitRetry();

    ResultMessage sendHTTP(MessageConsumer messageConsumer);

    ResultMessage sendMQ(MessageConsumer messageConsumer);

    int update(Query query, Update update);

    int successMessage(String id, MessageConsumer data);

}
