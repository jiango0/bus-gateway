package com.bus.gateway.api.resend.service;

import com.bus.gateway.entity.model.Transaction;

public interface TransactionService {

    Transaction sendTransaction(Transaction transaction);

    int confirmTransaction(String providerId);

    Transaction sendEventTransaction(Transaction transaction);

}
