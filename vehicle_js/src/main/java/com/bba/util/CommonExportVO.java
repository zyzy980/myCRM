package com.bba.util;


/**
 * 公共导出实体类
 */
public class CommonExportVO<T> {
	
	private String url;
	
	private String within_code;
	
	private String whcenter;
	
	private String FiledData;
	/**
	 * 导出excel标题
	 */
	private String export_title;
	
	/**
	 * 导出列英文名称数组
	 */
	private String[] Export_ColumnCode;
	
	/**
	 * 导出列英文名称数组
	 */
	private String[] Export_ColumnName;

	/**
	 * 导出列宽度数组
	 */
	private String[] Export_ColumnWidth;

	/**
	 * 导出列对齐方式数组
	 */
	private String[] Export_ColumnAlign;

	/**
	 * 导出设置必填列为红色字体数组
	 */
	private String[] Export_ColumnNeed;

	/**
	 * 导出列类型, 0.以查询条件导出数据, 1.导出当前界面数据, 2.导出列表选中数据
	 */
	private String Export_KIND;
	
	/**
	 * 查询对条件对象json格式数据
	 */
	private Object Export_customSearchFilters;

	/**
	 * 当前页所有对象json格式数据
	 */
	private String Export_CurrentPageData;
	
	/**
	 * 方法标识
	 */
	private String flag;

	public String getExport_title() {
		return export_title;
	}

	public void setExport_title(String export_title) {
		this.export_title = export_title;
	}

	public String[] getExport_ColumnCode() {
		return Export_ColumnCode;
	}

	public void setExport_ColumnCode(String[] export_ColumnCode) {
		Export_ColumnCode = export_ColumnCode;
	}

	public String[] getExport_ColumnName() {
		return Export_ColumnName;
	}

	public void setExport_ColumnName(String[] export_ColumnName) {
		Export_ColumnName = export_ColumnName;
	}

	public String[] getExport_ColumnWidth() {
		return Export_ColumnWidth;
	}

	public void setExport_ColumnWidth(String[] export_ColumnWidth) {
		Export_ColumnWidth = export_ColumnWidth;
	}

	public String[] getExport_ColumnAlign() {
		return Export_ColumnAlign;
	}

	public void setExport_ColumnAlign(String[] export_ColumnAlign) {
		Export_ColumnAlign = export_ColumnAlign;
	}

	public String[] getExport_ColumnNeed() {
		return Export_ColumnNeed;
	}

	public void setExport_ColumnNeed(String[] Export_ColumnNeed) {
		this.Export_ColumnNeed = Export_ColumnNeed;
	}


	public String getExport_KIND() {
		return Export_KIND;
	}

	public void setExport_KIND(String export_KIND) {
		Export_KIND = export_KIND;
	}

	public Object getExport_customSearchFilters() {
		return Export_customSearchFilters;
	}

	public void setExport_customSearchFilters(Object export_customSearchFilters) {
		Export_customSearchFilters = export_customSearchFilters;
	}

	public String getExport_CurrentPageData() {
		return Export_CurrentPageData;
	}

	public void setExport_CurrentPageData(String export_CurrentPageData) {
		Export_CurrentPageData = export_CurrentPageData;
	}

	public String getWhcenter() {
		return whcenter;
	}

	public void setWhcenter(String whcenter) {
		this.whcenter = whcenter;
	}

	public String getWithin_code() {
		return within_code;
	}

	public void setWithin_code(String within_code) {
		this.within_code = within_code;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getFiledData() {
		return FiledData;
	}

	public void setFiledData(String filedData) {
		FiledData = filedData;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
