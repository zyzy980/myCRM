package com.bba.settlement.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.ht.vo.Ht_CarrierVO;
import com.bba.ht.vo.Ht_Carrier_FreightVO;
import com.bba.settlement.vo.*;
import com.bba.util.JqGridParamModel;

import java.util.List;


public interface IJs_Vin_Down_AmountDao extends BaseMapper<Js_Vin_Down_AmountVO> {

    public List<Js_Vin_Down_AmountVO> getListForGrid(JqGridParamModel jqGridParamModel);
    public int getListForGridCount(JqGridParamModel jqGridParamModel);


    public List<Js_Vin_DownTemp_AmountVO> getListForGrid_Temp(JqGridParamModel jqGridParamModel);
    public int getListForGridCount_Temp(JqGridParamModel jqGridParamModel);


    public void UpdateDataList(List<Js_Vin_Down_AmountVO> list);

    public void UpdateTempDataList(List<Js_Vin_DownTemp_AmountVO> list);

    public List<Ht_CarrierVO> findHt_CarrierVO(Ht_CarrierVO vo);


    public String findJS_VIN_AMOUNT_DZ_SHEET(Js_Vin_AmountVO vo);

    public List<Ht_Carrier_FreightVO> findHt_Carrier_FreightVO(Js_Vin_Down_AmountVO vo);

    public void updateJs_Vin_DownTemp_Amount(List<Js_Vin_DownTemp_AmountVO> list);

    void invoice_update(Js_Vin_Down_AmountVO updateJs_vin_down_amountVO);





    public List<Js_Vin_Down_AmountVO> GetJs_Vin_Down_AmountVO(List<Js_Vin_Down_AmountVO> list);
    public List<Js_Vin_DownTemp_AmountVO> GetJs_Vin_DownTemp_AmountVO(List<Js_Vin_DownTemp_AmountVO> list);

    List<Js_Vin_Down_CompensationAmountVO> getCompensationListForGrid(JqGridParamModel jqGridParamModel);

    int getCompensationListForGridCount(JqGridParamModel jqGridParamModel);
}
