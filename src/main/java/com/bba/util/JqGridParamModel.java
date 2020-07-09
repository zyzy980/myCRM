package com.bba.util;

import java.io.Serializable;

import org.springframework.util.StringUtils;


/**
 * 已弃用 待全部代码改正后, 直接删除
 */
public class JqGridParamModel implements Serializable {

	private static final long serialVersionUID = -2947651640240143391L;

	private Integer page = 1;
	private Integer rows = 15;
	// 过滤条件
	private String filters;
	private Boolean _search;
	// 排序字段
	private String sidx;
	// 排序关键字
	private String sord;
	
	private Object extension;
	
	// 是否分页
	private Boolean isPage = true; 
	
	private Object param;
	
	public void put(String filed,String value){
		put(filed , "eq", value);
	}
	public void put(String filed,String op,String value){
		if(StringUtils.isEmpty(filters)){
			filters = "{\"rules\":[{\"field\":\""+filed+"\",\"op\":\""+op+"\",\"data\":\""+value+"\"}]}";
		}else if(filters.endsWith("[]}")){
			filters = filters.substring(0, filters.length()-2);
			filters+="{\"field\":\""+filed+"\",\"op\":\""+op+"\",\"data\":\""+value+"\"}]}";
		}else{
			filters = filters.substring(0, filters.length()-2);
			filters+=",{\"field\":\""+filed+"\",\"op\":\""+op+"\",\"data\":\""+value+"\"}]}";
		}
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public Boolean getIsPage() {
		return isPage;
	}

	public void setIsPage(Boolean isPage) {
		this.isPage = isPage;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public Boolean get_search() {
		return _search;
	}

	public void set_search(Boolean _search) {
		this._search = _search;
	}

	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}
	public Object getParam() {
		return param;
	}
	public void setParam(Object param) {
		this.param = param;
	}
	public Object getExtension() {
		return extension;
	}
	public void setExtension(Object extension) {
		this.extension = extension;
	}

}