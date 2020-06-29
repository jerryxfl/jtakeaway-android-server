package com.jerry.jtakeaway.utils;

import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.Msg;
import com.jerry.jtakeaway.bean.User;
import com.jerry.jtakeaway.service.imp.MsgServiceImp;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AsyncUtils {
    @Resource
    MsgServiceImp msgServiceImp;

    @Resource
    WebSocketUtils webSocketUtils;

    @Async
    public void  sendUnreadMsg(User user){
        //定时推送
        List<Msg> msgList;
        msgList = msgServiceImp.getRepository().findByAcceptuserid(user.getId());
        if(msgList!=null){
            new Thread(() -> {
                for (Msg m:msgList) {
                    System.out.println("2:"+m.getAcceptuserid());
                    if(m.getPushalready()==0){
                        if(webSocketUtils.sendMessageToTargetUser(JSONObject.toJSONString(m),user)){
                            m.setPushalready(1);
                            msgServiceImp.getRepository().saveAndFlush(m);
                        }
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
