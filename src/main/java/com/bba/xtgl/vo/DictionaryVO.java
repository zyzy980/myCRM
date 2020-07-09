package com.bba.xtgl.vo;

import com.bba.common.vo.PageVO;

public class DictionaryVO extends PageVO{
	
	private String sn;				//SN
	
	private String typeName;		//数据字典类别名称
		
	private String parentSN;		//父节点SN
	
	private String typeCode;		//数据字典类别编码
	
	private String remark;		 	//备注
	
	private String dictext;			//字典名称x`  中文
	
	private String dictext_en;			//字典名称x` 英文

	private String dictext_vi;      // 字典名称  越南语
	
	private String dicorder;		//排序
	
	private String isDefault;		//是否默认
	
	private String operateType;		//判断执行什么操作
	
	private String dicvalue;
	
	private String within_code;
	private String whcenter;

	public String getDictext_vi() {
		return dictext_vi;
	}

	public void setDictext_vi(String dictext_vi) {
		this.dictext_vi = dictext_vi;
	}
	
	public String getDicvalue() {
		return dicvalue;
	}

	public void setDicvalue(String dicvalue) {
		this.dicvalue = dicvalue;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getDictext() {
		return dictext;
	}

	public void setDictext(String dictext) {
		this.dictext = dictext;
	}

	public String getDicorder() {
		return dicorder;
	}

	public void setDicorder(String dicorder) {
		this.dicorder = dicorder;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getParentSN() {
		return parentSN;
	}

	public void setParentSN(String parentSN) {
		this.parentSN = parentSN;
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

	public String getDictext_en()
	{
		return dictext_en;
	}

	public void setDictext_en(String dictext_en)
	{
		this.dictext_en = dictext_en;
	}
	
	
}
