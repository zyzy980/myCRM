package com.bba.cpgl.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.cpgl.vo.BjCostVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysUserVO;

import java.util.List;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:24
 */
public interface IBjCostService extends IService<BjCostVO> {
    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO saveDetail(BjCostVO vo, SysUserVO sysUserVO);

    BjCostVO getDetail(BjCostVO vo);

    ResultVO batchDelete(List<BjCostVO> vos);
}
