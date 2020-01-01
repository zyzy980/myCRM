package com.bba.dz.service.api;

import com.bba.common.service.api.IBaseService;
import com.bba.common.vo.ResultVO;
import com.bba.dz.vo.Js_Dz_SheetAccountVO;
import com.bba.dz.vo.Js_Dz_Sheet_DetailAccountVO;

import java.util.List;


public interface IJs_Dz_SheetAccountService extends IBaseService {

    public ResultVO UpdateJs_Dz_Sheet_state(List<Js_Dz_SheetAccountVO> list, Js_Dz_SheetAccountVO vo);

    public void UpdateJs_Vin_Amount(List<Js_Dz_SheetAccountVO> list);

    public void replayUpdateData(List<Js_Dz_Sheet_DetailAccountVO> list);

    public void deleteJs_Dz_Sheet(Js_Dz_Sheet_DetailAccountVO vo);

    public void updateJs_Dz_Sheet(Js_Dz_Sheet_DetailAccountVO vo);

    public List<Js_Dz_Sheet_DetailAccountVO> GetJs_Dz_Sheet_DetailVO(List<Js_Dz_Sheet_DetailAccountVO> list);

    public ResultVO reback(List<Js_Dz_SheetAccountVO> list);

    public ResultVO save(Js_Dz_SheetAccountVO vo);

    public ResultVO  buildSheet(Js_Dz_Sheet_DetailAccountVO vo);

    Integer findNullBillNumber(Js_Dz_SheetAccountVO item);

    List<Js_Dz_Sheet_DetailAccountVO> findBillNumber(List<Js_Dz_Sheet_DetailAccountVO> detailList);

    String transModeSelect(String contract_no);

    String getCusContractNo(String contract_sheet_no);
}
