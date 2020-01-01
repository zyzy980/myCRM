package com.bba.settlement.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.settlement.vo.Js_Vin_Down_PremiumVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysUserVO;

import java.util.List;

/**
 * 业务数据管理服务层
 *@Author:Suwendaidi
 *2019/7/11
 */
public interface IJs_Down_PremiumService extends IService<Js_Vin_Down_PremiumVO> {

    PageVO findPremiumPageVO(JqGridParamModel jqGridParamModel, String customSearchFilters);

    PageVO findPremiumGroupPageVO(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO settlement(List<Js_Vin_Down_PremiumVO> vos, SysUserVO sysUserVO);

    PageVO selectPremiumGroupByMonth(JqGridParamModel jqGridParamModel, String customSearchFilters);
}
