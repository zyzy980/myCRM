package com.bba.jcda.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.jcda.vo.Js_Bill_NumberVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

public interface IJs_Bill_NumberDao extends BaseMapper<Js_Bill_NumberVO> {
    List<Js_Bill_NumberVO> findListForGrid(JqGridParamModel jqGridParamModel);

    int findListForGridCount(JqGridParamModel jqGridParamModel);

}

