package com.bba.common.constant;

import lombok.Getter;

@Getter
public enum LedgerEnum {

	/**台账状态*/
	NORMAL("0", "正常"),
	CHECK("1", "审核"),
	CANCEL("2", "注销"),
	/**台账类型*/
	NORMAL_LEDGER("0", "正常台账"),
	UP_COMPENSATION("1", "对上补差"),
	DOWN_COMPENSATION("2", "对下补差"),
	DOWN_YUGU_COMPENSATION("3", "预估补差"),
	/**对账单状态*/
	DZ_CUS_CONFIRM("5","客户确认"),
	DZ_CREATE_LEDGER("6","台账"),
	/**台账是否开票*/
	INVOICE_FLAG_Y("Y","是"),
	INVOICE_FLAG_N("N","否"),

	/**对下保费状态*/
	PREMIUM_NORMAL("0","正常"),
	PREMIUM_LEDGER("1","台账");

	private String code;
	private String name;

	private LedgerEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static String getCodeByName(String name) {
		for (LedgerEnum enums : LedgerEnum.values()) {
			if (name.equals(enums.getName())) {
				return enums.getCode();
			}
		}
		return null;
	}
}
