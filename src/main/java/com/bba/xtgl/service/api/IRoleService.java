package com.bba.xtgl.service.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bba.common.vo.PageVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.RoleVO;
import com.bba.xtgl.vo.SysUserVO;
import com.bba.common.vo.ResultVO;

public interface IRoleService {
	/**
	 * 查询按钮权限
	 * 
	 * @param roleVO
	 * @return
	 */
	public List<RoleVO> findRoleButtonList(RoleVO roleVO);

	/**
	 * 查询角色管理清单
	 * 
	 * @param roleVO
	 * @return
	 */
	public PageVO getListForGrid(JqGridParamModel jqGridParamModel,
			String customSearchFilters);

	/**
	 * 新增角色
	 * 
	 * @param roleVO
	 */
	public void insertRole(List<RoleVO> rolelist, HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * 删除角色
	 * 
	 * @param moduleVO
	 */
	public void deleteRole(List<RoleVO> list);

	public ResultVO copyRole(String roleid_src,String roleid_to,SysUserVO sysUserVO);
	
	
	//权限开始
	public List<RoleVO> GetRole(HttpServletRequest request,HttpServletResponse response);
	//权限结束
	
	
	
	
}
