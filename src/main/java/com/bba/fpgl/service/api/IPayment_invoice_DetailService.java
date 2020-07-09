package com.bba.fpgl.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.vo.PageVO;
import com.bba.fpgl.vo.Payment_invoice_DetailVO;
import com.bba.util.JqGridParamModel;


/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:24
 */
public interface IPayment_invoice_DetailService extends IService<Payment_invoice_DetailVO> {
    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);
}
