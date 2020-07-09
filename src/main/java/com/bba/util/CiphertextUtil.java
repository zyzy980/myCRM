package com.bba.util;

import java.io.UnsupportedEncodingException;

/**
 * 加密解密
 * @author fanx
 * @date 2018/11/06
 */
public class CiphertextUtil {
	
	/**
	 * 解密
	 * @param 		需要解密的字符串
	 * @return		解密后的密文
	 */
    public static String decrypt(String str) {
        if ( str == null ) {
            return "转换失败";
        }
        byte[] s = pack(str); //十六进制转byte数组
        String gbk;
        try {
            gbk = new String(s, "gbk"); //byte数组转中文字符串
        } catch ( UnsupportedEncodingException ignored ) {
            gbk = "转换失败";
        }
        return gbk;
    }
    
	/**
	 * 加密
	 * @param str 	需要加密的字符串
	 * @return		加密后的密文
	 */
    public static String encryption(String str) {
        if ( str == null ) {
            return "转换失败";
        }
        String gbk;
        try {
            byte[] by = str.getBytes("GBK");  //中文字符串转byte数组
            gbk = unpack(by); // byte数组转十六进制
        } catch ( Exception E ) {
            gbk = "转换失败";
        }
        return gbk;
    }
    
    /**
     * 十六进制转byte数组
     */
    public static byte[] pack(String str) {
        int nibbleshift = 4;
        int position = 0;
        int len = str.length() / 2 + str.length() % 2;
        byte[] output = new byte[len];
        for (char v : str.toCharArray()) {
            byte n = (byte) v;
            if (n >= '0' && n <= '9') {
                n -= '0';
            } else if (n >= 'A' && n <= 'F') {
                n -= ('A' - 10);
            } else if (n >= 'a' && n <= 'f') {
                n -= ('a' - 10);
            } else {
                continue;
            }
            output[position] |= (n << nibbleshift);
            if (nibbleshift == 0) {
                position++;
            }
            nibbleshift = (nibbleshift + 4) & 7;
        }
        return output;
    }
	
	/**
     * byte数组转十六进制
     */
    private static String unpack(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder + "";
    }
    
    public static void main(String[] args) {
        String encryption = encryption("ZT1811060009");
		System.out.println(encryption);
		System.out.println(decrypt(encryption));
        
	}
}
