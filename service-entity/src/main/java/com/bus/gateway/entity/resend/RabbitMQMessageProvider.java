package com.bus.gateway.entity.resend;

import java.util.List;

public class RabbitMQMessageProvider {

    private String id;

    private Long createDate;

    private List<RabbitMQMessageDetail> messageDetail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public List<RabbitMQMessageDetail> getMessageDetail() {
        return messageDetail;
    }

    public void setMessageDetail(List<RabbitMQMessageDetail> messageDetail) {
        this.messageDetail = messageDetail;
    }
}
