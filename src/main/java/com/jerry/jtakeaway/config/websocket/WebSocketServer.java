package com.jerry.jtakeaway.config.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.Msg;
import com.jerry.jtakeaway.bean.User;
import com.jerry.jtakeaway.service.imp.MsgServiceImp;
import com.jerry.jtakeaway.service.imp.UserServiceImp;
import com.jerry.jtakeaway.utils.*;
import com.jerry.jtakeaway.utils.bean.Renum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * websocket接口处理类
 */
@ServerEndpoint("/connect")
@Component
public class WebSocketServer {
    private static final AtomicInteger segId = new AtomicInteger(1);

    public  static  JwtUtils jwtUtils;

    public  static  UserServiceImp userServiceImp;

    public  static WebSocketUtils webSocketUtils ;

    public  static MsgServiceImp msgServiceImp ;

    public  static RedisUtils redisUtils ;

    public  static AsyncUtils asyncUtils ;

    @OnOpen
    public void onOpen(Session session) throws Throwable {
        System.out.println("I'm open.");
        if (session.getRequestParameterMap().get("jwt") != null) {
            String jwt = session.getRequestParameterMap().get("jwt").get(0);
            System.out.println("jwt:"+jwt);
            Claims claims = null;
            try{
                claims = jwtUtils.parseJWT(jwt);
                String subject = claims.getSubject();
                JSONObject jsonObject = JSONObject.parseObject(subject);
                User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
                if(redisUtils.get(user.getAccount())!=null){
                    webSocketUtils.addSession(user,session);
                    System.out.println("1:"+user.getId());
                    asyncUtils.sendUnreadMsg(user);
                }else{
                    throw new Throwable("未登录");
                }
            }catch (ExpiredJwtException e){
                e.printStackTrace();
                throw new Throwable("jwt已过期");
            }catch (NullPointerException e){
                throw new Throwable("jwt解析失败");
            }
        }else{
            throw new Throwable("认证失败");
        }
    }


    @OnClose
    public void onClose(){
        System.out.println("I'm close.");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("I'm String onMessage.");
//        sendMessage(session, JSON.toJSONString(getResult()));
    }

    @OnMessage
    public void onMessage(byte[] message, Session session) {
        System.out.println("I'm byte onMessage.");
//        sendMessage(session, JSON.toJSONString(getResult()));
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
        System.out.println("I'm error: " + error.getMessage());
        webSocketUtils.removeSession(session);
    }






}
