package com.bba.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * JSON格式帮助类
 */
public class JSONUtils {

	private static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 将json字符转化为指定对象List数据格式
	 * @param json  
	 * @param class1 
	 * @return
	 */
	public static <T>List<T> toJSONObjectList(Class<T> class1, Object jsonObject){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, class1);
        try {
        	List<T> list = null;
        	if(jsonObject instanceof String){
        		list = objectMapper.readValue((String)jsonObject, javaType);
        	}else{
        		list = objectMapper.convertValue(jsonObject, javaType);
        	}
        	
			return list;
		} catch (IOException e) {
			e.printStackTrace();
			throw new OperatorException(e.getMessage());
		}
	}
	
	/**
	 * 将json字符转化为指定对象List数据格式
	 * @param json  
	 * @param class1 
	 * @return
	 */
	public static <T>T toJSONObject(Class<T> class1, Object jsonObject){
		try {
			return objectMapper.readValue(""+jsonObject, class1);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static String toJSONString(Object result) {
        try {
			return objectMapper.writeValueAsString(result);
		} catch (IOException e) {
//			e.printStackTrace();
			throw new RuntimeException("解析json格式数据错误",e);
		}
	}

}
