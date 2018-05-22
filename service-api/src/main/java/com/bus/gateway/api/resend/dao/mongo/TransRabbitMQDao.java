package com.bus.gateway.api.resend.dao.mongo;

import com.bus.gateway.common.dao.mongo.AbstractBaseMongoDao;
import com.bus.gateway.entity.resend.RabbitMQMessageProvider;
import org.springframework.stereotype.Component;

@Component
public class TransRabbitMQDao extends AbstractBaseMongoDao<RabbitMQMessageProvider, String> {
}
