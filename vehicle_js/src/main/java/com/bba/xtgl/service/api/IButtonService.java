package com.bba.xtgl.service.api;

import java.util.List;

import com.bba.common.vo.PageVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.ButtonVO;

public interface IButtonService {

	/**
	 * 查询按钮管理清单
	 * 
	 * @param jqGridParamModel
	 * @param customSearchFilters
	 * @return
	 */
	public PageVO getListForGrid(JqGridParamModel jqGridParamModel,
			String customSearchFilters);

	/**
	 * 根据属性对象集,拼接条件查询按钮数据
	 * 
	 * @return
	 */
	public List<ButtonVO> findButtonByPropertyList(List<ButtonVO> buttonVOList);

	/**
	 * 查询模块按钮
	 * 
	 * @param buttonVO
	 * @return
	 */
	public List<ButtonVO> findButtonByModuleId(ButtonVO buttonVO);

	/**
	 * 更新按钮权限
	 * 
	 * @param buttonVO
	 * @return
	 */
	public void updateButton(List<ButtonVO> buttonlist);

	/**
	 * 更新按钮
	 * 
	 * @param buttonVO
	 * @return
	 */
	public void updateButtonMsg(List<ButtonVO> buttonlist);

	/**
	 * 删除按钮
	 * 
	 * @param list
	 */
	public void deleteButton(List<ButtonVO> list);

	public List<ButtonVO> findButtonList(ButtonVO buttonVO);
}
