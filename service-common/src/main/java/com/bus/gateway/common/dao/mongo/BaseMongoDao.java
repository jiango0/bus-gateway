package com.bus.gateway.common.dao.mongo;

import com.bus.gateway.common.entity.PageList;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Collection;
import java.util.List;

public interface BaseMongoDao<T, K> {

    T findOne(Query query);

    List<T> find(Query query);

    void insert(T t);

    int update(Query query, Update update);

    T findById(K id);

    PageList<T> findPage(Integer currentPage, Integer pageSize, Query query, Sort sort);

    long count(Query query);

    void batchInsert(Collection<T> collection);

    T findAndModify(Query query, Update update);

}
