package com.bba.jcda.vo;
/**
 * 订单类型
 * @author Administrator
 *
 */
public class Zd_Sheet_KindVO {
   
	private String sn;//SN
	private String code;//货主编号
	private String name;//货物编号
	private String create_by;//创建
	private String create_date;//创建时间
	private String remark;//备注
	private String state;//状态
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
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
}
