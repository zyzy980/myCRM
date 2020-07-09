package com.bba.settlement.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.ht.vo.Ht_CusVO;
import com.bba.jcda.vo.Tr_statistical_rulesVO;
import com.bba.settlement.vo.Js_Vin_AmountVO;
import com.bba.settlement.vo.VehicleTotalVO;
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
public interface Js_Vin_AmountDao extends BaseMapper<Js_Vin_AmountVO> {

    List<Js_Vin_AmountVO> getListForGrid(JqGridParamModel jqGridParamModel);

    int getListForGridCount(JqGridParamModel jqGridParamModel);

    void updateBill_Number(List<Js_Vin_AmountVO> list);


    List<VehicleTotalVO> getListForGridBaobiao(JqGridParamModel jqGridParamModel);

    int getListForGridBaobiaoCount(JqGridParamModel jqGridParamModel);

    List<Ht_CusVO> findHt_cusVO(Ht_CusVO vo);

    List<Tr_statistical_rulesVO> findTR_STATISTICAL_RULES();

    void invoice_update(Js_Vin_AmountVO updateJs_vin_amountVO);

    List<Js_Vin_AmountVO> selectCompensationList(List<String> list);
}
