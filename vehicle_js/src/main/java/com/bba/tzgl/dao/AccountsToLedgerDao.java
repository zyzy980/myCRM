package com.bba.tzgl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.nosettlement.vo.Js_Dz_Non_Sheet_DetailVO;
import com.bba.nosettlement.vo.Js_Non_Down_VehicleVO;
import com.bba.nosettlement.vo.Js_Non_VehicleVO;
import com.bba.settlement.vo.Js_Vin_Down_AmountVO;
import com.bba.tzgl.vo.Js_CompensationVO;
import com.bba.tzgl.vo.LedgerVO;
import com.bba.tzgl.vo.Ledger_DetailVO;
import com.bba.tzgl.vo.Ledger_Dz_SheetVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

public interface AccountsToLedgerDao extends BaseMapper<Ledger_Dz_SheetVO> {
    List<Ledger_Dz_SheetVO> findListForGrid(JqGridParamModel jqGridParamModel);
    int findListForGridCount(JqGridParamModel jqGridParamModel);
    List<Ledger_DetailVO> selectLedgerDetailByBillNo(Ledger_Dz_SheetVO requestLedger_dz_sheetVO);
    LedgerVO selectUpLedger(Ledger_Dz_SheetVO requestLedger_dz_sheetVO);
    void updateDownData(Js_Vin_Down_AmountVO updateJs_vin_down_amountVO);

    List<Ledger_Dz_SheetVO> getStatisticsListForGrid(JqGridParamModel jqGridParamModel);

    int getStatisticsListForGridCount(JqGridParamModel jqGridParamModel);

    List<Ledger_Dz_SheetVO> getNonStatisticsListForGrid(JqGridParamModel jqGridParamModel);

    int getNonStatisticsListForGridCount(JqGridParamModel jqGridParamModel);

    LedgerVO selectStatisticsUpLedger(Ledger_Dz_SheetVO requestLedger_dz_sheetVO);

    List<Ledger_DetailVO> selectStatisticsLedgerDetailByBillNo(Ledger_Dz_SheetVO requestLedger_dz_sheetVO);

    LedgerVO selectNonStatisticsUpLedger(Ledger_Dz_SheetVO requestLedger_dz_sheetVO);

    List<Ledger_DetailVO> selectNonStatisticsLedgerDetailByBillNo(Ledger_Dz_SheetVO requestLedger_dz_sheetVO);

    List<Ledger_Dz_SheetVO> findNonListForGrid(JqGridParamModel jqGridParamModel);

    int findNonListForGridCount(JqGridParamModel jqGridParamModel);

    LedgerVO selectNonUpLedger(Ledger_Dz_SheetVO requestLedger_dz_sheetVO);

    List<Ledger_DetailVO> selectNonLedgerDetailByBillNo(Ledger_Dz_SheetVO requestLedger_dz_sheetVO);

    void updateDzNonDetail(Js_Dz_Non_Sheet_DetailVO updateJs_dz_sheet_detailVO);

    void updateJsNonVehicle(Js_Non_VehicleVO updateJs_non_vehicleVO);

    void updateNonDownData(Js_Non_Down_VehicleVO updateJs_non_down_vehicleVO);

    List<Js_CompensationVO> getCompensationListForGrid(JqGridParamModel jqGridParamModel);

    int getCompensationListForGridCount(JqGridParamModel jqGridParamModel);

    //void updateNonDownDataByJsno(Js_Non_Down_VehicleVO updateJs_non_down_vehicleVO);

    void updateUpNonJsVehicle(Js_Non_VehicleVO updateJs_non_vehicleVO);

    List<Js_CompensationVO> getDownCompensationListForGrid(JqGridParamModel jqGridParamModel);

    int getDownCompensationListForGridCount(JqGridParamModel jqGridParamModel);
}

