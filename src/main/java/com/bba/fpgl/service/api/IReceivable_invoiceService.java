package com.bba.fpgl.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.fpgl.vo.Receivable_invoiceVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:24
 */
public interface IReceivable_invoiceService extends IService<Receivable_invoiceVO> {
    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO saveDetail(Receivable_invoiceVO requestReceivable_invoiceVO, SysUserVO sysUserVO);

    ResultVO cancel(Receivable_invoiceVO receivable_invoiceVO, SysUserVO sysUserVO);

    Receivable_invoiceVO getDetail(Receivable_invoiceVO receivable_invoiceVO);

    ResultVO importData(MultipartFile file);
}
