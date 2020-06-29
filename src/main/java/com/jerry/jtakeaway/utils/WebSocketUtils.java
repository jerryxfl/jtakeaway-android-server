package com.jerry.jtakeaway.utils;

import com.jerry.jtakeaway.bean.User;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class WebSocketUtils {
    private static final Map<User, Session> sessionUserMap = new HashMap<>();

    public void addSession(User user, Session session) {
        System.out.println("添加成功");
        sessionUserMap.put(user, session);
    }


    public void removeSession(Session session) {
        for (User u : sessionUserMap.keySet()){
            Session sessionUser = sessionUserMap.get(u);
            if(sessionUser == session){
                try {
                    sessionUserMap.get(u).close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sessionUserMap.remove(u);
            }
        }
    }

    public void removeSession(User user) {
        for (User u : sessionUserMap.keySet()){
            if(u.getAccount().equals(user.getAccount())){
                try {
                    sessionUserMap.get(u).close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sessionUserMap.remove(u);
            }
        }
    }

    //全局推送
    public void sendMessageToAll(String messageJson) {
        for (User u : sessionUserMap.keySet()) {
            Session session = sessionUserMap.get(u);
            if (session.isOpen()) {
               if(messageJson.length()!=0){
                   try {
                       System.out.println("推送消息内容 : " + messageJson);
                       session.getBasicRemote().sendText(messageJson);
                   } catch (Exception e) {
                       System.out.println("send message error : " + e.getMessage());
                       e.printStackTrace();
                   }
               }
            }else{
                removeSession(session);
            }
        }
    }


    public  boolean  sendMessageToTargetUser(String messageJson,User user){
        for (User u:sessionUserMap.keySet()) {
            if(u.getAccount().equals(user.getAccount())){
                Session session = sessionUserMap.get(u);
                if(session.isOpen()){
                    if(messageJson.length()!=0){
                        try {
                            System.out.println("向"+user.getUsernickname()+"推送消息内容 : " + messageJson);
                            session.getBasicRemote().sendText(messageJson);
                            return true;
                        } catch (Exception e) {
                            System.out.println("send message error : " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }else{
                    removeSession(session);
                    return false;
                }
            }
        }
        return false;
    }
}
