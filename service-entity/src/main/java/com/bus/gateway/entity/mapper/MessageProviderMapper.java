package com.bus.gateway.entity.mapper;

import com.bus.gateway.entity.model.MessageProvider;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageProviderMapper {

    MessageProvider selectById(Long id);

    void insertBatch(List<MessageProvider> messageProvider);

    int update(MessageProvider messageProvider);

}