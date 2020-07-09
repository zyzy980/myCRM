package com.bba.jcda.vo;


/**
 * 定制订单表状态
 * @author Administrator
 *
 */
public class Zd_BusinessStateVO {
	private String sn;//SN
	private String within_code;//内码
	private String business_type;//业务系统类型
	private String state;//状态编号
	private String state_name;//状态名称
	private String create_by;//创建
	private String create_date;//创建时间
	private String update_by;//更新
	private String update_date;//更新时间
	private String is_state_main;//主状态
	private String state_shortname;
	private String is_show_light;
	private String operatetype;
	private String forglobal_state;//在途状态
	private String istruckarrive_state; //是否为车辆到达
	private String state_name_en;//英文名称
	private String location_code;
	
	
	
	
  
	
	
	public String getState_name_en() {
		return state_name_en;
	}
	public void setState_name_en(String state_name_en) {
		this.state_name_en = state_name_en;
	}
	public String getIstruckarrive_state() {
		return istruckarrive_state;
	}
	public void setIstruckarrive_state(String istruckarrive_state) {
		this.istruckarrive_state = istruckarrive_state;
	}
	public String getForglobal_state() {
		return forglobal_state;
	}
	public void setForglobal_state(String forglobal_state) {
		this.forglobal_state = forglobal_state;
	}
	public String getLocation_code() {
		return location_code;
	}
	public void setLocation_code(String location_code) {
		this.location_code = location_code;
	}
	public String getIs_show_light() {
		return is_show_light;
	}
	public void setIs_show_light(String is_show_light) {
		this.is_show_light = is_show_light;
	}
	public String getState_shortname() {
		return state_shortname;
	}
	public void setState_shortname(String state_shortname) {
		this.state_shortname = state_shortname;
	}
	public String getOperatetype() {
		return operatetype;
	}
	public void setOperatetype(String operatetype) {
		this.operatetype = operatetype;
	}
	public String getIs_state_main() {
		return is_state_main;
	}
	public void setIs_state_main(String is_state_main) {
		this.is_state_main = is_state_main;
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
	public String getBusiness_type() {
		return business_type;
	}
	public void setBusiness_type(String business_type) {
		this.business_type = business_type;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getState_name() {
		return state_name;
	}
	public void setState_name(String state_name) {
		this.state_name = state_name;
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
