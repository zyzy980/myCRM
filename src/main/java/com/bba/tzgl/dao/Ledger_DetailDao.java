package com.bba.tzgl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.nosettlement.vo.Js_Dz_Non_Sheet_DetailVO;
import com.bba.tzgl.vo.Ledger_DetailVO;

import java.util.List;

public interface Ledger_DetailDao extends BaseMapper<Ledger_DetailVO> {
    List<Ledger_DetailVO> findJsProject(Ledger_DetailVO ledger_detailVO);

}

