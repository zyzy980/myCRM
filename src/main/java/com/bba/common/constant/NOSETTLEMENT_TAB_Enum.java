package com.bba.common.constant;

import lombok.Getter;

@Getter
public enum NOSETTLEMENT_TAB_Enum {

	VIP("0", "VIP","VIP"),
	SEC("1", "SEC","保密车"),
	CON("2", "CON","合同价"),
	SAV("3", "SAV","救援车");


	private String code;
	private String name;
	private String title;

	private NOSETTLEMENT_TAB_Enum(String code, String name,String title) {
		this.code = code;
		this.name = name;
		this.title = title;
	}
	
}
