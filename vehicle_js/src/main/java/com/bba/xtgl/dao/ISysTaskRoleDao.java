package com.bba.xtgl.dao;

import java.util.List;
import java.util.Map;

import com.bba.xtgl.vo.SysTaskRoleVO;

public interface ISysTaskRoleDao {

	public List<SysTaskRoleVO> findListByProperty(SysTaskRoleVO sysTaskRoleVO);
	
	
	public void deleteTaskRole(List<SysTaskRoleVO> taskRoleList);
	
	public void insertTaskRole(List<SysTaskRoleVO> taskRoleList);
	
	
	public void findHomeTaskRoleList(Map<String,Object> param);
	
	

}
