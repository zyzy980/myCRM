package com.bba.common.constant;

import lombok.Getter;

@Getter
public enum BaseDataKeyEnum {

	CR_CUSTOMER("CR_CUSTOMER", "客户档案"),
	ZD_CARRIER("ZD_CARRIER", "承运商档案"),
	ZD_CARRIER_PREMIUM("4", "保险公司");

	private String code;
	private String name;

	private BaseDataKeyEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
}
