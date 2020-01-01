package com.bba.jsyw.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.jsyw.vo.Tr_BusinessVO;
import com.bba.util.JqGridParamModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ITr_BusinessDao extends BaseMapper<Tr_BusinessVO> {
    void updateJsState(List<Tr_BusinessVO> vos);
    List<Tr_BusinessVO> findListForGrid(JqGridParamModel jqGridParamModel);
    int findListForGridCount(JqGridParamModel jqGridParamModel);

    void insertJs_vin_amount(@Param(value = "id") Long id,@Param(value="create_by") String create_by);

    void insertJs_vin_down_amount(@Param(value = "id") Long id,@Param(value="create_by") String create_by);

    void insertJs_vin_down_premium(@Param(value = "id") Long id,@Param(value="create_by") String create_by);
}
