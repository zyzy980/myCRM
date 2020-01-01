package com.bba.jcda.vo;

import java.util.List;

import com.bba.common.interceptor.EntityClass;
import com.bba.common.interceptor.EntityField;
import com.bba.jcda.vo.contractor.BaseVO;
import lombok.Data;

@EntityClass(value="车辆档案")
@Data
public class Zd_TruckVO extends BaseVO {
	@EntityField(value="SN")
	private String sn;
	@EntityField(value="内码")
	private String within_code;
    @EntityField(value="车牌号码")
	private String truckno;
    @EntityField(value="承运商编号")
	private String contractor;
    @EntityField(value="承运商姓名")
	private String applicontr_name;
    @EntityField(value="默认驾驶员")
	private String truck_owner;
    @EntityField(value="司机")
	private String truck_owner_name;
    @EntityField(value="默认驾驶员手机号码")
	private String truck_owner_tel;
    @EntityField(value="车辆分类")
	private String truck_type;
	private String trucktype;
    @EntityField(value="车型编号")
	private String trucktypename;
    @EntityField(value="车辆识别代码")
	private String vehicle_code;
    @EntityField(value="发动机号码")
	private String engine_number;
    @EntityField(value="车长")
	private String conductor;
    @EntityField(value="车厢门数")
	private String carriagenumber;
    @EntityField(value="品牌型号")
	private String brand_models;
    @EntityField(value="行驶证注册时间")
	private String registration_time;
    @EntityField(value="我司强制报废日期")
	private String gsexpired_date;
    @EntityField(value="国家强制报废日期")
	private String gjexpired_date;
    @EntityField(value="检验有效期")
	private String validity;
    @EntityField(value="整备质量/KG")
	private String curb_weight;
    @EntityField(value="核定质量/KG")
	private String check_weight;
    @EntityField(value="道路运输证号")
	private String dlxkzcode;
    @EntityField(value="道路运输证期限")
	private String dlxkzcodetime;
    @EntityField(value="交强险")
	private String insurance;
    @EntityField(value="交强险有效期")
	private String insurancedate;
    @EntityField(value="三责险")
	private String threerisks;
    @EntityField(value="三责险有效期")
	private String threerisksdate;
    @EntityField(value="被保险人")
	private String insured;
    @EntityField(value="车辆排放标准")
	private String vehicle_emissions;
    @EntityField(value="是否固定车辆")
	private String isgd;
    @EntityField(value="车辆业务范围")
	private String truckservice;
    @EntityField(value="使用状态")
	private String using_state;
    @EntityField(value="常用路线")
	private String using_route;
    @EntityField(value="车厢内径")
	private String car_inside;
    @EntityField(value="车辆类型")
	private String typename;
    @EntityField(value="所有人(承运商)")
	private String truckcontractor;
    @EntityField(value="住址")
	private String address;
    @EntityField(value="核定人数")
	private String check_num;
    @EntityField(value="外廓尺寸")
	private String car_outside;
    @EntityField(value="审核人")
	private String check_by;
    @EntityField(value="审核日期")
	private String check_date;
    @EntityField(value="审核状态")
	private String state;
    @EntityField(value="延期过期状态")
	private String isgq;
    @EntityField(value="备注")
	private String remark;
    @EntityField(value="创建人")
	private String create_by;
    @EntityField(value="创建日期")
	private String create_date;
    @EntityField(value="更新人")
	private String update_by;
    @EntityField(value="更新日期")
	private String update_date;
    @EntityField(value="车辆入场证有效期起")
    private String admissiondt_begin;
    @EntityField(value="车辆入场证有效期止")
    private String admissiondt_end;
    private String is_fixed;

    private String gpsId;

	private String vol;
	
	private List<Zd_Image> list;//非数据库字段
	private String code;//车型编号 & 司机编号
	private String name;//车型名称 & 司机名称
	private String strImageJson;
	private String type;
	@EntityField(value="司机编号")
	private String driver_id;//驾驶员编号
	@EntityField(value="司机名称")
	private String driver_name;
    private String telphone;
    private String location_code;
    private String sum;//全部
    private String sum1;//正常
    private String sum2;//已经过期
    private String sum3;//即将过期
	private String relateSn; // zd_truck_relate sn编号


	@EntityField(value="车辆监控状态")
	private String truck_onway;

	private String truck_transport_type;

	
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || this.getClass() != o.getClass()) return false;
		Zd_TruckVO z = (Zd_TruckVO) o;
		return this.getWithin_code().equals(z.getWithin_code()) && this.getTruckno().equals(z.getTruckno());
	}
	public int hashCode() {
		return this.getWithin_code().hashCode() + this.getTruckno().hashCode();
	}
	
}
