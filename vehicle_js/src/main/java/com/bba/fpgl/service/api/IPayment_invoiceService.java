package com.bba.fpgl.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.fpgl.dto.Payment_invoiceDTO;
import com.bba.fpgl.vo.Payment_invoiceVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysUserVO;

import java.util.List;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:24
 */
public interface IPayment_invoiceService extends IService<Payment_invoiceVO> {
    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO saveDetail(Payment_invoiceDTO requestPayment_invoiceDTO, SysUserVO sysUserVO);

    ResultVO cancel(Payment_invoiceVO pyment_invoiceVO, SysUserVO sysUserVO);

    Payment_invoiceDTO getDetail(Payment_invoiceVO pyment_invoiceVO);

    PageVO getBeforListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    PageVO getBeforCompensationListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO createInvoice(List<Payment_invoiceVO> vos, SysUserVO sysUserVO,String type);

    ResultVO check(List<Payment_invoiceVO> vos, SysUserVO sysUserVO);
}
