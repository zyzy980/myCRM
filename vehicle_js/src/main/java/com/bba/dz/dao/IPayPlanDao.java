package com.bba.dz.dao;



import com.bba.dz.vo.Tr_Payment_PlanVO;
import com.bba.util.JqGridParamModel;

import java.util.List;


public interface IPayPlanDao  {

    List<Tr_Payment_PlanVO> getListForGrid(JqGridParamModel jqGridParamModel);

    int getListForGridCount(JqGridParamModel jqGridParamModel);


    public List<Tr_Payment_PlanVO> findSumDataList_normal(List<Tr_Payment_PlanVO> list);
    public List<Tr_Payment_PlanVO> findSumDataList_expect(List<Tr_Payment_PlanVO> list);
}
