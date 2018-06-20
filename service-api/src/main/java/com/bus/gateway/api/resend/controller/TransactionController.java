package com.bus.gateway.api.resend.controller;

import com.bus.gateway.api.resend.service.TransactionService;
import com.bus.gateway.common.web.ResultEntity;
import com.bus.gateway.entity.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(value = "confirm/{providerId}")
    public ResultEntity<Integer> confirmTransaction(@PathVariable String providerId) {
        return ResultEntity.returnSuccess(transactionService.confirmTransaction(providerId));
    }

    @RequestMapping(value = "send/event")
    public ResultEntity<Transaction> sendEventTransaction(@RequestBody Transaction transaction) {
        return ResultEntity.returnSuccess(transactionService.sendEventTransaction(transaction));
    }

}
