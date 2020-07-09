package com.bba.common.constant;

import lombok.Getter;

@Getter
public enum QuestionsEnum {

	/**发票状态*/
	NORMAL("0", "未使用"),
	USED("1", "已使用"),
	CANCEL("2", "注销");
	private String code;
	private String name;

	private QuestionsEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
}
