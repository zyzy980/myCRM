/**
	此文件归微科创源有限公司所有
	
	创  建  人:caining
	创建时间:2018年5月31日

	文件描述:
		提供对通用档案的服务访问
*/
package com.bba.jcda.controller;


import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bba.jcda.service.api.IBasicService;

@Controller
@RequestMapping("/scts/basic")
public class BasicController {

	@Resource
	private IBasicService iBasicService;


}
