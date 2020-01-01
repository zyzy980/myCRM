package com.bba.xtgl.vo;


/**
 * APP版本信息
 * @author lichengan
 *
 */
public class AppUpdateVo extends BaseVo{
	
	public String version;//版本

	public String title;//标题
	
	public String app_name;//APP名称
	
	public String url;//URL
	
	public String remark;//内容
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
