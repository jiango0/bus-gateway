package com.bus.gateway.retry.resend.task;

import com.bus.gateway.common.constant.MessageConstant;
import com.bus.gateway.common.message.ResultMessage;
import com.bus.gateway.entity.model.MessageConsumer;
import com.bus.gateway.retry.resend.service.MessageService;
import com.bus.gateway.retry.resend.service.impl.MessageServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Component
public class MessageRetryTask {

    public static Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    MessageService messageService;

    @Scheduled(cron = "0/30 * * * * ?")
    public void execute() {
        //获取重发列表
        List<MessageConsumer> messageConsumers = messageService.selectWaitRetry();
        if(!CollectionUtils.isEmpty(messageConsumers) ) {
            Date now = new Date();
            messageConsumers.forEach(messageConsumer -> {
                if(messageConsumer.getMessageType() != null) {
                    Update update = new Update();
                    update.set("lastDate", now.getTime());
                    update.set("useStatus", MessageConstant.USE_MESSAGE_WAIT);
                    //先把状态修改为待发送
                    messageService.update(Query.query(Criteria.where("id").is(messageConsumer.getId())), update);

                    ResultMessage resultMessage = null;
                    if(MessageConstant.TYPE_MQ.equals(messageConsumer.getMessageType())) {
                        resultMessage = messageService.sendMQ(messageConsumer);
                    } else if(MessageConstant.TYPE_HTTP.equals(messageConsumer.getMessageType())) {
                        resultMessage = messageService.sendHTTP(messageConsumer);
                    }

                    boolean isReply = false;
                    if(resultMessage != null && resultMessage.isStatus()) {
                        update.set("useStatus", MessageConstant.USE_MESSAGE_SUCCESS);
                        isReply = true;
                    } else {
                        update.set("useStatus", MessageConstant.USE_MESSAGE_FAIL);
                    }
                    //更新状态
                    messageService.update(Query.query(Criteria.where("id").is(messageConsumer.getId())), update);

                    try {
                        //回调到api
                        if(isReply) {
                            Integer useStatus = messageService.successMessage(messageConsumer.getId(), messageConsumer) == 1
                                    ? MessageConstant.USE_MESSAGE_END : MessageConstant.USE_REPLY_FAIL;
                            update.set("useStatus", useStatus);
                            messageService.update(Query.query(Criteria.where("id").is(messageConsumer.getId())), update);
                        }
                    } catch (Exception e) {
                        logger.error("更新回复状态失败：", e);
                    }
                }
            });
        }
    }


}
