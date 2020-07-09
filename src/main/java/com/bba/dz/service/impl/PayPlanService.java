package com.bba.dz.service.impl;

import com.bba.common.service.impl.BaseService;
import com.bba.common.vo.PageVO;
import com.bba.dz.dao.IPayPlanDao;
import com.bba.dz.service.api.IPayPlanService;
import com.bba.dz.vo.Js_Dz_SheetVO;
import com.bba.dz.vo.Tr_Payment_PlanVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class PayPlanService extends BaseService implements IPayPlanService {

    @Autowired
    private IPayPlanDao iPayPlanDao;


    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Tr_Payment_PlanVO> list = iPayPlanDao.getListForGrid(jqGridParamModel);
        int records = iPayPlanDao.getListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
    }

    @Override
    public List<Tr_Payment_PlanVO> findSumDataList_normal(List<Tr_Payment_PlanVO> list)
    {
        return iPayPlanDao.findSumDataList_normal(list);
    }

    @Override
    public List<Tr_Payment_PlanVO> findSumDataList_expect(List<Tr_Payment_PlanVO> list)
    {
        return iPayPlanDao.findSumDataList_expect(list);
    }

}
