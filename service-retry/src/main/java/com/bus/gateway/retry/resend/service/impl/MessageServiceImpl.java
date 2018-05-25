package com.bus.gateway.retry.resend.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bus.gateway.common.constant.MessageConstant;
import com.bus.gateway.common.message.ResultMessage;
import com.bus.gateway.entity.model.MessageConsumer;
import com.bus.gateway.entity.model.MessageMQParam;
import com.bus.gateway.retry.resend.dao.mongo.MessageConsumerDao;
import com.bus.gateway.retry.resend.service.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {

    public static Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Value("${message.retry.num:3}")
    private Integer reTryNum;

    @Autowired
    MessageConsumerDao messageConsumerDao;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public ResultMessage<MessageConsumer> receiveMessage(String data) {
        MessageConsumer consumer = JSON.parseObject(data, new TypeReference<MessageConsumer>() {});
        consumer.setProviderId(consumer.getId());
        if(messageConsumerDao.exist(consumer.getProviderId())) {
            return ResultMessage.error("500", "消息已处理存在，不能重复执行");
        }
        consumer.setId(UUID.randomUUID().toString());
        consumer.setCreateDate(new Date().getTime());
        consumer.setUseStatus(MessageConstant.USE_MESSAGE_RECEIVE);
        messageConsumerDao.insert(consumer);

        return new ResultMessage<MessageConsumer>(true, "200", "success", consumer);
    }

    public List<MessageConsumer> selectWaitRetry() {
        Query query = new Query();
        //查询接收成功和发送失败的记录
        List<Integer> statusList = new ArrayList<Integer>(){{
            add(MessageConstant.USE_MESSAGE_RECEIVE);
            add(MessageConstant.USE_MESSAGE_FAIL);
        }};
        query.addCriteria(Criteria.where("useStatus").in(statusList));
//                .addCriteria(Criteria.where("reTryNum").gte(reTryNum));
        query.skip(1).limit(100);
        query.with(new Sort(Sort.Direction.ASC, "lastDate"));
        return messageConsumerDao.find(query);
    }

    public ResultMessage sendHTTP(MessageConsumer messageConsumer) {
        return null;
    }

    public ResultMessage sendMQ(MessageConsumer messageConsumer) {
        try {
            MessageMQParam messageMQParam = JSON.parseObject(messageConsumer.getMessageParam(), MessageMQParam.class);
            if( messageMQParam != null
                    && !StringUtils.isEmpty(messageMQParam.getExchange())
                    && !StringUtils.isEmpty(messageMQParam.getRoutingkey())
                    && !StringUtils.isEmpty(messageConsumer.getContent()) ) {
                rabbitTemplate.convertAndSend(messageMQParam.getExchange(),
                        messageMQParam.getRoutingkey(),
                        messageConsumer.getContent());
            }
        } catch (Exception e) {
            logger.error("发送MQ队列失败：" + JSON.toJSONString(messageConsumer), e);
            return ResultMessage.error("500", e.getMessage());
        }

        return new ResultMessage(true, "200", "success", null);
    }

    public int update(Query query, Update update) {
        return messageConsumerDao.update(query, update);
    }

    @Override
    public int successMessage(String id, MessageConsumer data) {

        MessageConsumer consumer = messageConsumerDao.findById(id);
        if(consumer != null) {
            HttpResponse httpResponse = HttpRequest.post("http://127.0.0.1:8080/api/confirm/"+consumer.getProviderId())
                    .body("")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .execute(false);

            if (httpResponse.getStatus() == HttpStatus.HTTP_OK) {
                JSONObject jsonObject = JSON.parseObject(httpResponse.body());
                String code = jsonObject.getString("code");
                Integer result = jsonObject.getInteger("data");
                if("200".equals(code) && Integer.valueOf(1).equals(result) ) {
                    return 1;
                }
            }
        }

        return 0;
    }

}
