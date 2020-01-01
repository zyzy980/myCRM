package com.bba.tzgl.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.vo.PageVO;
import com.bba.tzgl.vo.Js_CompensationVO;
import com.bba.util.JqGridParamModel;


/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:24
 */
public interface IJs_CompensationService extends IService<Js_CompensationVO> {
    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);
}
