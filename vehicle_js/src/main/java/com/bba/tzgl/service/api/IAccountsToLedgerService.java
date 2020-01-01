package com.bba.tzgl.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.tzgl.vo.Js_CompensationVO;
import com.bba.tzgl.vo.Ledger_Dz_SheetVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysUserVO;

import java.util.List;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/24 13:36
 */
public interface IAccountsToLedgerService extends IService<Ledger_Dz_SheetVO>  {

    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO createLedger(List<Ledger_Dz_SheetVO> vos, SysUserVO sysUserVO);

    PageVO getStatisticsListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    PageVO getNonStatisticsListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO createStatisticsLedger(List<Ledger_Dz_SheetVO> vos, SysUserVO sysUserVO);

    ResultVO createNonStatisticsLedger(List<Ledger_Dz_SheetVO> vos, SysUserVO sysUserVO);

    PageVO getNonListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO createNonLedger(List<Ledger_Dz_SheetVO> vos, SysUserVO sysUserVO);

    PageVO getCompensationListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO createUpCompensationLedger(List<Js_CompensationVO> vos, SysUserVO sysUserVO);

    ResultVO createDownCompensationLedger(List<Js_CompensationVO> vos, SysUserVO sysUserVO);

    PageVO getDownCompensationListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);
}
