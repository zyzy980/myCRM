package com.bba.xtgl.vo;

import com.bba.common.vo.PageVO;

public class SysLanguageVo extends PageVO {

	private Integer sn;
	private String within_code;
	private String whcenter;
	private String moduleid;
	private String code;
	private String zh;
	private String en;
	private String remark;
	private String createby;
	private String createdate;
	private String updateby;
	private String updatedate;
	private String moduleurl;
	
	public Integer getSn()
	{
		return sn;
	}
	public void setSn(Integer sn)
	{
		this.sn = sn;
	}
	public String getWithin_code()
	{
		return within_code;
	}
	public void setWithin_code(String within_code)
	{
		this.within_code = within_code;
	}
	public String getWhcenter()
	{
		return whcenter;
	}
	public void setWhcenter(String whcenter)
	{
		this.whcenter = whcenter;
	}
	public String getModuleid()
	{
		return moduleid;
	}
	public void setModuleid(String moduleid)
	{
		this.moduleid = moduleid;
	}
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code = code;
	}
	public String getZh()
	{
		return zh;
	}
	public void setZh(String zh)
	{
		this.zh = zh;
	}
	public String getEn()
	{
		return en;
	}
	public void setEn(String en)
	{
		this.en = en;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public String getCreateby()
	{
		return createby;
	}
	public void setCreateby(String createby)
	{
		this.createby = createby;
	}
	public String getCreatedate()
	{
		return createdate;
	}
	public void setCreatedate(String createdate)
	{
		this.createdate = createdate;
	}
	public String getUpdateby()
	{
		return updateby;
	}
	public void setUpdateby(String updateby)
	{
		this.updateby = updateby;
	}
	public String getUpdatedate()
	{
		return updatedate;
	}
	public void setUpdatedate(String updatedate)
	{
		this.updatedate = updatedate;
	}
	 
	public String getModuleurl()
	{
		return moduleurl;
	}
	public void setModuleurl(String moduleurl)
	{
		this.moduleurl = moduleurl;
	}
	 
}
