package com.jerry.jtakeaway.controller;

import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.*;
import com.jerry.jtakeaway.exception.JException;
import com.jerry.jtakeaway.requestBean.pay;
import com.jerry.jtakeaway.requestBean.payBean;
import com.jerry.jtakeaway.responseBean.PayMoney;
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

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Api(description = "普通用户")
@RestController
@RequestMapping("/N")
//@SuppressWarnings("all")
public class NController {
    @Resource
    JwtUtils jwtUtils;

    @Resource
    MsgServiceImp msgServiceImp;

    @Resource
    UserServiceImp userServiceImp;

    @Resource
    SuserServiceImp suserServiceImp;

    @Resource
    NuserServiceImp nuserServiceImp;

    @Resource
    WalletServiceImp walletServiceImp;

    @Resource
    MenusServiceImp menusServiceImp;

    @Resource
    ConponServiceImp conponServiceImp;

    @Resource
    TransactionServiceImp transactionServiceImp;

    @Resource
    OrdeServiceImp ordeServiceImp;

    @Resource
    UserConponServiceImp userConponServiceImp;

    @Resource
    AddressServiceImp addressServiceImp;

    @ApiOperation("生成订单 ")
    @GetMapping("/order")
    public Result order(HttpServletRequest request, int sId, int mId, int mSize,int addressId) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user1 = JSONObject.toJavaObject(jsonObject, User.class);
        User user = userServiceImp.getRepository().findByAccount(user1.getAccount());
        //先判断该订单是否存在 userid 与 suserid 同时有
        Orde order = new Orde();
        order.setCreatedTime(new Timestamp(new Date().getTime()));
        order.setSuserid(sId);
        order.setNuserid(user.getUserdetailsid());
        order.setStatusid(1);
        JSONObject json = new JSONObject();
        json.put(String.valueOf(mId), mSize);
        order.setMenus(json.toJSONString());
        UUID uuid = UUID.randomUUID();
        order.setUuid(uuid.toString());
        Menus menus = menusServiceImp.getRepository().findById(mId).orElse(null);
        Address address = addressServiceImp.getRepository().findById(addressId).orElse(null);

        JSONObject detailJson = new JSONObject();
        detailJson.put("menu",menus);
        detailJson.put("address",address);
        order.setDetailedinformation(detailJson.toString());

