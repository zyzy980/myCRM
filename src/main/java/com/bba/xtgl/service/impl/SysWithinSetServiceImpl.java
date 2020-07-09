package com.bba.xtgl.service.impl;

import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.util.SessionUtils;
import com.bba.xtgl.dao.ISys_within_setDao;
import com.bba.xtgl.service.api.SysWithinSetService;
import com.bba.xtgl.vo.SysUserVO;
import com.bba.xtgl.vo.Sys_within_setVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author bcmaidou
 * @Date 2019/3/25 10:23
 */
@Service
@Transactional
public class SysWithinSetServiceImpl implements SysWithinSetService {

    @Autowired
    private ISys_within_setDao iSysWithinSetDao;

    /**
     * 保存内码设置
     * @Author bcmaidou
     * @Date 10:49 2019/3/25
     */
    @Override
    public ResultVO saveWithinSet(Sys_within_setVO sys_within_setVO) {
        iSysWithinSetDao.updateWithinSet(sys_within_setVO);
        return ResultVO.successResult();
    }

    /**
     * 查询单个详情
     * @Author bcmaidou
     * @Date 15:00 2019/3/25
     */
    @Override
    public ResultVO searchWithinSetDetail() {

        SysUserVO sysUserVO = SessionUtils.currentSession();
        Sys_within_setVO sys_within_setVO = iSysWithinSetDao.searchWithin(sysUserVO.getWithin_code());
        ResultVO resultVO = new ResultVO();
        resultVO.setResultDataFull(sys_within_setVO);
        return resultVO;
    }

}
