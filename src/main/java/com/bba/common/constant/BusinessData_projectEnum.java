package com.bba.common.constant;

import lombok.Getter;

@Getter
public enum BusinessData_projectEnum {

	COMMODITY_CAR("1", "商品车"),
	NON_COMMODITY_CAR("2", "非商品车"),
	OTHER_CAR("3", "其他");

	private String code;
	private String name;

	private BusinessData_projectEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static String getCodeByName(String name){
		for(BusinessData_projectEnum enums:BusinessData_projectEnum.values()){
			if(name.equals(enums.getName())){
				return enums.getCode();
			}
		}
		return  null;
	}

}
