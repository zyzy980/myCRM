package com.bba.fpgl.dao;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.fpgl.vo.Payment_invoiceVO;
import com.bba.fpgl.vo.Payment_invoice_DetailVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

public interface IPayment_invoice_DetailDao extends BaseMapper<Payment_invoice_DetailVO> {

    /**
     * 发票明细管理 清单查询
     * @param jqGridParamModel
     * @return
     */
    List<Payment_invoice_DetailVO> findListForGrid(JqGridParamModel jqGridParamModel);

    int findListForGridCount(JqGridParamModel jqGridParamModel);
}

