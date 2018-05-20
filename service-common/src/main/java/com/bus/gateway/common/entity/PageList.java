package com.bus.gateway.common.entity;

import java.io.Serializable;
import java.util.List;

public class PageList<T> implements Serializable {

    /**页码*/
    private Integer currentPage = 1;
    /**每页大小*/
    private Integer pageSize = 10;
    /**实体数据集合*/
    private List<T> entities;
    /**总页码*/
    private Long count;

    public PageList(Integer currentPage, Integer pageSize, List<T> entities, Long count) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.entities = entities;
        this.count = count;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getEntities() {
        return entities;
    }

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "PageList{" +
                "currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", entities=" + entities +
                ", count=" + count +
                '}';
    }

}
