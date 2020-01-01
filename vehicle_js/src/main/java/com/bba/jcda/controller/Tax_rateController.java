package com.bba.jcda.controller;

import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.jcda.service.api.ITax_rateService;
import com.bba.jcda.vo.Tax_rateVO;
import com.bba.util.*;
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
 * 税率档案
 * @Author:Suwendaidi
 * @Date: 2019/8/1
 */

@RestController
@RequestMapping("/jcda/tax_rate")
public class Tax_rateController {

    @Autowired
    private ITax_rateService tax_rateService;

    @Log(value = "税率档案-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        PageVO pageVO = tax_rateService.getListForGrid(jqGridParamModel,customSearchFilters);
        return pageVO;
    }

    @Log(value = "税率档案-保存")
    @RequestMapping("saveDetail")
    public ResultVO saveDetail(@RequestBody Tax_rateVO requestTax_rateVO) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = tax_rateService.saveDetail(requestTax_rateVO, sysUserVO);
        return resultVO;
    }


    @Log(value = "税率档案-删除税率")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public ResultVO UpdateJsState(@RequestBody List<Tax_rateVO> list) {
        List<Tax_rateVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<Tax_rateVO>>(){}.getType());
        return tax_rateService.remove(vos);
    }
    @Log(value = "税率档案-明细查询")
    @RequestMapping("getDetail")
    public ResultVO getDetail(Tax_rateVO requestTax_rateVO) {
        Tax_rateVO returnTax_rateVO = tax_rateService.getDetail(requestTax_rateVO);
        if(returnTax_rateVO == null){
            return ResultVO.failResult("该发票未查询到任何数据");
        }
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(returnTax_rateVO);
        return resultVO;
    }

}
