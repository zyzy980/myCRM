package com.bba.jcda.vo;

import com.bba.common.interceptor.EntityField;

public class Zd_Transport_RouteVO {
	@EntityField(value = "SN")
	private String sn;
	@EntityField(value = "内码")
	private String within_code;
	@EntityField(value = "GUID")
	private String guid;
	@EntityField(value = "起运PDC")
	private String warehouse_code;
	@EntityField(value = "起运地")
	private String start_city;
	@EntityField(value = "目的省份")
	private String dest_provence;
	@EntityField(value = "目的城市")
	private String dest_city;
	@EntityField(value = "专线代码")
	private String line_code;
	@EntityField(value = "专线名称")
	private String line_codename;
	@EntityField(value = "起运站点")
	private String th_sitecode;
	@EntityField(value = "经销商代码")
	private String dealer_code;
	@EntityField(value = "创建")
	private String create_by;
	@EntityField(value = "创建时间")
	private String create_date;
	@EntityField(value = "修改")
	private String update_by;
	@EntityField(value = "修改时间")
	private String update_date;
	@EntityField(value = "类型")
	private String type;
	@EntityField(value = "二级承建商名称")
	private String twoconname;
	@EntityField(value = "二级承建商代码")
	private String twoconcode;
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getWithin_code() {
		return within_code;
	}
	public void setWithin_code(String within_code) {
		this.within_code = within_code;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getWarehouse_code() {
		return warehouse_code;
	}
	public void setWarehouse_code(String warehouse_code) {
		this.warehouse_code = warehouse_code;
	}
	public String getStart_city() {
		return start_city;
	}
	public void setStart_city(String start_city) {
		this.start_city = start_city;
	}
	public String getDest_provence() {
		return dest_provence;
	}
	public void setDest_provence(String dest_provence) {
		this.dest_provence = dest_provence;
	}
	public String getDest_city() {
		return dest_city;
	}
	public void setDest_city(String dest_city) {
		this.dest_city = dest_city;
	}
	public String getLine_code() {
		return line_code;
	}
	public void setLine_code(String line_code) {
		this.line_code = line_code;
	}
	public String getLine_codename() {
		return line_codename;
	}
	public void setLine_codename(String line_codename) {
		this.line_codename = line_codename;
	}
	public String getTh_sitecode() {
		return th_sitecode;
	}
	public void setTh_sitecode(String th_sitecode) {
		this.th_sitecode = th_sitecode;
	}
	public String getDealer_code() {
		return dealer_code;
	}
	public void setDealer_code(String dealer_code) {
		this.dealer_code = dealer_code;
	}
	public String getCreate_by() {
		return create_by;
	}
	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getUpdate_by() {
		return update_by;
	}
	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}
	public String getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTwoconname() {
		return twoconname;
	}
	public void setTwoconname(String twoconname) {
		this.twoconname = twoconname;
	}
	public String getTwoconcode() {
		return twoconcode;
	}
	public void setTwoconcode(String twoconcode) {
		this.twoconcode = twoconcode;
	}
	
}
