package com.bus.gateway.api.resend.controller;

import com.bus.gateway.api.resend.service.TransactionService;
import com.bus.gateway.common.web.ResultEntity;
import com.bus.gateway.entity.resend.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "api")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @RequestMapping(value = "send")
    public ResultEntity<Transaction> send(@RequestBody Transaction transaction) {
        transaction.setId(System.currentTimeMillis());
        transaction.setCreateDate(new Date());
        return ResultEntity.returnSuccess(transactionService.sendTransaction(transaction));
    }

}
