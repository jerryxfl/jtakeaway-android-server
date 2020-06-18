package com.jerry.jtakeaway.controller;

import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.*;
import com.jerry.jtakeaway.config.websocket.Greeting;
import com.jerry.jtakeaway.config.websocket.HelloMessage;
import com.jerry.jtakeaway.exception.JException;
import com.jerry.jtakeaway.requestBean.pay;
import com.jerry.jtakeaway.requestBean.payBean;
import com.jerry.jtakeaway.service.imp.*;
import com.jerry.jtakeaway.utils.JwtUtils;
import com.jerry.jtakeaway.utils.RUtils;
import com.jerry.jtakeaway.utils.bean.Renum;
import com.jerry.jtakeaway.utils.bean.Result;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(description="普通用户")
@RestController
@RequestMapping("/N")
@SuppressWarnings("all")
public class NController {

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    @Resource
    JwtUtils jwtUtils;

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
    MsgServiceImp msgServiceImp;

    @ApiOperation("生成订单 ")
    @GetMapping("/order")
    public Result order(HttpServletRequest request,int sId, int mId, int mSize){
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user1 = JSONObject.toJavaObject(jsonObject, User.class);
        User user = userServiceImp.getRepository().findByAccount(user1.getAccount());
        //先判断该订单是否存在 userid 与 suserid 同时有
        List<Orde> orders = new ArrayList<Orde>();
        orders = ordeServiceImp.getRepository().findByNuseridAndSuserid(user.getId(), sId);
        if(orders.isEmpty()){
            Orde order = new Orde();
            order.setCreatedTime(new Timestamp(new Date().getTime()));
            order.setSuserid(sId);
            order.setNuserid(user.getId());
            order.setStatusid(1);
            JSONObject json = new JSONObject();
            json.put(String.valueOf(mId),mSize);
            order.setMenus(json.toJSONString());
            ordeServiceImp.getRepository().save(order);
            return RUtils.success(order);
        }else{
            for(Orde o:orders){
                if(o.getStatusid()==1){
                    JSONObject json = (JSONObject) JSONObject.parse(o.getMenus());
                    json.put(String.valueOf(mId),mSize);
                    o.setMenus(json.toJSONString());
                    ordeServiceImp.getRepository().save(o);
                    return RUtils.success(o);
                }
            }
            Orde order = new Orde();
            order.setCreatedTime(new Timestamp(new Date().getTime()));
            order.setSuserid(sId);
            order.setNuserid(user.getId());
            order.setStatusid(1);
            JSONObject json = new JSONObject();
            json.put(String.valueOf(mId),mSize);
            order.setMenus(json.toJSONString());
            ordeServiceImp.getRepository().save(order);
            return RUtils.success(order);
        }
    }

