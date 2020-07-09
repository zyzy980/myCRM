package com.bba.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 字符转编码，解码帮助类
 */
public class ENDECodeUtils {

	
	/**
	 * url参数编码
	 * @param code
	 * @return
	 */
	public static String URLEncode(String code){
		if(code == null){
			return "";
		}
		try {
			return java.net.URLEncoder.encode(code, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
public static void main(String[] args) {
	System.out.println(URLEncode("{ss:\"ss11\"}"));
}	
	/**
	 * url参数解码
	 * @param code
	 * @return
	 */
	public static String URLDecode(String code){
		if(code == null){
			return "";
		}
		try {
			return java.net.URLDecoder.decode(code, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	

	/**
	 * MD5 32位加密
	 * @param code 源字符
	 * @return 加密字符
	 */
	public static String MD5Encode(String code) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(code.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString().toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
