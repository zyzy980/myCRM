package com.bba.common.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FenceListV1
{
	private String drawType;
	private List<polygonFenceVO> coordDatas;

	public String getDrawType() {
		return drawType;
	}

	public void setDrawType(String drawType) {
		this.drawType = drawType;
	}

	public List<polygonFenceVO> getCoordDatas() {
		return coordDatas;
	}

	public void setCoordDatas(List<Map> coordDatas) {

		List<polygonFenceVO> polygonFenceVOS = new ArrayList<>();
		for (Map coordData : coordDatas) {
			polygonFenceVOS.add(new polygonFenceVO(coordData));
		}

		this.coordDatas = polygonFenceVOS;
	}

}
