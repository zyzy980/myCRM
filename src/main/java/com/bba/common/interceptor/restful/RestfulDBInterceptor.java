/**
	此文件归微科创源有限公司所有
	
	创  建  人:caining
	创建时间:2018年9月10日

	文件描述:
		restfulDB拦截器
*/
package com.bba.common.interceptor.restful;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bba.common.interceptor.restful.service.RestfulService;

@RestController
@RequestMapping("/restful")
public class RestfulDBInterceptor {
	
	@Resource
	private RestfulService restfulService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/*/*/*", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody Object find(HttpServletRequest request) throws ClassNotFoundException {
		
		// 构建参数
		Map<String,String> paramMap = buildRequestParameter(request.getParameterMap());
		
		// 获取URI
		String[] uri = request.getRequestURI().split("/");
		
		return restfulService.execute(uri[uri.length-3],uri[uri.length-2], uri[uri.length-1],paramMap);
	}
	
	// 转化请求参数
	private Map<String,String> buildRequestParameter(Map<String,String[]> map){
		Map<String,String> newMap = new HashMap<String, String>();
		
		for (Map.Entry<String, String[]> entry : map.entrySet()) {
			newMap.put(entry.getKey(), entry.getValue()[0]);
		}
		return newMap;
	}
}
