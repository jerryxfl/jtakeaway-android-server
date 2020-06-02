package com.jerry.jtakeaway.controller;


import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.*;
import com.jerry.jtakeaway.service.imp.*;
import com.jerry.jtakeaway.utils.JwtUtils;
import com.jerry.jtakeaway.utils.RUtils;
import com.jerry.jtakeaway.utils.bean.Result;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Api(description = "管理员接口")
@RestController
@RequestMapping("/X")
public class XController {
    @Resource
    UserServiceImp userServiceImp;

    @Resource
    SuserServiceImp suserServiceImp;

    @Resource
    BroadcastServiceImp broadcastServiceImp;


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
    SlideServiceImp slideServiceImp;

    @Resource
    XuserServiceImp xuserServiceImp;

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
        Xuser xuser = xuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
        Object[] params = new Object[2];
        params[0] = user;
        params[1] = xuser;
        return params;
    }

    @ApiOperation("删除轮播图       slideid")
    @GetMapping("/d_xslide")
    public Result d_slide(HttpServletRequest request,int slideid){
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Xuser xuser = (Xuser) params[1];
        if(xuser==null)throw new NullPointerException();
        Slide slide = slideServiceImp.getRepository().findById(slideid).orElse(null);
        if(slide==null)throw new NullPointerException();
        if(slide.getUserid()!=user.getId())throw new IllegalArgumentException();
        slideServiceImp.getRepository().delete(slide);
        return RUtils.success();
    }


    @ApiOperation("添加轮播图    slide")
    @PostMapping("/a_xslide")
    public Result a_menu(HttpServletRequest request,@RequestBody Slide slide){
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Xuser xuser = (Xuser) params[1];
        if(xuser==null)throw new NullPointerException();
        Slide save = slideServiceImp.getRepository().save(slide);
        return RUtils.success(save);
    }

    @ApiOperation("修改轮播图       slide")
    @PostMapping("/alter_xslide")
    public Result alter_menu(HttpServletRequest request,@RequestBody Slide slide){
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Xuser xuser = (Xuser) params[1];
        if(xuser==null)throw new NullPointerException();
        if(slide==null)throw new NullPointerException();
        if(slide.getUserid()!=user.getId())throw new IllegalArgumentException();
        Slide save = slideServiceImp.getRepository().save(slide);
        return RUtils.success(save);
    }


    @ApiOperation("添加广播        content")
    @PostMapping("/a_broadcasts")
    public Result a_broadcasts(HttpServletRequest request,@RequestBody String content){
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Xuser xuser = (Xuser) params[1];
        if(xuser==null)throw new NullPointerException();
        Broadcasts broadcasts = new Broadcasts();
        broadcasts.setContent(content);
        Broadcasts save = broadcastServiceImp.getRepository().save(broadcasts);
        return RUtils.success(save);
    }



    @ApiOperation("获得一部分申请列表     size")
    @GetMapping("/a_broadcasts")
    public Result a_broadcasts(HttpServletRequest request,int size) {
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Xuser xuser = (Xuser) params[1];
        if(xuser==null)throw new NullPointerException();
        List<Orde> all = applyServiceImp.getRepository().getAll(size, size + 15);
        return RUtils.success(all);
    }




}
