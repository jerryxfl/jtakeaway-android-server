package com.jerry.jtakeaway.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.User;
import com.jerry.jtakeaway.exception.JException;
import com.jerry.jtakeaway.service.imp.UserServiceImp;
import com.jerry.jtakeaway.utils.JwtUtils;
import com.jerry.jtakeaway.utils.RedisUtils;
import com.jerry.jtakeaway.utils.bean.Renum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@SuppressWarnings("all")
public class JUInterceptor implements HandlerInterceptor {

    /**
     * 之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("用户拦截");
        return isLogin(request);
    }
    @Resource
    RedisUtils redisUtils;

    @Resource
    JwtUtils jwtUtils;

    @Resource
    UserServiceImp userServiceImp;


    public boolean isLogin(HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        System.out.println("token"+jwt);

        if (jwt != null && !"".equals(jwt))  {
            Claims claims;
            try{
                claims = jwtUtils.parseJWT(jwt);
            }catch (ExpiredJwtException e) {
                System.out.println("jwt过期");
                throw new JException(Renum.JWT_FAIL.getCode(), Renum.JWT_FAIL.getMsg());
            }

            String subject = claims.getSubject();
            JSONObject jsonObject = JSONObject.parseObject(subject);
            User mUser = JSONObject.toJavaObject(jsonObject, User.class);
            if (redisUtils.get(mUser.getAccount()) != null)
                if (redisUtils.get(mUser.getAccount()).equals(jwt)) {
                    return true;
                }
                else throw new JException(Renum.JWT_FAIL.getCode(), Renum.JWT_FAIL.getMsg());
        }
        throw new JException(Renum.NO_LOGIN.getCode(), Renum.NO_LOGIN.getMsg());
    }
}
