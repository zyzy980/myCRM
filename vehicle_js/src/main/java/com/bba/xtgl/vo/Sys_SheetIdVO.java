/**
 * 生成单号数据VO
 * BY:LTJ
 * DATE:2018-07-10
 * */
package com.bba.xtgl.vo;

import lombok.Data;

/**
 * 生成单号数据VO
 * */
@Data
public class Sys_SheetIdVO {

	private String within_code;
	private String yw_location;
	private String table_name;
	private String function_name;
	private String front;
	private String kind;
	private String l;
	private String currid;
	private String create_date;
	private String create_by;
	private String last_sheet;
	private String record;
	private String record_date;

	private String sn;

}
