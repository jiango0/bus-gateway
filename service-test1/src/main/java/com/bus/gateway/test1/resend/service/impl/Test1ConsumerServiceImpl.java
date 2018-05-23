package com.bus.gateway.test1.resend.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bus.gateway.common.constant.MessageConstant;
import com.bus.gateway.common.message.ResultMessage;
import com.bus.gateway.entity.mapper.MessageConsumerMapper;
import com.bus.gateway.entity.model.MessageConsumer;
import com.bus.gateway.test1.resend.service.Test1ConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class Test1ConsumerServiceImpl implements Test1ConsumerService {

    public static Logger logger = LoggerFactory.getLogger(Test1ConsumerServiceImpl.class);

    @Autowired
    MessageConsumerMapper messageConsumerMapper;

    public ResultMessage<MessageConsumer> receiveMessage(String data) {
        MessageConsumer consumer = JSON.parseObject(data, new TypeReference<MessageConsumer>() {});
        //获取消息
        MessageConsumer messageConsumer = messageConsumerMapper.selectByProviderId(consumer.getProviderId());
        if(messageConsumer != null) {
            logger.error("消息已处理存在，不能重复执行");
            return ResultMessage.returnStatus(false);
        }

        consumer.setCreateDate(new Date());
        consumer.setUseStatus(MessageConstant.USE_MESSAGE_RECEIVE);
        messageConsumerMapper.insert(consumer);

        return ResultMessage.returnStatus(true);
    }

    public void successMessage(String data) {

    }

}
