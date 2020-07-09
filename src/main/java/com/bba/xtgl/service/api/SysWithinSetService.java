package com.bba.xtgl.service.api;

import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.Sys_within_setVO;

/**
 * @Author bcmaidou
 * @Date 2019/3/25 10:22
 */
public interface SysWithinSetService {

    /**
     * 保存内码设置
     * @Author bcmaidou
     * @Date 10:49 2019/3/25
     */
    ResultVO saveWithinSet(Sys_within_setVO sys_within_setVO);

    /**
     * 查询单个详情
     * @Author bcmaidou
     * @Date 14:59 2019/3/25
     */
    ResultVO searchWithinSetDetail();
}
