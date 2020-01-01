package com.bba.tzgl.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.settlement.vo.Js_Vin_Down_PremiumVO;
import com.bba.tzgl.dto.LedgerDTO;
import com.bba.tzgl.vo.LedgerVO;
import com.bba.tzgl.vo.Ledger_DetailVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysUserVO;

import java.util.List;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:24
 */
public interface ILedgerService extends IService<LedgerVO> {
    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO saveDetail(LedgerDTO requestLedgerDTO, SysUserVO sysUserVO);

    ResultVO check(LedgerVO ledgerVO, SysUserVO sysUserVO);

    ResultVO uncheck(LedgerVO ledgerVO, SysUserVO sysUserVO);

    ResultVO cancel(LedgerVO ledgerVO, SysUserVO sysUserVO);

    LedgerDTO getDetail(LedgerVO ledgerVO);

    List<LedgerVO> findCusJsProject(LedgerVO vo);

    ResultVO addPremium(List<Js_Vin_Down_PremiumVO> vos);

    ResultVO batchRrmovePremium(List<Ledger_DetailVO> ledger_detailVOs);

    ResultVO createInvoice(List<LedgerVO> vos, SysUserVO sysUserVO);
}
