package com.bba.settlement.service.api;

import com.bba.common.service.api.IBaseService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.ht.vo.Ht_CarrierVO;
import com.bba.ht.vo.Ht_Carrier_FreightVO;
import com.bba.settlement.vo.Js_Vin_AmountVO;
import com.bba.settlement.vo.Js_Vin_DownTemp_AmountVO;
import com.bba.settlement.vo.Js_Vin_Down_AmountVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.service.api.ISysSheetIdService;
import com.bba.xtgl.vo.SysUserVO;

import java.util.List;


public interface IJs_Vin_Down_AmountService extends IBaseService {

    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters,String contract_type);



    public void UpdateDataList(List<Js_Vin_Down_AmountVO> list);

    public void UpdateTempDataList(List<Js_Vin_DownTemp_AmountVO> list);

    public List<Ht_CarrierVO> findHt_CarrierVO(Ht_CarrierVO vo);

    public String findJS_VIN_AMOUNT_DZ_SHEET(Js_Vin_AmountVO vo);

    public List<Ht_Carrier_FreightVO> findHt_Carrier_FreightVO(Js_Vin_Down_AmountVO vo);

    public ResultVO two_settlementDetail(List<String> list, ISysSheetIdService iSysSheetIdService);

    public ResultVO un_settlement(List<String> list, SysUserVO sysUserVO);

    public ResultVO data_check(Js_Vin_Down_AmountVO vo,List<Js_Vin_Down_AmountVO> list,List<Js_Vin_Down_AmountVO> datalist);


    public List<Js_Vin_Down_AmountVO> GetJs_Vin_Down_AmountVO(List<Js_Vin_Down_AmountVO> list);
    public List<Js_Vin_DownTemp_AmountVO> GetJs_Vin_DownTemp_AmountVO(List<Js_Vin_DownTemp_AmountVO> list);

    PageVO getCompensationListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);
}
