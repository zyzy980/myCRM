package com.bba.common.constant;

import lombok.Getter;

@Getter
public enum SETTLEMENT_StateEnum {

	NORMAL("0", "正常"),
	SETTLEMENT("1", "结算"),
	GENERATE_BILLS("2", "生成账单"),
	BILL_REVIEW("3", "账单审核"),
	BUSINESS_AUDIT("4", "商务审核"),
	FINANCIAL_AUDIT("5", "财务审核"),
	SEND_MAIL("6", "发送邮件"),
	CUSTOMER_CONFIRMATION("7", "客户确认"),
	GENERATING_ACCOUNTS("8", "台账"),
	INVOICED("9", "已开票");

	private String code;
	private String name;

	private SETTLEMENT_StateEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
}
