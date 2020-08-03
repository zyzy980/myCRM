package com.bba.cpgl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.cpgl.vo.ReceiveRecordVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

public interface IPurchaseManagerDao extends BaseMapper<ReceiveRecordVO> {
    List<ReceiveRecordVO> findListForGrid(JqGridParamModel jqGridParamModel);
    int findListForGridCount(JqGridParamModel jqGridParamModel);
    void batchDelete(List<ReceiveRecordVO> vos);
}

