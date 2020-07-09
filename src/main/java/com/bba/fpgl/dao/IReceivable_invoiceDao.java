package com.bba.fpgl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.fpgl.vo.Receivable_invoiceVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

public interface IReceivable_invoiceDao extends BaseMapper<Receivable_invoiceVO> {
    List<Receivable_invoiceVO> findListForGrid(JqGridParamModel jqGridParamModel);

    int findListForGridCount(JqGridParamModel jqGridParamModel);
}

