/**
	此文件归微科创源有限公司所有
	
	创  建  人:caining
	创建时间:2018年5月31日

	文件描述:
		基本通用档案实体
*/
package com.bba.jcda.vo;

import java.io.Serializable;

import com.bba.common.interceptor.EntityField;
import com.bba.jcda.util.Const;
import com.bba.jcda.vo.contractor.BaseLocationVO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BasicVO extends BaseLocationVO implements Serializable{
	
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private String sn;// 序列号
	
	private String within_code; //内码

	@EntityField(value = "类型")
	private String type;// 类型

	@EntityField(value = "类型名称")
	private String type_name; // 类型名称

	@EntityField(value = "值")
	private String code;// 编号

	@EntityField(value = "描述")
	private String values1;// 值1

	private String values2;// 值2

	private String values3;// 值3

	@EntityField(value = "备注")
	private String remark;// 备注

	@EntityField(value = "创建人")
	private String create_by;// 创建人

	@EntityField(value = "创建时间")
	private String create_date;// 创建时间

	@EntityField(value = "更新人")
	private String update_by;// 更新人
	
	@EntityField(value = "更新时间")
	private String update_date;// 更新时间
	
	private String isLocationCode; // 0|1

	public String getKind_code() {
		return Const.JCDA_TABLE_SCTS_BASIC;
	}
}
