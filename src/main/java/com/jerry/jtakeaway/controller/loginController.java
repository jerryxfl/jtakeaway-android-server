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

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Resource
    JwtUtils jwtUtils;

    @Resource
    UserServiceImp userServiceImp;

    @ApiOperation("用户认证操作")
    @PostMapping("/jwtLogin")
    public Result jwtLogin(@RequestBody User user, HttpServletRequest request) {
        //获得token
        String token = request.getHeader("token");
        if (token == null || "".equals(token)) {
            //用户未登录 且未携带任何token
            return login(user);
        } else {
            Claims claims;
            try{
                 claims = jwtUtils.parseJWT(token);
            }catch (ExpiredJwtException e) {
                System.out.println("token过期");
                return login(user);
            }
            String subject = claims.getSubject();
            JSONObject jsonObject = JSONObject.parseObject(subject);
            User mUser = JSONObject.toJavaObject(jsonObject, User.class);
            if (redisUtils.get(mUser.getAccount()) != null) {
                if(redisUtils.get(user.getAccount()).equals(token)){
                    return RUtils.success();
                }else{
                    return login(user);
                }
            } else {
                return login(user);
            }
        }
    }


    private Result login(User user) {
        //先判断要登录用户是否已经登录
        if (redisUtils.get(user.getAccount()) == null) {
            //当前用户并未登录
            if (userServiceImp.getRepository().findByAccount(user.getAccount()) == null) {
                return RUtils.Err(Renum.USER_NOT_EXIST.getCode(), Renum.USER_NOT_EXIST.getMsg());
            } else if (userServiceImp.getRepository().findByAccountAndPassword(user.getAccount(), user.getPassword()) == null) {
                return RUtils.Err(Renum.PWD_ERROE.getCode(), Renum.PWD_ERROE.getMsg());
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("account", user.getAccount());
                jsonObject.put("password", user.getPassword());
                String resultToken = jwtUtils.createJWT(jsonObject.toJSONString());
                System.out.println(user.getAccount() + "-----:" + resultToken);
                //数据存库
                redisUtils.set(user.getAccount(), resultToken);
                JSONObject json  = new JSONObject();
                json.put("token", resultToken);
                json.put("user", userServiceImp.getRepository().findByAccount(user.getAccount()));
                return RUtils.success(json.toJSONString());
            }
        } else {
            //当前用户已登录
            return RUtils.Err(Renum.USER_IS_EXISTS.getCode(), Renum.USER_IS_EXISTS.getMsg());
        }
    }


    @ApiOperation("退出登录")
    @GetMapping("/jwtLogout")
    public Result jwtLogout(HttpServletRequest request) {
        String token = request.getHeader("token");
        Claims claims = jwtUtils.parseJWT(token);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = JSONObject.toJavaObject(jsonObject, User.class);
        if (redisUtils.get(user.getAccount()) != null) {
            //凭证有效  删除数据库凭证
            redisUtils.delete(user.getAccount());
            System.out.println("成功退出登录");
        } else {
            return RUtils.Err(Renum.JWT_FAIL.getCode(), Renum.JWT_FAIL.getMsg());
        }
        return RUtils.success();
    }

}
