package com.bba.xtgl.vo;

public class SysUserDetailRolesVO {
	private String within_code;//roleId 
	private String id;//roleId 
	private String userId;
	private String text;//roleName
	private boolean checked;//是否选中

	private String roleid;
	
	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getWithin_code() {
		return within_code;
	}
	public void setWithin_code(String within_code) {
		this.within_code = within_code;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	
}
