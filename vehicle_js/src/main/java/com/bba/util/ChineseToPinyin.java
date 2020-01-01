package com.bba.util;

import java.text.SimpleDateFormat;

public class ChineseToPinyin {
	public static String getPinYinHeadChar(String str) {
/*
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}*/
		return str;
	}

	/**
	 * 20位末尾的数字id
	 */
	public static int Guid=100;

	public static String getGuid() {
		
		ChineseToPinyin.Guid+=1;

		long now = System.currentTimeMillis();  
		//获取4位年份数字  
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy");  
		//获取时间戳  
		String time=dateFormat.format(now);
		String info=now+"";
		//获取三位随机数  
		//int ran=(int) ((Math.random()*9+1)*100); 
		//要是一段时间内的数据连过大会有重复的情况，所以做以下修改
		int ran=0;
		if(ChineseToPinyin.Guid>999){
			ChineseToPinyin.Guid=100;    	
		}
		ran=ChineseToPinyin.Guid;
				
		return time+info.substring(2, info.length())+ran;  
	}
}
