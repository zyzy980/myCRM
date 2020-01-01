package com.bba.common.constant;

import lombok.Getter;

@Getter
public enum BusinessNon_CommonEnum {

	//属性
	ATT_IN_SYSTEM("0", "体系内"),
	ATT_OUT_SYSTEM("1", "体系外"),
	//分类
	TYPE_VIP("0", "VIP"),
	TYPE_SECRET("1", "保密"),
	TYPE_HTJ("2", "合同价"),
	TYPE_JYC("3", "救援车");


	private String code;
	private String name;

	private BusinessNon_CommonEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
}
