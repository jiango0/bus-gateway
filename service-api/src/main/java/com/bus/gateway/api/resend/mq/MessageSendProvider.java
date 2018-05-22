package com.bus.gateway.api.resend.mq;

import com.alibaba.fastjson.JSON;
import com.bus.gateway.api.resend.constant.MessageConstant;
import com.bus.gateway.api.resend.dao.mongo.TransRabbitMQDao;
import com.bus.gateway.entity.resend.RabbitMQMessageDetail;
import com.bus.gateway.entity.resend.RabbitMQMessageProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Component
public class MessageSendProvider {

    private Logger logger = LoggerFactory.getLogger(MessageSendProvider.class);

    @Autowired
    TransRabbitMQDao transRabbitMQDao;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 批量发送消息到rabbitmq队列
     * @param   provider
     * */
    public void send(RabbitMQMessageProvider provider) {
        try {
            if(provider != null) {
                List<RabbitMQMessageDetail> messageDetail = provider.getMessageDetail();
                if(!CollectionUtils.isEmpty(messageDetail)) {
                    messageDetail.forEach(md -> {
                        try {
                            if(!StringUtils.isEmpty(md.getExchange())
                                    && !StringUtils.isEmpty(md.getRoutingKey())
                                    && md.getData() != null  ) {
                                rabbitTemplate.convertAndSend(md.getExchange(), md.getRoutingKey(), JSON.toJSONString(md));
                                md.setSendStatus(MessageConstant.SEND_MESSAGE_SUCCESS);
                                md.setSendDate(new Date().getTime());
                            }
                        } catch (Exception e) {
                            md.setSendStatus(MessageConstant.SEND_MESSAGE_FAIL);
                            logger.error("发送消息队列失败 messageDetail : " + JSON.toJSONString(md) , e);
                        }
                    });
                    //更新数据库
                    transRabbitMQDao.findAndModify(Query.query(Criteria.where("id").is(provider.getId())), Update.update("messageDetail", messageDetail));
                }
            }
        } catch (Exception e) {
            logger.error("发送消息队列失败", e);
        }
    }

}
