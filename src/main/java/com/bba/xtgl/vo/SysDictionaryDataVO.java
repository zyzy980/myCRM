package com.bba.xtgl.vo;

import java.io.Serializable;

public class SysDictionaryDataVO implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer sn;

	private String dicText;

	private String dicText_en;
	
	private String dicValue;

	private String typeCode;

	private Integer dicorder;

	private String remark;

	private Integer isDefault;

	private String operateType;
	
	private String within_code;

	
	public String getWithin_code() {
		return within_code;
	}

	public void setWithin_code(String within_code) {
		this.within_code = within_code;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public String getDicText() {
		return dicText;
	}

	public void setDicText(String dicText) {
		this.dicText = dicText;
	}

	public String getDicValue() {
		return dicValue;
	}

	public void setDicValue(String dicValue) {
		this.dicValue = dicValue;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public Integer getDicorder() {
		return dicorder;
	}

	public void setDicorder(Integer dicorder) {
		this.dicorder = dicorder;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getDicText_en()
	{
		return dicText_en;
	}

	public void setDicText_en(String dicText_en)
	{
		this.dicText_en = dicText_en;
	}

	 
}
