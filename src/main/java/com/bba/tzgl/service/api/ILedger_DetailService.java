package com.bba.tzgl.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.tzgl.vo.Ledger_DetailVO;
import java.util.List;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:24
 */
public interface ILedger_DetailService extends IService<Ledger_DetailVO> {
    List<Ledger_DetailVO> findJsProject(Ledger_DetailVO vo);
}
