package com.bba.tzgl.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.constant.LedgerEnum;
import com.bba.tzgl.dao.Ledger_DetailDao;
import com.bba.tzgl.service.api.ILedger_DetailService;
import com.bba.tzgl.vo.Ledger_DetailVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:26
 */
@Service
@Transactional
public class Ledger_DetailServiceimpl extends ServiceImpl<Ledger_DetailDao, Ledger_DetailVO> implements ILedger_DetailService {

    @Resource
    private Ledger_DetailDao ledger_DetailDao;

    @Override
    public List<Ledger_DetailVO> findJsProject(Ledger_DetailVO ledger_DetailVO) {
        ledger_DetailVO.setCarrier_invoice(LedgerEnum.INVOICE_FLAG_N.getCode());//做通用，在前台控制
        return ledger_DetailDao.findJsProject(ledger_DetailVO);
    }
}
