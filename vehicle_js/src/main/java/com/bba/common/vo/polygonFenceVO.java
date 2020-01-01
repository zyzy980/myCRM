package com.bba.common.vo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

/**
 * 圆形电子围栏
 * @author fs
 *
 */
@Data
public class polygonFenceVO {
	private double P;
	private double O;
	private double lng;			//经度
	private double lat;			//纬度


	public polygonFenceVO(Map map) {
		P = Double.valueOf(map.get("P").toString());
		O = Double.valueOf(map.get("O").toString());
		this.lng = Double.valueOf(map.get("lng").toString());
		this.lat = Double.valueOf(map.get("lat").toString());
	}
}
