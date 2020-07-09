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
public class SysSheetIdVO {

	//p_within_code varchar2,               --内码
	//p_whcenter varchar2,                --仓储中心
	//p_tablename varchar2,              --表名
	//p_output out varchar2              --输出结果

	private String sn;
	private String p_within_code; //内码
	private String p_whcenter; // 仓储中心
	private String p_tablename; //表名
	private String p_output; //输出结果
	private String p_values1; //??
	private String p_functionName; //功能名
	private String p_front; //单号前缀
	private String p_kind; // 生成规则YYYY,YYYYMM,YYYYMMDD
	private String p_L; //流水长度
	private String p_currid; //当前单号
	private String p_create_by;
	private String p_yw_location; //业务地点
	private String p_ok;

//	public SysSheetIdVO(Sys_SheetIdVO sys_sheetIdVO) {
//		this.p_within_code = sys_sheetIdVO.getWithin_code();
//		this.p_tablename = sys_sheetIdVO.getTable_name();
//		this.p_functionName = sys_sheetIdVO.getFunction_name();
//		this.p_front = sys_sheetIdVO.getFront();
//		this.p_kind = sys_sheetIdVO.getKind();
//		this.p_L = sys_sheetIdVO.getL();
//		this.p_currid = sys_sheetIdVO.getCurrid();
//		this.p_create_by = sys_sheetIdVO.getCreate_by();
//		this.p_yw_location = sys_sheetIdVO.getYw_location();
//	}
//
//	public SysSheetIdVO() {
//	}
}
