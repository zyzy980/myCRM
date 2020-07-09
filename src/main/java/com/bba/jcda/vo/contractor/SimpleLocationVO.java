/**
	此文件归微科创源有限公司所有
	
	创  建  人:caining
	创建时间:2018年7月4日

	文件描述:
*/
package com.bba.jcda.vo.contractor;

import com.bba.jcda.util.Const;
import com.bba.util.SessionUtils;
import com.bba.util.StringUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SimpleLocationVO extends BaseLocationVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code; //地点编号
	
	private String name; //地点名称
	
	public String getKind_code() {
		return Const.JCDA_TABLE_ZD_LOCATION;
	}
	
	//承运商编号
	public String getContractor_code(){
		
		String contractorCode = "";
		
		if(StringUtils.equals("Y",SessionUtils.currentSession().getIsContractor())){
			contractorCode = SessionUtils.currentSession().getWhcenter();
		} 
		return contractorCode;
	}

}
