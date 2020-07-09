package com.bba.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class FenceListVO
{
	//{"drawType":"circle","coordDatas":{"radius":1287.6481456657787,"lng":123.157782,"lat":41.677005}}
	private String drawType;
	private List<CircularFenceVO> coordDatas;

}
