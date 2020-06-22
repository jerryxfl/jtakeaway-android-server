package com.jerry.jtakeaway.controller;


import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.*;
import com.jerry.jtakeaway.exception.JException;
import com.jerry.jtakeaway.responseBean.ResponseUser;
import com.jerry.jtakeaway.service.imp.*;
import com.jerry.jtakeaway.utils.RUtils;
import com.jerry.jtakeaway.utils.bean.Renum;
import com.jerry.jtakeaway.utils.bean.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Api(description = "公共接口")
@RestController
@RequestMapping("/G")
public class GController {
    @Resource
    UserServiceImp userServiceImp;

    @Resource
    NuserServiceImp nusersServiceImp;

    @Resource
    HuserServiceImp huserServiceImp;

    @Resource
    SuserServiceImp suserServiceImp;

    @Resource
    ApplyServiceImp applyServiceImp;

    @Resource
    WalletServiceImp walletRepository;

    @Resource
    XuserServiceImp xuserServiceImp;

    @Resource
    CommentServiceImp commentServiceImp;

    @Resource
    TransactionServiceImp transactionServiceImp;

    @Resource
    SlideServiceImp slideServiceImp;

    @Resource
    BroadcastServiceImp broadcastServiceImp;

    @ApiOperation("获取商家列表 传入大小")
    @GetMapping("/shops")
    public Result shops(int size) {
        System.out.println("请求者来了");
        List<Suser> surerList = suserServiceImp.getRepository().shopList(size, size + 15);
        List<Suser> resultList = new ArrayList<>();
        for (int i = 0; i < surerList.size(); i++) {
            if (surerList.get(i).getApplyid() != 0) {
                Apply apply = applyServiceImp.getRepository().findById(surerList.get(i).getApplyid()).orElse(null);
                if (apply != null) {
                    if (apply.getAuditstatus() == 2) resultList.add(surerList.get(i));
                }
            }
        }
        return RUtils.success(resultList);
    }

    @ApiOperation("获取商家 传入id")
    @GetMapping("/shop")
    public Result shop(int id) {
        System.out.println("请求者来了");
        Suser suser = suserServiceImp.getRepository().findById(id).orElse(null);
        if (suser != null)
            if (suser.getApplyid() != 0) {
                Apply apply = applyServiceImp.getRepository().findById(suser.getApplyid()).orElse(null);
                if (apply != null) {
                    if (apply.getAuditstatus() == 2) {
                        suser.setIdcard(null);
                        suser.setWalletid(null);
                        return RUtils.success(suser);
                    }
                }
            }
        return RUtils.success();
    }

    @ApiOperation("获取热门商家")
    @GetMapping("/hot_shop")
    public Result hot_shop() {
        List<Suser> susers = new ArrayList<>();
        List<Suser> rSusers = new ArrayList<>();
        susers = suserServiceImp.getRepository().findAll();
        for (Suser s : susers) {
            Wallet wallet = walletRepository.getRepository().findById(s.getWalletid()).orElse(null);
            if (wallet != null) {
                if (wallet.getTransactionid() != null) {
                    String[] deals = wallet.getTransactionid().split(":");
                    if (deals.length >= 3) {
                        s.setWalletid(null);
                        s.setIdcard("");
                        s.setShoplicense("");
                        rSusers.add(s);
                    }
                }
            }
        }
        return RUtils.success(rSusers);
    }

    @ApiOperation("获取热门商家随机菜单")
    @GetMapping("/hot_shop_menu")
    public Result hot_shop_menu() {
        List<Suser> susers = new ArrayList<>();
        List<Menus> rMenus = new ArrayList<>();
        susers = suserServiceImp.getRepository().findAll();
        for (Suser s : susers) {
            Wallet wallet = walletRepository.getRepository().findById(s.getWalletid()).orElse(null);
            if (wallet != null) {
                if (wallet.getTransactionid() != null) {
                    String[] deals = wallet.getTransactionid().split(":");
                    if (deals.length >= 3) {
                        List<Menus> m = new ArrayList<>();
                        m = menusServiceImp.getRepository().findAll();
                        Random random = new Random();
                        int n = random.nextInt(m.size());
                        Menus menu = m.get(n);
                        rMenus.add(menu);
                    }
                }
            }
        }
        return RUtils.success(rMenus);
    }


    @ApiOperation("获取5星商家")
    @GetMapping("/five_level_shop")
    public Result five_level_shop(int size) {
        List<Suser> susers = new ArrayList<>();
        susers = suserServiceImp.getRepository().FiveLevelShopList(size, size + 10, 4);
        return RUtils.success(susers);
    }


