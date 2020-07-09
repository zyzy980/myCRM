/**
	此文件归微科创源有限公司所有
	
	创  建  人:caining
	创建时间:2018年6月13日

	文件描述:
		EPLocation业务实体类
*/
package com.bba.jcda.vo;

import com.bba.common.interceptor.EntityField;
import com.bba.jcda.vo.contractor.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class EPLocationVO extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sn; // 序列
	
	private String within_code; // 内码
	
	@EntityField(value="编号")
	private String ep_code; // 编号
	
	@EntityField(value="工厂编号")
	private String ep_plant; // 工厂
	
	@EntityField(value="类型")
	private String ep_type; // 类型
	
	@EntityField(value="GR Location")
	private String gr_location; // GR_LOCATION
	
	@EntityField(value="创建人")
	private String create_by; // 创建人
	
	@EntityField(value="创建日期")
	private String create_date; // 创建日期
	
	@EntityField(value="更新人")
	private String update_by; // 更新人
	
	@EntityField(value="更新日期")
	private String update_date; // 更新日期

}
