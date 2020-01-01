package com.bba.util;

import com.google.gson.Gson;

/**
 * 序列化工具
 * @author fs
 *
 */
public class GsonUtil {
	
	/**
	 * 获得gson对象
	 * @return
	 */
	public static Gson getGson(){
		Gson gson=new Gson();
		return gson;
	}
	
	public static String getJsonData(Object object){
		Gson gson = new Gson(); 
		return gson.toJson(object).replace("\\","");
	}
}