    @ApiOperation("使用优惠卷支付 密码需要md5加密后再传输")
    @PostMapping("/coupon_pay")
    public Result coupon_pay(HttpServletRequest request,@RequestBody payBean pay_bean){
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        if(user.getUsertype()==0){
            Nuser nuser = nuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
            if(nuser==null)return RUtils.Err(Renum.USER_NOT_EXIST.getCode(),Renum.USER_NOT_EXIST.getMsg());
            Wallet wallet = walletServiceImp.getRepository().findById(nuser.getWallet()).orElse(null);
            if(wallet==null) return RUtils.Err(Renum.NO_WALLTE.getCode(),Renum.NO_WALLTE.getMsg());
            else{
                Orde order = ordeServiceImp.getRepository().findById(pay_bean.getOrdeId()).orElse(null);
                if(order==null) return RUtils.Err(Renum.NO_ORDE.getCode(),Renum.NO_ORDE.getMsg());
                User sUser = userServiceImp.getRepository().findByUsertypeAndUserdetailsid(1,order.getSuserid());
                if(sUser==null) throw new NullPointerException();
                else{
                    //计算总价格
                    double money = 0.00;
                    JSONObject json = (JSONObject) JSONObject.parse(order.getMenus());
                    Map<String, Object> map =json;
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        System.out.println(entry.getKey()+"="+entry.getValue());
                        int key = Integer.parseInt(entry.getKey());
                        int value =  (int)entry.getValue();

                        Menus menus = menusServiceImp.getRepository().findById(key).orElse(null);
                        assert menus != null;
                        if(menus.getFoodlowprice()!=null){
                            money = money + menus.getFoodlowprice().doubleValue()*value;
                        }else{
                            money = money + menus.getFoodprice().doubleValue()*value;
                        }
                    }
                    System.out.println("seurid:"+order.getSuserid());
                    Suser suser = suserServiceImp.getRepository().findById(order.getSuserid()).orElse(null);
                    if(suser==null) throw new NullPointerException();

                    //计算优惠卷使用后价格
                    Coupon coupon = conponServiceImp.getRepository().findById(pay_bean.getCouponId()).orElse(null);
                    //判断用户是否有此优惠卷
                    Userconpon userconpon = userConponServiceImp.getRepository().findByConponidAndNuserid(coupon.getId(),nuser.getId());
                    if(userconpon==null) return RUtils.Err(Renum.USER_NO_CONPON.getCode(),Renum.USER_NO_CONPON.getMsg());
                    if(coupon==null) return RUtils.Err(Renum.NO_CONPON.getCode(),Renum.NO_CONPON.getMsg());
                    //判断优惠卷是否有效
                    if(userconpon.getStatus()==1){//已被使用
                        return RUtils.Err(Renum.CONPON_NO_ZQ.getCode(),Renum.CONPON_NO_ZQ.getMsg());
                    }
                    Date now  = new Date();
                    Date couponDate = coupon.getConponfailuretime();
                    int compareTo = now.compareTo(couponDate);
                    if(compareTo > 0)return RUtils.Err(Renum.CONPON_FAIL.getCode(),Renum.CONPON_FAIL.getMsg());
                    if(coupon.getConpontarget()==null||coupon.getConpontarget()==suser.getId())
                        money = money*coupon.getConponprice().doubleValue();
                    else return RUtils.Err(Renum.CONPON_NO_ZQ.getCode(),Renum.CONPON_NO_ZQ.getMsg());
                    System.out.println("总价:"+money);
                    if(!pay_bean.getPayPassword().equals(wallet.getPaymentpassword())) return RUtils.Err(Renum.PAYPAS_FAIL.getCode(),Renum.PAYPAS_FAIL.getMsg());
                    if(wallet.getBalance().doubleValue()>money){
                        //钱包扣钱
                        wallet.setBalance(BigDecimal.valueOf(wallet.getBalance().doubleValue()-money));
                        //创建交易记录
                        String uuid = UUID.randomUUID().toString();
                        Transaction transaction = new Transaction();
                        transaction.setCouponid(pay_bean.getCouponId());
                        transaction.setMore("使用优惠卷购买了");
                        transaction.setPaymoney(BigDecimal.valueOf(money));
                        transaction.setPaytime(new Timestamp(new Date().getTime()));
                        transaction.setUserid(user.getId());
                        transaction.setTargetuserid(sUser.getId());
                        transaction.setUuid(uuid);
                        transactionServiceImp.getRepository().save(transaction);
                        transaction = transactionServiceImp.getRepository().findByUuid(uuid);
                        if(wallet.getTransactionid().equals("")){
                            wallet.setTransactionid(String.valueOf(transaction.getId()));
                        }else{
                            wallet.setTransactionid(wallet.getTransactionid()+":"+transaction.getId());
                        }
                        walletServiceImp.getRepository().save(wallet);

                        //设置优惠将已用
                        userconpon.setStatus(1);
                        userConponServiceImp.getRepository().save(userconpon);

                        Wallet shopWallet = walletServiceImp.getRepository().findById(suser.getWalletid()).orElse(null);
                        if(shopWallet==null) throw new NullPointerException();
                        shopWallet.setBalance(BigDecimal.valueOf(shopWallet.getBalance().doubleValue()+money/coupon.getConponprice().doubleValue()));
                        Transaction shopTransaction = new Transaction();
                        shopTransaction.setCouponid(pay_bean.getCouponId());
                        shopTransaction.setMore("被用优惠卷购买了");
                        shopTransaction.setPaymoney(BigDecimal.valueOf(money));
                        shopTransaction.setPaytime(new Timestamp(new Date().getTime()));
                        shopTransaction.setUserid(sUser.getId());
                        shopTransaction.setTargetuserid(user.getId());
                        String uuid2 = UUID.randomUUID().toString();
                        shopTransaction.setUuid(uuid2);
                        transactionServiceImp.getRepository().save(shopTransaction);

                        shopTransaction = transactionServiceImp.getRepository().findByUuid(uuid2);
                        if(shopWallet.getTransactionid().equals("")){
                            shopWallet.setTransactionid(String.valueOf(shopTransaction.getId()));
                        }else{
                            shopWallet.setTransactionid(shopWallet.getTransactionid()+":"+shopTransaction.getId());
                        }
                        walletServiceImp.getRepository().save(shopWallet);
                        order.setStatusid(2);
                        ordeServiceImp.getRepository().save(order);
                        return RUtils.success(order);
                    }else return RUtils.Err(Renum.NO_MONEY.getCode(), Renum.NO_MONEY.getMsg());
                }
            }
        }
        return RUtils.success();
    }


    @ApiOperation("直接支付 密码需要md5加密后再传输")
    @PostMapping("/pay")
    public Result pay(HttpServletRequest request,@RequestBody pay pay_bean){
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        if(user.getUsertype()==0){
            Nuser nuser = nuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
            if(nuser==null)return RUtils.Err(Renum.USER_NOT_EXIST.getCode(),Renum.USER_NOT_EXIST.getMsg());
            Wallet wallet = walletServiceImp.getRepository().findById(nuser.getWallet()).orElse(null);
            if(wallet==null) return RUtils.Err(Renum.NO_WALLTE.getCode(),Renum.NO_WALLTE.getMsg());
            else{
                Orde order = ordeServiceImp.getRepository().findById(pay_bean.getOrdeId()).orElse(null);
                if(order==null) return RUtils.Err(Renum.NO_ORDE.getCode(),Renum.NO_ORDE.getMsg());
                User sUser = userServiceImp.getRepository().findByUsertypeAndUserdetailsid(1,order.getSuserid());
                if(sUser==null) throw new NullPointerException();
                else{
                    //计算总价格
                    double money = 0.00;
                    JSONObject json = (JSONObject) JSONObject.parse(order.getMenus());
                    Map<String, Object> map =json;
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        System.out.println(entry.getKey()+"="+entry.getValue());
                        int key = Integer.parseInt(entry.getKey());
                        int value =  (int)entry.getValue();

                        Menus menus = menusServiceImp.getRepository().findById(key).orElse(null);
                        assert menus != null;
                        if(menus.getFoodlowprice()!=null){
                            money = money + menus.getFoodlowprice().doubleValue()*value;
                        }else{
                            money = money + menus.getFoodprice().doubleValue()*value;
                        }
                    }
                    System.out.println("总价:"+money);
                    if(!pay_bean.getPayPassword().equals(wallet.getPaymentpassword())) return RUtils.Err(Renum.PAYPAS_FAIL.getCode(),Renum.PAYPAS_FAIL.getMsg());
                    if(wallet.getBalance().doubleValue()>money){
                        //钱包扣钱
                        wallet.setBalance(BigDecimal.valueOf(wallet.getBalance().doubleValue()-money));
                        //创建交易记录
                        String uuid = UUID.randomUUID().toString();
                        Transaction transaction = new Transaction();
                        transaction.setMore("直接购买了");
                        transaction.setPaymoney(BigDecimal.valueOf(money));
                        transaction.setPaytime(new Timestamp(new Date().getTime()));
                        transaction.setUserid(user.getId());
                        transaction.setTargetuserid(sUser.getId());
                        transaction.setUuid(uuid);
                        transactionServiceImp.getRepository().save(transaction);
                        transaction = transactionServiceImp.getRepository().findByUuid(uuid);
                        if(wallet.getTransactionid().equals("")){
                            wallet.setTransactionid(String.valueOf(transaction.getId()));
                        }else{
                            wallet.setTransactionid(wallet.getTransactionid()+":"+transaction.getId());
                        }                        walletServiceImp.getRepository().save(wallet);

                        System.out.println("seurid:"+order.getSuserid());
                        Suser suser = suserServiceImp.getRepository().findById(order.getSuserid()).orElse(null);
                        if(suser==null) throw new NullPointerException();

                        Wallet shopWallet = walletServiceImp.getRepository().findById(suser.getWalletid()).orElse(null);
                        if(shopWallet==null) throw new NullPointerException();
                        shopWallet.setBalance(BigDecimal.valueOf(shopWallet.getBalance().doubleValue()+money));
                        Transaction shopTransaction = new Transaction();
                        shopTransaction.setMore("直接购买了");
                        shopTransaction.setPaymoney(BigDecimal.valueOf(money));
                        shopTransaction.setPaytime(new Timestamp(new Date().getTime()));
                        shopTransaction.setUserid(sUser.getId());
                        shopTransaction.setTargetuserid(user.getId());
                        String uuid2 = UUID.randomUUID().toString();
                        shopTransaction.setUuid(uuid2);
                        transactionServiceImp.getRepository().save(shopTransaction);

                        shopTransaction = transactionServiceImp.getRepository().findByUuid(uuid2);
                        if(shopWallet.getTransactionid().equals("")){
                            shopWallet.setTransactionid(String.valueOf(shopTransaction.getId()));
                        }else{
                            shopWallet.setTransactionid(shopWallet.getTransactionid()+":"+shopTransaction.getId());
                        }                        walletServiceImp.getRepository().save(shopWallet);
                        order.setStatusid(2);
                        ordeServiceImp.getRepository().save(order);
                        return RUtils.success(order);
                    }else return RUtils.Err(Renum.NO_MONEY.getCode(), Renum.NO_MONEY.getMsg());
                }
            }
        }
        return RUtils.success();
    }


    @ApiOperation("获得可领取的优惠卷")
    @GetMapping("/coupons")
    public Result coupons(){
        List<Coupon> coupons = new ArrayList<Coupon>();
        List<Coupon> rCoupons = new ArrayList<Coupon>();
        coupons = conponServiceImp.getRepository().findAll();
        for(Coupon coupon : coupons){
            //判断优惠卷是否有效
            Date now  = new Date();
            Date couponDate = coupon.getConponfailuretime();
            int compareTo = now.compareTo(couponDate);
            if(compareTo < 0)rCoupons.add(coupon);
        }
        return RUtils.success(rCoupons);
    }

    @ApiOperation("领取优惠卷")
    @GetMapping("/c_coupon")
    public Result c_coupon(HttpServletRequest request,int conponid){
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        Nuser nuser = nuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
        if(nuser==null) throw new JException(Renum.UNKNOWN_ERROR.getCode(), Renum.UNKNOWN_ERROR.getMsg());
        Coupon coupon = conponServiceImp.getRepository().findById(conponid).orElse(null);
        if(coupon==null) throw new JException(Renum.UNKNOWN_ERROR.getCode(), Renum.UNKNOWN_ERROR.getMsg());
        Userconpon userconpon = new Userconpon();
        userconpon.setConponid(conponid);
        userconpon.setNuserid(nuser.getId());
        userconpon.setCreatetime(new Timestamp(new Date().getTime()));
        userConponServiceImp.getRepository().save(userconpon);
        return RUtils.success();
    }

    @ApiOperation("已领优惠卷")
    @GetMapping("/m_coupon")
    public Result m_coupon(HttpServletRequest request){
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
        for (Userconpon userconpon : userconpons){
            Coupon coupon = conponServiceImp.getRepository().findById(userconpon.getId()).orElse(null);
            if(coupon!=null)coupons.add(coupon);
        }
        return RUtils.success(coupons);
    }

    @ApiOperation("获得订单列表 传入size")
    @GetMapping("/orders")
    public Result orders(HttpServletRequest request,int size) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        //获得普通用户订单
        Nuser nuser = nuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
        List<Orde> orders = new ArrayList<Orde>();
        if(nuser==null)throw new NullPointerException();
        else {
            orders = ordeServiceImp.getRepository().getAll(size,size+10,nuser.getId());
            return RUtils.success(orders);
        }
    }

    @ApiOperation("获得指定订单 传入id")
    @GetMapping("/g_order")
    public Result g_order(HttpServletRequest request,int id){
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        //获得普通用户订单
        Nuser nuser = nuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
        if(nuser==null)throw new NullPointerException();
        //先判断是否有此订单
        Orde orde = ordeServiceImp.getRepository().findByNuseridAndId(nuser.getId(),id);
        if(orde == null) throw new NullPointerException();
        return RUtils.success(orde);
    }

    @ApiOperation(".获得一部分消息   size")
    @GetMapping("/msgs")
    public Result msgs(HttpServletRequest request,int size){
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        //获得普通用户订单
        Nuser nuser = nuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
        if(nuser==null) throw new NullPointerException();
        List<Msg> msgs = new ArrayList<>();
        msgs = msgServiceImp.getRepository().getAll(size,size+15,nuser.getId());
        return RUtils.success(msgs);
    }

}
