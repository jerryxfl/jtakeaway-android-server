package com.jerry.jtakeaway.config.webMvc;

import com.jerry.jtakeaway.interceptor.JHInterceptor;
import com.jerry.jtakeaway.interceptor.JNInterceptor;
import com.jerry.jtakeaway.interceptor.JSInterceptor;
import com.jerry.jtakeaway.interceptor.JXInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class JWebMvcConfigurer implements WebMvcConfigurer {
    /**
     * 配置静态访问资源
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/", "/static", "/public", "templates");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    /**
     * 配置拦截路径
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JNInterceptor())
                //添加需要验证登录用户操作权限的请求
                .addPathPatterns("/N/**");
                //排除不需要验证登录用户操作权限的请求
        //这里可以用registry.addInterceptor添加多个拦截器实例，后面加上匹配模式
        registry.addInterceptor(new JSInterceptor())
                //添加需要验证登录用户操作权限的请求
                .addPathPatterns("/S/**");
                //排除不需要验证登录用户操作权限的请求

        registry.addInterceptor(new JHInterceptor())
                //添加需要验证登录用户操作权限的请求
                .addPathPatterns("/H/**");
                //排除不需要验证登录用户操作权限的请求

        registry.addInterceptor(new JXInterceptor())
                //添加需要验证登录用户操作权限的请求
                .addPathPatterns("/X/**");
                //排除不需要验证登录用户操作权限的请求

    }
}