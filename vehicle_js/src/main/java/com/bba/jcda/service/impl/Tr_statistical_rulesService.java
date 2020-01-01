package com.bba.jcda.service.impl;

import com.bba.common.service.impl.BaseService;

import com.bba.common.vo.PageVO;
import com.bba.jcda.dao.*;
import com.bba.jcda.service.api.ITr_statistical_rulesService;

import com.bba.jcda.vo.Tr_statistical_rulesVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class Tr_statistical_rulesService extends BaseService implements ITr_statistical_rulesService {

	@Autowired
	private ITr_statistical_rulesDao iTr_statistical_rulesDao;

	/**
	 * 列表数据查询
	 * */
	public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters)
	{
		String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		List<Tr_statistical_rulesVO> list = iTr_statistical_rulesDao.getListForGrid(jqGridParamModel);
		int records = iTr_statistical_rulesDao.getListForGridCount(jqGridParamModel);
		return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
	}

}
