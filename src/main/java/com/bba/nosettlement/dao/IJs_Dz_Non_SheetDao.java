package com.bba.nosettlement.dao;

import com.bba.nosettlement.vo.Js_Dz_Non_SheetVO;
import com.bba.nosettlement.vo.Js_Dz_Non_Sheet_DetailVO;
import com.bba.nosettlement.vo.Js_Non_VehicleVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 */
public interface IJs_Dz_Non_SheetDao {

    public List<Js_Dz_Non_SheetVO> getListForGrid(JqGridParamModel jqGridParamModel);

    public int getListForGridCount(JqGridParamModel jqGridParamModel);

    public List<Js_Dz_Non_Sheet_DetailVO> getListForGridDetail(JqGridParamModel jqGridParamModel);

    public int getListForGridDetailCount(JqGridParamModel jqGridParamModel);

    public void UpdateJs_Dz_Sheet_state(List<Js_Dz_Non_SheetVO> list);


    public void replayUpdateData(List<Js_Dz_Non_Sheet_DetailVO> list);
    public void deleteJs_Dz_Sheet(Js_Dz_Non_Sheet_DetailVO vo);
    public void updateJs_Dz_Sheet(Js_Dz_Non_Sheet_DetailVO vo);

    public void reback(List<Js_Dz_Non_SheetVO> list);


    //一键账单
    public List<Js_Dz_Non_Sheet_DetailVO> groupbyDataList(Js_Dz_Non_Sheet_DetailVO vo);
    public Js_Non_VehicleVO GetMaxBill_NumberByYear_JS_VIN_AMOUNT(Js_Dz_Non_Sheet_DetailVO vo);
    public void updateBill_Number(Js_Dz_Non_Sheet_DetailVO vo);
    Integer findNullBillNumber(Js_Dz_Non_SheetVO item);
}
