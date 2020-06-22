package com.jerry.jtakeaway.controller;


import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.Huser;
import com.jerry.jtakeaway.bean.Orde;
import com.jerry.jtakeaway.bean.Suser;
import com.jerry.jtakeaway.bean.User;
import com.jerry.jtakeaway.service.imp.*;
import com.jerry.jtakeaway.utils.JwtUtils;
import com.jerry.jtakeaway.utils.RUtils;
import com.jerry.jtakeaway.utils.bean.Result;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
        return RUtils.success(rOrders);
    }


    @ApiOperation("接受订单  商家id  订单id")
    @GetMapping("/accept_order")
    public Result accept_order(HttpServletRequest request,int suserid,int ordeid){
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Huser huser = (Huser) params[1];
        if(huser==null)throw new NullPointerException();
        Suser suser = suserServiceImp.getRepository().findById(suserid).orElse(null);
        if(suser==null)throw new NullPointerException();
        Orde orde = ordeServiceImp.getRepository().findById(ordeid).orElse(null);
        if(orde==null)throw new NullPointerException();
        if(orde.getSuserid()!=suser.getId())throw new IllegalArgumentException();
        orde.setStatusid(5);
        orde.setHuserid(huser.getId());
        Orde saveAndFlush = ordeServiceImp.getRepository().saveAndFlush(orde);
        return RUtils.success(saveAndFlush);
    }


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
        orde.setStatusid(6);
        Orde saveAndFlush = ordeServiceImp.getRepository().saveAndFlush(orde);
        return RUtils.success(saveAndFlush);
    }



}
