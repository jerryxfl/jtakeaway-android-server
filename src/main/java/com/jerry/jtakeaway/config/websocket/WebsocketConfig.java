package com.jerry.jtakeaway.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //启用/userTest，/topicTest,两个消息前缀
        config.enableSimpleBroker("/userTest","/topicTest");
        //如果不设置下面这一句，用convertAndSendToUser来发送消息，前端订阅只能用/user开头。
        config.setUserDestinationPrefix("/userTest");
        //客户端（html等）向服务端发送消息的前缀
        config.setApplicationDestinationPrefixes("/app");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        //客户端和服务端进行连接的endpoint
        stompEndpointRegistry.addEndpoint("/websocket-endpoint").setAllowedOrigins("*").withSockJS();
    }
}