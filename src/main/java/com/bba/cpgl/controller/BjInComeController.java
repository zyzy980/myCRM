package com.bba.cpgl.controller;

import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.cpgl.service.api.IBjInComeService;
import com.bba.cpgl.vo.BjInComeVO;
import com.bba.cpgl.vo.OpenRecordVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.MyUtils;
import com.bba.util.SessionUtils;
import com.bba.xtgl.vo.SysUserVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @Author:Suwendaidi
 * @Date: 2020/5/6
 */
@RestController
@RequestMapping("/cpgl/BjInComeController")
public class BjInComeController {

    @Autowired
    private IBjInComeService bjInComeService;

    @Log(value = "收入管理-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        PageVO pageVO = bjInComeService.getListForGrid(jqGridParamModel,customSearchFilters);
        return pageVO;
    }

    @Log(value = "收入管理-保存")
    @RequestMapping("saveDetail")
    public ResultVO saveDetail(@RequestBody BjInComeVO requestBjInComeVO) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = bjInComeService.saveDetail(requestBjInComeVO, sysUserVO);
        return resultVO;
    }

    @Log(value = "收入管理-批量删除")
    @RequestMapping("delete")
    public ResultVO delete(@RequestBody List<BjInComeVO> list) {
        List<BjInComeVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<BjInComeVO>>(){}.getType());
        return bjInComeService.batchDelete(vos);
    }

    @Log(value = "收入管理-明细查询")
    @RequestMapping("getDetail")
    public ResultVO getDetail(BjInComeVO bjInComeVO) {
        BjInComeVO returnBjInComeVO = bjInComeService.getDetail(bjInComeVO);
        if(returnBjInComeVO == null){
            return ResultVO.failResult("该问题未查询到任何数据");
        }
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(returnBjInComeVO);
        return resultVO;
    }


    @Log(value = "收入管理-收款")
    @RequestMapping(value = "toCompany", method = RequestMethod.POST)
    @ResponseBody
    public ResultVO check(@RequestBody List<BjInComeVO> list) {
        List<BjInComeVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<BjInComeVO>>(){}.getType());
        return bjInComeService.toCompany(vos);
    }

    @Log(value = "收入管理-管理看板")
    @RequestMapping(value = "kanban", method = RequestMethod.POST)
    @ResponseBody
    public ResultVO kanban(OpenRecordVO vo) {
        return bjInComeService.getKanbanreportInfo(vo);
    }

}
