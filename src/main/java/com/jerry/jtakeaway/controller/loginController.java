package com.jerry.jtakeaway.controller;


import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.*;
import com.jerry.jtakeaway.responseBean.ResponseUser;
import com.jerry.jtakeaway.service.imp.*;
import com.jerry.jtakeaway.utils.JwtUtils;
import com.jerry.jtakeaway.utils.RUtils;
import com.jerry.jtakeaway.utils.RedisUtils;
import com.jerry.jtakeaway.utils.bean.Renum;
import com.jerry.jtakeaway.utils.bean.Result;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


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
    UserServiceImp userServiceImp;


    @Resource
    NuserServiceImp nusersServiceImp;

    @Resource
    SuserServiceImp suserServiceImp;

    @Resource
    HuserServiceImp huserServiceImp;

    @Resource
    XuserServiceImp xuserServiceImp;

    @ApiOperation("用户认证操作")
    @PostMapping("/jwtLogin")
    public Result jwtLogin(@RequestBody User user, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        if(jwt!=null&&!"".equals(jwt)){
            //jwt不为空
            //验证jwt是否过期
            Claims claims;
            try {
                claims = jwtUtils.parseJWT(jwt);
                //验证redis数据库中是否有该用户
                if(redisUtils.get(user.getAccount())!=null){
                    //用户不为空 表示用户还在登录  判断token是否相等
                    if(jwt.equals(redisUtils.get(user.getAccount()))){
                        //是本人登录
                        User Quser = userServiceImp.getRepository().findByAccount(user.getAccount());
                        if(Quser.getPassword().equals(user.getPassword())){
                            ResponseUser responseUser = new ResponseUser();
                            responseUser.setId(Quser.getId());
                            responseUser.setAccount(Quser.getAccount());
                            responseUser.setPassword(Quser.getPassword());
                            responseUser.setPhone(Quser.getPhone());
                            responseUser.setEmail(Quser.getEmail());
                            responseUser.setUseradvatar(Quser.getUseradvatar());
                            responseUser.setUsernickname(Quser.getUsernickname());
                            responseUser.setUsertype(Quser.getUsertype());
                            if(Quser.getUsertype()==0){
                                Nuser nuser = nusersServiceImp.getRepository().findById(Quser.getUserdetailsid()).orElse(null);
                                responseUser.setUserdetails(JSONObject.toJSONString(nuser));
                            }
                            if(Quser.getUsertype()==1){
                                Suser suser = suserServiceImp.getRepository().findById(Quser.getUserdetailsid()).orElse(null);
                                responseUser.setUserdetails(JSONObject.toJSONString(suser));
                            }
                            if(Quser.getUsertype()==2){
                                Huser huser = huserServiceImp.getRepository().findById(Quser.getUserdetailsid()).orElse(null);
                                responseUser.setUserdetails(JSONObject.toJSONString(huser));
                            }
                            if(Quser.getUsertype()==3){
                                Xuser xuser = xuserServiceImp.getRepository().findById(Quser.getUserdetailsid()).orElse(null);
                                responseUser.setUserdetails(JSONObject.toJSONString(xuser));
                            }
                            JSONObject resultJson = new JSONObject();
                            resultJson.put("jwt",jwt);
                            resultJson.put("user",responseUser);
                            return RUtils.success(resultJson.toJSONString());
                        }else{
                            return RUtils.Err(Renum.PWD_ERROE.getCode(),Renum.PWD_ERROE.getMsg());
                        }
                    }else{
                        //其他人登录
                        /**
                         *
                         * 这里可以用websocket通知已登录用户有异地登录请求
                         *
                         */


                        return RUtils.Err(Renum.USER_IS_EXISTS.getCode(),Renum.USER_IS_EXISTS.getMsg());
                    }
                }else{
                    //用户不存在于数据库 表示用户从未登录 jwt也就对此用户无效
                   return login(user);
                }
            }catch (ExpiredJwtException e) {
                System.out.println("jwt已过期");
                //过期删除原来数据库中的jwt 重新登录
                if(redisUtils.get(user.getAccount())!=null)redisUtils.delete(user.getAccount());
                return login(user);
            }
        }else{
            //jwt为空
            return login(user);
        }
    }


    private Result login(User user) {
        //判断用户是否已登录
        if(redisUtils.get(user.getAccount())!=null){
            //用户已登录
            return  RUtils.Err(Renum.USER_IS_EXISTS.getCode(),Renum.USER_IS_EXISTS.getMsg());
        }else{
            //生成jwt
            User qUser = userServiceImp.getRepository().findByAccount(user.getAccount());
            if(qUser!=null){
                if(user.getPassword().equals(qUser.getPassword())){
                    JSONObject json = new JSONObject();
                    json.put("account",user.getAccount());
                    json.put("password",user.getPassword());
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
                    if(qUser.getUsertype()==0){
                        Nuser nuser = nusersServiceImp.getRepository().findById(qUser.getUserdetailsid()).orElse(null);
                        responseUser.setUserdetails(JSONObject.toJSONString(nuser));
                    }
                    if(qUser.getUsertype()==1){
                        Suser suser = suserServiceImp.getRepository().findById(qUser.getUserdetailsid()).orElse(null);
                        responseUser.setUserdetails(JSONObject.toJSONString(suser));
                    }
                    if(qUser.getUsertype()==2){
                        Huser huser = huserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                        responseUser.setUserdetails(JSONObject.toJSONString(huser));
                    }
                    if(qUser.getUsertype()==3){
                        Xuser xuser = xuserServiceImp.getRepository().findById(qUser.getUserdetailsid()).orElse(null);
                        responseUser.setUserdetails(JSONObject.toJSONString(xuser));
                    }


                    JSONObject resultJson = new JSONObject();
                    resultJson.put("jwt",jwt);
                    resultJson.put("user",responseUser);
                    System.out.println("jwt --------:"+jwt);
                    return RUtils.success(resultJson.toJSONString());
                }else{
                    return RUtils.Err(Renum.PWD_ERROE.getCode(),Renum.PWD_ERROE.getMsg());
                }
            }else{
                return RUtils.Err(Renum.USER_NOT_EXIST.getCode(),Renum.USER_NOT_EXIST.getMsg());
            }
        }

    }


    @ApiOperation("退出登录")
    @GetMapping("/jwtLogout")
    public Result jwtLogout(HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        if(jwt != null && !"".equals(jwt)){

            Claims claims;
            try{
                claims = jwtUtils.parseJWT(jwt);
                String subject = claims.getSubject();
                JSONObject json = JSONObject.parseObject(subject);
                User user = JSONObject.toJavaObject(json,User.class);
                if(redisUtils.get(user.getAccount())!=null&&redisUtils.get(user.getAccount()).equals(jwt)){
                    redisUtils.delete(user.getAccount());
                    System.out.println("退出登录成功");
                    return RUtils.success();
                }
            }catch (ExpiredJwtException e) {
                String subject = e.getClaims().getSubject();
                JSONObject json = JSONObject.parseObject(subject);
                User user = JSONObject.toJavaObject(json,User.class);
                if(redisUtils.get(user.getAccount())!=null&&redisUtils.get(user.getAccount()).equals(jwt)){
                    redisUtils.delete(user.getAccount());
                    System.out.println("退出登录成功");
                    return RUtils.success();
                }
            }
        }
        return RUtils.Err(Renum.NO_LOGIN.getCode(),Renum.NO_LOGIN.getMsg());
    }

}
