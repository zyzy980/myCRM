package com.bba.bbgl.Controller;

import com.bba.bbgl.dto.Js_Analysis_SummaryDTO;
import com.bba.bbgl.service.api.IJs_Analysis_SummaryService;
import com.bba.bbgl.service.api.IJs_Analysis_Summary_DetailService;
import com.bba.bbgl.vo.Js_Analysis_SummaryVO;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 报表管理--结算分析汇总
 * @Author:Suwendaidi
 * @Date: 2019/7/24 11:51
 */

@RestController
@RequestMapping("/bbgl/Js_Analysis_SummaryController")
public class Js_Analysis_SummaryController {

    @Autowired
    private IJs_Analysis_SummaryService js_Analysis_SummaryService;

    @Autowired
    private IJs_Analysis_Summary_DetailService js_Analysis_Summary_DetailService;

    @Log(value = "结算分析汇总-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        PageVO pageVO = js_Analysis_SummaryService.getListForGrid(jqGridParamModel,customSearchFilters);
        return pageVO;
    }

    @Log(value = "结算分析汇总-明细查询")
    @RequestMapping("getDetail")
    public ResultVO getDetail(Js_Analysis_SummaryVO vo) {
        if(StringUtils.isBlank(vo.getSheet_no())){
            return ResultVO.failResult("单号不能为空");
        }
        Js_Analysis_SummaryDTO returnJs_Analysis_SummaryDTO = js_Analysis_SummaryService.getDetail(vo);
        if(returnJs_Analysis_SummaryDTO == null){
            return ResultVO.failResult("未查询到任何数据");
        }
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(returnJs_Analysis_SummaryDTO);
        return resultVO;
    }


    @Log(value = "结算分析汇总-生成月数据")
    @RequestMapping("create_month_data")
    public ResultVO create_month_data(String month,String type) {
        if(StringUtils.isBlank(month)){
            return ResultVO.failResult("月份不能为空");
        }
        return js_Analysis_SummaryService.create_month_data( month,type);
    }

}
