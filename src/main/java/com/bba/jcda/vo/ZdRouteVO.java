package com.bba.jcda.vo;

import java.util.List;

public class ZdRouteVO {
	private String sn;
	private String within_code;
	private String route_code;
	private String route_name;
	private String orderseq;
	private String state;
	private String remark;
	private String create_by;
	private String create_date;
	private String update_by;
	private String update_date;
	private String truck_type;
	private String begin_local;
	private String end_local;
	private String contractor_code;

	private String routeCity;
	private List<Zd_Route_DetailVO> zd_route_detaillist;
	private List<Zd_Route_DestVO> zd_route_destvol;
	private List<String> ids;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getWithin_code() {
		return within_code;
	}

	public void setWithin_code(String within_code) {
		this.within_code = within_code;
	}

	public String getRoute_code() {
		return route_code;
	}

	public void setRoute_code(String route_code) {
		this.route_code = route_code;
	}

	public String getRoute_name() {
		return route_name;
	}

	public void setRoute_name(String route_name) {
		this.route_name = route_name;
	}

	public String getOrderseq() {
		return orderseq;
	}

	public void setOrderseq(String orderseq) {
		this.orderseq = orderseq;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreate_by() {
		return create_by;
	}

	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getUpdate_by() {
		return update_by;
	}

	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}

	public String getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}

	public String getTruck_type() {
		return truck_type;
	}

	public void setTruck_type(String truck_type) {
		this.truck_type = truck_type;
	}

	public String getBegin_local() {
		return begin_local;
	}

	public void setBegin_local(String begin_local) {
		this.begin_local = begin_local;
	}

	public String getEnd_local() {
		return end_local;
	}

	public void setEnd_local(String end_local) {
		this.end_local = end_local;
	}

	public String getContractor_code() {
		return contractor_code;
	}

	public void setContractor_code(String contractor_code) {
		this.contractor_code = contractor_code;
	}

	public String getRouteCity() {
		return routeCity;
	}

	public void setRouteCity(String routeCity) {
		this.routeCity = routeCity;
	}

	public List<Zd_Route_DetailVO> getZd_route_detaillist() {
		return zd_route_detaillist;
	}

	public void setZd_route_detaillist(List<Zd_Route_DetailVO> zd_route_detaillist) {
		this.zd_route_detaillist = zd_route_detaillist;
	}

	public List<Zd_Route_DestVO> getZd_route_destvol() {
		return zd_route_destvol;
	}

	public void setZd_route_destvol(List<Zd_Route_DestVO> zd_route_destvol) {
		this.zd_route_destvol = zd_route_destvol;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}
}
