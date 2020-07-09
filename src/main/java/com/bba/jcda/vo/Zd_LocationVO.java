package com.bba.jcda.vo;

import com.bba.common.interceptor.EntityClass;
import com.bba.common.interceptor.EntityField;
import com.bba.jcda.vo.contractor.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper = false)
@EntityClass(value = "地点档案")
public class Zd_LocationVO extends BaseVO{

//	private static final long serialVersionUID = 1L;
	private String sn;// SN
	private String guid;
	private String within_code;
	private String code;
	@EntityField(value = "地点编号")
	private String ex_code;
	@EntityField(value = "地点名称")
	private String name;
	private String shortname;
	@EntityField(value = "省份")
	private String province;
	@EntityField(value = "城市")
	private String city;
	@EntityField(value = "详细地址")
	private String address;
	@EntityField(value = "联系人")
	private String linkman;
	@EntityField(value = "联系人电话")
	private String linktel;
	@EntityField(value = "地点分类")
	private String kind;
	private String yw_location;
	private String remark;
	@EntityField(value = "经度")
	private String lo;
	@EntityField(value = "纬度")
	private String la;
	private String fence;
	private String create_by;
	private String create_date;
	private String update_by;
	private String update_date;
	private String state;
	private String contractor_code;
	private String address_code;
	private String cusName;
	private String location_code;
	private String sfKind;
	private String address_name;
	private String real_name;
	private String detailAddress;
	@EntityField(value = "客户编号")
	private String cus_id;
	private String area;
	@EntityField(value = "区")
	private String district;
	private String cityZjm;//速记码
	private String addressCode;
	@EntityField(value = "收发货地")
	private String sf_kind;//收发货地分类
	private String begin_arrive_trigger_event;//作为起运地到达时触发事件
	private String begin_arrive_trigger_state;//作为起运地到达时触发事件的状态编号
	private String end_arrive_trigger_event;//作为目的地到达时触发事件
	private String end_arrive_trigger_state;//作为目的地到达时触发事件的状态编号

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || this.getClass() != o.getClass())
			return false;
		Zd_LocationVO z = (Zd_LocationVO) o;
		return this.getWithin_code().equals(z.getWithin_code()) && this.getEx_code().equals(z.getEx_code());
	}

	public int hashCode() {
		return this.getWithin_code().hashCode() + this.getEx_code().hashCode();
	}
}
