package com.bba.jcda.vo;


public class Zd_Route_DetailVO {
	private String sn;
	private String within_code;
	private String route_code;
	private String  local_code;
	private String  local_name;
	private String  arrive_trigger_event;
	private String  arrive_trigger_state;
	private String  leave_trigger_event;
	private String  leave_trigger_state;
	private String  load_time;
	private String  unload_time;
	private String  ordernum;
	private String  remark;
	private String create_by;
	private String create_date;
	private String update_by;
	private String update_date;
	private String fence;
	private String stateType;
	
	private String dest_km;
	private String drive_time;
	
	private String citys[];
	
	private String isOrder;
	
	
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
	public String getLocal_code() {
		return local_code;
	}
	public void setLocal_code(String local_code) {
		this.local_code = local_code;
	}
	public String getLocal_name() {
		return local_name;
	}
	public void setLocal_name(String local_name) {
		this.local_name = local_name;
	}
	public String getArrive_trigger_event() {
		return arrive_trigger_event;
	}
	public void setArrive_trigger_event(String arrive_trigger_event) {
		this.arrive_trigger_event = arrive_trigger_event;
	}
	public String getArrive_trigger_state() {
		return arrive_trigger_state;
	}
	public void setArrive_trigger_state(String arrive_trigger_state) {
		this.arrive_trigger_state = arrive_trigger_state;
	}
	public String getLeave_trigger_event() {
		return leave_trigger_event;
	}
	public void setLeave_trigger_event(String leave_trigger_event) {
		this.leave_trigger_event = leave_trigger_event;
	}
	public String getLeave_trigger_state() {
		return leave_trigger_state;
	}
	public void setLeave_trigger_state(String leave_trigger_state) {
		this.leave_trigger_state = leave_trigger_state;
	}
	public String getLoad_time() {
		return load_time;
	}
	public void setLoad_time(String load_time) {
		this.load_time = load_time;
	}
	public String getUnload_time() {
		return unload_time;
	}
	public void setUnload_time(String unload_time) {
		this.unload_time = unload_time;
	}
	public String getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
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
	public String getDest_km() {
		return dest_km;
	}
	public void setDest_km(String dest_km) {
		this.dest_km = dest_km;
	}
	public String getDrive_time() {
		return drive_time;
	}
	public void setDrive_time(String drive_time) {
		this.drive_time = drive_time;
	}
	public String[] getCitys() {
		return citys;
	}
	public void setCitys(String[] citys) {
		this.citys = citys;
	}
	public String getIsOrder() {
		return isOrder;
	}
	public void setIsOrder(String isOrder) {
		this.isOrder = isOrder;
	}
	public String getFence() {
		return fence;
	}
	public void setFence(String fence) {
		this.fence = fence;
	}
	public String getStateType() {
		return stateType;
	}
	public void setStateType(String stateType) {
		this.stateType = stateType;
	}

}
