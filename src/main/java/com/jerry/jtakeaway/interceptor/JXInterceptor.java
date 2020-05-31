package com.jerry.jtakeaway.interceptor;

import com.jerry.jtakeaway.exception.JException;
import com.jerry.jtakeaway.utils.loginUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class JXInterceptor implements HandlerInterceptor {

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
        return loginUtils.isLogin(request,3);
    }
}
