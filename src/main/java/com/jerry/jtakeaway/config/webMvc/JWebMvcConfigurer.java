package com.jerry.jtakeaway.config.webMvc;

import com.jerry.jtakeaway.interceptor.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class JWebMvcConfigurer implements WebMvcConfigurer {
    @Value(value = "${web.resources-path}")
    private String webResourcesPath;

    @Value("${file.staticAccessPath}")
    private String staticAccessPath;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowCredentials(true).allowedMethods("*").maxAge(3600);
    }

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

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/templates/");

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler(staticAccessPath)
                .addResourceLocations("file:"+webResourcesPath+"/");
    }

    @Resource
    JNInterceptor jnInterceptor;

    @Resource
    JHInterceptor jhInterceptor;

    @Resource
    JXInterceptor jxInterceptor;

    @Resource
    JSInterceptor jsInterceptor;

    @Resource
    JUInterceptor juInterceptor;
    /**
     * 配置拦截路径
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jnInterceptor)
                //添加需要验证登录用户操作权限的请求
                .addPathPatterns("/N/**");
                //排除不需要验证登录用户操作权限的请求
        //这里可以用registry.addInterceptor添加多个拦截器实例，后面加上匹配模式
        registry.addInterceptor(jsInterceptor)
                //添加需要验证登录用户操作权限的请求
                .addPathPatterns("/S/**");
        //排除不需要验证登录用户操作权限的请求

        registry.addInterceptor(jhInterceptor)
                //添加需要验证登录用户操作权限的请求
                .addPathPatterns("/H/**");
        //排除不需要验证登录用户操作权限的请求

        registry.addInterceptor(jxInterceptor)
                //添加需要验证登录用户操作权限的请求
                .addPathPatterns("/X/**");
        //排除不需要验证登录用户操作权限的请求

        registry.addInterceptor(juInterceptor)
                //添加需要验证登录用户操作权限的请求
                .addPathPatterns("/U/**");
        //排除不需要验证登录用户操作权限的请求
    }
}
