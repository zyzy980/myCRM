package com.bba.cpgl.dao;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.cpgl.vo.CpVO;
import com.bba.util.JqGridParamModel;
import java.util.List;

public interface ICpDao extends BaseMapper<CpVO> {
    List<CpVO> findListForGrid(JqGridParamModel jqGridParamModel);
    int findListForGridCount(JqGridParamModel jqGridParamModel);
    void updateQuestionState(List<CpVO> vos);
    void batchDelete(List<CpVO> vos);
}

