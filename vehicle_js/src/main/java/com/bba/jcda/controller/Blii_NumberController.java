package com.bba.jcda.controller;

import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.jcda.service.api.ITax_rateService;
import com.bba.jcda.vo.Tax_rateVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.MyUtils;
import com.bba.util.SessionUtils;
import com.bba.xtgl.vo.SysUserVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 账单编号
 * @Author:Suwendaidi
 * @Date: 2019/8/1
 */

@RestController
@RequestMapping("/jcda/billNumber")
public class Blii_NumberController {

    @Autowired
    private ITax_rateService tax_rateService;

    @Log(value = "账单编号-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        PageVO pageVO = tax_rateService.getListForGrid(jqGridParamModel,customSearchFilters);
        return pageVO;
    }

}
