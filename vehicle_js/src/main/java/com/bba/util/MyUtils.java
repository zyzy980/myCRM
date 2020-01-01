package com.bba.util;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import com.bba.jcda.vo.Tr_statistical_rulesVO;
import com.bba.settlement.vo.VehicleTotalVO;
import org.apache.commons.lang.math.NumberUtils;

import com.google.gson.Gson;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

public class MyUtils {

	private static Gson gson = new Gson();
	
	public static String decode(String code) {
		/*if (code == null) {
			return code;
		}
		try {
			if(new String(code.getBytes("UTF-8"), "UTF-8").equals(code)) {
				return code;
			}
			return new String(code.getBytes("ISO8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}*/
		return paramDecode(code);
	}
	
	public static boolean isMessyCode(String strName) {
		try {
			Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
			Matcher m = p.matcher(strName);
			String after = m.replaceAll("");
			String temp = after.replaceAll("\\p{P}", "");
			char[] ch = temp.trim().toCharArray();
			int length = (ch != null) ? ch.length : 0;
			for (int i = 0; i < length; i++) {
				char c = ch[i];
				if (!Character.isLetterOrDigit(c)) {
					String str = "" + ch[i];
					if (!str.matches("[\u4e00-\u9fa5]+")) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
         return false;
    }

	
	public static String paramDecode(String code) {
		try {
			if (code == null) {
				return code;
			}
			
			if(isMessyCode(code)){
				return new String(code.getBytes("ISO8859-1"), "UTF-8");
			}
			return code;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
		/*try {
			Map<String, Object> map = JSONUtils.toJSONObject(Map.class, code);
			List<Map<String, String>> obj = (List<Map<String, String>>) map.get(Const.FRONT_CONTROL_GRID_PARAM_FIELD);
			for (Map<String, String> item : obj) {
				if(item!=null && item.get(Const.FRONT_CONTROL_GRID_PARAM_FIELD_DATA)!=null){
					String tempStr = item.get(Const.FRONT_CONTROL_GRID_PARAM_FIELD_DATA);
					if(new String(tempStr.getBytes(Const.STRING_CODE_UTF_8), Const.STRING_CODE_UTF_8).equals(item.get(Const.FRONT_CONTROL_GRID_PARAM_FIELD_DATA))) {
						item.put(Const.FRONT_CONTROL_GRID_PARAM_FIELD_DATA, new String(tempStr.getBytes(Const.STRING_CODE_ISO8859), Const.STRING_CODE_UTF_8));
					}
				}
			}
			return decode(JSONUtils.toJSONString(map));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}*/
	}
	
	public static String nvl(Object obj,String newValue){
		if(obj == null){
			return newValue;
		}
		
		return ""+obj;
	}
	
	public static String toExcelValue(Object obj){
		if(obj!=null){
			if(NumberUtils.isNumber(obj.toString())){
				return ""+Double.parseDouble(obj.toString());
			}
			return obj.toString();
			
		}
		return "";
	} 
	public static String toJSONString(Map<String, Object> params){
		StringBuffer buf = new StringBuffer();
    	for (String key : params.keySet()) {
    		Object value = params.get(key);
    		if(value == null){
				continue;
			}
    		// 对常见的类型自行处理
    		if(value instanceof String){
    			if(NumberUtils.isNumber(value.toString())){
    				buf.append(","+key+":"+value.toString());
    			}else{
    				buf.append(","+key+":\""+value.toString()+"\"");
    			}
    		} else if(value instanceof String[]){
    			buf.append(","+key+":[");
    			for (String v : (String[])value) {
					if(v == null){
						buf.append(v+",");
					}else{
						buf.append("\""+v+"\",");
					}
				}
    			if(buf.toString().endsWith(",")){
    				buf.setLength(buf.length() -1);
    			}
    			buf.append("]");
    		} else if(value instanceof Integer){
    			buf.append(","+key+":"+value.toString());
    		} else if(value instanceof Integer[]){
    			buf.append(","+key+":[");
    			for (Integer v : (Integer[])value) {
					buf.append(v+",");
				}
    			if(buf.toString().endsWith(",")){
    				buf.setLength(buf.length() -1);
    			}
    			buf.append("]");
    		} else{
    			String s = gson.toJson(value);
    			s = s.replaceAll("\"(\\w+)\"(\\s*:\\s*)", "$1$2");
    			buf.append(","+key+":"+s);
    		}
		}
    	if(buf.length() > 0){
    		return buf.substring(1);
    	}
    	return buf.toString();

	}
	
	
	public static String urlDecode(String code) {
		if (code == null) {
			return code;
		}
		try {
			return URLDecoder.decode(code,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * @param name
	 * @return 字符串首字母大写;
	 */
	 public static String captureName(String name) {
       name = name.substring(0, 1).toUpperCase() + name.substring(1);
       return  name;
    }
	 public static String shuzu(String[] obj){
		 String shuzu="";
		 StringBuffer buf = new StringBuffer();
		if(obj.length==1){
			buf.append("'"+obj[0]+"'");
			shuzu=buf.toString();
		}else if(obj.length>1){
			for (int i = 0; i < obj.length; i++) {
				buf.append("'"+obj[i]+"',");
			}
			shuzu=buf.toString();
			shuzu=shuzu.substring(0,shuzu.length()-1);
		}
		 return shuzu;
	 }
	 public static String shuzuOne(String[] obj){
		 String shuzu="";
		 StringBuffer buf = new StringBuffer();
		if(obj.length==1){
			buf.append(""+obj[0]+"");
			shuzu=buf.toString();
		}else if(obj.length>1){
			for (int i = 0; i < obj.length; i++) {
				buf.append(""+obj[i]+"|");
			}
			shuzu=buf.toString();
			shuzu=shuzu.substring(0,shuzu.length()-1);
		}
		 return shuzu;
	 }

	 /**
	  * 数据流水号,参数('201701', 1, 3) = 201701001
	  * 
	  * @param startWith 前缀
	  * @param count 数量
	  * @param endLength 后缀
	  * @return 流水号
	  */
	 @SuppressWarnings("unused")
	public static String getDataNumber(String startWith, int count, int endLength){
			String countStr = String.valueOf(count);
			for (int i = 0; endLength - countStr.length() > 0; i++) {
				countStr = "0" + countStr;
			}
			return startWith + countStr;
	 }
	 
	 /**
	  * 到处excel出现错误就进行提示
	  * @param response
	  */
	 public static void responseErrorExcel(HttpServletResponse response, String message){
		 response.setCharacterEncoding("UTF-8");
		 response.setContentType("text/html;charset=UTF-8");
		 try {
			response.getWriter().println(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		 
		/* try {
			response.getWriter().println("");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 ServletRequestAttributes servletContainer = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
	        HttpServletRequest request = servletContainer.getRequest();
			String rootPath = request.getSession().getServletContext().getRealPath("/Index/error.html");
		try {
			response.sendRedirect(rootPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	 }
	 
	 
	 /**
	  * 号码部分信息隐藏
	  */
	 public static String hiddenTel(String tel) {
		 if(tel == null || StringUtils.length(tel) < 7) {
			 return tel;
		 }	
		 return tel.substring(0, 3) + "****" + tel.substring(tel.length()-4, tel.length());
	 }

	 /**
	  * 姓名部分信息隐藏
	  */
	 public static String hiddenName(String name) {
		 if(name == null) {
			 return "";
		 }
		 return name.substring(0, 1) + "**";
	 }

	 /**
	  * 车牌部分信息隐藏
	  */
	 public static String hiddenTruckno(String truckno) {
		 if(truckno == null) {
			 return "";
		 }
		truckno = truckno.substring(0, 3) + "***" + truckno.substring(truckno.length() - 1, truckno.length()); 
		return truckno;
	 }


	/***
	 * 四舍五入保留两位
	 * @param d
	 * @return
	 */
	public static double formatDouble2(double d) {
		// 新方法，如果不需要四舍五入，可以使用RoundingMode.DOWN
		BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.UP);
		return bg.doubleValue();
	}

	/**
	 * 保留两位小数(不四舍五入)
	 * @param doubleValue
	 * @return
	 */
	public static String calculateProfit(double doubleValue) {
		// 保留4位小数
		DecimalFormat df = new DecimalFormat("0.0000");
		String result = df.format(doubleValue);
		// 获取小数 . 号第一次出现的位置
		int index = firstIndexOf(result, ".");
		return result.substring(0, index + 3);
	}

	private static int firstIndexOf(String str, String pattern) {
		for (int i = 0; i < (str.length() - pattern.length()); i++) {
			int j = 0;
			while (j < pattern.length()) {
				if (str.charAt(i + j) != pattern.charAt(j))
					break;
				j++;
			}
			if (j == pattern.length())
				return i;
		}
		return -1;
	}

	//验证匹配规则
	public static boolean matchingRules (Tr_statistical_rulesVO rulesVO, VehicleTotalVO vehicletotalVO) {
		boolean flag = false;
		if (vehicletotalVO.getTrans_mode().equalsIgnoreCase(rulesVO.getTrans_mode())
				&& vehicletotalVO.getBegin_city().equalsIgnoreCase(rulesVO.getBegin_city())
				&& vehicletotalVO.getEnd_city().equalsIgnoreCase(rulesVO.getEnd_city())
				&& vehicletotalVO.getBegin_date().getTime()>rulesVO.getBegin_date().getTime()
				&& vehicletotalVO.getReceipt_date().getTime()<rulesVO.getEnd_date().getTime()) {
			flag=true;
		}
		return flag;
	}
}
