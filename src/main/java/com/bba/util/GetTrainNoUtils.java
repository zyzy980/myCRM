package com.bba.util;

import java.util.HashMap;
import java.util.Map;


public class GetTrainNoUtils {

	/**
	 * @Description 获取车次号
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	public static Map<String,Object> getParams(String withinCode, String ywLocation){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("p_within_code" , withinCode);
		params.put("p_whcenter", ywLocation);
		params.put("p_tablename", "YW_PLAN_EXEC");
		params.put("p_output", "");
		params.put("p_ok",0);
		return params;
	}

	/**
	 * @Description 获取运单号
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	public static Map<String,Object> getMostlyPArams(String withinCode, String ywLocation) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("p_within_code", withinCode);
		params.put("p_tablename", "YW_ORDER_MOSTLY");
		params.put("p_whcenter", ywLocation);
		params.put("p_output", "");
		params.put("p_ok",0);
		return params;
	}
	/**
	 * @Description 获取订单号
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	public static Map<String,Object> getSaleParams(String withinCode, String ywLocation){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("p_within_code", withinCode);
		params.put("p_tablename", "YW_SALE_MOSTLY");
		params.put("p_whcenter", ywLocation);
		params.put("p_output", "");
		params.put("p_ok",0);
		return params;
	}
	/**
	 * @Description 获取地址编号
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	public static Map<String,Object> getAddressCode(String withinCode, String ywLocation){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("p_within_code", withinCode);
		params.put("p_tablename", "ZD_LOCATION");
		params.put("p_whcenter", ywLocation);
		params.put("p_output", "");
		params.put("p_ok",0);
		return params;
	}


	public static Map<String,Object> getzdLocaitonParams(String withinCode, String ywLocation){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("p_within_code", withinCode);
		params.put("p_tablename", "ZD_LOCATION");
		params.put("p_whcenter", ywLocation);
		params.put("p_output", "");
		params.put("p_ok",0);
		return params;
	}
}
