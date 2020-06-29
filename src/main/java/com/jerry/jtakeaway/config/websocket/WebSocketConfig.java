package com.jerry.jtakeaway.config.websocket;

import com.jerry.jtakeaway.service.imp.MsgServiceImp;
import com.jerry.jtakeaway.service.imp.UserServiceImp;
import com.jerry.jtakeaway.utils.AsyncUtils;
import com.jerry.jtakeaway.utils.JwtUtils;
import com.jerry.jtakeaway.utils.RedisUtils;
import com.jerry.jtakeaway.utils.WebSocketUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 添加一个服务端点，来接收客户端的连接
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect").setAllowedOrigins("*").withSockJS();
    }

    /**
     * 开启WebSocket支持
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        WebSocketServer.jwtUtils = jwtUtils;
    }

    @Autowired
    public void setUserService(UserServiceImp userServiceImp) {
        WebSocketServer.userServiceImp = userServiceImp;
    }


    @Autowired
    public void setWebSocketUtils(WebSocketUtils webSocketUtils) {
        WebSocketServer.webSocketUtils = webSocketUtils;
    }

    @Autowired
    public void setMsgService(MsgServiceImp msgServiceImp) {
        WebSocketServer.msgServiceImp = msgServiceImp;
    }

    @Autowired
    public void setRedisUtils(RedisUtils redisUtils) {
        WebSocketServer.redisUtils = redisUtils;
    }


    @Autowired
    public void setAsyncUtils(AsyncUtils asyncUtils) {
        WebSocketServer.asyncUtils = asyncUtils;
    }

}