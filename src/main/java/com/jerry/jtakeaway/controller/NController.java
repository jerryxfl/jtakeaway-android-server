package com.jerry.jtakeaway.controller;

import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.User;
import com.jerry.jtakeaway.config.websocket.Greeting;
import com.jerry.jtakeaway.config.websocket.HelloMessage;
import com.jerry.jtakeaway.service.imp.UserServiceImp;
import com.jerry.jtakeaway.utils.JwtUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;

@Api
@RestController
@RequestMapping("/N")
public class NController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Resource
    JwtUtils jwtUtils;

    @Resource
    UserServiceImp userServiceImp;


    @GetMapping("/getUser")
    public String getUser(){
        System.out.println("请求者来了");
        User user = userServiceImp.getRepository().findById(1);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("account", user.getAccount());
        jsonObject.put("password", user.getPassword());
        return jwtUtils.createJWT(jsonObject.toJSONString());
    }

}
