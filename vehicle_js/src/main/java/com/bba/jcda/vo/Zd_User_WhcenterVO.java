package com.bba.jcda.vo;

import java.util.List;

import com.bba.common.vo.PageVO;

 

public class Zd_User_WhcenterVO extends PageVO{
	private	String within_code;
	private String whcenter;
	private String userid;
	private String remark;
	private String create_date;
	private String create_by;
	private String id;
	private String logo;
	
	//非数据库字段
	private List<Zd_User_WhcenterVO> Wz_User_Whcenters;
	private List<Zd_User_WhcenterVO> deleteWhcenters;
	private String idDefault;
	private String name;//仓储中心名称
	private String name_en;//仓储中心名称
	
	private String yw_title;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdDefault() {
		return idDefault;
	}
	public void setIdDefault(String idDefault) {
		this.idDefault = idDefault;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Zd_User_WhcenterVO> getWz_User_Whcenters() {
		return Wz_User_Whcenters;
	}
	public void setWz_User_Whcenters(List<Zd_User_WhcenterVO> wz_User_Whcenters) {
		Wz_User_Whcenters = wz_User_Whcenters;
	}
	public List<Zd_User_WhcenterVO> getDeleteWhcenters() {
		return deleteWhcenters;
	}
	public void setDeleteWhcenters(List<Zd_User_WhcenterVO> deleteWhcenters) {
		this.deleteWhcenters = deleteWhcenters;
	}
	public String getWithin_code() {
		return within_code;
	}
	public void setWithin_code(String within_code) {
		this.within_code = within_code;
	}
	public String getWhcenter() {
		return whcenter;
	}
	public void setWhcenter(String whcenter) {
		this.whcenter = whcenter;
	}
	public String getuserid() {
		return userid;
	}
	public void setuserid(String userid) {
		this.userid = userid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getCreate_by() {
		return create_by;
	}
	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}
	public String getYw_title() {
		return yw_title;
	}
	public void setYw_title(String yw_title) {
		this.yw_title = yw_title;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	
	public String getName_en() {
		return name_en;
	}
	public void setName_en(String name_en) {
		this.name_en = name_en;
	}
	
}
