package com.bus.gateway.test2.resend.dao.mongo;

import com.bus.gateway.common.dao.mongo.AbstractBaseMongoDao;
import com.bus.gateway.entity.resend.RabbitMQMessageConsumer;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class Test2MessageConsumerDao extends AbstractBaseMongoDao<RabbitMQMessageConsumer, String> {

    public boolean exist(String providerId) {
        return super.getMongoTemplate().exists(Query.query(Criteria.where("providerId").is(providerId)), RabbitMQMessageConsumer.class);
    }

}