    @ApiOperation("获取顶部轮播图")
    @GetMapping("/top_slides")
    public Result top_slides() {
        List<Slide> slides = new ArrayList<Slide>();
        List<Slide> rSlides = new ArrayList<Slide>();
        slides = slideServiceImp.getRepository().findAll();
        for (Slide slide : slides) {
            User user = userServiceImp.getRepository().findById(slide.getUserid());
            if (user != null) {
                if (user.getUsertype() == 3) {
                    rSlides.add(slide);
                }
            }
        }
        return RUtils.success(rSlides);
    }

    @ApiOperation("获得广播")
    @GetMapping("/broadcasts")
    public Result broadcasts() {
        List<Broadcasts> broadcasts = new ArrayList<>();
        broadcasts = broadcastServiceImp.getRepository().findAll();
        return RUtils.success(broadcasts);
    }


    @ApiOperation("获得用户信息  ")
    @GetMapping("/g_user_info")
    public Result g_user_info(int userid) {
        User user = userServiceImp.getRepository().findById(userid);
        user.setEmail("");
        user.setPhone("");
        user.setPassword("");
        switch (user.getUsertype()) {
            case 0:
                ResponseUser responseUser = new ResponseUser();
                Nuser nuser = nusersServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (nuser == null) throw new NullPointerException();
                responseUser.setUserdetails(JSONObject.toJSONString(nuser));
                return RUtils.success(responseUser);
            case 1:
                ResponseUser responseUser1 = new ResponseUser();
                Suser suser = suserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (suser == null) throw new NullPointerException();
                responseUser1.setUserdetails(JSONObject.toJSONString(suser));
                return RUtils.success(responseUser1);
            case 2:
                ResponseUser responseUser2 = new ResponseUser();
                Huser huser = huserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (huser == null) throw new NullPointerException();
                responseUser2.setUserdetails(JSONObject.toJSONString(huser));
                return RUtils.success(responseUser2);
            case 3:
                ResponseUser responseUser3 = new ResponseUser();
                Xuser xuser = xuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (xuser != null) throw new NullPointerException();
                responseUser3.setUserdetails(JSONObject.toJSONString(xuser));
                return RUtils.success(responseUser3);
            default:
                throw new IllegalArgumentException();
        }
    }

    @ApiOperation("获取商家评论")
    @GetMapping("/shop_comment")
    public Result shop_comment(int suserid) {
        Suser suser = suserServiceImp.getRepository().getOne(suserid);
        List<Comment> comments = commentServiceImp.getRepository().findBySuserid(suser.getId());
        return RUtils.success(comments);
    }

    @Resource
    MenusServiceImp menusServiceImp;

    @ApiOperation("获得指定商家一部分菜单 size")
    @GetMapping("/g_shops_menus")
    public Result g_shops_menus(int shopid, int size) {
        Suser suser = suserServiceImp.getRepository().findById(shopid).orElse(null);
        if (suser == null) throw new NullPointerException();
        List<Menus> menus = new ArrayList<Menus>();
        menus = menusServiceImp.getRepository().getAll(size, size + 15, suser.getId());
        return RUtils.success(menus);
    }


    @ApiOperation("获得热门菜单")
    @GetMapping("/g_hot_menus")
    public Result g_hot_menus() {
        List<Menus> menus = new ArrayList<Menus>();
        List<Menus> rMenus = new ArrayList<Menus>();
        menus = menusServiceImp.getRepository().findAll();
        Random rand = new Random();
        if (menus.size() > 10) {
            for (int i = 0; i < 10; i++) {
                Menus menu = menus.get(rand.nextInt(menus.size()));
                rMenus.add(menu);
                menus.remove(menu);
            }
        } else {
            for (int i = 0; i < menus.size(); i++) {
                Menus menu = menus.get(rand.nextInt(menus.size()));
                rMenus.add(menu);
                menus.remove(menu);
            }
        }
        return RUtils.success(rMenus);
    }


    @ApiOperation("获取商家轮播图")
    @GetMapping("/shop_slides")
    public Result shop_slides(int suserid) {
        List<Slide> slides = new ArrayList<Slide>();
        List<Slide> rSlides = new ArrayList<Slide>();
        slides = slideServiceImp.getRepository().findAll();
        for (Slide slide : slides) {
            User user = userServiceImp.getRepository().findById(slide.getUserid());
            if (user != null) if (user.getUsertype() == 1) if (user.getUserdetailsid() == suserid) rSlides.add(slide);
        }
        return RUtils.success(rSlides);
    }

}
