package com.bba.xtgl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.ModuleVO;

public interface IModuleDao {

	
	
	public List<ModuleVO> getListForGrid(JqGridParamModel jqGridParamModel);
	public int getListForGridCount(JqGridParamModel jqGridParamModel);
	
	
	/**
	 * 查询模块父菜单list
	 * @param moduleVO
	 * @return
	 */
	public List<ModuleVO> findModuleList(ModuleVO moduleVO);
	
	/**
	 * 查询模块子菜单list
	 * @param moduleVO
	 * @return
	 */
	public List<ModuleVO> findNextModuleList(ModuleVO moduleVO);
	
	/**
	 * 查询模块有权限菜单list
	 * @param moduleVO
	 * @return
	 */
	public List<ModuleVO> findRoleModuleList(ModuleVO moduleVO);
	
	
	/**
	 * 新增模块
	 * @param moduleVO
	 */
	public void insertModule(ModuleVO moduleVO);
	
	/**
	 * 查询模块总数
	 * @param moduleVO
	 * @return
	 */
	public List<ModuleVO> findModuleVO(ModuleVO moduleVO);
	
	/**
	 * 更新模块
	 * @param moduleVO
	 */
	public void updateModule(ModuleVO moduleVO);
	
	/**
	 * 批量更新
	 * @param moduleVO
	 */
	public void batchUpdate(List<ModuleVO> list);
	
	/**
	 * 删除模块
	 * @param moduleVO
	 */
	public void deleteModule(List<ModuleVO> list);
	void deleteSysLanguage(@Param("list")List<ModuleVO> list, @Param("withinCode")String withinCode);
	void deleteSysRoleButtons(@Param("list")List<ModuleVO> list, @Param("withinCode")String withinCode);
	void deleteSysAuthorith(@Param("list")List<ModuleVO> list, @Param("withinCode")String withinCode);
	
	/**
	 * 查询模块list
	 * @param moduleVO
	 * @return
	 */
	public List<ModuleVO> findModuleAll(ModuleVO moduleVO);
	
	/**
	 * 查询用户左菜单所有模块
	 * @param userId
	 */
	public List<ModuleVO> findModuleByUserId(@Param(value="within_code")String within_code,@Param(value="userId")String userId);
	
	/**
	 * 统计是否有访问权限URL界面
	 * @param userId
	 */
	public Integer findModuleByUserId_Count(@Param(value="within_code")String within_code,@Param(value="userId")String userId,@Param(value="moduleurl")String moduleurl);
	
	
	/**
	 * 管理中 - 查询用户左菜单所有模块
	 * @param userId
	 */
	public List<ModuleVO> findModuleByAdminId(@Param(value="within_code")String within_code,@Param(value="userId")String userId);


	public Long GetModuleSeq();

}
