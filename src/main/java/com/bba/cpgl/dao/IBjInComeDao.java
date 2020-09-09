package com.bba.cpgl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.cpgl.vo.BjInComeVO;
import com.bba.cpgl.vo.OpenRecordVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

public interface IBjInComeDao extends BaseMapper<BjInComeVO> {
    List<BjInComeVO> findListForGrid(JqGridParamModel jqGridParamModel);
    int findListForGridCount(JqGridParamModel jqGridParamModel);
    void batchDelete(List<BjInComeVO> vos);

    void updateState(List<BjInComeVO> vos);

    List<OpenRecordVO> getGoodsTypeReport();

    List<OpenRecordVO> getValueReport();

    List<OpenRecordVO> getSellModeReport();

    List<OpenRecordVO> getSalesTrendReport();

    List<OpenRecordVO> getSalesRankingReport();

    List<OpenRecordVO> getRollReport();
}

