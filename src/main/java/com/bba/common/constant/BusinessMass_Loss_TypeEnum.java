package com.bba.common.constant;

import lombok.Getter;

@Getter
public enum BusinessMass_Loss_TypeEnum {

	NOMAL("0", "正常"),
	GENERAL_DAMAGE("1", "普损"),
	HEAVY_LOSS("2", "重损");

	private String code;
	private String name;

	private BusinessMass_Loss_TypeEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static String getCodeByName(String name){
		for(BusinessMass_Loss_TypeEnum enums: BusinessMass_Loss_TypeEnum.values()){
			if(name.equals(enums.getName())){
				return enums.getCode();
			}
		}
		return  null;
	}

}
