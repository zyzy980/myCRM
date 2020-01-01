package com.bba.jcda.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.jcda.vo.Tax_rateVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysUserVO;

import java.util.List;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:24
 */
public interface ITax_rateService extends IService<Tax_rateVO> {
    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO saveDetail(Tax_rateVO vo, SysUserVO sysUserVO);

    ResultVO cancel(Tax_rateVO vo, SysUserVO sysUserVO);

    Tax_rateVO getDetail(Tax_rateVO vo);

    ResultVO remove(List<Tax_rateVO> vos);
}
