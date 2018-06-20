package com.bus.gateway.api.resend.service;

import com.bus.gateway.entity.resend.Product;
import com.bus.gateway.entity.resend.Transaction;

public interface TransRabbitMQService {

    Transaction sendTransaction(Transaction transaction);

    Transaction fanoutSend(Transaction transaction);

    Product topicSend(Product product);

}
