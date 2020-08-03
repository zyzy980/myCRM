package com.bba.cpgl.controller;

import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.cpgl.service.api.IPurchaseManagerService;
import com.bba.cpgl.vo.ReceiveRecordVO;
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
@RequestMapping("/cpgl/PurchaseManagerController")
public class PurchaseManagerController {

    @Autowired
    private IPurchaseManagerService purchaseManagerService;

    @Log(value = "进货管理-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        PageVO pageVO = purchaseManagerService.getListForGrid(jqGridParamModel,customSearchFilters);
        return pageVO;
    }

    @Log(value = "进货管理-保存")
    @RequestMapping("saveDetail")
    public ResultVO saveDetail(@RequestBody ReceiveRecordVO requestReceiveRecordVO) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = purchaseManagerService.saveDetail(requestReceiveRecordVO, sysUserVO);
        return resultVO;
    }

    @Log(value = "进货管理-批量删除")
    @RequestMapping("delete")
    public ResultVO delete(@RequestBody List<ReceiveRecordVO> list) {
        List<ReceiveRecordVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<ReceiveRecordVO>>(){}.getType());
        return purchaseManagerService.batchDelete(vos);
    }

    @Log(value = "进货管理-明细查询")
    @RequestMapping("getDetail")
    public ResultVO getDetail(ReceiveRecordVO receiveRecordVO) {
            if(StringUtils.isBlank(receiveRecordVO.getSheet_no())){
            return ResultVO.failResult("单号不能为空");
        }
        ReceiveRecordVO returnReceiveRecordVO = purchaseManagerService.getDetail(receiveRecordVO);
        if(returnReceiveRecordVO == null){
            return ResultVO.failResult("该问题未查询到任何数据");
        }
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(returnReceiveRecordVO);
        return resultVO;
    }

}
