package com.bba.jsyw.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.jsyw.vo.Tr_Non_BusinessVO;
import com.bba.util.JqGridParamModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ITr_Non_BusinessDao extends BaseMapper<Tr_Non_BusinessVO> {
    void updateJsState(List<Tr_Non_BusinessVO> vos);
    List<Tr_Non_BusinessVO> findListForGrid(JqGridParamModel jqGridParamModel);
    int findListForGridCount(JqGridParamModel jqGridParamModel);

    void insertJs_non_vehicle(@Param(value = "id") Long id, @Param(value = "create_by") String create_by);

    void insertJs_non_down_vehicle(@Param(value = "id") Long id, @Param(value = "create_by") String create_by);

    void insertJs_vin_down_premium(@Param(value = "id") Long id, @Param(value = "create_by") String create_by);
}
