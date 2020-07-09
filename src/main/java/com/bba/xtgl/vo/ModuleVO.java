package com.bba.xtgl.vo;

import java.util.List;

import com.bba.common.vo.PageVO;
import com.bba.util.StringUtils;

public class ModuleVO extends PageVO {


	private String moduleid;			//模块ID
	
	private String modulename;			//模块名称
	
	private String modulename_en;			//模块名称
	
	private String moduledescription;	//模块类型
	
	private String moduleurl;		    //模块url
	
	private String modulefatherid;		//模块父节点id
	
	private String modulefathername;	//模块父节点名称
	
	private String isshow;				//模块是否展示
	
	private String isnav;				//模块是否导航
	
	private String ishomepage;			//是否是主页
	
	private String ico;					//图标
	
	private String roleId;				//权限id
	
	private String userCode;			//用户id	
	
	private String orderid;				//排序
	
	private String within_code;
	
	private List<ModuleVO> modulelist;

	private String blue_ico;

	public String getModuleid() {
		return moduleid;
	}

	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}

	public String getModulename() {
		return modulename;
	}

	public void setModulename(String modulename) {
		this.modulename = modulename;
	}
	
	public String getModulename_en() {
		return modulename_en;
	}

	public void setModulename_en(String modulename_en) {
		this.modulename_en = modulename_en;
	}

	public String getModuledescription() {
		return moduledescription;
	}

	public void setModuledescription(String moduledescription) {
		this.moduledescription = moduledescription;
	}

	public String getModuleurl() {
		return moduleurl;
	}

	public void setModuleurl(String moduleurl) {
		this.moduleurl = moduleurl;
	}

	public String getModulefatherid() {
		return modulefatherid;
	}

	public void setModulefatherid(String modulefatherid) {
		this.modulefatherid = modulefatherid;
	}

	public String getModulefathername() {
		return modulefathername;
	}

	public void setModulefathername(String modulefathername) {
		this.modulefathername = modulefathername;
	}

	public String getIsshow() {
		return isshow;
	}

	public void setIsshow(String isshow) {
		this.isshow = isshow;
	}

	public String getIsnav() {
		return isnav;
	}

	public void setIsnav(String isnav) {
		this.isnav = isnav;
	}

	public String getIshomepage() {
		return ishomepage;
	}

	public void setIshomepage(String ishomepage) {
		this.ishomepage = ishomepage;
	}

	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public List<ModuleVO> getModulelist() {
		return modulelist;
	}

	public void setModulelist(List<ModuleVO> modulelist) {
		this.modulelist = modulelist;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	
	public String getWithin_code() {
		return within_code;
	}

	public void setWithin_code(String within_code) {
		this.within_code = within_code;
	}

	public String getBlue_ico() {return blue_ico;}

	public void setBlue_ico(String blue_ico) {

		this.blue_ico = blue_ico;
	}



}
