package com.bba.common.vo;

import com.bba.jcda.vo.contractor.BaseVO;
import com.bba.util.JqGridSearchParamHandler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageVO extends BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String total;
	
	private String records;
	
	private Object userData;
	
	private String pageRows;
	
	private String rowNum;
	
	private String minPage;
	
	private String maxPage;
	
	private String endTime;		//结束时间-用于分页的条件判断
	
	private String page;		//请求时当前页数
	
	private Object rows;		//请求时每页条数
	
	private String sidx;		//排序字段
	
	private String sord;		//排序关键字
	
	private String filters;		//jqgrid参数

	public PageVO() {
		super();
	}
	
	public PageVO(int page,int pageRows,Object rows,int records) {
		this.page = ""+page;
		this.pageRows =""+ pageRows;
		this.rows = rows;
		this.records = ""+records;
		if (records <= pageRows) {
			this.total = "1";
		} else if (records % pageRows == 0) {
			this.total = "" + records / pageRows;
		} else {
			this.total = "" + (records / pageRows+1);
		}
	}
	
	public PageVO(String page, Object pageRows, Object rows, String records) {
		this.page = ""+page;
		this.pageRows =""+ pageRows;
		this.rows = rows;
		this.records = ""+records;
		if (Integer.valueOf(records) <= Integer.valueOf("" +pageRows)) {
			this.total = "1";
		} else if (Integer.valueOf(records) % Integer.valueOf("" + pageRows) == 0) {
			this.total = "" + Integer.valueOf(records) / Integer.valueOf("" +pageRows);
		} else {
			this.total = "" + (Integer.valueOf(records) / Integer.valueOf("" + pageRows)+1);
		}
	}
	
	public PageVO(String page, Object pageRows, Object rows, String records, Object userData) {
		this.page = ""+page;
		this.pageRows =""+ pageRows;
		this.rows = rows;

		this.userData = userData;
		this.records = ""+records;
		if (Integer.valueOf(records) <= Integer.valueOf("" +pageRows)) {
			this.total = "1";
		} else if (Integer.valueOf(records) % Integer.valueOf("" + pageRows) == 0) {
			this.total = "" + Integer.valueOf(records) / Integer.valueOf("" +pageRows);
		} else {
			this.total = "" + (Integer.valueOf(records) / Integer.valueOf("" + pageRows)+1);
		}
	}
	
	public void setSord(String sord) {
		this.sord = sord;
//		filters = JqGridSearchParamHandler.newProcessSql(filters, sidx, sord);
	}

	public void setFilters(String filters) {
		this.filters = filters;
//		filters = JqGridSearchParamHandler.newProcessSql(filters, sidx, sord);
	}
}
