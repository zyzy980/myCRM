package com.bba.tzgl.controller;
import com.bba.common.constant.BusinessData_projectEnum;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.tzgl.service.api.IAccountsToLedgerService;
import com.bba.tzgl.vo.Js_CompensationVO;
import com.bba.tzgl.vo.Ledger_Dz_SheetVO;
import com.bba.util.*;
import com.bba.xtgl.vo.SysUserVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 可生成台账的结算号
 * @Author:Suwendaidi
 * @Date: 2019/7/24 11:51
 */

@RestController
@RequestMapping("/tzgl/AccountsToLedger")
public class AccountsToLedgerController {

    @Autowired
    private IAccountsToLedgerService accountsToLedgerService;

    @Log(value = "生成台账的账单编号-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGridById(JqGridParamModel jqGridParamModel, String customSearchFilters,String project) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        PageVO pageVO = null;
        if(BusinessData_projectEnum.COMMODITY_CAR.getCode().equals(project)){//商品车
            pageVO = accountsToLedgerService.getListForGrid(jqGridParamModel,customSearchFilters);
        }else{
            pageVO = accountsToLedgerService.getNonListForGrid(jqGridParamModel,customSearchFilters);
        }
        return pageVO;
    }

    @Log(value = "生成台账的账单编号-生成台账")
    @RequestMapping("createLedger")
    public ResultVO createLedger(@RequestBody List<Ledger_Dz_SheetVO> list,String project) {
        List<Ledger_Dz_SheetVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<Ledger_Dz_SheetVO>>(){}.getType());
        ResultVO resultVO = new ResultVO();
        SysUserVO sysUserVO = SessionUtils.currentSession();
        if (BusinessData_projectEnum.COMMODITY_CAR.getCode().equals(project)) {
            resultVO = accountsToLedgerService.createLedger(vos,sysUserVO);
        } else {
            resultVO = accountsToLedgerService.createNonLedger(vos,sysUserVO);
        }
        return resultVO;
    }

    @Log(value = "生成台账的统计表账单编号-清单查询")
    @RequestMapping("getStatisticsListForGrid")
    public PageVO getStatisticsListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters,String project) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        PageVO pageVO = null;
        if(BusinessData_projectEnum.COMMODITY_CAR.getCode().equals(project)){//商品车
            pageVO = accountsToLedgerService.getStatisticsListForGrid(jqGridParamModel,customSearchFilters);
        }else{
            pageVO = accountsToLedgerService.getNonStatisticsListForGrid(jqGridParamModel,customSearchFilters);
        }
        return pageVO;
    }

    @Log(value = "统计表生成台账的账单编号-生成台账")
    @RequestMapping("createStatisticsLedger")
    public ResultVO createStatisticsLedger(@RequestBody List<Ledger_Dz_SheetVO> list, String project) {
        List<Ledger_Dz_SheetVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<Ledger_Dz_SheetVO>>(){}.getType());
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = new ResultVO();
        if (BusinessData_projectEnum.COMMODITY_CAR.getCode().equals(project)) {
            resultVO = accountsToLedgerService.createStatisticsLedger(vos,sysUserVO);
        } else {
            resultVO = accountsToLedgerService.createNonStatisticsLedger(vos,sysUserVO);
        }
        return resultVO;
    }


    @Log(value = "生成台账对上的补差列表-清单查询")
    @RequestMapping("getCompensationListForGrid")
    public PageVO getCompensationListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        PageVO pageVO = accountsToLedgerService.getCompensationListForGrid(jqGridParamModel,customSearchFilters);;
        return pageVO;
    }

    @Log(value = "生成台账对下的补差列表-补差台账查询")
    @RequestMapping("getDownCompensationListForGrid")
    public PageVO getDownCompensationListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        PageVO pageVO = accountsToLedgerService.getDownCompensationListForGrid(jqGridParamModel,customSearchFilters);;
        return pageVO;
    }

    @Log(value = "对上补差-生成台账")
    @RequestMapping("createUpCompensationLedger")
    public ResultVO createUpCompensationLedger(@RequestBody List<Js_CompensationVO> list) {
        List<Js_CompensationVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<Js_CompensationVO>>(){}.getType());
        SysUserVO sysUserVO = SessionUtils.currentSession();
        return accountsToLedgerService.createUpCompensationLedger(vos,sysUserVO);
    }

    @Log(value = "对下补差-生成台账")
    @RequestMapping("createDownCompensationLedger")
    public ResultVO createDownCompensationLedger(@RequestBody List<Js_CompensationVO> list) {
        List<Js_CompensationVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<Js_CompensationVO>>(){}.getType());
        SysUserVO sysUserVO = SessionUtils.currentSession();
        return accountsToLedgerService.createDownCompensationLedger(vos,sysUserVO);
    }
}
