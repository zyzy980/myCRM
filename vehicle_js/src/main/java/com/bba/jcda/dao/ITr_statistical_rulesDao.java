package com.bba.jcda.dao;

import com.bba.jcda.vo.Tr_statistical_rulesVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

public interface ITr_statistical_rulesDao {

	public List<Tr_statistical_rulesVO> getListForGrid(JqGridParamModel jqGridParamModel);

	public int getListForGridCount(JqGridParamModel jqGridParamModel);


}
