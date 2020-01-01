package com.bba.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource("resource.yml")
@Data
public class Resource {/*
	
	@Value("${staticPath1}")
	private String staticPath;*/

	@Value("${pdfPath}")
	private String pdfPath;
	
	@Value("${htmlPath}")
	private String htmlPath; //静态文件项目外地址，可随意配置

	@Value("${uploadPath}")
	private String uploadPath;
}
