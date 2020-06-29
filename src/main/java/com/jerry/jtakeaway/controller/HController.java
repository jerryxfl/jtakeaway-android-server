package com.jerry.jtakeaway.controller;


import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.*;
import com.jerry.jtakeaway.responseBean.ResponseOrder;
import com.jerry.jtakeaway.service.imp.*;
import com.jerry.jtakeaway.utils.JwtUtils;
import com.jerry.jtakeaway.utils.RUtils;
import com.jerry.jtakeaway.utils.WebSocketUtils;
import com.jerry.jtakeaway.utils.bean.Renum;
import com.jerry.jtakeaway.utils.bean.Result;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;

@Api(description = "骑手接口")
@RestController
@RequestMapping("/H")
public class HController {
    @Resource
    UserServiceImp userServiceImp;

    @Resource
    SuserServiceImp suserServiceImp;


    @Resource
    HuserServiceImp huserServiceImp;

    @Resource
    ApplyServiceImp applyServiceImp;

    @Resource
    WalletServiceImp walletRepository;

    @Resource
    TransactionServiceImp transactionServiceImp;

    @Resource
    OrdeServiceImp ordeServiceImp;

    @Resource
    JwtUtils jwtUtils;

    @Resource
    MsgServiceImp msgServiceImp;

    //解析出用户信息
    private Object[] parseSUER(HttpServletRequest request){
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        //先判断上一次时效过没有
        Huser huser = huserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
        Object[] params = new Object[2];
        params[0] = user;
        params[1] = huser;
        return params;
    }

    @ApiOperation("获得指定商家一部分可接订单    传入大小")
    @GetMapping("/g_a_horde")
    public Result g_a_horde(HttpServletRequest request,int suserid,int size){
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Huser huser = (Huser) params[1];
        if(huser==null)throw new NullPointerException();
        List<Orde> orders = new ArrayList<Orde>();
        List<Orde> rOrders = new ArrayList<Orde>();
        orders = ordeServiceImp.getRepository().getAllBySuserId(size,size+15,suserid);
        for(Orde order : orders){
            if(order.getStatusid()==1){
                rOrders.add(order);
            }
        }
        return RUtils.success(getResponseOrders(rOrders,user));
    }

    @Resource
    WebSocketUtils webSocketUtils;

    @ApiOperation("接受订单  商家id  订单id")
    @GetMapping("/accept_order")
    public Result accept_order(HttpServletRequest request,int suserid,int ordeid){
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Huser huser = (Huser) params[1];
        if(huser==null)throw new NullPointerException();
        List<Orde> ordeList = ordeServiceImp.getRepository().findByHuserid(huser.getId());
        int tag = 0;
        for (int i = 0; i < ordeList.size(); i++) {
            if(ordeList.get(i).getStatusid()!=6)tag++;
        }
        if(tag>=1){
            return RUtils.Err(Renum.HAVE_ORDER_NOT_COMPLETE.getCode(),Renum.HAVE_ORDER_NOT_COMPLETE.getMsg());
        }
        Suser suser = suserServiceImp.getRepository().findById(suserid).orElse(null);
        if(suser==null)throw new NullPointerException();
        Orde orde = ordeServiceImp.getRepository().findById(ordeid).orElse(null);
        if(orde==null)throw new NullPointerException();
        if(orde.getSuserid()!=suser.getId()||orde.getStatusid()==0||orde.getStatusid()==3)throw new IllegalArgumentException();
        orde.setStatusid(5);
        orde.setHuserid(huser.getId());
        Orde saveAndFlush = ordeServiceImp.getRepository().saveAndFlush(orde);

        User nuser = userServiceImp.getRepository().findByUsertypeAndUserdetailsid(0,orde.getNuserid());
        if(nuser!=null){
            Msg msg = new Msg();
            msg.setAcceptuserid(nuser.getId());
            msg.setContent("你的订单正在配送中");
            msg.setSendTime(new Timestamp(new Date().getTime()));
            msg.setSenduserid(user.getId());
            msg.setReadalready(0);
            msg.setPushalready(0);
            msgServiceImp.getRepository().saveAndFlush(msg);
            if(webSocketUtils.sendMessageToTargetUser(JSONObject.toJSONString(msg),nuser)){
                msg.setPushalready(1);
                msgServiceImp.getRepository().saveAndFlush(msg);
            }
        }
        User msgSuser = userServiceImp.getRepository().findByUsertypeAndUserdetailsid(1,suserid);
        if(msgSuser!=null){
            Msg msg = new Msg();
            msg.setAcceptuserid(msgSuser.getId());
            msg.setContent("订单"+orde.getId()+"已被接单");
            msg.setSendTime(new Timestamp(new Date().getTime()));
            msg.setSenduserid(user.getId());
            msg.setReadalready(0);
            msg.setPushalready(0);
            msgServiceImp.getRepository().save(msg);
            if(webSocketUtils.sendMessageToTargetUser(JSONObject.toJSONString(msg),nuser)){
                msg.setPushalready(1);
                msgServiceImp.getRepository().saveAndFlush(msg);
            }
        }
        ArrayList<Orde> ordes = new ArrayList<>();
        ordes.add(saveAndFlush);
        return RUtils.success(getResponseOrders(ordes,user));
    }

