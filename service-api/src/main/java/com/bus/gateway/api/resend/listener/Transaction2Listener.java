package com.bus.gateway.api.resend.listener;

import com.alibaba.fastjson.JSON;
import com.bus.gateway.api.resend.event.TransactionEvent;
import com.bus.gateway.common.constant.MessageConstant;
import com.bus.gateway.entity.mapper.MessageProviderMapper;
import com.bus.gateway.entity.model.MessageProvider;
import com.bus.gateway.entity.model.Transaction;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class Transaction2Listener {

    @Autowired
    MessageProviderMapper messageProviderMapper;

    @EventListener
    public void onMessage(TransactionEvent event) {

        List<MessageProvider> providerList = new ArrayList<>();
        MessageProvider provider = new MessageProvider();
        provider.setProviderId(UUID.randomUUID().toString());
        provider.setExchangeType(ExchangeTypes.DIRECT);
        provider.setExchange("Test2");
        provider.setQueue("");
        provider.setRoutingkey("Test2");
        provider.setCreateDate(new Date());
        provider.setContent("");
        provider.setSendStatus(MessageConstant.SEND_MESSAGE_WAIT);
        providerList.add(provider);
        messageProviderMapper.insertBatch(providerList);

        Transaction transaction = event.getTransaction();

        System.out.println("Transaction2Listener :" + JSON.toJSONString(transaction));

    }

}
