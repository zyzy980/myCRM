package com.bba.xtgl.controller;

import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.SessionUtils;
import com.bba.xtgl.service.api.ISaleStateService;
import com.bba.xtgl.vo.SaleStateV0;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单状态管理
 * @Author bcmaidou
 * @Date 2019/4/8 10:29
 */
@Controller
@ResponseBody
@RequestMapping(value = "sysInfo/saleState")
public class SaleStateController {

    @Autowired
    private ISaleStateService iSaleStateService;

    @Log("订单状态管理-查询")
    @RequestMapping(value = "getList",method = {RequestMethod.POST,RequestMethod.GET})
    public PageVO getList(JqGridParamModel jqGridParamModel,String filters){

        SysUserVO sysUserVO = SessionUtils.currentSession();
        jqGridParamModel.put("within_code",sysUserVO.getWithin_code());
        return iSaleStateService.getList(jqGridParamModel,filters);
    }

    @Log("运单状态管理-删除")
    @RequestMapping(value = "delData",method = {RequestMethod.POST,RequestMethod.GET})
    public ResultVO delData(String sn){

        return iSaleStateService.delData(sn);
    }

    @Log("运单状态管理-保存")
    @RequestMapping(value = "saveData",method = {RequestMethod.POST,RequestMethod.GET})
    public ResultVO saveData(@RequestBody List<SaleStateV0> list){

        return iSaleStateService.saveData(list);
    }
}
