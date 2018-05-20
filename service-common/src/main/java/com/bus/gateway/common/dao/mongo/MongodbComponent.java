package com.bus.gateway.common.dao.mongo;

import com.bus.gateway.common.entity.GenericEntity;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Component
public class MongodbComponent<T extends GenericEntity> {

    @Autowired
    private MongoTemplate mongoTemplate;

    public T selectById(Long id) {
        return mongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), this.getType());
    }

    public void insert(T t) {
        mongoTemplate.insert(t);
    }

    public int update(Query query, Update update, Class<T> t) {
        WriteResult writeResult = mongoTemplate.updateMulti(query, update, t);
        if(writeResult != null) {
            return writeResult.getN();
        }

        return 0;
    }

    public int save(Query query, Update update, Class<T> t) {
        WriteResult writeResult = mongoTemplate.upsert(query, update, t);
        if(writeResult != null) {
            return writeResult.getN();
        }

        return 0;
    }


    public long count(Query query, Class<T> t) {
        return mongoTemplate.count(query, t);
    }

    private Class<T> getType() {
        Type genType = getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<T>) actualTypeArguments[0];
    }

}
