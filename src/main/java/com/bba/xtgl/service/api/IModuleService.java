package com.bba.xtgl.service.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.ModuleVO;
import com.bba.xtgl.vo.SysUserVO;

public interface IModuleService {

	
	public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

	
	/**
	 * 查询左侧菜单栏
	 * @param moduleVO
	 * @return
	 */
	public List<ModuleVO> findModuleList(ModuleVO moduleVO,HttpServletRequest request,HttpServletResponse response);

	
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
	public ModuleVO findModuleVO(ModuleVO moduleVO);
	
	/**
	 * 更新模块
	 * @param moduleVO
	 */
	public void updateModule(ModuleVO moduleVO);
	
	
	/**
	 * 删除模块
	 * @param moduleVO
	 */
	public void deleteModule(List<ModuleVO> list);
	
	
	/**
	 * 排序模块
	 * @param moduleVO
	 */
	public void orderModule(List<ModuleVO> list);
	

	/**
	 * 查询模块list
	 * @param moduleVO
	 * @return
	 */
	public List<ModuleVO> findModuleAll(ModuleVO moduleVO);
	
	/**
	 * 查询左侧菜单栏
	 * @param moduleVO
	 * @return
	 */
	public List<ModuleVO> findModuleLeftList(ModuleVO moduleVO);


	/**
	 * 查询用户左菜单所有模块***
	 * @param userId
	 */
	public ResultVO findModuleByUserId(SysUserVO sysUserVO);
	
	
	
	/**
	 * 查询用户左菜单所有模块***
	 * @param userId
	 */
	public Integer findModuleByUserId_Count(SysUserVO sysUserVO,String moduleurl);

}
