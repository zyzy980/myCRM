package com.bba.tzgl.controller;

import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.tzgl.service.api.IJs_CompensationService;
import com.bba.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 补差
 * @Author:Suwendaidi
 * @Date: 2019/8/1
 */

@RestController
@RequestMapping("/tzgl/Js_CompensationController")
public class Js_CompensationController {

    @Autowired
    private IJs_CompensationService js_CompensationService;

    @Log(value = "发票管理-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        PageVO pageVO = js_CompensationService.getListForGrid(jqGridParamModel,customSearchFilters);
        return pageVO;
    }

}
