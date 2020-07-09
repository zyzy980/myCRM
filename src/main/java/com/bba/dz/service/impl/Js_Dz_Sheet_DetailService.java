package com.bba.dz.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.vo.PageVO;
import com.bba.dz.dao.Js_Dz_SheetDao;
import com.bba.dz.dao.Js_Dz_Sheet_DetailDao;
import com.bba.dz.service.api.IJs_Dz_SheetService;
import com.bba.dz.service.api.IJs_Dz_Sheet_DetailService;
import com.bba.dz.vo.Js_Dz_SheetVO;
import com.bba.dz.vo.Js_Dz_Sheet_DetailVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
@Service
public class Js_Dz_Sheet_DetailService extends ServiceImpl<Js_Dz_Sheet_DetailDao, Js_Dz_Sheet_DetailVO> implements IJs_Dz_Sheet_DetailService {

    @Autowired
    private Js_Dz_Sheet_DetailDao js_dz_sheet_detailDao;

    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(
                customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Js_Dz_Sheet_DetailVO> list = js_dz_sheet_detailDao.getListForGrid(jqGridParamModel);
        int records = js_dz_sheet_detailDao.getListForGridCount(jqGridParamModel);
        // 获取用户所有角色
        return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
    }

}
