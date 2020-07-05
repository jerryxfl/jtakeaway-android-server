package com.jerry.jtakeaway.controller;


import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.*;
import com.jerry.jtakeaway.requestBean.Sign;
import com.jerry.jtakeaway.responseBean.ResponseUser;
import com.jerry.jtakeaway.responseBean.SignResult;
import com.jerry.jtakeaway.service.imp.*;
import com.jerry.jtakeaway.utils.JwtUtils;
import com.jerry.jtakeaway.utils.RUtils;
import com.jerry.jtakeaway.utils.RedisUtils;
import com.jerry.jtakeaway.utils.WebSocketUtils;
import com.jerry.jtakeaway.utils.bean.Renum;
import com.jerry.jtakeaway.utils.bean.Result;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Api(description = "登录 登出")
@RestController
@RequestMapping("/authen")
public class loginController {
    @Resource
    private RedisUtils redisUtils;

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    @Resource
    JwtUtils jwtUtils;

    @Resource
    LoginRecordServiceImp loginRecordServiceImp;

    @Resource
    UserServiceImp userServiceImp;


    @Resource
    NuserServiceImp nusersServiceImp;

    @Resource
    SuserServiceImp suserServiceImp;

    @Resource
    HuserServiceImp huserServiceImp;

    @Resource
    XuserServiceImp xuserServiceImp;

    @Resource
    WebSocketUtils webSocketUtils;

    @Resource
    HttpServletRequest request;

    @ApiOperation("用户认证操作")
    @PostMapping("/jwtLogin")
    public Result jwtLogin(@RequestBody User user) {
        String jwt = request.getHeader("jwt");
        if (jwt != null && !"".equals(jwt)) {
            //jwt不为空
            //验证jwt是否过期
            Claims claims;
            try {
                claims = jwtUtils.parseJWT(jwt);
                //验证redis数据库中是否有该用户
                if (redisUtils.get(user.getAccount()) != null) {
                    //用户不为空 表示用户还在登录  判断token是否相等
                    if (jwt.equals(redisUtils.get(user.getAccount()))) {
                        //是本人登录
                        User Quser = userServiceImp.getRepository().findByAccount(user.getAccount());
                        if (Quser.getPassword().equals(user.getPassword())) {
                            Loginrecord loginrecord = new Loginrecord();
                            loginrecord.setAddress("绵阳");
                            loginrecord.setLotintime(new Timestamp(new Date().getTime()));
                            loginrecord.setUser(Quser);
                            loginRecordServiceImp.getRepository().save(loginrecord);


                            ResponseUser responseUser = new ResponseUser();
                            responseUser.setId(Quser.getId());
                            responseUser.setAccount(Quser.getAccount());
                            responseUser.setPassword(Quser.getPassword());
                            responseUser.setPhone(Quser.getPhone());
                            responseUser.setEmail(Quser.getEmail());
                            responseUser.setUseradvatar(Quser.getUseradvatar());
                            responseUser.setUsernickname(Quser.getUsernickname());
                            responseUser.setUsertype(Quser.getUsertype());
                            if (Quser.getUsertype() == 0) {
                                Nuser nuser = nusersServiceImp.getRepository().findById(Quser.getUserdetailsid()).orElse(null);
                                responseUser.setUserdetails(JSONObject.toJSONString(nuser));
                            }
                            if (Quser.getUsertype() == 1) {
                                Suser suser = suserServiceImp.getRepository().findById(Quser.getUserdetailsid()).orElse(null);
                                responseUser.setUserdetails(JSONObject.toJSONString(suser));
                            }
                            if (Quser.getUsertype() == 2) {
                                Huser huser = huserServiceImp.getRepository().findById(Quser.getUserdetailsid()).orElse(null);
                                responseUser.setUserdetails(JSONObject.toJSONString(huser));
                            }
                            if (Quser.getUsertype() == 3) {
                                Xuser xuser = xuserServiceImp.getRepository().findById(Quser.getUserdetailsid()).orElse(null);
                                responseUser.setUserdetails(JSONObject.toJSONString(xuser));
                            }
                            JSONObject resultJson = new JSONObject();
                            resultJson.put("jwt", jwt);
                            resultJson.put("user", responseUser);

                            return RUtils.success(resultJson.toJSONString());
                        } else {
                            return RUtils.Err(Renum.PWD_ERROE.getCode(), Renum.PWD_ERROE.getMsg());
                        }
                    } else {
                        //其他人登录
                        /**
                         *
                         * 这里可以用websocket通知已登录用户有异地登录请求
                         *
                         */
                        Msg msg = new Msg();
                        msg.setSendTime(new Timestamp(new Date().getTime()));
                        msg.setContent("账号在其他设备尝试登录,注意账号安全");
                        webSocketUtils.sendMessageToTargetUser(JSONObject.toJSONString(msg), user);
                        return RUtils.Err(Renum.USER_IS_EXISTS.getCode(), Renum.USER_IS_EXISTS.getMsg());
                    }
                } else {
                    //用户不存在于数据库 表示用户从未登录 jwt也就对此用户无效
                    return login(user);
                }
            } catch (ExpiredJwtException e) {
                System.out.println("jwt已过期");
                //过期删除原来数据库中的jwt 重新登录
                if (redisUtils.get(user.getAccount()) != null) {
                    if (jwt.equals(redisUtils.get(user.getAccount()))) {
                        redisUtils.delete(user.getAccount());
                    } else {
                        Msg msg = new Msg();
                        msg.setSendTime(new Timestamp(new Date().getTime()));
                        msg.setContent("账号在其他设备尝试登录,注意账号安全");
                        webSocketUtils.sendMessageToTargetUser(JSONObject.toJSONString(msg), user);
                        return RUtils.Err(Renum.USER_IS_EXISTS.getCode(), Renum.USER_IS_EXISTS.getMsg());
                    }
                }
                return login(user);
            }
        } else {
            //jwt为空
            return login(user);
        }
    }


