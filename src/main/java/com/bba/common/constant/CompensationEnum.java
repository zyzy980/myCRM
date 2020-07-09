package com.bba.common.constant;

import lombok.Getter;

@Getter
public enum CompensationEnum {

	/**补差类型*/
	UP_COMPENSATION("1", "对上补差"),
	DOWN_COMPENSATION("2", "对下补差"),
	DOWN_YUGU_COMPENSATION("3", "预估补差"),
	/**补差状态*/
	COMPENSATION_NORMAL("0", "正常"),
	COMPENSATION_LEDGER("1", "台账"),
	COMPENSATION_INVOICE("2", "已开票");


	private String code;
	private String name;

	private CompensationEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static String getCodeByName(String name) {
		for (CompensationEnum enums : CompensationEnum.values()) {
			if (name.equals(enums.getName())) {
				return enums.getCode();
			}
		}
		return null;
	}
}
