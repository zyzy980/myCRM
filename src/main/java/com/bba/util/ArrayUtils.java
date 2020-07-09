package com.bba.util;

import java.lang.reflect.Field;
import java.util.*;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

/**
 * 数组帮助类
 */
public class ArrayUtils {

	/**
	 * 默认数组长度
	 */
	private final static int DEFAULT_ARRAY_LENGH = 3;
	
	/**
	 * 单个实体对象转成List数组
	 * @param t 实体
	 * @return
	 */
	public static <T>List<T> toObjectArray(T t) {
		if(t == null) {
			throw new NullArgumentException("");
		}
		List<T> list = new ArrayList<T>(ArrayUtils.DEFAULT_ARRAY_LENGH);
		list.add(t);
		return list;
	}
	
	/**
	 * 数组中是否包含指定值
	 * @param objArray 		验证数组
	 * @param value			值
	 * @return  true:包含	false:不包含
	 */
	public static boolean contains(Object[] objArray, String value){
		if(objArray == null){
			return false;
		}
		for (Object obj : objArray) {
			if(obj == null){
				continue;
			}
			if(StringUtils.equals(obj.toString(), value)){
				return true;
			}
		}
		return false;
	}

	public static int indexOf(String[] array, String name) {
		if(array == null || array.length == 0){
			return -1;
		}
		for(int i = 0; i < array.length; i++){
			if(StringUtils.equals(array[i],name)){
				return i;
			}
		}
		return  -1;
	}

	public static String join(Collection collection, String split){
		StringBuffer buf = new StringBuffer();
		for(Object obj: collection){
			buf.append(split).append(obj);
		}
		if(buf.length() > 0){
			return buf.substring(split.length());
		}
		return buf.toString();
	}


	/**
	 * 集合对象分组
	 * @param list �?��分组的集�?
	 * @param columnName 按照对象那个属�?值分�?
	 * @return Map<String, Object>
	 * @remind 该方法返回的结果是无序的
	 */
	public static <T> Map<String, List<T>> GroupBy(List<T> list, String columnName){
		//反射获取单个对象的属�?
		Field field = null;
		Map<String, List<T>> map = new HashMap<String, List<T>>();;
		try {
			Iterator<T> iterator = list.iterator();

			while (iterator.hasNext()) {
				T t = iterator.next();
				field = t.getClass().getDeclaredField(columnName);
				field.setAccessible(true);

				if(map.containsKey(field.get(t) + "")){
					map.get(field.get(t) + "").add(t);
				}else {
					List<T> newList = new ArrayList<T>();
					newList.add(t);
					map.put(field.get(t)+ "", newList);
				}
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

}
