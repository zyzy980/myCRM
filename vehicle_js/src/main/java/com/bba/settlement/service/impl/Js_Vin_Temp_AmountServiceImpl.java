package com.bba.settlement.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.common.Sys_sheetidUtil;
import com.bba.common.constant.Contract_TypeEnum;
import com.bba.common.constant.SETTLEMENT_StateEnum;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.dz.dao.Js_Dz_SheetDao;
import com.bba.dz.dao.Js_Dz_Sheet_DetailDao;
import com.bba.dz.vo.Js_Dz_SheetVO;
import com.bba.dz.vo.Js_Dz_Sheet_DetailVO;
import com.bba.ht.service.api.IHt_CusService;
import com.bba.ht.service.api.IHt_Cus_FreightService;
import com.bba.ht.vo.Ht_CusVO;
import com.bba.settlement.dao.Js_Vin_Temp_AmountDao;
import com.bba.settlement.dao.Js_Vin_LogDao;
import com.bba.settlement.dao.Js_Vin_Temp_AmountDao;
import com.bba.settlement.service.api.IJs_Vin_Temp_AmountService;
import com.bba.settlement.vo.Js_Vin_Temp_AmountVO;
import com.bba.settlement.vo.Js_Vin_Temp_AmountVO;
import com.bba.util.ArrayUtils;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.util.StringUtils;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
public class Js_Vin_Temp_AmountServiceImpl extends ServiceImpl<Js_Vin_Temp_AmountDao, Js_Vin_Temp_AmountVO> implements IJs_Vin_Temp_AmountService {

    @Autowired
    private Js_Vin_Temp_AmountDao js_vin_temp_amountDao;



    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(
                customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Js_Vin_Temp_AmountVO> list = js_vin_temp_amountDao.getListForGrid(jqGridParamModel);
        int records = js_vin_temp_amountDao.getListForGridCount(jqGridParamModel);
        // 获取用户所有角色
        return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
    }


}
