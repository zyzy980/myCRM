package com.bba.jcda.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.vo.PageVO;
import com.bba.jcda.dao.IJs_Bill_NumberDao;
import com.bba.jcda.service.api.IJs_Bill_NumberService;
import com.bba.jcda.vo.Js_Bill_NumberVO;
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
public class Bill_NumberServiceimpl extends ServiceImpl<IJs_Bill_NumberDao, Js_Bill_NumberVO> implements IJs_Bill_NumberService {

    @Resource
    private IJs_Bill_NumberDao js_bill_numberdao;


    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Js_Bill_NumberVO> list = js_bill_numberdao.findListForGrid(jqGridParamModel);
        int count = js_bill_numberdao.findListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }
}
