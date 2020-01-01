package com.bba.jcda.vo;



import java.util.List;

import com.bba.common.interceptor.EntityClass;
import com.bba.common.interceptor.EntityField;
import com.bba.jcda.vo.contractor.BaseVO;
import lombok.Data;

/**
 * 司机档案
 * @author Administrator
 *
 */
@Data
@EntityClass(value="司机档案")
public class ZdDriverVO extends BaseVO {
	@EntityField(value="SN")
	private String sn;//SN
	@EntityField(value="司机编号")
	private String code;//司机编号
	@EntityField(value="司机姓名")
	private String name;//司机名称
	@EntityField(value="年龄")
	private String age;//年龄
	@EntityField(value="驾驶证领取日期")
	private String start_date;//驾驶证领取日期
	@EntityField(value="驾驶证起始期限")
	private String usingstart_date;//驾驶证有效开始日期
	@EntityField(value="驾驶证有效期限")
	private String end_date;//驾驶证失效日期
	@EntityField(value="驾驶证类别")
	private String kind;//驾驶证类别
	@EntityField(value="出生年月")
	private String brithday;//出生年月
	@EntityField(value="驾龄")
	private String transage;//驾龄
	@EntityField(value="从业资格证有效期")
	private String transportdate;//从业资格证有效期
	@EntityField(value="从业资格类别")
	private String transporttype;//从业资格类别
	@EntityField(value="驾驶证从业资格证")
	private String transportcode;//驾驶员从业资格证
	@EntityField(value="联系电话")
	private String telphone;//联系方式
	@EntityField(value="籍贯")
	private String origo;//籍贯
	@EntityField(value="住址")
	private String address;//住址
	@EntityField(value="身份证号")
	private String personcard;//身份证号
	@EntityField(value="驾驶证号")
	private String credentials;//驾驶证件号
	@EntityField(value="车型")
	private String trucktype;//准驾车型
    @EntityField(value="驾驶年龄")
	private String drivingage;//驾驶年龄
    @EntityField(value="备注说明")
	private String remark;//备注说明
    @EntityField(value="审核人")
	private String check_by;//审核人
    @EntityField(value="审核日期")
	private String check_date;//审核日期
    @EntityField(value="审核状态")
	private String state;//审核状态
    @EntityField(value="过期状态")
	private String isgq;//延期过期状态
    @EntityField(value="创建")
	private String create_by;//创建
    @EntityField(value="创建时间")
	private String create_date;//创建时间
    @EntityField(value="更新")
	private String update_by;//更新
    @EntityField(value="更新时间")
	private String update_date;//更新时间
    @EntityField(value="司机入场证有效期起")
    private String admissiondt_begin;
    @EntityField(value="司机入场证有效期止")
    private String admissiondt_end;
    private String guid;
    private List<Zd_Image> list;//非数据库字段
	private String strImageJson;
	private List<ZdDriverRelateVO> driverRelateVO;
	
	private String type;
	private String startTime;
	
	private String within_code;
	@EntityField(value="承运商编号")
	private String contractor;
	
	private String contractor_name;
	
	private String sum;
	private String sum1;
	private String sum2;
	private String sum3;
	private String location_code;
	private String dicvalue;

	private String conditionSql;

	private String relateSn;
	


	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || this.getClass() != o.getClass()) return false;
		ZdDriverVO z = (ZdDriverVO) o;
		return this.getWithin_code().equals(z.getWithin_code()) && this.getCode().equals(z.getCode());
	}
	public int hashCode() {
		return this.getWithin_code().hashCode() + this.getCode().hashCode();
	}
}