    @Resource
    WalletServiceImp walletServiceImp;


    @ApiOperation("订单送达   商家id  订单id")
    @GetMapping("/success_order")
    public Result success_order(HttpServletRequest request,int suserid,int ordeid){
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Huser huser = (Huser) params[1];
        if(huser==null)throw new NullPointerException();
        Suser suser = suserServiceImp.getRepository().findById(suserid).orElse(null);
        if(suser==null)throw new NullPointerException();
        Orde orde = ordeServiceImp.getRepository().findById(ordeid).orElse(null);
        if(orde==null)throw new NullPointerException();
        if(orde.getSuserid()!=suser.getId())throw new IllegalArgumentException();
        if(orde.getStatusid()!=5)throw new IllegalArgumentException();
        orde.setStatusid(6);
        Orde saveAndFlush = ordeServiceImp.getRepository().saveAndFlush(orde);

        User nuser = userServiceImp.getRepository().findByUsertypeAndUserdetailsid(0,orde.getNuserid());
        User msgSuser = userServiceImp.getRepository().findByUsertypeAndUserdetailsid(1,suserid);

        JSONObject json = JSONObject.parseObject(orde.getDetailedinformation());
        Double price = json.getObject("menu",Menus.class).getFoodprice();

        JSONObject json2 = JSONObject.parseObject(orde.getMenus());
        int size = 1;
        for (Map.Entry<String, Object> entry: json2.entrySet()) {
            size = (int) entry.getValue();
        }

        Double payMoney = price*size*0.2;

        Jtransaction shopTransaction = new Jtransaction();
        shopTransaction.setMore("支付骑手"+user.getUsernickname()+"佣金  $ -"+payMoney);
        shopTransaction.setPaymoney(-price*size);
        shopTransaction.setPaytime(new Timestamp(new Date().getTime()));
        shopTransaction.setUserid(msgSuser.getId());
        shopTransaction.setTargetuserid(user.getId());
        String uuid2 = UUID.randomUUID().toString();
        shopTransaction.setUuid(uuid2);
        transactionServiceImp.getRepository().saveAndFlush(shopTransaction);
        Wallet suserWallet = walletServiceImp.getRepository().findById(suser.getWalletid()).orElse(null);
        if(suserWallet!=null){
            shopTransaction = transactionServiceImp.getRepository().findByUuid(uuid2);

            if(suserWallet.getTransactionid().equals("")||suserWallet.getTransactionid()==null){
                suserWallet.setTransactionid(String.valueOf(shopTransaction.getId()));
            }else{
                suserWallet.setTransactionid(suserWallet.getTransactionid()+":"+shopTransaction.getId());
            }
        }


        Jtransaction houseTransaction = new Jtransaction();
        houseTransaction.setMore("商家"+suser.getName()+"支付给你 $+"+payMoney+"佣金");
        houseTransaction.setPaymoney(payMoney);
        houseTransaction.setPaytime(new Timestamp(new Date().getTime()));
        houseTransaction.setUserid(user.getId());
        houseTransaction.setTargetuserid(msgSuser.getId());
        String uuid = UUID.randomUUID().toString();
        houseTransaction.setUuid(uuid);
        transactionServiceImp.getRepository().saveAndFlush(houseTransaction);
        Wallet huserWallet = walletServiceImp.getRepository().findById(huser.getWalletid()).orElse(null);
        if(huserWallet!=null){
            houseTransaction = transactionServiceImp.getRepository().findByUuid(uuid);

            if(huserWallet.getTransactionid().equals("")||huserWallet.getTransactionid()==null){
                huserWallet.setTransactionid(String.valueOf(houseTransaction.getId()));
            }else{
                huserWallet.setTransactionid(huserWallet.getTransactionid()+":"+houseTransaction.getId());
            }
        }



        if(nuser!=null){
            Msg msg = new Msg();
            msg.setAcceptuserid(nuser.getId());
            msg.setContent("你的订单已送达");
            msg.setSendTime(new Timestamp(new Date().getTime()));
            msg.setSenduserid(user.getId());
            msg.setReadalready(0);
            msg.setPushalready(0);
            msgServiceImp.getRepository().save(msg);
            if(webSocketUtils.sendMessageToTargetUser(JSONObject.toJSONString(msg),nuser)){
                msg.setPushalready(1);
                msgServiceImp.getRepository().saveAndFlush(msg);
            }
        }
        if(msgSuser!=null){
            Msg msg = new Msg();
            msg.setAcceptuserid(msgSuser.getId());
            msg.setContent("订单"+orde.getId()+"已送达");
            msg.setSendTime(new Timestamp(new Date().getTime()));
            msg.setSenduserid(user.getId());
            msg.setReadalready(0);
            msg.setPushalready(0);
            msgServiceImp.getRepository().saveAndFlush(msg);
            if(webSocketUtils.sendMessageToTargetUser(JSONObject.toJSONString(msg),nuser)){
                msg.setPushalready(1);
                msgServiceImp.getRepository().saveAndFlush(msg);
            }
        }
        ArrayList<Orde> ordes = new ArrayList<>();
        ordes.add(saveAndFlush);
        return RUtils.success(getResponseOrders(ordes,user));
    }


