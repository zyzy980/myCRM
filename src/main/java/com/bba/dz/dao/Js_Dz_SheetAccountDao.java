package com.bba.dz.dao;


import com.bba.dz.vo.Js_Dz_SheetAccountVO;

import com.bba.dz.vo.Js_Dz_Sheet_DetailAccountVO;
import com.bba.settlement.vo.Js_Vin_AmountVO;
import org.apache.ibatis.annotations.Param;


import java.util.List;


public interface Js_Dz_SheetAccountDao {

    public void UpdateJs_Dz_Sheet_state(List<Js_Dz_SheetAccountVO> list);

    public void UpdateJs_Vin_Amount(List<Js_Dz_SheetAccountVO> list);

    public void replayUpdateData(List<Js_Dz_Sheet_DetailAccountVO> list);

    public void deleteJs_Dz_Sheet(Js_Dz_Sheet_DetailAccountVO vo);

    public void updateJs_Dz_Sheet(Js_Dz_Sheet_DetailAccountVO vo);

    public List<Js_Dz_Sheet_DetailAccountVO> GetJs_Dz_Sheet_DetailVO(List<Js_Dz_Sheet_DetailAccountVO> list);

    public void reback(List<Js_Dz_SheetAccountVO> list);


    public List<Js_Dz_Sheet_DetailAccountVO> groupbyDataList(Js_Dz_Sheet_DetailAccountVO vo);

    public Js_Vin_AmountVO GetMaxBill_NumberByYear_JS_VIN_AMOUNT(Js_Dz_Sheet_DetailAccountVO vo);

    public void updateBill_Number(Js_Dz_Sheet_DetailAccountVO vo);

    Integer findNullBillNumber(Js_Dz_SheetAccountVO item);

    List<Js_Dz_Sheet_DetailAccountVO> findBillNumber(List<Js_Dz_Sheet_DetailAccountVO> detailList);

    Integer transModeSelect(@Param("contract_no") String contract_no);

    String getCusContractNo(@Param("contract_sheet_no") String contract_sheet_no);
}
