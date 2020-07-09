package com.bba.xtgl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.RoleVO;

public interface IRoleDao {
	/**
	 * 查询角色管理清单
	 * 
	 * @param roleVO
	 * @return
	 */
	public List<RoleVO> getListForGrid(JqGridParamModel jqGridParamModel);

	/**
	 * 查询角色管理清单数量
	 * 
	 * @param roleVO
	 * @return
	 */
	public int getListForGridCount(JqGridParamModel jqGridParamModel);

	/**
	 * 查询按钮权限
	 * 
	 * @param roleVO
	 * @return
	 */
	public List<RoleVO> findRoleButtonList(RoleVO roleVO);

	/**
	 * 新增角色
	 * 
	 * @param roleVO
	 */
	public void insertRole(List<RoleVO> list);

	/**
	 * 更新角色
	 * 
	 * @param roleVO
	 */
	public void updateRole(List<RoleVO> list);

	/**
	 * 删除角色
	 * 
	 * @param moduleVO
	 */
	public void deleteRole(@Param("list")List<RoleVO> list, @Param("withinCode")String withinCode);
	void deleteAuthorith(@Param("list")List<RoleVO> list, @Param("withinCode")String withinCode);
	void deleteSysRole(@Param("list")List<RoleVO> list, @Param("withinCode")String withinCode);
	
	
	public List<RoleVO> GetAdminRole(@Param(value="within_code")String within_code,@Param(value="url")String url);
	public List<RoleVO> GetUserRole(@Param(value="within_code")String within_code,@Param(value="url")String url,@Param(value="UserId")String UserId);
	
	public void save(@Param(value="sql")String sql);

	public List<RoleVO> GetRoleButtonAllList(RoleVO vo);
	public Long GetRoleButtonSeq();
	public void insert(@Param(value="item") RoleVO vo);
}
