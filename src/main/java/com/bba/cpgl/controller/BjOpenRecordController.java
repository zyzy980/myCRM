package com.bba.cpgl.controller;

import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.cpgl.service.api.IOpenRecordService;
import com.bba.cpgl.vo.OpenRecordVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.MyUtils;
import com.bba.util.SessionUtils;
import com.bba.util.StringUtils;
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
@RequestMapping("/cpgl/openRecordController")
public class BjOpenRecordController {

    @Autowired
    private IOpenRecordService openRecordService;

    @Log(value = "拆件管理-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        PageVO pageVO = openRecordService.getListForGrid(jqGridParamModel,customSearchFilters);
        return pageVO;
    }

    @Log(value = "拆件管理-保存")
    @RequestMapping("saveDetail")
    public ResultVO saveDetail(@RequestBody OpenRecordVO requestOpenRecordVO) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = openRecordService.saveDetail(requestOpenRecordVO, sysUserVO);
        return resultVO;
    }

    @Log(value = "拆件管理-批量删除")
    @RequestMapping("delete")
    public ResultVO delete(@RequestBody List<OpenRecordVO> list) {
        List<OpenRecordVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<OpenRecordVO>>(){}.getType());
        return openRecordService.batchDelete(vos);
    }

    @Log(value = "拆件管理-明细查询")
    @RequestMapping("getDetail")
    public ResultVO getDetail(OpenRecordVO openRecordVO) {
            if(StringUtils.isBlank(openRecordVO.getSheet_no())){
            return ResultVO.failResult("单号不能为空");
        }
        OpenRecordVO returnOpenRecordVO = openRecordService.getDetail(openRecordVO);
        if(returnOpenRecordVO == null){
            return ResultVO.failResult("该问题未查询到任何数据");
        }
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(returnOpenRecordVO);
        return resultVO;
    }


    @Log(value = "收入管理-收款")
    @RequestMapping(value = "getmoney", method = RequestMethod.POST)
    @ResponseBody
    public ResultVO check(@RequestBody List<OpenRecordVO> list) {
        List<OpenRecordVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<OpenRecordVO>>(){}.getType());
        return openRecordService.getmoney(vos);
    }

}
