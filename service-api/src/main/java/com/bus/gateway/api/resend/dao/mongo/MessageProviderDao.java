package com.bus.gateway.api.resend.dao.mongo;

import com.bus.gateway.common.dao.mongo.AbstractBaseMongoDao;
import com.bus.gateway.entity.model.MessageProvider;
import org.springframework.stereotype.Component;

@Component
public class MessageProviderDao extends AbstractBaseMongoDao<MessageProvider, String> {
}
