package com.bus.gateway.api.resend.service;

import com.bus.gateway.entity.resend.Transaction;

public interface TransRabbitMQService {

    Transaction sendTransaction(Transaction transaction);

}
