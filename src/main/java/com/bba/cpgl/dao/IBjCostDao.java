package com.bba.cpgl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.cpgl.vo.BjCostVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

public interface IBjCostDao extends BaseMapper<BjCostVO> {
    List<BjCostVO> findListForGrid(JqGridParamModel jqGridParamModel);
    int findListForGridCount(JqGridParamModel jqGridParamModel);
    void batchDelete(List<BjCostVO> vos);
}

