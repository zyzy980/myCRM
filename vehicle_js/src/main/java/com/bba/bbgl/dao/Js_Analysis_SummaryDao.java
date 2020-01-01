package com.bba.bbgl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.bbgl.vo.Js_Analysis_SummaryVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

public interface Js_Analysis_SummaryDao extends BaseMapper<Js_Analysis_SummaryVO> {
    List<Js_Analysis_SummaryVO> findListForGrid(JqGridParamModel jqGridParamModel);

    int findListForGridCount(JqGridParamModel jqGridParamModel);

}

