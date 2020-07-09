/**
	此文件归微科创源有限公司所有
	
	创  建  人:caining
	创建时间:2018年6月21日

	文件描述:
*/
package com.bba.jcda.vo.contractor;

import com.bba.jcda.util.Const;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ContractorZdVO extends BaseLocationVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sn; //序列号
	
	private String within_code; //内码
	
	private String code; //承运商编号
	
	private String shortname; //承运商简称
	
	private String name; //承运商名称
	
	private String linkman;//联系人
	
	private String linkmobile;//联系方式
	
	private String address;//地址
	
	private String remark;//备注说明
	
	private String create_by;//创建人
	
	private String create_date; //创建时间
	
	private String update_by; //更新人
	
	private String update_date; //更新时间
	
	private String state; //状态
	
	private String kind;


	public String getKind_code() {
		return Const.JCDA_TABLE_ZD_CONTRACTOR;
	}
}
