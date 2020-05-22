package com.bba.wdgl.dao;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.util.JqGridParamModel;
import com.bba.wdgl.vo.QusetionsVO;
import java.util.List;

public interface IQuestionsDao extends BaseMapper<QusetionsVO> {
    List<QusetionsVO> findListForGrid(JqGridParamModel jqGridParamModel);
    int findListForGridCount(JqGridParamModel jqGridParamModel);
    void updateQuestionState(List<QusetionsVO> vos);
    void batchDelete(List<QusetionsVO> vos);
}

