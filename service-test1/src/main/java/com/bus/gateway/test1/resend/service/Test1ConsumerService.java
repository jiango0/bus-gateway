package com.bus.gateway.test1.resend.service;

import com.bus.gateway.common.message.ResultMessage;
import com.bus.gateway.entity.model.MessageConsumer;

public interface Test1ConsumerService {

    ResultMessage<MessageConsumer> receiveMessage(String data);

    void successMessage(Long id, String data);

}
