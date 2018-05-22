package com.bus.gateway.common.dao.mongo;

import com.bus.gateway.common.entity.PageList;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

public abstract class AbstractBaseMongoDao<T, K> implements BaseMongoDao<T, K> {

    @Autowired
    private MongoTemplate mongoTemplate;

    public T findOne(Query query) {
        return mongoTemplate.findOne(query, this.getEntityClass());
    }

    public List<T> find(Query query) {
        return mongoTemplate.find(query, this.getEntityClass());
    }

    public void insert(T t) {
        mongoTemplate.insert(t);
    }

    public int update(Query query, Update update) {
        WriteResult result = mongoTemplate.upsert(query, update, this.getEntityClass());
        if(result != null) {
            return result.getN();
        }

        return 0;
    }

    public T findById(K id) {
        return mongoTemplate.findById(id, this.getEntityClass());
    }

    public PageList<T> findPage(Integer currentPage, Integer pageSize, Query query, Sort sort) {
        long count = this.count(query);
        List<T> list = new ArrayList<>();
        if(count > 0) {
            query.skip((currentPage - 1) * pageSize).limit(pageSize);
            query.with(sort);
            list = mongoTemplate.find(query, this.getEntityClass());
        }

        return new PageList<>(currentPage, pageSize, list, count);
    }

    public long count(Query query) {
        return mongoTemplate.count(query, this.getEntityClass());
    }

    public void batchInsert(Collection<T> collection) {
        mongoTemplate.insert(collection, this.getEntityClass());
    }

    public T findAndModify(Query query, Update update) {
        return mongoTemplate.findAndModify(query, update, this.getEntityClass());
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public Class<T> getEntityClass() {
        return (Class) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
