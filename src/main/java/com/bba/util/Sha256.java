package com.bba.util;

import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * sha256 demo
 * @author ych
 */
public class Sha256 {

    /**
     * 生成签名返回map
     */
    public static Map<String, String> generateSignRetMap(String key, String app_secret) {
        Map<String, String> param = new HashMap<>(4);
        //密钥
        param.put("app_key", key);
        param.put("app_secret", app_secret);
        //随机参数
        String time = String.valueOf(new Date().getTime()).substring(0, 10);
        String uuid = UUID.randomUUID().toString();
        param.put("timestamp", time);
        param.put("noncestr", uuid);
        String sign = generateSign(param);
        param.put("sign", sign);
        param.put("app_key", URLEncoder.encode(key));
        return param;
    }

    /***
     * 生成签名返回字符
     * @return
     */
    public static String generateSign(String key, String app_secret) {
        return generateSignRetMap(key, app_secret).get("sign");
    }

    /***
     * 根据map生成签名
     * @param param
     * @return
     */
    public static String generateSign(Map<String, String> param) {
        return getSha256(format(param));
    }


    /**
     * 将参数按照ASCII 进行排序，并且拼接&
     * @param map 请求参数
     * @return 拼接好的参数字符串
     */
    public static String format(Map<String, String> map) {

        String result = "";
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());
        // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });

        // 构造签名键值对的格式
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> item : infoIds) {
            if (item.getKey() != null || item.getKey() != "") {
                String key = item.getKey();
                String val = item.getValue();
                sb.append(key + "=" + val + "&");
            }

        }
        return sb.substring(0, sb.length()-1);
    }

    /**
     * 利用java原生的类实现SHA256加密
     * @param s 待加密的报文
     * @return
     */
    public static String getSha256(final String s) {
        MessageDigest messageDigest;
        String encodestr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(s.getBytes("UTF-8"));
            encodestr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodestr;
    }

    private static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

   /* public static void main(String[] args){
        Map<String, String> param = new HashMap<>();
        param.put("app_key", request.getParameter("app_key"));
        param.put("timestamp", request.getParameter("timestamp"));
        param.put("noncestr", request.getParameter("noncestr"));
        param.put("app_secret", new SysConfig().getTmsSecret());
    }*/
}
