package com.bus.gateway.entity.model;

import java.util.Date;

public class Transaction {

    private Long id;

    private String name;

    private Integer age;

    private Date createDate;

    private Boolean canSuccess;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Boolean getCanSuccess() {
        return canSuccess;
    }

    public void setCanSuccess(Boolean canSuccess) {
        this.canSuccess = canSuccess;
    }
}
