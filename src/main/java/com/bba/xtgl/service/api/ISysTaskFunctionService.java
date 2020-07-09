package com.bba.xtgl.service.api;

import java.util.List;
import java.util.Map;

import com.bba.common.vo.PageVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysTaskRoleVO;

public interface ISysTaskFunctionService {

	
	
	/**
	 * 查询角色任务清单
	 * @param role_id
	 * @return 
	 */
	public PageVO findListForRoleId(JqGridParamModel jqGridParamModel,String role_id);

	
	/**
	 * 更新角色任务清单
	 * @param taskRoleList 
	 * @return 
	 */
	public void updateTaskRole(List<SysTaskRoleVO> taskRoleList);
	
	/**
	 * 查询用户主页任务信息
	 * @param userId
	 * @return
	 */
	public List<Map<String,Object>> findHomeTaskRoleList(String userId);

}
