package com.bba.tzgl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.tzgl.vo.LedgerVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

public interface LedgerDao extends BaseMapper<LedgerVO> {
    List<LedgerVO> findListForGrid(JqGridParamModel jqGridParamModel);

    int findListForGridCount(JqGridParamModel jqGridParamModel);

    List<LedgerVO> findCusJsProject(LedgerVO vo);

    String getToponeJsno();

    LedgerVO selectOneByConditions(LedgerVO newLedgerVO);
}

