package com.bus.gateway.api.resend.service;

import com.bus.gateway.entity.resend.Transaction;

public interface TransactionService {

    Transaction sendTransaction(Transaction transaction);

}
