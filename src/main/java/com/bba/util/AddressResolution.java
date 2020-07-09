package com.bba.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressResolution {
	/**
	 * 解析地址
	 * 
	 * @author liuj
	 * @param address
	 * @return
	 */
	public static List<Map<String, String>> addressResolution(String address) {
		String regex = "(?<province>[^省]+省|.+自治区|[^市]+市)(?<city>[^自治州]+自治州|[^市]+市|[^盟]+盟|[^地区]+地区|.+区划)(?<county>[^市]+市|[^县]+县|[^旗]+旗|.+区)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
		Matcher m = Pattern.compile(regex).matcher(address);
		String province = null, city = null, county = null, town = null, village = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> row = null;
		while (m.find()) {
			row = new LinkedHashMap<String, String>();
			province = m.group("province");
			row.put("province", province == null ? "" : province.trim());
			city = m.group("city");
			row.put("city", city == null ? "" : city.trim());
			county = m.group("county");
			row.put("county", county == null ? "" : county.trim());
			town = m.group("town");
			row.put("town", town == null ? "" : town.trim());
			village = m.group("village");
			row.put("village", village == null ? "" : village.trim());
			list.add(row);
		}
		return list;
	}
	
	/**
	 * 验证传入的字符串是否匹配正则表达式
	 * @param str
	 * @return
	 */
	public static boolean validateStrIsReges(String str) {
		boolean flag = false;
		String regex = "(?<province>[^省]+省|.+自治区|[^市]+市)(?<city>[^自治州]+自治州|[^市]+市|[^盟]+盟|[^地区]+地区|.+区划)(?<county>[^市]+市|[^县]+县|[^旗]+旗|.+区)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
		flag = str.matches(regex);
		return flag;
	}
	
	public static void main(String[] args) {//AS(新世界大丸百货)
		String address = "广东省深圳市龙岗区龙岗爱联如意96号";
		addressResolution(address);
		/*List<Map<String, String>> addressResolution = addressResolution("江苏省");
		for(Map<String,String> maps : addressResolution) {
			System.out.println(maps.get("province"));
		}
		System.out.println(addressResolution);*/
	}
}
