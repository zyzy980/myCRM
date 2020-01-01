/**
	此文件归微科创源有限公司所有
	
	创  建  人:caining
	创建时间:2018年6月14日

	文件描述:
		提供对本模块的一些通用工具
*/
package com.bba.jcda.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bba.util.StringUtils;

public class Util {

	private static class LazyHolder {
		private static final Util INSTANCE = new Util();
	}

	public static final Util getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * 生成业务地点参数
	 *
	 * @param kind
	 * @param locationCode
	 * @return List<Map<String, String>>
	 */
	public List<Map<String, String>> generalLocationParam(String kind, String locationCode) {
		List<Map<String, String>> list = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			Map<String, String> map = new HashMap<>();
			if (i == 0) {
				map.put(Const.JCDA_GRID_KEY_FIELD, "a.within_code");
				map.put(Const.JCDA_GRID_KEY_OP, Const.options.get(Const.JCDA_GRID_VAL_OPTION_EQ));
				map.put(Const.JCDA_GRID_KEY_DATA, "b.within_code");
				list.add(map);
			} else if (i == 1) {
				map.put(Const.JCDA_GRID_KEY_FIELD, "a.sn");
				map.put(Const.JCDA_GRID_KEY_OP, Const.options.get(Const.JCDA_GRID_VAL_OPTION_EQ));
				map.put(Const.JCDA_GRID_KEY_DATA, "b.base_sn");
				list.add(map);
			} else if (i == 2) {
				map.put(Const.JCDA_GRID_KEY_FIELD, "b.kind");
				map.put(Const.JCDA_GRID_KEY_OP, Const.options.get(Const.JCDA_GRID_VAL_OPTION_EQ));
				map.put(Const.JCDA_GRID_KEY_DATA, "'" + kind + "'");
				list.add(map);
			} else if (i == 3) {
				if(StringUtils.isNotEmpty(locationCode)){
					map.put(Const.JCDA_GRID_KEY_FIELD, "b.location_code");
					map.put(Const.JCDA_GRID_KEY_OP, Const.options.get(Const.JCDA_GRID_VAL_OPTION_EQ));
					map.put(Const.JCDA_GRID_KEY_DATA, "'" + locationCode + "'");
					list.add(map);
				}
			}
		}
		return list;
	}

	/**
	 * 导入校验
	 *
	 * @param
	 * @return boolean成功为true
	 */
	public boolean importValid(String[] intArr, String doubleArr, String mail, String phone, Method method) {
		return false;
	}

	/**
	 * 导入验证-验证值是否存在于指定数据集合中
	 * 
	 * @param value
	 *            需验证的值
	 * @param list
	 *            数据集合
	 * @param fieldName
	 *            字段名称（用于错误提示信息拼接）
	 * @return 错误提示信息字符串
	 */
	public static String valueValid(String value, List<String> list, String fieldName) {
		String res = "";
		if (!StringUtils.isEmpty(value)) {
			if (list == null || list.size() <= 0 || !list.contains(value)) {
				res = "、" + fieldName + "：" + value;
			}
		}
		return res;
	}

	/**
	 * 导入验证-值最大长度验证（中文2个字符、英文1个字符）
	 * 
	 * @param value
	 *            需验证的值
	 * @param maxLen
	 *            值的最大长度
	 * @param fieldName
	 *            字段名称（用于错误提示信息拼接）
	 * @return 错误提示信息字符串
	 */
	public static String valueLenValid(String value, int maxLen, String fieldName) {
		String res = "";
		if (!StringUtils.checkStrLen(value, maxLen)) {
			res = "、" + fieldName + "（值过长）：" + value;
		}
		return res;
	}

	/**
	 * 导入验证-验证值是否为数字格式并判断最大值
	 * 
	 * @param value
	 *            需验证的值
	 * @param type
	 *            验证类型（1：非负整数、2：非负数[整数或小数]）
	 * @param fieldName
	 *            字段名称（用于错误提示信息拼接）
	 * @param maxValue
	 *            最大值，null则使用默认值100000[十万]
	 * @return 错误提示信息字符串
	 */
	public static String valueNumberValid(String value, int type, String fieldName, Integer maxValue) {
		String res = "";
		if (!StringUtils.isEmpty(value)) {
			if (maxValue == null || maxValue < 0) {
				maxValue = 100000;
			}
			if (!StringUtils.checkIsPositiveNumber(value, type, maxValue)) {
				res = "、" + fieldName + "：" + value;
			}
		}
		return res;
	}

	/**
	 * 导入验证-验证值是否为日期格式
	 * 
	 * @param value
	 *            需验证的值
	 * @param fieldName
	 *            字段名称（用于错误提示信息拼接）
	 * @return 错误提示信息字符串
	 */
	public static String valueDateValid(String value, String fieldName) {
		String res = "";
		if (!StringUtils.isEmpty(value) && !StringUtils.isDate(value)) {
			res = "、" + fieldName + "：" + value;
		}
		return res;
	}
}
