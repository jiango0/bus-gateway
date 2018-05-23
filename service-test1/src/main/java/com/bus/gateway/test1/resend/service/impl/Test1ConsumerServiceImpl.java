package com.bus.gateway.test1.resend.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bus.gateway.common.constant.MessageConstant;
import com.bus.gateway.common.message.ResultMessage;
import com.bus.gateway.entity.mapper.MessageConsumerMapper;
import com.bus.gateway.entity.model.MessageConsumer;
import com.bus.gateway.test1.resend.service.Test1ConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
            String msg = "消息已处理存在，不能重复执行";
            logger.error(msg);
            return ResultMessage.error("500", msg);
        }

        consumer.setCreateDate(new Date());
        consumer.setUseStatus(MessageConstant.USE_MESSAGE_RECEIVE);
        messageConsumerMapper.insert(consumer);

        return new ResultMessage<MessageConsumer>(true, "200", "success", consumer);
    }

    public void successMessage(Long id, String data) {
        MessageConsumer consumer = messageConsumerMapper.selectById(id);
        if(consumer != null) {
            Integer useStatus = MessageConstant.USE_MESSAGE_SUCCESS;
            HttpResponse httpResponse = HttpRequest.post("http://127.0.0.1:8080/api/confirm/"+consumer.getProviderId())
                    .body("")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .execute(false);

            if (httpResponse.getStatus() == HttpStatus.HTTP_OK) {
                JSONObject jsonObject = JSON.parseObject(httpResponse.body());
                String code = jsonObject.getString("code");
                Integer result = jsonObject.getInteger("data");
                if("200".equals(code) && Integer.valueOf(1).equals(result) ) {
                    useStatus = MessageConstant.USE_MESSAGE_END;
                }
            }

            MessageConsumer messageConsumer = new MessageConsumer();
            messageConsumer.setId(id);
            messageConsumer.setUseStatus(useStatus);
            messageConsumerMapper.update(messageConsumer);
        }
    }

}
