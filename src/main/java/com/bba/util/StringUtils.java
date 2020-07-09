
package com.bba.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * String帮助类,扩展StringUtils
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {

	/**
	 * 判断2个字符是否不相等
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean notEquals(String str1, String str2) {
		if (org.apache.commons.lang.StringUtils.equals(str1, str2)) {
			return false;
		}
		return true;
	}

	/**
	 * 第一个参数为null,返回参数2，不为null返回转化后的字符
	 * 
	 * @param obj
	 * @param newStr
	 * @return
	 */
	public static String nvl(Object obj, String newStr) {
		if (obj == null) {
			return "" + newStr;
		}
		return "" + obj;
	}

	/**
	 * 验证字符串是否为整数
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNumber(String obj) {
		if (obj == null) {
			return false;
		}
		return obj.matches("^\\d+$|-\\d+$");
	}

	/**
	 * 验证字符串是否不为整数
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNotNumber(String obj) {
		if (obj == null) {
			return true;
		}
		return !obj.matches("^\\d+$|-\\d+$");
	}

	/**
	 * 验证字符串是否为小数
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNumberspace(String obj) {
		if (obj == null) {
			return false;
		}
		if (isNumber(obj)) {
			// 是否整数也返回true
			return true;
		}
		return obj.matches("\\d+\\.\\d+$|-\\d+\\.\\d+$");
	}

	public static String EmptyZero(String value)
	{
		if(null!=value)
		{
			try
			{
				value=Double.valueOf(value).toString();
			}
			catch(Exception e)
			{
				
			}
		}
		return value;
	}
	
	/**
	 * 判断字符串是否为日期格式
	 * 
	 * @param str
	 *            需验证的字符串
	 * @return
	 */
	public static boolean isDate(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		return str.matches(
				"^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
	}
	
	public static boolean ValidateDate(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		
		String rexp = "((\\d{2}(([02468][048])|([13579][26]))[\\-]((((0?[13578])|(1[02]))[\\-]((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-]((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-]((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-]((((0?[13578])|(1[02]))[\\-]((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-]((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-]((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		if(str.indexOf(":")!=-1)
			rexp = "((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))";
		Pattern pattern=Pattern.compile(rexp);
		Matcher matcher=pattern.matcher(str);
		
		return matcher.matches();
		
		
	}

	/**
	 * 验证是否为合法非负数字并判断是否超过最大值
	 * 
	 * @param str
	 *            需验证的字符串
	 * @param type
	 *            验证类型（1：非负整数、2：非负数）
	 * @param max
	 *            最大值（但不包括）例：真实的最大值为99，那么此时传的最大值为100，为空则不进行比较
	 * @return
	 */
	public static boolean checkIsPositiveNumber(String str, int type, Integer max) {
		if (isEmpty(str)) {
			return false;
		}
		boolean res = false;
		if (isNumber(str)) {// 非负整数
			int value = Integer.parseInt(str);
			if (value >= 0) {
				if (max != null && max > 0) {// 最大值判断
					if (max > value) {
						res = true;
					}
				} else {
					res = true;
				}
			}
		} else if (type == 2 && isNumberspace(str)) {// 非负数
			Float value = Float.parseFloat(str);
			if (value >= 0) {
				if (max != null && max > 0) {// 最大值判断
					if (max > value) {
						res = true;
					}
				} else {
					res = true;
				}
			}
		}

		return res;
	}

	/**
	 * 验证字符串真实长度是否合法
	 * <ul>
	 * <li>中文2个字符，英文1个字符</li>
	 * </ul>
	 * 
	 * @param str
	 *            需验证的字符串
	 * @param length
	 *            长度
	 * @return
	 */
	public static boolean checkStrLen(String str, int length) {
		boolean res = true;
		if (!StringUtils.isEmpty(str)) {
			int strLen = 0;
			String chinese = "[\u4e00-\u9fa5]";
			for (int i = 0; i < str.length(); i++) {
				String temp = str.substring(i, i + 1);
				if (temp.matches(chinese)) {
					strLen += 2;
				} else {
					strLen += 1;
				}
			}
			if (strLen > length) {
				res = false;
			}
		}
		return res;
	}

	/**
	 * 若字符串值为null，则返回""，否则返回原值
	 */
	public static String null2str(String str) {
		if (isEmpty(str)) {
			str = "";
		}
		return str;
	}

	public static void main(String[] args) {
		System.out.println("d233.33".matches("\\d+\\.\\d+$|-\\d+\\.\\d+$"));
	}

}
