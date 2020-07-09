package com.bba.xtgl.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bba.common.vo.PageVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.xtgl.dao.ISysTaskFunctionDao;
import com.bba.xtgl.dao.ISysTaskRoleDao;
import com.bba.xtgl.service.api.ISysTaskFunctionService;
import com.bba.xtgl.vo.SysTaskFunctionVO;
import com.bba.xtgl.vo.SysTaskRoleVO;

@Service
@Transactional
public class SysTaskFuctionService implements ISysTaskFunctionService {

	@Resource
	private ISysTaskFunctionDao sysTaskFunctionDao;
	
	@Resource
	private ISysTaskRoleDao sysTaskRoleDao;

	@Override
	public PageVO findListForRoleId(JqGridParamModel jqGridParamModel,String role_id) {
		// 获取所有模块
		String filters = JqGridSearchParamHandler.processSql(null, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		List<SysTaskFunctionVO> sysTaskFunctionList = sysTaskFunctionDao.getListForGrid(jqGridParamModel);
		int records = sysTaskFunctionDao.getListForGridCount(jqGridParamModel);
		
		// 获取角色拥有模块
		SysTaskRoleVO paramVO = new SysTaskRoleVO();
		paramVO.setRole_id(role_id);
		List<SysTaskRoleVO> sysTaskRoleList = sysTaskRoleDao.findListByProperty(paramVO);
		
		for (SysTaskFunctionVO functionVO : sysTaskFunctionList) {
			
			for (SysTaskRoleVO roleVO : sysTaskRoleList) {
				if(functionVO.getFunction_id().equals(roleVO.getFunction_id())){
					functionVO.setIsExist(true);
					break;
				}
			}
		}
		
		return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), sysTaskFunctionList, records);
	}

	@Override
	public void updateTaskRole(List<SysTaskRoleVO> taskRoleList) {
		List<SysTaskRoleVO> insertList=new ArrayList<SysTaskRoleVO>();
		List<SysTaskRoleVO> deleteList=new ArrayList<SysTaskRoleVO>();
		for(int i=0;i< taskRoleList.size();i++){
			SysTaskRoleVO taskRoleVO =taskRoleList.get(i);
			if(taskRoleVO.getIsExist()){
				insertList.add(taskRoleVO);
			}
		}
		// 先删除当前页所有权限
		if(taskRoleList.size()>0){
			sysTaskRoleDao.deleteTaskRole(taskRoleList);
		}
		// 在插入新的权限
		if(insertList.size()>0){
			sysTaskRoleDao.insertTaskRole(insertList);
		}
		
	}

	@Override
	public List<Map<String,Object>> findHomeTaskRoleList(String userId) {
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("p_userId", userId);
		
		sysTaskRoleDao.findHomeTaskRoleList(param);
		List<Map<String,Object>> taskRoleList = (List<Map<String,Object>>)param.get("p_task_cursor");
		return taskRoleList;
	}
	
	


}
