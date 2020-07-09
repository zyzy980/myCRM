package com.bba.fpgl.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.vo.PageVO;
import com.bba.fpgl.dao.IPayment_invoiceDao;
import com.bba.fpgl.dao.IPayment_invoice_DetailDao;
import com.bba.fpgl.service.api.IPayment_invoice_DetailService;
import com.bba.fpgl.vo.Payment_invoiceVO;
import com.bba.fpgl.vo.Payment_invoice_DetailVO;
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
public class Payment_invoiceService_Detailimpl extends ServiceImpl<IPayment_invoice_DetailDao, Payment_invoice_DetailVO> implements IPayment_invoice_DetailService {
    @Resource
    private IPayment_invoice_DetailDao payment_invoice_detailDao;

    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Payment_invoice_DetailVO> list = payment_invoice_detailDao.findListForGrid(jqGridParamModel);
        int record = payment_invoice_detailDao.findListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,record);
    }
}
