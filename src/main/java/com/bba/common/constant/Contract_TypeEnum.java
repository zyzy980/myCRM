package com.bba.common.constant;

import lombok.Getter;

@Getter
public enum Contract_TypeEnum {

	TEMP("0", "暂定合同"),
	FORMAL("1", "正式合同"),
	YUGU("2", "预估合同");

	private String code;
	private String name;

	private Contract_TypeEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
}
