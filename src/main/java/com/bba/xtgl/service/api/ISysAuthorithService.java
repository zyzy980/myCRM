package com.bba.xtgl.service.api;

import java.util.List;

import com.bba.xtgl.vo.SysAuthorithVO;

public interface ISysAuthorithService {
public List<SysAuthorithVO> getAuthorityListById(String userId, int moduleId);
	
	public List<SysAuthorithVO> getAuthorityListByName(String userId, String moduleName);
	
	
	/**
	 * 根据属性类拼接条件
	 * @return 按钮集合
	 */
	public List<SysAuthorithVO> findAuthorityByPropertyList(List<SysAuthorithVO> sysAuthorithVOList);
}
