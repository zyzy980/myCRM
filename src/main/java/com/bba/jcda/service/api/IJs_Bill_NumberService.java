package com.bba.jcda.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.vo.PageVO;
import com.bba.jcda.vo.Js_Bill_NumberVO;
import com.bba.util.JqGridParamModel;

public interface IJs_Bill_NumberService extends IService<Js_Bill_NumberVO> {
    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);
}
