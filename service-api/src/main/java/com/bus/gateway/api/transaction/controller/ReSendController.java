package com.bus.gateway.api.transaction.controller;

import com.bus.gateway.api.transaction.service.ReSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "reSend")
public class ReSendController {

    @Autowired
    ReSendService reSendService;



}
