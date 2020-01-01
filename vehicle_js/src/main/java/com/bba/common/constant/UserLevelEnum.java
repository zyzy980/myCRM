package com.bba.common.constant;

import lombok.Getter;

@Getter
public enum UserLevelEnum {

	USERLEVEL_YWY("1", "业务员"),
	USERLEVEL_SWY("2", "商务员"),
	USERLEVEL_CWY("3", "财务员"),
	USERLEVEL_FXY("4", "风险员"),
	USERLEVEL_CYS("5", "承运商"),
	USERLEVEL_GLY("0", "管理员");

	private String code;
	private String name;

	private UserLevelEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
}
