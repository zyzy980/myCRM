package com.bba.bean;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.MultipartConfigElement;

@Configuration
public class MultipartResolverConfig {

	@Bean
	public MultipartConfigElement multipartConfigElement(){
		MultipartConfigFactory factory=new MultipartConfigFactory();
		factory.setMaxFileSize("10MB");
		factory.setMaxRequestSize("10MB");
		return factory.createMultipartConfig();
	}
	/*
	@Bean
	public MultipartResolver multipartResolver(){
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("UTF-8");
		resolver.setResolveLazily(true);//resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
		resolver.setMaxUploadSize(10*1024*1024);
		resolver.setMaxInMemorySize(1);//文件太小不会创建临时文件需要加上
		return resolver;
	}*/
}
