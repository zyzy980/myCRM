package com.bba.cpgl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.cpgl.vo.OpenRecordVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

public interface IOpenRecordDao extends BaseMapper<OpenRecordVO> {
    List<OpenRecordVO> findListForGrid(JqGridParamModel jqGridParamModel);
    int findListForGridCount(JqGridParamModel jqGridParamModel);
    void batchDelete(List<OpenRecordVO> vos);
}

