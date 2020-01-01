package com.bba.common.constant;

import lombok.Getter;

@Getter
public enum InvoiceEnum {

	/**发票状态*/
	NORMAL("0", "正常"),
	CANCEL("1", "注销"),
	PAY_CHECK("2", "付款审核");//只有对下
	private String code;
	private String name;

	private InvoiceEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
}
