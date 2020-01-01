package com.bba.xtgl.vo;

import com.bba.common.vo.PageVO;

public class RoleVO extends PageVO {
	private String roleid;		//角色编号
	
	private String within_code; //内码
	
	private String rolename;	//角色名称
	
	private String roledescription;	//备注
	
	private String createby;	//创建人
	
	private String createdate;	//创建时间
	
	private String updateby;	//更新人
	
	private String updatedate;	//更新时间
	
	private String userid;		//用户id
	
	private String moduleid;	//模块id
	
	
	/**
	 *	权限按钮表
	 * @return
	 */
	
	private String buttonId;	//按钮Id
	
	private String moduleId;	//模块ID
	
	private String buttonName;	//按钮名称
	
	private String buttonName_en;	//按钮名称英文
	
	private String buttonUse;	//按钮使用
	
	private String buttonsDescription;	//按钮种类
	
	private String buttonIcon;	//按钮图标
	
	private String buttonOrder;	//按钮排序
	
	private String moduleName;	//模块名称
	
	private String operateType;	//判断执行什么操作
	
	
	public String getButtonId() {
		return buttonId;
	}

	public void setButtonId(String buttonId) {
		this.buttonId = buttonId;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
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

	public String getButtonUse() {
		return buttonUse;
	}

	public void setButtonUse(String buttonUse) {
		this.buttonUse = buttonUse;
	}

	public String getButtonsDescription() {
		return buttonsDescription;
	}

	public void setButtonsDescription(String buttonsDescription) {
		this.buttonsDescription = buttonsDescription;
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

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getWithin_code() {
		return within_code;
	}

	public void setWithin_code(String within_code) {
		this.within_code = within_code;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getRoledescription() {
		return roledescription;
	}

	public void setRoledescription(String roledescription) {
		this.roledescription = roledescription;
	}

	public String getCreateby() {
		return createby;
	}

	public void setCreateby(String createby) {
		this.createby = createby;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getUpdateby() {
		return updateby;
	}

	public void setUpdateby(String updateby) {
		this.updateby = updateby;
	}

	public String getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(String updatedate) {
		this.updatedate = updatedate;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getModuleid() {
		return moduleid;
	}

	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}
 

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	
	
}
