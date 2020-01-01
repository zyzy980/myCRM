package com.bba.basics.utils;

import java.util.ArrayList;
import java.util.List;

public class PDfParamsVo {
	
	public String path;	//pdf文件保存路径
	
	public String code;	//报表编号

	public String cusName; 	//客户名称
	
	//yyyy-mm-dd 至 yyyy-mm-dd
	public String ywDate;	//业务时间
	
	public String amount;	//全部金额
	
	public String hxamount;	//待核销金额
	
	//和为100，A4纸张的宽度
	public int[] width = {20,18,13,20,14,15};
	
	public List<String> headList = new ArrayList<>();//明细表头
	
	public List<List<String>> date = new ArrayList<>(); //明细数据
	
	public String allAmount;	//所有待收金额
	
	


}
