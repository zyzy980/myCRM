package com.bba.tzgl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.tzgl.vo.Js_CompensationVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

public interface Js_CompensationDao extends BaseMapper<Js_CompensationVO> {
    List<Js_CompensationVO> findListForGrid(JqGridParamModel jqGridParamModel);

    int findListForGridCount(JqGridParamModel jqGridParamModel);

    void batchInsert(List<Js_CompensationVO> newList);

    void invoice_update(Js_CompensationVO updateJs_compensationVO);
}

