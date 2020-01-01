package com.bba.xtgl.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bba.common.interceptor.Sys_BaseOperator_LogVO;
import com.bba.common.vo.PageVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.xtgl.dao.ISys_BaseOperator_LogDao;
import com.bba.xtgl.service.api.ISys_BaseOperator_LogService;

@Service
@Transactional
public class Sys_BaseOperator_LogService implements ISys_BaseOperator_LogService {

	@Autowired
	private ISys_BaseOperator_LogDao sys_BaseOperator_LogDao;

	public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
		String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		List<Sys_BaseOperator_LogVO> list = sys_BaseOperator_LogDao.getListForGrid(jqGridParamModel);
		int records = sys_BaseOperator_LogDao.getListForGridCount(jqGridParamModel);
		return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
	}

	public Sys_BaseOperator_LogVO getDetail(Sys_BaseOperator_LogVO vo) {
		List<Sys_BaseOperator_LogVO> vos = sys_BaseOperator_LogDao.getDetail(vo);
		if (vos != null && vos.size() > 1) {
			throw new RuntimeException("查询单条数据失败，匹配结果数量为多条");
		}
		return vos.size() == 0 ? null : vos.get(0);
	}

}