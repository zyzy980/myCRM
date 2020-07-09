/**
	此文件归微科创源有限公司所有
	
	创  建  人:caining
	创建时间:2018年6月21日

	文件描述:
		包含业务地点的扩展实体
*/
package com.bba.jcda.vo.contractor;

import com.bba.util.SessionUtils;
import com.bba.util.StringUtils;

public abstract class BaseLocationVO extends BaseVO implements YwKind{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getLocation_code(){
		
		String locationCode = ""; 
		
		if(StringUtils.equals("Y",SessionUtils.currentSession().getIsContractor())){
			locationCode = SessionUtils.currentSession().getWhcenter();
		} else {
			locationCode = SessionUtils.currentSession().getWhcenter();
		}
		return locationCode;
	}
}
