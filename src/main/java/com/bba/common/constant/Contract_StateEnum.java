package com.bba.common.constant;

import lombok.Getter;

@Getter
public enum Contract_StateEnum {

	NORMAL("0", "正常"),
	CHECK("1", "审核"),
	CANCEL("2", "注销");

	private String code;
	private String name;

	private Contract_StateEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
}
