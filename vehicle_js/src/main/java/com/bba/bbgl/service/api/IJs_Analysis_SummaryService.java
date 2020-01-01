package com.bba.bbgl.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.bbgl.dto.Js_Analysis_SummaryDTO;
import com.bba.bbgl.vo.Js_Analysis_SummaryVO;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysUserVO;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:24
 */
public interface IJs_Analysis_SummaryService extends IService<Js_Analysis_SummaryVO> {
    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO saveDetail(Js_Analysis_SummaryDTO requestJs_Analysis_SummaryDTO, SysUserVO sysUserVO);

    Js_Analysis_SummaryDTO getDetail(Js_Analysis_SummaryVO vo);

    ResultVO create_month_data(String month, String type);
}
