package com.jerry.jtakeaway.controller;


import com.jerry.jtakeaway.bean.Apply;
import com.jerry.jtakeaway.bean.Suser;
import com.jerry.jtakeaway.service.imp.ApplyServiceImp;
import com.jerry.jtakeaway.service.imp.SuserServiceImp;
import com.jerry.jtakeaway.service.imp.UserServiceImp;
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
import java.util.Optional;

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

    @ApiOperation("获取热门商家 传入size")
    @GetMapping("/hot_shop")
    public Result hot_shop(int size){


    }

}
