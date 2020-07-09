package com.bba.tzgl.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.vo.PageVO;
import com.bba.tzgl.dao.Js_CompensationDao;
import com.bba.tzgl.service.api.IJs_CompensationService;
import com.bba.tzgl.vo.Js_CompensationVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:26
 */
@Service
@Transactional
public class Js_CompensationServiceimpl extends ServiceImpl<Js_CompensationDao, Js_CompensationVO> implements IJs_CompensationService {

    @Resource
    private Js_CompensationDao js_compensationDao;

    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Js_CompensationVO> list = js_compensationDao.findListForGrid(jqGridParamModel);
        int count = js_compensationDao.findListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }
}
