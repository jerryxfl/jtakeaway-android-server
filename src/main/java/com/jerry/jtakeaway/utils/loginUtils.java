package com.jerry.jtakeaway.utils;

import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.User;
import com.jerry.jtakeaway.exception.JException;
import com.jerry.jtakeaway.service.imp.UserServiceImp;
import com.jerry.jtakeaway.utils.bean.Renum;
import io.jsonwebtoken.Claims;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

public class loginUtils {
    @Resource
    private static RedisUtils redisUtils;

    @Resource
    private static JwtUtils jwtUtils;

    @Resource
    private static UserServiceImp userServiceImp;

// 0 普通用户 1商家 2骑手 3系统管理员
    public static boolean isLogin(HttpServletRequest request, int type) {
        System.out.println("开始拦截"+type);
        String token = request.getHeader("token");
        System.out.println("token"+token);

        if (token != null && !"".equals(token))  {
            Claims claims = jwtUtils.parseJWT(token);
            String subject = claims.getSubject();
            JSONObject jsonObject = JSONObject.parseObject(subject);
            User mUser = JSONObject.toJavaObject(jsonObject, User.class);
            if (redisUtils.get(mUser.getAccount()) != null)
                if (redisUtils.get(mUser.getAccount()).equals(token)) {
                    if (userServiceImp.getRepository().findByAccount(mUser.getAccount()).getUsertype() == type) {
                        return true;
                    } else {
                        throw new JException(Renum.PREMS_FAIL.getCode(), Renum.PREMS_FAIL.getMsg());
                    }
                }
            else throw new JException(Renum.JWT_FAIL.getCode(), Renum.JWT_FAIL.getMsg());
        }
        throw new JException(Renum.NO_LOGIN.getCode(), Renum.NO_LOGIN.getMsg());
    }

}
