package com.bba.xtgl.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.ButtonVO;

public interface IButtonDao {
	
	
	public List<ButtonVO> getListForGrid(JqGridParamModel jqGridParamModel);
	public int getListForGridCount(JqGridParamModel jqGridParamModel);
	
	
	/**
	 * 查询所有按钮
	 * @param buttonVO
	 * @return
	 */
	public List<ButtonVO> findListByPropertyList(List<ButtonVO> list);
	
	/**
	 * 查询当前角色按钮权限
	 * @param buttonVO
	 * @return
	 */
	public List<ButtonVO> findButtonByRole(ButtonVO buttonVO);
	
	

	/**
	 * 新增角色按钮权限
	 * @param roleVO
	 */
	public void insertAuthorith(@Param(value="list")List<ButtonVO> list);
	
	/**
	 * 删除角色按钮权限
	 * @param roleVO
	 */
	public void deleteAuthorith(List<ButtonVO>  list);
	
	/**
	 * 级联修改角色按钮
	 */
	public void updateAuthorith(List<Map<String, Object>> list);
	
	/**
	 * 新增按钮
	 * @param list
	 */
	public void insertButton(@Param("list")List<ButtonVO>  list);
	
	/**
	 * 删除按钮
	 * @param list
	 */
	public void deleteButton(List<ButtonVO>  list);
	void deleteSysRoleButtons(@Param("list")List<ButtonVO> list, @Param("withinCode")String withinCode);
	void deleteSysAuthorith(@Param("withinCode")String withinCode, @Param("buttonId")String buttonId, @Param("moduleid")String moduleid);

	
	/**
	 * 更新按钮
	 * @param list
	 */
	public void updateButton(List<ButtonVO>  list);


	public void insert(@Param("item") ButtonVO vo);
}
