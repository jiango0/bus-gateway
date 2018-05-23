package com.bus.gateway.entity.mapper;

import com.bus.gateway.entity.model.MessageConsumer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageConsumerMapper {

    MessageConsumer selectById(Long id);

    MessageConsumer selectByProviderId(String providerId);

    void insert(MessageConsumer messageConsumer);

    int update(MessageConsumer messageConsumer);

}