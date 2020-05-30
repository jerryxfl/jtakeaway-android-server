package com.jerry.jtakeaway.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController("/api")
public class apiController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    
}
