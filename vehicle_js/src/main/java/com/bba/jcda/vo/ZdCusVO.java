package com.bba.jcda.vo;

import com.bba.common.interceptor.EntityField;

/**
 * 客户档案
 * 
 * @author lenovo
 *
 */
public class ZdCusVO {
	@EntityField(value = "工厂编号")
	private String within_code;

	@EntityField(value = "客户编号")
	private String code;

	@EntityField(value = "客户名称")
	private String name;

	private String shortname;
	private String kind;
	private String remark;
	private String state;

	@EntityField(value = "创建人")
	private String create_by;

	private String create_date;
	private String sn;
	private String update_by;

	@EntityField(value = "更新日期")
	private String update_date;
	/* public List<ZdCusAddressVO> addressVOs; */

	// 以下字段作为接受传值，不做业务需求
	private String city;
	private String address;
	private String linkMan;
	private String linkTel;
	private String linkQQ;
	private String linkMail;
	private String location_code;

	public ZdCusVO() {
	}

	public String getLocation_code() {
		return location_code;
	}

	public void setLocation_code(String location_code) {
		this.location_code = location_code;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getLinkTel() {
		return linkTel;
	}

	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}

	public String getLinkQQ() {
		return linkQQ;
	}

	public void setLinkQQ(String linkQQ) {
		this.linkQQ = linkQQ;
	}

	public String getLinkMail() {
		return linkMail;
	}

	public void setLinkMail(String linkMail) {
		this.linkMail = linkMail;
	}

	public String getWithin_code() {
		return within_code;
	}

	public void setWithin_code(String within_code) {
		this.within_code = within_code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
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
}
