package com.bba.jsyw.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
  * 接口回传
  * @author weekeyanYear
  *
  */
public class InterfaceResultVO {

	@JsonProperty
	private Boolean  Success;
	@JsonProperty
	private String ErrorMessage;
	@JsonProperty
	private String Data1;
	@JsonProperty
	private String Data2;
	@JsonProperty
	private String Data3;
	@JsonProperty
	private String Data4;
	@JsonProperty
	private Object Data5;
	@JsonProperty
	private Boolean Cancel;
	
	/**
	 * 无参构造
	 */
	public InterfaceResultVO() {
		super();
	};
	
	
	/**
	 * Ture
	 * @return
	 */
	public static InterfaceResultVO successResult () {
		InterfaceResultVO result = new InterfaceResultVO(true,"",null,false);
		return result;
	}
	
	
	/**
	 * Fale
	 * @return
	 */
	public static InterfaceResultVO failResult (String simpleMessage) {
		InterfaceResultVO result = new InterfaceResultVO(false,simpleMessage,null,false);
		return result;
	}

	 /***
	  *
	  * @param Success
	  * @param ErrorMessage
	  * @param Data5
	  * @param Cancel
	  */
	public InterfaceResultVO (Boolean Success,String ErrorMessage,Object Data5,Boolean Cancel) {
		super();
		this.Success = Success;
		this.ErrorMessage = ErrorMessage;
		this.Data1 = "";
		this.Data2 = "";
		this.Data3 = "";
		this.Data4 = "";
		this.Data5 = Data5;
		this.Cancel = Cancel;
				
	}

	 /**
	  *
	  * @param Success
	  * @param ErrorMessage
	  * @param Data1
	  * @param Data2
	  * @param Data3
	  * @param Data4
	  * @param Data5
	  * @param Cancel
	  */
	public InterfaceResultVO(Boolean Success, String ErrorMessage, String Data1,
			String Data2, String Data3, String Data4, Object Data5,
			Boolean Cancel) {
		super();
		this.Success = Success;
		this.ErrorMessage = ErrorMessage;
		this.Data1 = Data1;
		this.Data2 = Data2;
		this.Data3 = Data3;
		this.Data4 = Data4;
		this.Data5 = Data5;
		this.Cancel = Cancel;
	}

	@JsonIgnore
	public boolean isSuccess() {
		return Success;
	}
	@JsonIgnore
	public void setSuccess(boolean Success) {
		this.Success = Success;
	}
	@JsonIgnore
	public String getErrormessage() {
		return ErrorMessage;
	}
	@JsonIgnore
	public void setErrormessage(String ErrorMessage) {
		this.ErrorMessage = ErrorMessage;
	}
	@JsonIgnore
	public String getData1() {
		return Data1;
	}
	@JsonIgnore
	public void setData1(String Data1) {
		this.Data1 = Data1;
	}
	@JsonIgnore
	public String getData2() {
		return Data2;
	}
	@JsonIgnore
	public void setData2(String Data2) {
		this.Data2 = Data2;
	}
	@JsonIgnore
	public String getData3() {
		return Data3;
	}
	@JsonIgnore
	public void setData3(String Data3) {
		this.Data3 = Data3;
	}
	@JsonIgnore
	public String getData4() {
		return Data4;
	}
	@JsonIgnore
	public void setData4(String Data4) {
		this.Data4 = Data4;
	}
	@JsonIgnore
	public Object getData5() {
		return Data5;
	}
	@JsonIgnore
	public void setData5(Object Data5) {
		this.Data5 = Data5;
	}
	@JsonIgnore
	public boolean isCancel() {
		return Cancel;
	}
	@JsonIgnore
	public void setCancel(boolean Cancel) {
		this.Cancel = Cancel;
	}
	
	
}
