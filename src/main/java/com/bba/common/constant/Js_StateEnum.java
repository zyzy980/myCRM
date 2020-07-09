package com.bba.common.constant;

import lombok.Getter;

@Getter
public enum Js_StateEnum {

	/**对上结算状态*/
	NORMAL("0", "正常"),
	SETTLEMENT("1", "结算"),
	CREATE_ZDSHEET("2", "生成账单"),
	CHECK_ZDSHEET("3", "业务审核"),
	SW_CHECK("4", "商务审核"),
	CW_CHECK("5", "财务审核"),
	EMAIL_DZ("6", "发送邮件"),
	CUS_OK("7", "客户确认"),
	CREATE_TZ("8", "台账"),
	CREATE_FP("9", "已开票"),

	/**对下结算状态*/
	DOWN_NORMAL("0", "正常"),
	DOWN_SETTLEMENT("1", "结算"),
	DOWN_CHECK_ZDSHEET("2", "业务审核"),
	DOWN_CW_CHECK("3", "财务审核"),
	DOWN_CARRIER_OK("4", "承运商审核"),
	DOWN_CREATE_TZ("5", "台账"),
	DOWN_CREATE_FP("6", "已开票");

	private String code;
	private String name;

	private Js_StateEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
}
