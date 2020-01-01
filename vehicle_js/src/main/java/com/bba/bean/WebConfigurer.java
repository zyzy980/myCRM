package com.bba.bean;

import com.bba.common.filter.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    LoginInterceptor loginInterceptor;

    final String[] notLoginInterceptPaths = {"/static/**","/admin/login","/error/**","/login"};


    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
/*
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // TODO Auto-generated method stub
        //排除静态资源拦截
        registry.addResourceHandler("/static/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/static/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
    */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TODO Auto-generated method stub
        // addPathPatterns("/**") 表示拦截所有的请求，
        // excludePathPatterns("/login", "/register") 表示除了登陆与注册之外，因为登陆注册不需要登陆也可以访问
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/",
                        "/inroad/omsValidateData",
                        "/inroad/omsOrderRemove",
                        "/inroad/omsPlaceOrder",
                        "/inroad/getNods",
                        "/inroad/getNodesList",
                        "/sysInfo/user/loginIn",
                        "/sysInfo/user/code.do",
                        "/admin/syslanguage/saveLocalLang",
                        "/sysInfo/user/register",
                        "/sysInfo/SheelNo/getSys_sheetid",
                        "/Index/**",
                        "/View/**",
                        "/Resource/**"
                );
    }

}