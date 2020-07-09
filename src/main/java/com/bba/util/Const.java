/**
	此文件归微科创源有限公司所有
	
	创  建  人:caining
	创建时间:2018年6月20日

	文件描述:
		提供项目级常量
*/
package com.bba.util;

import org.springframework.web.bind.annotation.RequestMethod;

public interface Const {

	//--------------HTTP相关常量start-------------//
	String HTTP_METHOD_POST = RequestMethod.POST.toString();
	
	String HTTP_METHOD_GET = RequestMethod.GET.toString();
	//--------------HTTP相关常量end-------------//

	
	//--------------String相关常量start-------------//
	String STRING_CODE_ISO8859 = "ISO8859-1";
	
	String STRING_CODE_UTF_8 = "UTF-8";
	//--------------String相关常量end-------------//

	
	//---------------用户身份start--------------//
	
	String USER_LEVEL_SUPPLIER="5";
	String USER_LEVEL_CONTRACTOR="2";
	
	//---------------用户身份end--------------//

	
	//--------------前端网格参数相关常量end-------------//
	String FRONT_CONTROL_GRID_PARAM_FIELD_NAME = "field";
	
	String FRONT_CONTROL_GRID_PARAM_FIELD_OP = "eq";
	
	String FRONT_CONTROL_GRID_PARAM_FIELD_DATA = "data";
	
	String FRONT_CONTROL_GRID_PARAM_FIELD = "rules";
	//--------------前端网格参数相关常量end-------------//
}
