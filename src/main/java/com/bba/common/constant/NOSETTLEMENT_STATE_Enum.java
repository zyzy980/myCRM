package com.bba.common.constant;

import lombok.Getter;

@Getter
public enum NOSETTLEMENT_STATE_Enum {

	NORMAL("0", "正常"),
	SETTLEMENT("1", "结算"),
	BILL("2", "生成账单"),
	BILLCHECK("3", "账单审核"),
	BUSSCHECK("4", "商务审核"),
	AMOUNTCHECK("5", "财务审核"),
	EMAIL("6", "发送邮件"),
	CUS("7", "客户确认"),
	LEDGER("8", "台账"),
	INVOICE("9", "已开票");


	private String code;
	private String name;

	private NOSETTLEMENT_STATE_Enum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
}
