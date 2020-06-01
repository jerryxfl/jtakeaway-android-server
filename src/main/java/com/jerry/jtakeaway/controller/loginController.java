package com.jerry.jtakeaway.controller;


import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.User;
import com.jerry.jtakeaway.service.imp.UserServiceImp;
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


@Api
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
                        return RUtils.Err(Renum.SUCCESS.getCode(),Renum.SUCCESS.getMsg());
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
            JSONObject json = new JSONObject();
            json.put("account",user.getAccount());
            json.put("password",user.getPassword());
            String jwt = jwtUtils.createJWT(json.toJSONString());
            //存库
            redisUtils.set(user.getAccount(), jwt);
            User resultUser = userServiceImp.getRepository().findByAccount(user.getAccount());
            resultUser.setPassword("");
            JSONObject resultJson = new JSONObject();
            resultJson.put("jwt",jwt);
            resultJson.put("user",resultUser);
            System.out.println("jwt --------:"+jwt);
            return RUtils.success(resultJson.toJSONString());
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
