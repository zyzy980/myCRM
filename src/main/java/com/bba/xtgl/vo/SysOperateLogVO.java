package com.bba.xtgl.vo;

public class SysOperateLogVO {
	private String sn; // SN

	private String withinCode; // 内码

	private String operator; // 操作人(编号-名称)

	private String operatedate; // 操作时间

	private String operateitem; // 操作项

	private String operateresult; // 操作结果（Fail,Success）

	private String params; // 相关参数或者语句

	private String platform; // 系统来源(网页;小程序;公众号;)

	private String operatemodule; // 操作模块

	private String ywLocation; // 业务地点

	private String remark; // 备注

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getWithinCode() {
		return withinCode;
	}

	public void setWithinCode(String withinCode) {
		this.withinCode = withinCode;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperatedate() {
		return operatedate;
	}

	public void setOperatedate(String operatedate) {
		this.operatedate = operatedate;
	}

	public String getOperateitem() {
		return operateitem;
	}

	public void setOperateitem(String operateitem) {
		this.operateitem = operateitem;
	}

	public String getOperateresult() {
		return operateresult;
	}

	public void setOperateresult(String operateresult) {
		this.operateresult = operateresult;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getOperatemodule() {
		return operatemodule;
	}

	public void setOperatemodule(String operatemodule) {
		this.operatemodule = operatemodule;
	}

	public String getYwLocation() {
		return ywLocation;
	}

	public void setYwLocation(String ywLocation) {
		this.ywLocation = ywLocation;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
