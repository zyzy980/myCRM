package com.bba.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils {

	
	public static final String[] NORMAL_DATE_FORMAT = {"yyyy-MM-dd", "yyyy/MM/dd"};  
    
	public static final String LIU_SHUI_HAO = "yyyyMMdd";
	
	public static final String NORMAL_DATE_FORMAT_NEW = "yyyy-MM-dd HH:mm:ss";
    
	private static boolean isLeapYear(int year){
		return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) ;
	}
	public static boolean isDateFormat(String dateString){
		//使用正则表达式 测试 字符 符合 dddd-dd-dd 的格式(d表示数字)
			Pattern p = Pattern.compile("\\d{4}+[-]\\d{1,2}+[-]\\d{1,2}+|\\d{4}+[/]\\d{1,2}+[/]\\d{1,2}+");
			Matcher m = p.matcher(dateString);
			if(!m.matches()){	return false;} 
			
			
			//得到年月日
			String[] array = dateString.split(dateString.contains("-")?"-":"/");
			int year = Integer.valueOf(array[0]);
			int month = Integer.valueOf(array[1]);
			int day = Integer.valueOf(array[2]);
			
			if(month<1 || month>12){	return false;}
			int[] monthLengths = new int[]{0, 31, -1, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
			if(isLeapYear(year)){
				monthLengths[2] = 29;
			}else{
				monthLengths[2] = 28;
			}
			int monthLength = monthLengths[month];
			if(day<1 || day>monthLength){
				return false;	
			}
			return true;
	}
	
	public static String dateCommonFormat(String dateStr){
		SimpleDateFormat sdf = null;
		if(dateStr.contains("-")){
			sdf = new SimpleDateFormat(NORMAL_DATE_FORMAT[0]);
		}else if(dateStr.contains("/")){
			sdf = new SimpleDateFormat(NORMAL_DATE_FORMAT[1]);
		}else{
			return ""; 
		}
		try {
			return DateUtils.dateFormat(sdf.parse(dateStr));
		} catch (Exception e) {
		}
		
		return "";
	}
	
	public static String nowDate() {
		return dateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	public static String nowDate(String pattern) {
		return dateFormat(new Date(), pattern);
	}
	
	
	public static String nowDateNew() {
		return dateFormat(new Date(), "yyyy-MM-dd");
	}
	
	public static String dateFormat() {
		return dateFormat(new Date());
	}
	
	public static String dateFormat(Date date) {
		return dateFormat(date,"yyyy-MM-dd");
	}
	
	public static String dateFormat(Date date,String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}
	
	public static String dateStringFormat(String date) {
		if(date == null){
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d = formatter.parse(date);
			return dateFormat(d,"yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getDate(String canshu){
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(canshu);
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static Date parseDate(String string, String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		try {
			return formatter.parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date getDateBefore(Date d,int day){  
		Calendar now =Calendar.getInstance();  
		now.setTime(d);  
		now.set(Calendar.DATE,now.get(Calendar.DATE)-day);  
		return now.getTime();  
	}  

	/** 
	 * 得到几天后的时间 
	 * @param d 
	 * @param day 
	 * @return 
	 */  
	public static Date getDateAfter(Date d,int day){  
		Calendar now =Calendar.getInstance();  
		now.setTime(d);  
		now.set(Calendar.DATE,now.get(Calendar.DATE)+day);  
		return now.getTime();  
	}

	/**
	 * 获取几个小时后的时间
	 * @Description TODO
	 * @param date 当前时间
	 * @param hours 小时
	 * @Author lao li
	 * @Date
	 * @return
	*/
	public static Date getDateAfterHours(Date date, int hours) {
		Calendar ca=Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.HOUR_OF_DAY, hours);
		return ca.getTime();
	}
	
	
	public static Integer getDayOfWeek(Date d){  
		Calendar now =Calendar.getInstance();  
		now.setTime(d);  
		Integer dayOfWeek = now.get(Calendar.DAY_OF_WEEK) - 1;
		return dayOfWeek == 0 ? 7 : dayOfWeek;  
	}  
	
	/**
	 * 指定月份中工作日历与发货频次的结合
	 * @param strYear
	 * @param strMonth
	 * @param strWorkDay
	 * @param strPingCi
	 * @return
	 */
	public static Integer getfreqByMonth(String strYear, String strMonth, String strWorkDay, String strPingCi){  
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		String[] arr = strWorkDay.split("");
		Calendar cal=Calendar.getInstance();		
		cal.set(Calendar.YEAR, Integer.parseInt(strYear));
		cal.set(Calendar.MONTH, Integer.parseInt(strMonth) - 1 );
		int  count = 0;
		for(int i = 0 ; i < arr.length ; i++){			
			cal.set(Calendar.DAY_OF_MONTH, i + 1);
			if(arr[i].equals("1") && strPingCi.contains(String.valueOf(getDayOfWeek(cal.getTime())))){
				count=count + 1;
			}
		}		
		return count;  
	}  
	
	public static Integer getfreqByWeek(String strYear, String strWeek, String strWorkDay, String strPingCi){  
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal=Calendar.getInstance();	
		cal.setTime(getDateByYearWeek(Integer.parseInt(strYear),Integer.parseInt(strWeek)));
		String[] arr = strWorkDay.split("");			
		int  count = 0;
		for(int i = 0 ; i < arr.length ; i++){			
			cal.set(Calendar.DAY_OF_MONTH, i);
			if(arr[i].equals("1") && strPingCi.contains(String.valueOf(getDayOfWeek(cal.getTime())))){
				count=count + 1;
			}
		}		
		return count;  
	}  
	
	/**
	 * 根据开始日期计算零件发货次数
	 * @param startDate
	 * @param strWorkDay
	 * @param strPingCi
	 * @return
	 */
	public static Integer getfreqByDate(Date startDate, String strWorkDay, String strPingCi){  
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		String[] arr = strWorkDay.split("");
		Calendar cal=Calendar.getInstance();		
		cal.setTime(startDate);
		int  count = 0;
		if(strPingCi == null || strPingCi.isEmpty()){
			for(int i = 0 ; i < arr.length ; i++){			
				if(arr[i].equals("1")){
					count=count + 1;
				}
			}
		}else{
			for(int i = 0 ; i < arr.length ; i++){			
				cal.set(Calendar.DATE, i + 1);
				if(arr[i].equals("1") && strPingCi.contains(String.valueOf(getDayOfWeek(cal.getTime())))){
					count=count + 1;
				}
			}
		}
				
		return count;  
	}  
	/**
	 * 根据具体年份周数获取日期范围
	 * @param year
	 * @param week
	 * @return
	 */
	public static Date getDateByYearWeek(int year, int week) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal=Calendar.getInstance();
		// 设置每周的开始日期
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.WEEK_OF_YEAR, week);
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		return cal.getTime();
	}
	/**
	 * 根据具体年份月份获取日期范围
	 * @param year
	 * @param week
	 * @return
	 */
	public static Date getDateByYearMonth(int year, int month) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal=Calendar.getInstance();
		// 设置每月的开始日期
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}
	/**
	 * 根据具体年份月份获取日期范围
	 * @param year
	 * @param week
	 * @return
	 */
	public static Date getDateByYearMonth(String year, String month) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal=Calendar.getInstance();
		// 设置每月的开始日期
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}
	
	
	public static Date getDateAddByDay(Date date, int day) {
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}

	public static Date getDateAddByMonth(Date date, int month) {
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, month);
		return cal.getTime();
	}
	
	/**
	 * 这个时间到现在之间有多少天
	 * @param date
	 * @return
	 */
	public static int compareDate(Date date) {
		return compareDate(date, new Date());
	}
	
	/**
	 * 2个时间相差的天数
	 * @param date
	 * @param date2
	 * @return
	 */
	public static int compareDate(Date date, Date date2) {
		Long i = date.getTime();
		Long j = date2.getTime();
		Long num = new BigDecimal(i).subtract(new BigDecimal(j)).longValue();
		long day = num/(60 * 60 *1000 * 24);
		return (int)Math.abs(day);
	}

	/**
	 * 计算两个时间相差几个小时
	 * @Description TODO
	 * @param afterDate 未来时间
	 * @param currentDate 当前时间
	 * @Author lao li
	 * @Date
	 * @return
	*/
	public static long compareDateHours(Date afterDate, Date currentDate) {
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = afterDate.getTime() - currentDate.getTime();
		// 计算差多少小时
		long hour = diff % nd / nh;
		return hour;
	}
	
	public static void main(String[] args) throws Exception{

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = "2019-05-16 21:05:00";
		String currentDate = "2019-05-16 20:05:00";
		Date afterDate = sdf.parse(date);
		System.out.println(sdf.format(new Date()));
		System.out.println(compareDateHours(afterDate, sdf.parse(currentDate)));
	}
}
