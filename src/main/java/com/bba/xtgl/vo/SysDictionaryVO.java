package com.bba.xtgl.vo;

public class SysDictionaryVO {
	private Integer sn;

	private String typeName;

	private String typeCode;

	private Integer parentSn;

	private String remark;
	
	private String within_code;
	private String whcenter;

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public Integer getParentSn() {
		return parentSn;
	}

	public void setParentSn(Integer parentSn) {
		this.parentSn = parentSn;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
}
