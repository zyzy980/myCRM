package com.bba.jcda.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.jcda.vo.Tax_rateVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

public interface ITax_rateDao extends BaseMapper<Tax_rateVO> {
    List<Tax_rateVO> findListForGrid(JqGridParamModel jqGridParamModel);

    int findListForGridCount(JqGridParamModel jqGridParamModel);

    void deleteBatchIds();
}

