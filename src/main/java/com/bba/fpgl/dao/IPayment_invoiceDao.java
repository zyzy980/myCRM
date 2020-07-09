package com.bba.fpgl.dao;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.fpgl.vo.Payment_invoiceVO;
import com.bba.fpgl.vo.Tr_Payment_PlanVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

public interface IPayment_invoiceDao extends BaseMapper<Payment_invoiceVO> {
    List<Payment_invoiceVO> findListForGrid(JqGridParamModel jqGridParamModel);

    int findListForGridCount(JqGridParamModel jqGridParamModel);

    List<Payment_invoiceVO> findBeforListForGrid(JqGridParamModel jqGridParamModel);

    int findBeforListForGridCount(JqGridParamModel jqGridParamModel);

    List<Payment_invoiceVO> findBeforCompensationListForGrid(JqGridParamModel jqGridParamModel);

    int findBeforCompensationListForGridCount(JqGridParamModel jqGridParamModel);

    List<Tr_Payment_PlanVO> selectCompensationPayMentList(Payment_invoiceVO dataPayment_invoiceVO);

    List<Tr_Payment_PlanVO> selectPayMentList(Payment_invoiceVO dataPayment_invoiceVO);

    List<Tr_Payment_PlanVO> selectNonPayMentList(Payment_invoiceVO dataPayment_invoiceVO);

    List<Payment_invoiceVO> findBeforListForGridByReceiptDate(JqGridParamModel jqGridParamModel);
}

