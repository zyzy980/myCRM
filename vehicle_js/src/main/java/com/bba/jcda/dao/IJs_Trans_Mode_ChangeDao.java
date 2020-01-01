package com.bba.jcda.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.jcda.vo.Js_Trans_Mode_ChangeVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

public interface IJs_Trans_Mode_ChangeDao extends BaseMapper<Js_Trans_Mode_ChangeVO> {
    List<Js_Trans_Mode_ChangeVO> findListForGrid(JqGridParamModel jqGridParamModel);

    int findListForGridCount(JqGridParamModel jqGridParamModel);

}

