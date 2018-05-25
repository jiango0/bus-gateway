package com.bus.gateway.api.resend.mq;

import com.alibaba.fastjson.JSON;
import com.bus.gateway.api.resend.dao.mongo.MessageProviderDao;
import com.bus.gateway.common.constant.MessageConstant;
import com.bus.gateway.entity.model.MessageMQParam;
import com.bus.gateway.entity.model.MessageProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class MessageConvertSend {

    private Logger logger = LoggerFactory.getLogger(MessageConvertSend.class);

    ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MessageProviderDao messageProviderDao;

    /**
     * 发送消息到rabbitmq队列
     * @param   provider
     * */
    public void send (MessageProvider provider) {
        try {
            executor.execute(() -> {
                if(provider != null) {
                    Date now = new Date();
                    Update update = new Update();
                    update.set("lastDate", now.getTime());
                    update.set("useStatus", MessageConstant.SEND_MESSAGE_WAIT);
                    messageProviderDao.update(Query.query(Criteria.where("id").is(provider.getId())), update);

                    try {
                        rabbitTemplate.convertAndSend("RETRY_MESSAGE_EXCHANGE",
                                "RETRY_MESSAGE_QUEUE",
                                JSON.toJSONString(provider));

                        update.set("useStatus", MessageConstant.SEND_MESSAGE_SUCCESS);
                        update.set("sendDate", new Date().getTime());
                    } catch (Exception e) {
                        logger.error("发送MQ消息失败", e);
                        update.set("useStatus", MessageConstant.SEND_MESSAGE_FAIL);
                    }
                    messageProviderDao.update(Query.query(Criteria.where("id").is(provider.getId())), update);
                }
            });
        } catch (Exception e) {
            logger.error("发送失败", e);
        }
    }

}
