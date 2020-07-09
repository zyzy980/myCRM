/**
	此文件归微科创源有限公司所有
	
	创  建  人:caining
	创建时间:2018年6月13日

	文件描述:
		提供字符常量
*/
package com.bba.jcda.util;

import java.util.HashMap;
import java.util.Map;

public interface Const {

	//--------------表名称(业务地点用)start-------------//
	
	String JCDA_TABLE_SCTS_BASIC = "SCTS_BASIC";
	String JCDA_TABLE_SCTS_CONTRACTOR = "SCTS_CONTRACTOR";
	String JCDA_TABLE_ZD_CONTRACTOR = "ZD_CONTRACTOR";
	String JCDA_TABLE_SCTS_USERMAIL = "SCTS_USERMAIL";
	String JCDA_TABLE_SCTS_SUPPLIER = "SCTS_SUPPLIER";
	String JCDA_TABLE_ZD_LOCATION = "ZD_LOCATION";
	String JCDA_TABLE_ZD_BUSINESS_STATE= "ZD_BUSINESS_STATE";
	String JCDA_TABLE_LP_SUPPLIER_DISPLOC_ROUTE = "LP_SUPPLIER_DISPLOC_ROUTE";
	
	String ZD_CONTRACTOR = "ZD_CONTRACTOR";
	String ZD_ROUTE = "ZD_ROUTE";
	String ZD_LOCATION="kind";
	String ZD_TRUCKTYPE="UNLOAD_TYPE";
	String ZD_DRIVER="ZdDriverKind";
	String ZD_TRUCK="CommonStateYesNo";
	String ZD_TRUCKTYPE1="LOAD_TYPE";
	String YWGL_YW_PLAN_EXEC = "YW_PLAN_EXEC";
	String LP_LU_SET="DIRECTION";
	
	//--------------表名称(业务地点用)end---------------//
	
	
	//--------------列名称(业务地点用)start-------------//
	
	String JCDA_GENERAL_COL_SN = "sn";
	String JCDA_GENERAL_COL_WITHIN_CODE = "within_code";
	String JCDA_GENERAL_COL_CREATE_BY = "create_by";
	String JCDA_GENERAL_COL_UPDATE_BY = "update_by";
	
	//--------------列名称(业务地点用)end-------------//
	
	
	//--------------通用常量start---------------//
	
	String JCDA_GENERAL_STATE_EFFECTIVE = "0";
	String JCDA_GENERAL_STATE_INVALID = "1";
	
	//--------------通用常量end-----------------//
	
	
	//--------------业务地点参数start------------------//
	
	String JCDA_LOCATION_GCJ = "GCJ";
	String JCDA_LOCATION_JKJ = "JKJ";
	
	//--------------业务地点参数start------------------//

	
	//--------------常量字符start---------------//
	
	String JCDA_GRID_KEY_FIELD = "field";
	String JCDA_GRID_KEY_OP = "op";
	String JCDA_GRID_KEY_DATA = "data";
	String JCDA_GRID_VAL_OPTION_EQ = "eq";
	
	//---------------常量字符end----------------//

	
	//---------------邮箱常量start----------------//
	
	String JCDA_USERMAIL_ROLE_BBA = "BBA";
	String JCDA_USERMAIL_ROLE_LSP = "LSP";
	String JCDA_USERMAIL_ROLE_SUPPLIER = "SUPPLIER";
	String JCDA_USERMAIL_GROUP_LOADING = "LoadingPlan";
	String JCDA_USERMAIL_GROUP_EMERGENCY = "Emergency";
	String JCDA_USERMAIL_GROUP_NORMAL = "Normal";
	
	//---------------邮箱常量start----------------//
	
	@SuppressWarnings("serial")
	Map<String,String> options = new HashMap<String,String>(){{
		put(JCDA_GRID_VAL_OPTION_EQ,JCDA_GRID_VAL_OPTION_EQ);
	}};
	
}