    private Result login(User user) {
        //判断用户是否已登录
        if (redisUtils.get(user.getAccount()) != null) {
            //用户已登录
            Msg msg = new Msg();
            msg.setSendTime(new Timestamp(new Date().getTime()));
            msg.setContent("账号在其他设备尝试登录,注意账号安全");
            webSocketUtils.sendMessageToTargetUser(JSONObject.toJSONString(msg), user);
            return RUtils.Err(Renum.USER_IS_EXISTS.getCode(), Renum.USER_IS_EXISTS.getMsg());
        } else {
            //生成jwt
            User qUser = userServiceImp.getRepository().findByAccount(user.getAccount());
            if (qUser != null) {
                if (user.getPassword().equals(qUser.getPassword())) {
                    Loginrecord loginrecord = new Loginrecord();
                    loginrecord.setAddress("绵阳");
                    loginrecord.setLotintime(new Timestamp(new Date().getTime()));
                    loginrecord.setUser(qUser);
                    loginRecordServiceImp.getRepository().save(loginrecord);

                    JSONObject json = new JSONObject();
                    json.put("account", user.getAccount());
                    json.put("password", user.getPassword());
                    String jwt = jwtUtils.createJWT(json.toJSONString());
                    //存库
                    redisUtils.set(user.getAccount(), jwt);
                    ResponseUser responseUser = new ResponseUser();
                    responseUser.setId(qUser.getId());
                    responseUser.setAccount(qUser.getAccount());
                    responseUser.setPassword(qUser.getPassword());
                    responseUser.setPhone(qUser.getPhone());
                    responseUser.setEmail(qUser.getEmail());
                    responseUser.setUseradvatar(qUser.getUseradvatar());
                    responseUser.setUsernickname(qUser.getUsernickname());
                    responseUser.setUsertype(qUser.getUsertype());
                    if (qUser.getUsertype() == 0) {
                        Nuser nuser = nusersServiceImp.getRepository().findById(qUser.getUserdetailsid()).orElse(null);
                        responseUser.setUserdetails(JSONObject.toJSONString(nuser));
                    }
                    if (qUser.getUsertype() == 1) {
                        Suser suser = suserServiceImp.getRepository().findById(qUser.getUserdetailsid()).orElse(null);
                        responseUser.setUserdetails(JSONObject.toJSONString(suser));
                    }
                    if (qUser.getUsertype() == 2) {
                        Huser huser = huserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                        responseUser.setUserdetails(JSONObject.toJSONString(huser));
                    }
                    if (qUser.getUsertype() == 3) {
                        Xuser xuser = xuserServiceImp.getRepository().findById(qUser.getUserdetailsid()).orElse(null);
                        responseUser.setUserdetails(JSONObject.toJSONString(xuser));
                    }


                    JSONObject resultJson = new JSONObject();
                    resultJson.put("jwt", jwt);
                    resultJson.put("user", responseUser);
                    System.out.println("jwt --------:" + jwt);

                    return RUtils.success(resultJson.toJSONString());
                } else {
                    return RUtils.Err(Renum.PWD_ERROE.getCode(), Renum.PWD_ERROE.getMsg());
                }
            } else {
                return RUtils.Err(Renum.USER_NOT_EXIST.getCode(), Renum.USER_NOT_EXIST.getMsg());
            }
        }

    }


    @ApiOperation("退出登录")
    @GetMapping("/jwtLogout")
    public Result jwtLogout() {
        String jwt = request.getHeader("jwt");
        if (jwt != null && !"".equals(jwt)) {

            Claims claims;
            try {
                claims = jwtUtils.parseJWT(jwt);
                String subject = claims.getSubject();
                JSONObject json = JSONObject.parseObject(subject);
                User user = JSONObject.toJavaObject(json, User.class);
                if (redisUtils.get(user.getAccount()) != null && redisUtils.get(user.getAccount()).equals(jwt)) {
                    redisUtils.delete(user.getAccount());
                    System.out.println("退出登录成功");
                    return RUtils.success();
                }
            } catch (ExpiredJwtException e) {
                String subject = e.getClaims().getSubject();
                JSONObject json = JSONObject.parseObject(subject);
                User user = JSONObject.toJavaObject(json, User.class);
                if (redisUtils.get(user.getAccount()) != null && redisUtils.get(user.getAccount()).equals(jwt)) {
                    redisUtils.delete(user.getAccount());
                    System.out.println("退出登录成功");
                    return RUtils.success();
                }
            }
        }
        return RUtils.Err(Renum.NO_LOGIN.getCode(), Renum.NO_LOGIN.getMsg());
    }

