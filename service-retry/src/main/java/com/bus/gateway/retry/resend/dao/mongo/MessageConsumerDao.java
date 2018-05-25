package com.bus.gateway.retry.resend.dao.mongo;

import com.bus.gateway.common.dao.mongo.AbstractBaseMongoDao;
import com.bus.gateway.entity.model.MessageConsumer;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumerDao extends AbstractBaseMongoDao<MessageConsumer, String> {

    public boolean exist(String providerId) {
        return super.getMongoTemplate().exists(Query.query(Criteria.where("providerId").is(providerId)), MessageConsumer.class);
    }


}
