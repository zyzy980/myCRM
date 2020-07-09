package com.bba.jcda.vo;

import com.bba.common.interceptor.EntityClass;
import com.bba.common.interceptor.EntityField;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 运输车型信息
 * @author Administrator
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@EntityClass(value="运输车型信息")
public class ZdTruckTypeVO {
	@EntityField(value="内码")
	private String within_code;//内码
	@EntityField(value="车型编号")
	private String code;//车型编号
	@EntityField(value="车型名称")
	private String name;//车型名称
	@EntityField(value="准载重量(kg)")
	private Float gwt;//准载重量
	@EntityField(value="准载体积(t)")
	private Float vol;//准载体积
	@EntityField(value="长(MM)")
	private Float l;//长
	@EntityField(value="宽(MM)")
	private Float w;//宽
	@EntityField(value="高(MM)")
	private Float h;//高
	@EntityField(value="备注说明")
	private String remark;//备注说明
	@EntityField(value="创建")
	private String create_by;//创建
	@EntityField(value="创建时间")
	private String create_date;//创建时间
	@EntityField(value="更新")
	private String update_by;//更新
	@EntityField(value="更新时间")
	private String update_date;//更新时间
	@EntityField(value="SN")
	private String sn;//SN
	@EntityField(value="门宽(MM)")
	private Float door_h;
	@EntityField(value="门高(MM)")
	private Float door_w;
	@EntityField(value="装货方式")
	private String load_type;
	@EntityField(value="卸货方式")
	private String unload_type;
	@EntityField(value="车厢预留间隙高(MM)")
	private Float tbuffer_h;
	@EntityField(value="车厢预留间隙长(MM)")
	private Float tbuffer_l;
	@EntityField(value="车厢预留间隙宽(MM)")
	private Float tbuffer_w;
	@EntityField(value="容器预留间隙高(MM)")
	private Float cbuffer_h;
	@EntityField(value="容器预留间隙长(MM)")
	private Float cbuffer_l;
	@EntityField(value="容器预留间隙宽(MM)")
	private Float cbuffer_w;
	@EntityField(value="状态")
	private String state;
	private String location_code;
	

	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || this.getClass() != o.getClass()) return false;
		ZdTruckTypeVO z = (ZdTruckTypeVO) o;
		return this.getWithin_code().equals(z.getWithin_code()) && this.getCode().equals(z.getCode());
	}
	public int hashCode() {
		return this.getWithin_code().hashCode() + this.getCode().hashCode();
	}
	
	@Override
	public String toString() {
		return "ZdTruckTypeVO [within_code=" + within_code + ", code=" + code + ", name=" + name + ", gwt=" + gwt
				+ ", vol=" + vol + ", l=" + l + ", w=" + w + ", h=" + h + ", remark=" + remark + ", create_by="
				+ create_by + ", create_date=" + create_date + ", update_by=" + update_by + ", update_date="
				+ update_date + ", sn=" + sn + ", door_h=" + door_h + ", door_w=" + door_w + ", load_type=" + load_type
				+ ", unload_type=" + unload_type + ", tbuffer_h=" + tbuffer_h + ", tbuffer_l=" + tbuffer_l
				+ ", tbuffer_w=" + tbuffer_w + ", cbuffer_h=" + cbuffer_h + ", cbuffer_l=" + cbuffer_l + ", cbuffer_w="
				+ cbuffer_w + ", state=" + state + "]";
	}
	
	
}