    @Resource
    MenusServiceImp menusServiceImp;

    @Resource
    OrderStatusServiceImp orderStatusServiceImp;

    private List<ResponseOrder> getResponseOrders(List<Orde> orders, User user){
        List<ResponseOrder> responseOrders = new ArrayList<ResponseOrder>();
        for (Orde o:orders) {
            ResponseOrder responseOrder = new ResponseOrder();
            User suser = userServiceImp.getRepository().findByUsertypeAndUserdetailsid(1,o.getSuserid());
            responseOrder.setSuser(suser);
            User nuser = userServiceImp.getRepository().findByUsertypeAndUserdetailsid(0,o.getNuserid());
            responseOrder.setNuser(nuser);
            Orderstatus orderstatus = orderStatusServiceImp.getRepository().findById(o.getStatusid()).orElse(null);
            if(orderstatus!= null){
                responseOrder.setStatus(orderstatus);
            }
            JSONObject menuJsons = JSONObject.parseObject(o.getMenus());
            for (Map.Entry<String, Object> entry : menuJsons.entrySet()) {
                Menus mMenus = menusServiceImp.getRepository().findById(Integer.valueOf(entry.getKey())).orElse(null);
                if(mMenus!= null){
                    responseOrder.setMenus(mMenus);
                }
            }
            responseOrder.setId(o.getId());
            responseOrder.setCreatedTime(o.getCreatedTime());
            responseOrder.setDetailedinformation(o.getDetailedinformation());
            responseOrder.setLevel(o.getLevel());
            responseOrder.setUuid(o.getUuid());
            responseOrder.setHuser(user);
            responseOrders.add(responseOrder);
        }
        return responseOrders;
    }


}
