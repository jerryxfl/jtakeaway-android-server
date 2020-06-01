package com.jerry.jtakeaway.controller;

import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.*;
import com.jerry.jtakeaway.config.websocket.Greeting;
import com.jerry.jtakeaway.config.websocket.HelloMessage;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api
@RestController
@RequestMapping("/N")
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

    @ApiOperation("使用优惠卷支付 ")
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
                    //计算优惠卷使用后价格
                    Coupon coupon = conponServiceImp.getRepository().findById(pay_bean.getCouponId()).orElse(null);
                    if(coupon==null) return RUtils.Err(Renum.NO_CONPON.getCode(),Renum.NO_CONPON.getMsg());
                    //判断优惠卷是否有效
                    Date now  = new Date();
                    Date couponDate = coupon.getConponfailuretime();
                    int compareTo = now.compareTo(couponDate);
                    if(compareTo > 0)return RUtils.Err(Renum.CONPON_FAIL.getCode(),Renum.CONPON_FAIL.getMsg());
                    money = money*coupon.getConponprice().doubleValue();
                    System.out.println("总价:"+money);
                    if(!pay_bean.getPayPassword().equals(wallet.getPaymentpassword())) return RUtils.Err(Renum.PAYPAS_FAIL.getCode(),Renum.PAYPAS_FAIL.getMsg());
                    if(wallet.getBalance().doubleValue()>money){
                        //钱包扣钱
                        wallet.setBalance(BigDecimal.valueOf(wallet.getBalance().doubleValue()-money));
                        //商家进账


                        Transaction transaction = new Transaction();
                        transaction.setCouponid(pay_bean.getCouponId());
                        transaction.setMore("使用优惠卷购买了");
                        transaction.setPaymoney(BigDecimal.valueOf(money));
                        transaction.setPaytime(new Timestamp(new Date().getTime()));
                        transaction.setUserid(nuser.getId());
                        transactionServiceImp.getRepository().save(transaction);

//                        transaction = transactionServiceImp.getRepository().findOne(example).orElse(null);


                        walletServiceImp.getRepository().save(wallet);


                        order.setStatusid(2);
                        ordeServiceImp.getRepository().save(order);
                    }else return RUtils.Err(Renum.NO_MONEY.getCode(), Renum.NO_MONEY.getMsg());
                }
            }
        }
        return RUtils.success();
    }

}
