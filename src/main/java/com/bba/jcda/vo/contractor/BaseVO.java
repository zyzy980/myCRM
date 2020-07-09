/**
	此文件归微科创源有限公司所有
	
	创  建  人:caining
	创建时间:2018年6月21日

	文件描述:
		包含当前用户和内码的扩展实体
*/
package com.bba.jcda.vo.contractor;

import java.io.Serializable;

import com.bba.util.SessionUtils;
import com.bba.xtgl.vo.SysUserVO;

public abstract class BaseVO implements Serializable{

	private String current_within_code;
	private String current_user;
	private String yw_location;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getCurrent_within_code(){


		SysUserVO sysUserVO = SessionUtils.currentSession();
		if(sysUserVO == null){
			return null;
		}

		return sysUserVO.getWithin_code();
	}
	
	public String getCurrent_user(){
		SysUserVO sysUserVO = SessionUtils.currentSession();
		if(sysUserVO == null){
			return null;
		}
		return sysUserVO.getRealName();
	}

	public String getYw_location(){
		SysUserVO sysUserVO = SessionUtils.currentSession();
		if(sysUserVO == null){
			return null;
		}
		return sysUserVO.getWhcenter();
	}
	
}
