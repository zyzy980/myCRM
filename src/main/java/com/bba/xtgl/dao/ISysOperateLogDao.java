package com.bba.xtgl.dao;

import java.util.List;

import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysOperateLogVO;

public interface ISysOperateLogDao {

	/**
	 * 分页查询
	 */
	public List<SysOperateLogVO> getListForGrid(JqGridParamModel jqGridParamModel);

	/**
	 * 总记录数
	 */
	public int getListForGridCount(JqGridParamModel jqGridParamModel);

	/**
	 * 查询详情
	 */
	public List<SysOperateLogVO> findListByProperty(SysOperateLogVO vo);

	/**
	 * 持久化数据
	 */
	public void insert(SysOperateLogVO vo);

}
