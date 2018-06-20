package com.bus.gateway.api.resend.event;

import com.bus.gateway.entity.model.Transaction;
import org.springframework.context.ApplicationEvent;

public class TransactionEvent extends ApplicationEvent {

    private Transaction transaction;

    public TransactionEvent(Object source, Transaction transaction) {
        super(source);
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
