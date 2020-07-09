package com.bba.common.constant;

import lombok.Getter;

@Getter
public enum BusinessData_FromEnum {

	FROM_VL("0", "VL"),
	FROM_MANUAL("1", "手工录入");

	private String code;
	private String name;

	private BusinessData_FromEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
}
