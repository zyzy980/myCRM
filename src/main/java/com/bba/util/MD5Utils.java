package com.bba.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	/**
	 * MD5 32位加密
	 * 
	 * @param code
	 *            源字符
	 * @return 加密字符
	 */
	public static String encode(String code) {
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


	public static void main(String[] args) {
		
		System.out.println(encode("1"));
	}
}

