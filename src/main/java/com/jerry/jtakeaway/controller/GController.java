package com.jerry.jtakeaway.controller;


import com.jerry.jtakeaway.bean.*;
import com.jerry.jtakeaway.service.imp.*;
import com.jerry.jtakeaway.utils.RUtils;
import com.jerry.jtakeaway.utils.bean.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Api(description = "公共接口")
@RestController
@RequestMapping("/G")
public class GController {
    @Resource
    UserServiceImp userServiceImp;

    @Resource
    SuserServiceImp suserServiceImp;

    @Resource
    ApplyServiceImp applyServiceImp;

    @Resource
    WalletServiceImp walletRepository;

    @Resource
    TransactionServiceImp transactionServiceImp;

    @Resource
    SlideServiceImp slideServiceImp;

    @Resource
    BroadcastServiceImp broadcastServiceImp;

    @ApiOperation("获取商家列表 传入大小")
    @GetMapping("/shops")
    public Result shops(int size){
        System.out.println("请求者来了");
        List<Suser> surerList =suserServiceImp.getRepository().shopList(size,size+15);
        List<Suser> resultList = new ArrayList<>();
        for (int i = 0; i < surerList.size(); i++) {
            if(surerList.get(i).getApplyid()!=0){
                Apply apply = applyServiceImp.getRepository().findById(surerList.get(i).getApplyid()).orElse(null);
                if(apply!=null){
                    if(apply.getAuditstatus()==2)resultList.add(surerList.get(i));
                }
            }
        }
        return RUtils.success(resultList);
    }

    @ApiOperation("获取商家 传入id")
    @GetMapping("/shop")
    public Result shop(int id){
        System.out.println("请求者来了");
        Suser suser =suserServiceImp.getRepository().findById(id).orElse(null);
        if(suser!=null)
        if(suser.getApplyid()!=0){
            Apply apply = applyServiceImp.getRepository().findById(suser.getApplyid()).orElse(null);
            if(apply!= null){
                if(apply.getAuditstatus()==2){
                    suser.setIdcard(null);
                    suser.setShopaddress(null);
                    suser.setSlideid(null);
                    suser.setWalletid(null);
                    suser.setApplyid(0);
                    return RUtils.success(suser);
                }
            }
        }
        return RUtils.success();
    }

    @ApiOperation("获取热门商家")
    @GetMapping("/hot_shop")
    public Result hot_shop(){
            List<Suser> susers = new ArrayList<>();
            List<Suser> rSusers = new ArrayList<>();
            susers = suserServiceImp.getRepository().findAll();
            for(Suser s:susers){
                Wallet wallet = walletRepository.getRepository().findById(s.getWalletid()).orElse(null);
                if(wallet != null){
                    String[] deals = wallet.getTransactionid().split(":");
                    if(deals.length>=3){
                        s.setWalletid(null);
                        s.setSlideid(null);
                        s.setShopaddress(null);
                        s.setIdcard("");
                        s.setShoplicense("");
                        rSusers.add(s);
                    }
                }
            }
            return RUtils.success(rSusers);
    }

    @ApiOperation("获取顶部轮播图")
    @GetMapping("/top_slides")
    public Result top_slides(){
        List<Slide> slides = new ArrayList<Slide>();
        List<Slide> rSlides = new ArrayList<Slide>();
        slides = slideServiceImp.getRepository().findAll();
        for(Slide slide : slides){
            User user = userServiceImp.getRepository().findById(slide.getUserid());
            if(user != null){
                if(user.getUsertype()==3){
                    rSlides.add(slide);
                }
            }
        }
        return RUtils.success(rSlides);
    }

    @ApiOperation("获得广播")
    @GetMapping("/broadcasts")
    public Result broadcasts(){
        List<Broadcasts> broadcasts = new ArrayList<>();
        broadcasts = broadcastServiceImp.getRepository().findAll();
        return RUtils.success(broadcasts);
    }
}
