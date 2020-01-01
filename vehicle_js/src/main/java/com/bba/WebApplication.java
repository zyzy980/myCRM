package com.bba;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
@MapperScan(basePackages = "com.bba.*.dao")
public class WebApplication {

	public static void main(String[] args) {

		SpringApplication.run(WebApplication.class, args);
	}

}
