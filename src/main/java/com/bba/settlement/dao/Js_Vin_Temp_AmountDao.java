package com.bba.settlement.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.settlement.vo.Js_Vin_AmountVO;
import com.bba.settlement.vo.Js_Vin_Temp_AmountVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
public interface Js_Vin_Temp_AmountDao extends BaseMapper<Js_Vin_Temp_AmountVO> {

    List<Js_Vin_Temp_AmountVO> getListForGrid(JqGridParamModel jqGridParamModel);

    int getListForGridCount(JqGridParamModel jqGridParamModel);

    List<Js_Vin_Temp_AmountVO> selectByVinids(List<String> idList);
}
