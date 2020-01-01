package com.bba.dz.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.dz.vo.Js_Dz_SheetVO;
import com.bba.dz.vo.Js_Dz_Sheet_DetailVO;
import com.bba.ht.vo.Ht_Cus_FreightVO;
import com.bba.settlement.vo.Js_Vin_AmountVO;
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
public interface Js_Dz_Sheet_DetailDao extends BaseMapper<Js_Dz_Sheet_DetailVO> {

    List<Js_Dz_Sheet_DetailVO> getListForGrid(JqGridParamModel jqGridParamModel);

    int getListForGridCount(JqGridParamModel jqGridParamModel);

}
