package com.bba.jcda.vo;

import java.util.List;

public class ZdRouteVoCommon {
	private Zd_Transport_RouteVO transport_RouteVO;
	
	private List<Zd_Transport_Route_DetailVO> transport_Route_DetailVOs;

	public Zd_Transport_RouteVO getTransport_RouteVO() {
		return transport_RouteVO;
	}

	public void setTransport_RouteVO(Zd_Transport_RouteVO transport_RouteVO) {
		this.transport_RouteVO = transport_RouteVO;
	}

	public List<Zd_Transport_Route_DetailVO> getTransport_Route_DetailVOs() {
		return transport_Route_DetailVOs;
	}

	public void setTransport_Route_DetailVOs(
			List<Zd_Transport_Route_DetailVO> transport_Route_DetailVOs) {
		this.transport_Route_DetailVOs = transport_Route_DetailVOs;
	}
}
