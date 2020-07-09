package com.bba.xtgl.vo;

import com.bba.common.vo.PageVO;

public class ButtonVO extends PageVO {

	private String buttonId; // 按钮id

	private String moduleid; // 模块id

	private String buttonUse; // 按钮编码

	private String buttonName; // 按钮名称
	
	private String buttonName_en; // 按钮名称

	private String buttonSdescription; // 按钮说明

	private String buttonIcon; // 按钮图标

	private String buttonOrder; // 按钮排序

	private String roleId; // 角色

	private boolean isOwnAuthorith; // 是否存在

	private String operateType; // 判断执行什么操作

	public String getButtonId() {
		return buttonId;
	}

	public void setButtonId(String buttonId) {
		this.buttonId = buttonId;
	}

	public String getModuleid() {
		return moduleid;
	}

	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}

	public String getButtonUse() {
		return buttonUse;
	}

	public void setButtonUse(String buttonUse) {
		this.buttonUse = buttonUse;
	}

	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}
	
	
	
	public String getButtonName_en() {
		return buttonName_en;
	}

	public void setButtonName_en(String buttonName_en) {
		this.buttonName_en = buttonName_en;
	}
	

	public String getButtonSdescription() {
		return buttonSdescription;
	}

	public void setButtonSdescription(String buttonSdescription) {
		this.buttonSdescription = buttonSdescription;
	}

	public String getButtonIcon() {
		return buttonIcon;
	}

	public void setButtonIcon(String buttonIcon) {
		this.buttonIcon = buttonIcon;
	}

	public String getButtonOrder() {
		return buttonOrder;
	}

	public void setButtonOrder(String buttonOrder) {
		this.buttonOrder = buttonOrder;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public boolean getIsOwnAuthorith() {
		return isOwnAuthorith;
	}

	public void setIsOwnAuthorith(boolean isOwnAuthorith) {
		this.isOwnAuthorith = isOwnAuthorith;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	private String within_code;
	public String getWithin_code()
	{
		return within_code;
	}
	public void setWithin_code(String within_code)
	{
		this.within_code=within_code;	
	}
	
}
