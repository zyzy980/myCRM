package com.bba.common.vo;

import lombok.Data;

@Data
public class FenceItemVO
{
	//{"drawType":"circle","coordDatas":{"radius":1287.6481456657787,"lng":123.157782,"lat":41.677005}}
	private String drawType;
	private CircularFenceVO coordDatas;

}
