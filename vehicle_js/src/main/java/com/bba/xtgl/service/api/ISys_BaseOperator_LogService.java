package com.bba.xtgl.service.api;

import com.bba.common.interceptor.Sys_BaseOperator_LogVO;
import com.bba.common.vo.PageVO;
import com.bba.util.JqGridParamModel;

public interface ISys_BaseOperator_LogService {

	public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

	/**
	 * 获取操作日志详情
	 */
	/**
	 * @param Sys_BaseOperator_LogVO
	 * @return
	 */
	Sys_BaseOperator_LogVO getDetail(Sys_BaseOperator_LogVO vo);

}