    @Resource
    ApplyServiceImp applyServiceImp;

    @Resource
    WalletServiceImp walletServiceImp;

    @ApiOperation("注册")
    @PostMapping("/Sign")
    public Result Sign(@RequestBody Sign sign) {
        int detailId = -1;
        switch (sign.getType()) {
            case 0:
                Nuser nuser = new Nuser();
                Nuser save = nusersServiceImp.getRepository().saveAndFlush(nuser);
                detailId = save.getId();
                break;
            case 1:
                if(!isPhone(sign.getPhoneNumber()))return RUtils.Err(Renum.PHONE_FAIL.getCode(),Renum.PHONE_FAIL.getMsg());
                if(!checkIdCard(sign.getIdcard()))return RUtils.Err(Renum.IDCARD_FAIL.getCode(),Renum.IDCARD_FAIL.getMsg());
                List<Suser> suserList = suserServiceImp.getRepository().findAll();
                for (Suser suser : suserList){
                    if(suser.getIdcard().equals(sign.getIdcard())){
                        return RUtils.Err(Renum.IDCARD_CANT_USE.getCode(), Renum.IDCARD_CANT_USE.getMsg());
                    }
                }
                for (Suser suser : suserList){
                    if(suser.getShopname().equals(sign.getShopName())){
                        return RUtils.Err(Renum.SHOPNAME_CANT_USE.getCode(), Renum.SHOPNAME_CANT_USE.getMsg());
                    }
                }
                Wallet wallet = new Wallet();
                wallet.setBalance(0.0);
                Wallet wallet1 = walletServiceImp.getRepository().saveAndFlush(wallet);

                Suser suser = new Suser();
                suser.setLeveltime(0);
                suser.setLevel(0);
                suser.setIdcard(sign.getIdcard());
                suser.setShopname(sign.getShopName());
                suser.setShopaddress(sign.getAddress());
                suser.setWalletid(wallet1.getId());
                Suser suser1 = suserServiceImp.getRepository().saveAndFlush(suser);
                detailId = suser1.getId();
                break;
            case 2:
                if(!isPhone(sign.getPhoneNumber()))return RUtils.Err(Renum.PHONE_FAIL.getCode(),Renum.PHONE_FAIL.getMsg());
                if(!checkIdCard(sign.getIdcard()))return RUtils.Err(Renum.IDCARD_FAIL.getCode(),Renum.IDCARD_FAIL.getMsg());
                List<Huser> huserList = huserServiceImp.getRepository().findAll();
                for (Huser huser : huserList){
                    if(huser.getIdcard().equals(sign.getIdcard())){
                        return RUtils.Err(Renum.IDCARD_CANT_USE.getCode(), Renum.IDCARD_CANT_USE.getMsg());
                    }
                }
                Wallet wallet2 = new Wallet();
                wallet2.setBalance(0.0);
                Wallet wallet3 = walletServiceImp.getRepository().saveAndFlush(wallet2);
                Huser huser = new Huser();
                huser.setIdcard(sign.getIdcard());
                huser.setWalletid(wallet3.getId());
                Huser huser1 = huserServiceImp.getRepository().saveAndFlush(huser);
                detailId = huser1.getId();
                break;
        }
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        if (detailId != -1) {
            User user = new User();
            user.setPhone(sign.getPhoneNumber());
            user.setUsernickname(sign.getUserNickName());
            user.setUseradvatar("http://img1.imgtn.bdimg.com/it/u=3873278591,2846033136&fm=26&gp=0.jpg");
            user.setPassword(sign.getPassword());
            user.setUserdetailsid(detailId);
            user.setUsertype(sign.getType());
            user.setAccount(sb.toString());
            user.setCreatetime(new Timestamp(new Date().getTime()));
            User user1 = userServiceImp.getRepository().saveAndFlush(user);
            Apply apply = new Apply();
            if (user.getUsertype() == 0)
                apply.setApplyreason("用户注册");
            else if (user.getUsertype() == 1) apply.setApplyreason("商家注册");
            else if (user.getUsertype() == 2) apply.setApplyreason("骑手注册");
            apply.setAuditstatus(0);
            apply.setCreatetime(new Timestamp(new Date().getTime()));
            apply.setUserid(user1.getId());
            applyServiceImp.getRepository().saveAndFlush(apply);
        }
        return RUtils.success(new SignResult(sb.toString()));
    }

    public static boolean checkIdCard(String idCard) {

        String regex = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
        return Pattern.matches(regex, idCard);
    }

    private boolean isPhone(String phone){
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        if(phone.length() != 11){
            System.out.println("手机号应为11位数");
            return false;
        }else{
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            if(isMatch){
                System.out.println("您的手机号" + phone + "是正确格式@——@");
                return true;
            } else {
                System.out.println("您的手机号" + phone + "是错误格式！！！");
                return false;
            }
        }
    }

}
