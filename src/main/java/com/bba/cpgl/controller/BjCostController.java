package com.bba.cpgl.controller;

import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.cpgl.service.api.IBjCostService;
import com.bba.cpgl.vo.BjCostVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.MyUtils;
import com.bba.util.SessionUtils;
import com.bba.util.StringUtils;
import com.bba.xtgl.vo.SysUserVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @Author:Suwendaidi
 * @Date: 2020/5/6
 */
@RestController
@RequestMapping("/cpgl/BjCostController")
public class BjCostController {

    @Autowired
    private IBjCostService bjCostService;

    @Log(value = "进货管理-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        PageVO pageVO = bjCostService.getListForGrid(jqGridParamModel,customSearchFilters);
        return pageVO;
    }

    @Log(value = "进货管理-保存")
    @RequestMapping("saveDetail")
    public ResultVO saveDetail(@RequestBody BjCostVO requestBjCostVO) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = bjCostService.saveDetail(requestBjCostVO, sysUserVO);
        return resultVO;
    }

    @Log(value = "进货管理-批量删除")
    @RequestMapping("delete")
    public ResultVO delete(@RequestBody List<BjCostVO> list) {
        List<BjCostVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<BjCostVO>>(){}.getType());
        return bjCostService.batchDelete(vos);
    }

    @Log(value = "进货管理-明细查询")
    @RequestMapping("getDetail")
    public ResultVO getDetail(BjCostVO bjCostVO) {
        BjCostVO returnBjCostVO = bjCostService.getDetail(bjCostVO);
        if(returnBjCostVO == null){
            return ResultVO.failResult("该问题未查询到任何数据");
        }
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(returnBjCostVO);
        return resultVO;
    }
}