        ordeServiceImp.getRepository().saveAndFlush(order);
        Orde saveAndFlush = ordeServiceImp.getRepository().findByUuid(uuid.toString());
        System.out.println("生成订单的id" + saveAndFlush.getId());
        return RUtils.success(saveAndFlush);
    }

    @ApiOperation("使用优惠卷支付金额")
    @GetMapping("/coupon_pay_money")
    public Result coupon_pay_money(HttpServletRequest request, int orderid, int couponid) {
        System.out.println("获得使用优惠卷金额");
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        if (user.getUsertype() == 0) {
            Nuser nuser = nuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
            if (nuser == null) return RUtils.Err(Renum.USER_NOT_EXIST.getCode(), Renum.USER_NOT_EXIST.getMsg());
            else {
                Orde order = ordeServiceImp.getRepository().findById(orderid).orElse(null);
                if (order == null) return RUtils.Err(Renum.NO_ORDE.getCode(), Renum.NO_ORDE.getMsg());
                else {
                    //计算总价格
                    double money = 0.00;
                    JSONObject json = (JSONObject) JSONObject.parse(order.getMenus());
                    Map<String, Object> map = json;
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        System.out.println(entry.getKey() + "=" + entry.getValue());
                        int key = Integer.parseInt(entry.getKey());
                        int value = (int) entry.getValue();

                        Menus menus = menusServiceImp.getRepository().findById(key).orElse(null);
                        assert menus != null;
                        if (menus.getFoodlowprice() != null) {
                            if (menus.getLowpricefailed() != null) {
                                Date foodlowfila = menus.getLowpricefailed();
                                Date now1 = new Date();
                                if (now1.after(foodlowfila)) {
                                    money = money + menus.getFoodprice() * value;
                                } else {
                                    money = money + menus.getFoodlowprice() * value;
                                }
                            } else {
                                money = money + menus.getFoodlowprice() * value;
                            }
                        } else {
                            money = money + menus.getFoodprice() * value;
                        }
                    }
                    System.out.println("seurid:" + order.getSuserid());
                    Suser suser = suserServiceImp.getRepository().findById(order.getSuserid()).orElse(null);
                    if (suser == null) throw new NullPointerException();

                    //计算优惠卷使用后价格
                    Coupon coupon = conponServiceImp.getRepository().findById(couponid).orElse(null);
                    if (coupon == null) return RUtils.Err(Renum.NO_CONPON.getCode(), Renum.NO_CONPON.getMsg());

                    //判断用户是否有此优惠卷
                    Userconpon userconpon = userConponServiceImp.getRepository().findByConponidAndNuserid(coupon.getId(), nuser.getId()).get(0);
                    if (userconpon == null)
                        return RUtils.Err(Renum.USER_NO_CONPON.getCode(), Renum.USER_NO_CONPON.getMsg());
                    //判断优惠卷是否有效
                    if (userconpon.getStatus() == 1) {//已被使用
                        return RUtils.Err(Renum.CONPON_USE.getCode(), Renum.CONPON_USE.getMsg());
                    }
                    Date now = new Date();
                    Date couponDate = coupon.getConponfailuretime();
                    int compareTo = now.compareTo(couponDate);
                    if (compareTo > 0) return RUtils.Err(Renum.CONPON_FAIL.getCode(), Renum.CONPON_FAIL.getMsg());
                    if (coupon.getConpontarget() == null){
                        money = money * coupon.getConponprice();
                    }else if( coupon.getConpontarget() == suser.getId()){
                        money = money * coupon.getConponprice();
                    } else return RUtils.Err(Renum.CONPON_NO_ZQ.getCode(), Renum.CONPON_NO_ZQ.getMsg());
                    System.out.println("总价:" + money);
                    return RUtils.success(new PayMoney(String.valueOf(money)));
                }
            }
        } else {
            return RUtils.Err(Renum.PREMS_FAIL.getCode(), Renum.PREMS_FAIL.getMsg());
        }
    }

    @Resource
    WebSocketUtils webSocketUtils;

    @ApiOperation("使用优惠卷支付 密码需要md5加密后再传输")
    @PostMapping("/coupon_pay")
    public Result coupon_pay(HttpServletRequest request, @RequestBody payBean pay_bean) {
        System.out.println("使用优惠卷支付");

        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        if (user.getUsertype() == 0) {
            Nuser nuser = nuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
            if (nuser == null) return RUtils.Err(Renum.USER_NOT_EXIST.getCode(), Renum.USER_NOT_EXIST.getMsg());
            Wallet wallet = walletServiceImp.getRepository().findById(nuser.getWallet()).orElse(null);
            if (wallet == null) return RUtils.Err(Renum.NO_WALLTE.getCode(), Renum.NO_WALLTE.getMsg());
            else {
                Orde order = ordeServiceImp.getRepository().findById(pay_bean.getOrdeId()).orElse(null);
                if (order == null) return RUtils.Err(Renum.NO_ORDE.getCode(), Renum.NO_ORDE.getMsg());
                User sUser = userServiceImp.getRepository().findByUsertypeAndUserdetailsid(1, order.getSuserid());
                if (sUser == null) throw new NullPointerException();
                else {
                    //计算总价格
                    double money = 0.00;
                    JSONObject json = (JSONObject) JSONObject.parse(order.getMenus());
                    Map<String, Object> map = json;
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        System.out.println(entry.getKey() + "=" + entry.getValue());
                        int key = Integer.parseInt(entry.getKey());
                        int value = (int) entry.getValue();

                        Menus menus = menusServiceImp.getRepository().findById(key).orElse(null);
                        assert menus != null;
                        if (menus.getFoodlowprice() != null) {
                            if (menus.getLowpricefailed() != null) {
                                Date foodlowfila = menus.getLowpricefailed();
                                Date now1 = new Date();
                                if (now1.after(foodlowfila)) {
                                    money = money + menus.getFoodprice() * value;
                                } else {
                                    money = money + menus.getFoodlowprice() * value;
                                }
                            } else {
                                money = money + menus.getFoodlowprice() * value;
                            }
                        } else {
                            money = money + menus.getFoodprice() * value;
                        }
                    }
                    System.out.println("seurid:" + order.getSuserid());
                    Suser suser = suserServiceImp.getRepository().findById(order.getSuserid()).orElse(null);
                    if (suser == null) throw new NullPointerException();

                    //计算优惠卷使用后价格
                    Coupon coupon = conponServiceImp.getRepository().findById(pay_bean.getCouponId()).orElse(null);
                    if (coupon == null) return RUtils.Err(Renum.NO_CONPON.getCode(), Renum.NO_CONPON.getMsg());

                    //判断用户是否有此优惠卷
                    Userconpon userconpon = userConponServiceImp.getRepository().findByConponidAndNuserid(coupon.getId(), nuser.getId()).get(0);
                    if (userconpon == null)
                        return RUtils.Err(Renum.USER_NO_CONPON.getCode(), Renum.USER_NO_CONPON.getMsg());
                    //判断优惠卷是否有效
                    if (userconpon.getStatus() == 1) {//已被使用
                        return RUtils.Err(Renum.CONPON_USE.getCode(), Renum.CONPON_USE.getMsg());
                    }
                    Date now = new Date();
                    Date couponDate = coupon.getConponfailuretime();
                    int compareTo = now.compareTo(couponDate);
                    if (compareTo > 0) return RUtils.Err(Renum.CONPON_FAIL.getCode(), Renum.CONPON_FAIL.getMsg());
                    if (coupon.getConpontarget() == null)
                        money = money * coupon.getConponprice();
                    else if(coupon.getConpontarget() == suser.getId()){
                        money = money * coupon.getConponprice();
                    } else return RUtils.Err(Renum.CONPON_NO_ZQ.getCode(), Renum.CONPON_NO_ZQ.getMsg());
                    System.out.println("总价:" + money);
                    if (!pay_bean.getPayPassword().equals(wallet.getPaymentpassword()))
                        return RUtils.Err(Renum.PAYPAS_FAIL.getCode(), Renum.PAYPAS_FAIL.getMsg());
                    if (wallet.getBalance() > money) {
                        //钱包扣钱
                        wallet.setBalance(wallet.getBalance() - money);
                        //创建交易记录
                        String uuid = UUID.randomUUID().toString();
                        Jtransaction transaction = new Jtransaction();
                        transaction.setCouponid(pay_bean.getCouponId());
                        transaction.setMore("支付给"+sUser.getUsernickname()+"了$-"+money);
                        transaction.setPaymoney(-money);
                        transaction.setPaytime(new Timestamp(new Date().getTime()));
                        transaction.setUserid(user.getId());
                        transaction.setTargetuserid(sUser.getId());
                        transaction.setUuid(uuid);
                        transactionServiceImp.getRepository().saveAndFlush(transaction);
                        transaction = transactionServiceImp.getRepository().findByUuid(uuid);
                        if (wallet.getTransactionid()==null||"".equals(wallet.getTransactionid())) {
                            wallet.setTransactionid(String.valueOf(transaction.getId()));
                        } else {
                            wallet.setTransactionid(wallet.getTransactionid() + ":" + transaction.getId());
                        }
                        walletServiceImp.getRepository().saveAndFlush(wallet);

                        //设置优惠将已用
                        userconpon.setStatus(1);
                        userConponServiceImp.getRepository().saveAndFlush(userconpon);
                        User nUser = userServiceImp.getRepository().findByUsertypeAndUserdetailsid(0, order.getNuserid());

                        Wallet shopWallet = walletServiceImp.getRepository().findById(suser.getWalletid()).orElse(null);
                        if (shopWallet == null) throw new NullPointerException();
                        shopWallet.setBalance(shopWallet.getBalance() + money / coupon.getConponprice());
                        Jtransaction shopTransaction = new Jtransaction();
                        shopTransaction.setCouponid(pay_bean.getCouponId());
                        shopTransaction.setMore("收到"+nUser.getUsernickname()+"$"+money);
                        shopTransaction.setPaymoney(money);
                        shopTransaction.setPaytime(new Timestamp(new Date().getTime()));
                        shopTransaction.setUserid(sUser.getId());
                        shopTransaction.setTargetuserid(user.getId());
                        String uuid2 = UUID.randomUUID().toString();
                        shopTransaction.setUuid(uuid2);
                        transactionServiceImp.getRepository().saveAndFlush(shopTransaction);
                        shopTransaction = transactionServiceImp.getRepository().findByUuid(uuid2);
                        if (shopWallet.getTransactionid()==null||shopWallet.getTransactionid().equals("")) {
                            shopWallet.setTransactionid(String.valueOf(shopTransaction.getId()));
                        } else {
                            shopWallet.setTransactionid(shopWallet.getTransactionid() + ":" + shopTransaction.getId());
                        }
                        walletServiceImp.getRepository().saveAndFlush(shopWallet);
                        order.setStatusid(2);
                        ordeServiceImp.getRepository().saveAndFlush(order);

                        Msg msg = new Msg();
                        msg.setAcceptuserid(sUser.getId());
                        msg.setContent("用户"+user.getUsernickname()+"下了订单");
                        msg.setSendTime(new Timestamp(new Date().getTime()));
                        msg.setReadalready(0);
                        msg.setPushalready(0);
                        msgServiceImp.getRepository().saveAndFlush(msg);
                        if(webSocketUtils.sendMessageToTargetUser(JSONObject.toJSONString(msg),sUser)){
                            msg.setPushalready(1);
                            msgServiceImp.getRepository().saveAndFlush(msg);
                        }
                        return RUtils.success(order);
                    } else return RUtils.Err(Renum.NO_MONEY.getCode(), Renum.NO_MONEY.getMsg());
                }
            }
        } else {
            return RUtils.Err(Renum.PREMS_FAIL.getCode(), Renum.PREMS_FAIL.getMsg());
        }
    }

    @ApiOperation("直接支付金额")
    @GetMapping("/pay_money")
    public Result pay_money(HttpServletRequest request, int orderid) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        if (user.getUsertype() == 0) {
            Orde order = ordeServiceImp.getRepository().findById(orderid).orElse(null);
            if (order == null) return RUtils.Err(Renum.NO_ORDE.getCode(), Renum.NO_ORDE.getMsg());
            User sUser = userServiceImp.getRepository().findByUsertypeAndUserdetailsid(1, order.getSuserid());
            if (sUser == null) throw new NullPointerException();
            else {
                //计算总价格
                double money = 0.00;
                JSONObject json = (JSONObject) JSONObject.parse(order.getMenus());
                Map<String, Object> map = json;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    System.out.println(entry.getKey() + "=" + entry.getValue());
                    int key = Integer.parseInt(entry.getKey());
                    int value = (int) entry.getValue();

                    Menus menus = menusServiceImp.getRepository().findById(key).orElse(null);
                    assert menus != null;
                    if (menus.getFoodlowprice() != null) {
                        if (menus.getLowpricefailed() != null) {
                            Date foodlowfila = menus.getLowpricefailed();
                            Date now1 = new Date();
                            if (now1.after(foodlowfila)) {
                                money = money + menus.getFoodprice() * value;
                            } else {
                                money = money + menus.getFoodlowprice() * value;
                            }
                        } else {
                            money = money + menus.getFoodlowprice() * value;
                        }
                    } else {
                        money = money + menus.getFoodprice() * value;
                    }
                }
                System.out.println("总价:" + money);
                return RUtils.success(new PayMoney(String.valueOf(money)));
            }
        } else return RUtils.Err(Renum.PREMS_FAIL.getCode(), Renum.PREMS_FAIL.getMsg());
    }

    @ApiOperation("直接支付 密码需要md5加密后再传输")
    @PostMapping("/pay")
    public Result pay(HttpServletRequest request, @RequestBody pay pay_bean) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        if (user.getUsertype() == 0) {
            Nuser nuser = nuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
            if (nuser == null) return RUtils.Err(Renum.USER_NOT_EXIST.getCode(), Renum.USER_NOT_EXIST.getMsg());
            Wallet wallet = walletServiceImp.getRepository().findById(nuser.getWallet()).orElse(null);
            if (wallet == null) return RUtils.Err(Renum.NO_WALLTE.getCode(), Renum.NO_WALLTE.getMsg());
            else {
                Orde order = ordeServiceImp.getRepository().findById(pay_bean.getOrdeId()).orElse(null);
                if (order == null) return RUtils.Err(Renum.NO_ORDE.getCode(), Renum.NO_ORDE.getMsg());
                User sUser = userServiceImp.getRepository().findByUsertypeAndUserdetailsid(1, order.getSuserid());
                if (sUser == null) throw new NullPointerException();
                else {
                    //计算总价格
                    double money = 0.00;
                    JSONObject json = (JSONObject) JSONObject.parse(order.getMenus());
                    Map<String, Object> map = json;
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        System.out.println(entry.getKey() + "=" + entry.getValue());
                        int key = Integer.parseInt(entry.getKey());
                        int value = (int) entry.getValue();

                        Menus menus = menusServiceImp.getRepository().findById(key).orElse(null);
                        assert menus != null;
                        if (menus.getFoodlowprice() != null) {
                            if (menus.getLowpricefailed() != null) {
                                Date foodlowfila = menus.getLowpricefailed();
                                Date now1 = new Date();
                                if (now1.after(foodlowfila)) {
                                    money = money + menus.getFoodprice() * value;
                                } else {
                                    money = money + menus.getFoodlowprice() * value;
                                }
                            } else {
                                money = money + menus.getFoodlowprice() * value;
                            }
                        } else {
                            money = money + menus.getFoodprice() * value;
                        }
                    }
                    System.out.println("总价:" + money);
                    if (!pay_bean.getPayPassword().equals(wallet.getPaymentpassword()))
                        return RUtils.Err(Renum.PAYPAS_FAIL.getCode(), Renum.PAYPAS_FAIL.getMsg());
                    if (wallet.getBalance() > money) {
                        //钱包扣钱
                        wallet.setBalance(wallet.getBalance() - money);
                        //创建交易记录
                        String uuid = UUID.randomUUID().toString();
                        Jtransaction transaction = new Jtransaction();
                        transaction.setMore("支付给"+sUser.getUsernickname()+"了$ -"+money);
                        transaction.setPaymoney(-money);
                        transaction.setPaytime(new Timestamp(new Date().getTime()));
                        transaction.setUserid(user.getId());
                        transaction.setTargetuserid(sUser.getId());
                        transaction.setUuid(uuid);
                        transactionServiceImp.getRepository().saveAndFlush(transaction);
                        transaction = transactionServiceImp.getRepository().findByUuid(uuid);
                        System.out.println("第一次保存");
                        if (wallet.getTransactionid().equals("")) {
                            wallet.setTransactionid(String.valueOf(transaction.getId()));
                        } else {
                            wallet.setTransactionid(wallet.getTransactionid() + ":" + transaction.getId());
                        }
                        walletServiceImp.getRepository().saveAndFlush(wallet);
                        System.out.println("第二次保存");

                        System.out.println("seurid:" + order.getSuserid());
                        Suser suser = suserServiceImp.getRepository().findById(order.getSuserid()).orElse(null);
                        if (suser == null) throw new NullPointerException();
                        User nUser = userServiceImp.getRepository().findByUsertypeAndUserdetailsid(0, order.getNuserid());

                        Wallet shopWallet = walletServiceImp.getRepository().findById(suser.getWalletid()).orElse(null);
                        if (shopWallet == null) throw new NullPointerException();
                        shopWallet.setBalance(shopWallet.getBalance() + money);
                        Jtransaction shopTransaction = new Jtransaction();
                        shopTransaction.setMore("收到"+nUser.getUsernickname()+"$"+money);
                        shopTransaction.setPaymoney(money);
                        shopTransaction.setPaytime(new Timestamp(new Date().getTime()));
                        shopTransaction.setUserid(sUser.getId());
                        shopTransaction.setTargetuserid(user.getId());
                        String uuid2 = UUID.randomUUID().toString();
                        shopTransaction.setUuid(uuid2);
                        transactionServiceImp.getRepository().saveAndFlush(shopTransaction);
                        shopTransaction = transactionServiceImp.getRepository().findByUuid(uuid2);
                        System.out.println("第三次保存");
                        if (shopWallet.getTransactionid()==null||"".equals(shopWallet.getTransactionid())) {
                            shopWallet.setTransactionid(String.valueOf(shopTransaction.getId()));
                            System.out.println("交易记录为空");
                        } else {
                            shopWallet.setTransactionid(shopWallet.getTransactionid() + ":" + shopTransaction.getId());
                            System.out.println("交易记录不为空");
                        }
                        walletServiceImp.getRepository().saveAndFlush(shopWallet);
                        order.setStatusid(2);
                        ordeServiceImp.getRepository().saveAndFlush(order);
                        System.out.println("第四次保存");

                        Msg msg = new Msg();
                        msg.setAcceptuserid(sUser.getId());
                        msg.setContent("用户"+user.getUsernickname()+"下了订单");
                        msg.setSendTime(new Timestamp(new Date().getTime()));
                        msg.setReadalready(0);
                        msg.setPushalready(0);
                        msgServiceImp.getRepository().save(msg);
                        if(webSocketUtils.sendMessageToTargetUser(JSONObject.toJSONString(msg),sUser)){
                            msg.setPushalready(1);
                            msgServiceImp.getRepository().saveAndFlush(msg);
                        }

                        return RUtils.success(order);
                    } else return RUtils.Err(Renum.NO_MONEY.getCode(), Renum.NO_MONEY.getMsg());
                }
            }
        }
        return RUtils.success();
    }


    @ApiOperation("获得可领取的优惠卷")
    @GetMapping("/coupons")
    public Result coupons() {
        List<Coupon> coupons = new ArrayList<Coupon>();
        List<Coupon> rCoupons = new ArrayList<Coupon>();
        coupons = conponServiceImp.getRepository().findAll();
        for (Coupon coupon : coupons) {
            //判断优惠卷是否有效
            if (coupon.getNum() > 0) {
                Date now = new Date();
                Date couponDate = coupon.getConponfailuretime();
                int compareTo = now.compareTo(couponDate);
                if (compareTo < 0) rCoupons.add(coupon);
            }
        }
        return RUtils.success(rCoupons);
    }

    @ApiOperation("领取优惠卷")
    @GetMapping("/c_coupon")
    public Result c_coupon(HttpServletRequest request, int conponid) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        Nuser nuser = nuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
        if (nuser == null) throw new JException(Renum.UNKNOWN_ERROR.getCode(), Renum.UNKNOWN_ERROR.getMsg());
        Coupon coupon = conponServiceImp.getRepository().findById(conponid).orElse(null);
        if (coupon == null) throw new JException(Renum.UNKNOWN_ERROR.getCode(), Renum.UNKNOWN_ERROR.getMsg());
        if (coupon.getNum() <= 0) return RUtils.Err(Renum.CONPON_WAM.getCode(), Renum.CONPON_WAM.getMsg());
        Userconpon userconpon = new Userconpon();
        userconpon.setConponid(conponid);
        userconpon.setNuserid(nuser.getId());
        userconpon.setCreatetime(new Timestamp(new Date().getTime()));
        userConponServiceImp.getRepository().saveAndFlush(userconpon);
        coupon.setNum(coupon.getNum() - 1);
        conponServiceImp.getRepository().saveAndFlush(coupon);
        return RUtils.success();
    }

    @ApiOperation("已领优惠卷")
    @GetMapping("/m_coupon")
    public Result m_coupon(HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        Nuser nuser = nuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
        List<Userconpon> userconpons = new ArrayList<Userconpon>();
        assert nuser != null;
        userconpons = userConponServiceImp.getRepository().findByNuserid(nuser.getId());
        List<Coupon> coupons = new ArrayList<Coupon>();
        for (Userconpon userconpon : userconpons) {
            Coupon coupon = conponServiceImp.getRepository().findById(userconpon.getConponid()).orElse(null);
            if (coupon != null) coupons.add(coupon);
        }
        return RUtils.success(coupons);
    }

    @ApiOperation("已领取可用优惠卷")
    @GetMapping("/can_use_coupon")
    public Result can_use_coupon(HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        Nuser nuser = nuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
        List<Userconpon> userconpons = new ArrayList<Userconpon>();
        assert nuser != null;
        userconpons = userConponServiceImp.getRepository().findByNuserid(nuser.getId());
        List<Coupon> coupons = new ArrayList<Coupon>();
        for (Userconpon userconpon : userconpons) {
            if(userconpon.getStatus()==0){
                Coupon coupon = conponServiceImp.getRepository().findById(userconpon.getConponid()).orElse(null);
                if (coupon != null) {
                    Date failedDate = coupon.getConponfailuretime();
                    Date now = new Date();
                    if(now.before(failedDate)){
                        coupons.add(coupon);
                    }
                }
            }
        }
        return RUtils.success(coupons);
    }

    @ApiOperation("指定商家可用优惠卷")
    @GetMapping("/shop_can_use_coupon")
    public Result shop_can_use_coupon(HttpServletRequest request,int suserid) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        Nuser nuser = nuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
        List<Userconpon> userconpons = new ArrayList<Userconpon>();
        assert nuser != null;
        userconpons = userConponServiceImp.getRepository().findByNuserid(nuser.getId());
        List<Coupon> coupons = new ArrayList<Coupon>();
        for (Userconpon userconpon : userconpons) {
            if(userconpon.getStatus()==0) {
                Coupon coupon = conponServiceImp.getRepository().findById(userconpon.getConponid()).orElse(null);
                if (coupon != null) {
                    Date failedDate = coupon.getConponfailuretime();
                    Date now = new Date();
                    if(now.before(failedDate)){
                        if(coupon.getConpontarget()!= null){
                            if(coupon.getConpontarget() == suserid)coupons.add(coupon);
                        }else{
                            coupons.add(coupon);
                        }
                    }
                }
            }
        }
        return RUtils.success(coupons);
    }

    @Resource
    OrderStatusServiceImp orderStatusServiceImp;

    @ApiOperation("获得订单列表 传入size")
    @GetMapping("/orders")
    public Result orders(HttpServletRequest request, int size) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        //获得普通用户订单
        Nuser nuser = nuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
        List<Orde> orders = new ArrayList<Orde>();
        if (nuser == null) throw new NullPointerException();
        else {
            orders = ordeServiceImp.getRepository().getAll(size, size + 10, nuser.getId());
            return RUtils.success(getResponseOrders(orders,user));
        }
    }

    @ApiOperation("获得所有订单")
    @GetMapping("/all_orders")
    public Result all_orders(HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        //获得普通用户订单
        Nuser nuser = nuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
        List<Orde> orders = new ArrayList<Orde>();
        if (nuser == null) throw new NullPointerException();
        else {
            orders = ordeServiceImp.getRepository().findAll();

            return RUtils.success(getResponseOrders(orders,user));
        }
    }


    private List<ResponseOrder> getResponseOrders(List<Orde> orders,User user){
        List<ResponseOrder> responseOrders = new ArrayList<ResponseOrder>();
        for (Orde o:orders) {
            ResponseOrder responseOrder = new ResponseOrder();
            User suser = userServiceImp.getRepository().findByUsertypeAndUserdetailsid(1,o.getSuserid());
            responseOrder.setSuser(suser);
            if(o.getHuserid()!= null){
                User huser = userServiceImp.getRepository().findByUsertypeAndUserdetailsid(2,o.getHuserid());
                responseOrder.setHuser(huser);
            }
            Orderstatus orderstatus = orderStatusServiceImp.getRepository().findById(o.getStatusid()).orElse(null);
            if(orderstatus!= null){
                responseOrder.setStatus(orderstatus);
            }
            JSONObject menuJsons = JSONObject.parseObject(o.getMenus());
            for (Map.Entry<String, Object> entry : menuJsons.entrySet()) {
                Menus mMenus = menusServiceImp.getRepository().findById(Integer.valueOf(entry.getKey())).orElse(null);
                responseOrder.setMenuSize((Integer) entry.getValue());
                if(mMenus!= null){
                    responseOrder.setMenus(mMenus);
                }
            }
            Suser ssuser = suserServiceImp.getRepository().findById(o.getSuserid()).orElse(null);
            if(ssuser!=null) responseOrder.setSsuser(ssuser);

            responseOrder.setId(o.getId());
            responseOrder.setCreatedTime(o.getCreatedTime());
            responseOrder.setDetailedinformation(o.getDetailedinformation());
            responseOrder.setLevel(o.getLevel());
            responseOrder.setUuid(o.getUuid());
            responseOrder.setNuser(user);
            responseOrders.add(responseOrder);
        }
        return responseOrders;
    }

    @ApiOperation("获得所有未结账订单responseorder")
    @GetMapping("/all_no_pay_responseorders")
    public Result all_no_pay_responseorders(HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        //获得普通用户订单
        Nuser nuser = nuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
        List<Orde> orders = new ArrayList<Orde>();
        List<Orde> rOrders = new ArrayList<Orde>();
        if (nuser == null) throw new NullPointerException();
        else {
            orders = ordeServiceImp.getRepository().findAll();
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getStatusid() == 1) {
                    rOrders.add(orders.get(i));
                }
            }
            return RUtils.success(getResponseOrders(rOrders,user));
        }
    }

    @ApiOperation("获得所有未结账订单order")
    @GetMapping("/all_no_pay_orders")
    public Result all_no_pay_orders(HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        //获得普通用户订单
        Nuser nuser = nuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
        List<Orde> orders = new ArrayList<Orde>();
        List<Orde> rOrders = new ArrayList<Orde>();
        if (nuser == null) throw new NullPointerException();
        else {
            orders = ordeServiceImp.getRepository().findAll();
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getStatusid() == 1) {
                    rOrders.add(orders.get(i));
                }
            }
            return RUtils.success(rOrders);
        }
    }

    @ApiOperation("获得指定订单 传入id")
    @GetMapping("/g_order")
    public Result g_order(HttpServletRequest request, int id) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        //获得普通用户订单
        Nuser nuser = nuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
        if (nuser == null) throw new NullPointerException();
        //先判断是否有此订单
        Orde orde = ordeServiceImp.getRepository().findByNuseridAndId(nuser.getId(), id);
        if (orde == null) throw new NullPointerException();
        List<Orde> ordes = new ArrayList<Orde>();
        ordes.add(orde);
        return RUtils.success(getResponseOrders(ordes,user));
    }

    @ApiOperation(".获得一部分消息   size")
    @GetMapping("/msgs")
    public Result msgs(HttpServletRequest request, int size) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        //获得普通用户订单
        Nuser nuser = nuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
        if (nuser == null) throw new NullPointerException();
        List<Msg> msgs = new ArrayList<>();
        msgs = msgServiceImp.getRepository().getAll(size, size + 15, nuser.getId());
        return RUtils.success(msgs);
    }

    @Resource
    HttpServletRequest request;

    @ApiOperation("评分")
    @GetMapping("/s_level")
    public Result s_level(int orderid, Double level) throws IOException {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        Nuser nuser = nuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
        if (nuser == null) throw new NullPointerException();
        Orde orde = ordeServiceImp.getRepository().findById(orderid).orElse(null);
        if (orde == null) throw new NullPointerException();
        if (orde.getNuserid() != nuser.getId()) throw new IllegalArgumentException();

        Suser suser = suserServiceImp.getRepository().findById(orde.getSuserid()).orElse(null);
        if (suser == null) throw new NullPointerException();
        double all_level = suser.getAlllevel();
        int time = suser.getLeveltime();
        double rLevel = (all_level + level) / time + 1;
        suser.setLeveltime(time + 1);
        suser.setAlllevel(all_level + level);
        suser.setLevel(rLevel);
        suserServiceImp.getRepository().saveAndFlush(suser);
        orde.setLevel(level);
        ordeServiceImp.getRepository().saveAndFlush(orde);
        return RUtils.success(suser);
    }

}
