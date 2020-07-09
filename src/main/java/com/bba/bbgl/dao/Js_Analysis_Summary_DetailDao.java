package com.bba.bbgl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.bbgl.vo.Js_Analysis_Summary_DetailVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

public interface Js_Analysis_Summary_DetailDao extends BaseMapper<Js_Analysis_Summary_DetailVO> {
    List<Js_Analysis_Summary_DetailVO> findListForGrid(JqGridParamModel jqGridParamModel);
    int findListForGridCount(JqGridParamModel jqGridParamModel);

}

