package com.bba.xtgl.dao;

import java.util.List;
import java.util.Map;

import com.bba.xtgl.vo.SysAuthorithVO;

public interface ISysAuthorithDao {


	public int insertSys_authorith(List<SysAuthorithVO> list);

	public List<SysAuthorithVO> findListSys_authorith(SysAuthorithVO vo);


	public List<SysAuthorithVO> getAuthorityListById(Map<String,Object> paramMap);
	
	public List<SysAuthorithVO> getAuthorityListByName(Map<String,Object> paramMap);
	
	/**
	 * 根据属性类拼接条件
	 * @return 按钮集合
	 */
	public List<SysAuthorithVO> findAuthorityByPropertyList(List<SysAuthorithVO> list);

}
