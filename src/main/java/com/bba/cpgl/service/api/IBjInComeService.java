package com.bba.cpgl.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.cpgl.vo.BjInComeVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysUserVO;

import java.util.List;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:24
 */
public interface IBjInComeService extends IService<BjInComeVO> {
    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO saveDetail(BjInComeVO vo, SysUserVO sysUserVO);

    BjInComeVO getDetail(BjInComeVO vo);

    ResultVO batchDelete(List<BjInComeVO> vos);

    ResultVO toCompany(List<BjInComeVO> vos);
}
