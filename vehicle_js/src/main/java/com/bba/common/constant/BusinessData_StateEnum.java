package com.bba.common.constant;

import lombok.Getter;

@Getter
public enum BusinessData_StateEnum {

	NEWDATA("0", "新单"),
	NORMAL("1", "正常"),
	CHECK("2", "审核"),
	CANCEL("3", "注销");

	private String code;
	private String name;

	private BusinessData_StateEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
}
