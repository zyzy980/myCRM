package com.bba.jcda.service.api;

import com.bba.common.service.api.IBaseService;
import com.bba.common.vo.PageVO;
import com.bba.util.JqGridParamModel;

public interface ITr_statistical_rulesService extends IBaseService {

	public PageVO getListForGrid(JqGridParamModel jqGridParamModel,String customSearchFilters);

}
