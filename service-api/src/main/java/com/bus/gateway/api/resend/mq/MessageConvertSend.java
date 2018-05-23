package com.bus.gateway.api.resend.mq;

import com.alibaba.fastjson.JSON;
import com.bus.gateway.common.constant.MessageConstant;
import com.bus.gateway.entity.mapper.MessageProviderMapper;
import com.bus.gateway.entity.model.MessageProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Component
public class MessageConvertSend {

    private Logger logger = LoggerFactory.getLogger(MessageConvertSend.class);

    @Autowired
    MessageProviderMapper messageProviderMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 批量发送消息到rabbitmq队列
     * @param   providerList
     * */
    public void send(List<MessageProvider> providerList) {
        try {
            if(!CollectionUtils.isEmpty(providerList)) {
                Date now = new Date();
                providerList.forEach(messageProvider -> {
                    try {
                        if(!StringUtils.isEmpty(messageProvider.getExchange())
                                && !StringUtils.isEmpty(messageProvider.getRoutingkey())
                                && !StringUtils.isEmpty(messageProvider.getContent()) ) {
                            String mp = JSON.toJSONString(messageProvider);
                            logger.info("发送消息队列 messageProvider : " + mp);
                            rabbitTemplate.convertAndSend(messageProvider.getExchange(), messageProvider.getRoutingkey(), mp);
                            messageProvider.setSendStatus(MessageConstant.SEND_MESSAGE_SUCCESS);
                            messageProvider.setLastDate(now);
                            messageProvider.setSendDate(now);
                            messageProviderMapper.update(messageProvider);
                        }
                    } catch (Exception e) {
                        logger.error("发送消息队列失败 messageProvider : " + JSON.toJSONString(messageProvider) , e);
                        try {
                            messageProvider.setSendStatus(MessageConstant.SEND_MESSAGE_FAIL);
                            messageProvider.setLastDate(now);
                            messageProviderMapper.update(messageProvider);
                        } catch (Exception ex) {
                            logger.error("保存数据库失败 messageProvider : " + JSON.toJSONString(messageProvider) , ex);
                        }
                    }
                });
            }
        } catch (Exception e) {
            logger.error("发送消息队列失败", e);
        }
    }

}
