package com.bba.tzgl.dto;

import com.bba.tzgl.vo.LedgerVO;
import com.bba.tzgl.vo.Ledger_DetailVO;
import lombok.Data;

import java.util.List;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:39
 */
@Data
public class LedgerDTO {

    private LedgerVO ledgerVO;

    private List<Ledger_DetailVO> ledger_DetailVOList;
}
