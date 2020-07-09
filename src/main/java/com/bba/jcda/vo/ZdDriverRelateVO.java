package com.bba.jcda.vo;

import com.bba.jcda.vo.contractor.BaseVO;

public class ZdDriverRelateVO extends BaseVO {
    private String sn;
    private String within_code;
    private String code;
    private String contractor;
    private String contractor_name;
    private String name;
    private String mobile;
    private String state;
    private String create_by;
    private String create_date;
    private String update_by;
    private String update_date;
    
    
	public String getContractor_name() {
		return contractor_name;
	}
	public void setContractor_name(String contractor_name) {
		this.contractor_name = contractor_name;
	}
	public ZdDriverRelateVO() {
		// TODO Auto-generated constructor stub
	}
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getContractor() {
		return contractor;
	}
	public void setContractor(String contractor) {
		this.contractor = contractor;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
