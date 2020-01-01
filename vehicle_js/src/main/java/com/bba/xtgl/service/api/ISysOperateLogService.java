package com.bba.xtgl.service.api;

import com.bba.common.vo.PageVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysOperateLogVO;

public interface ISysOperateLogService {

	/**
	 * 操作日志信息列表查询
	 */
	PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

	/**
	 * 获取操作日志详情
	 */
	/**
	 * @param sysOperateLogVO
	 * @return
	 */
	SysOperateLogVO getDetail(SysOperateLogVO vo);

	/**
	 * 存储操作日志信息
	 */
	void save(SysOperateLogVO vo);

}
