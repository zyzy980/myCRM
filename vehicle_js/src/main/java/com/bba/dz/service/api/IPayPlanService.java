package com.bba.dz.service.api;


import com.bba.common.service.api.IBaseService;
import com.bba.common.vo.PageVO;
import com.bba.dz.vo.Tr_Payment_PlanVO;
import com.bba.util.JqGridParamModel;

import java.util.List;


public interface IPayPlanService extends IBaseService {

    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    List<Tr_Payment_PlanVO> findSumDataList_normal(List<Tr_Payment_PlanVO> list);

    List<Tr_Payment_PlanVO> findSumDataList_expect(List<Tr_Payment_PlanVO> list);
}
