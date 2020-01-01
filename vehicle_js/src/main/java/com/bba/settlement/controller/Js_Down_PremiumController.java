package com.bba.settlement.controller;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.settlement.service.api.IJs_Down_PremiumService;
import com.bba.settlement.vo.Js_Vin_Down_PremiumVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.SessionUtils;
import com.bba.xtgl.vo.SysUserVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 对下保费结算管理
 *@Author:Suwendaidi
 *2019/7/18
 */
@Controller
@RequestMapping("jsgl/premium")
public class Js_Down_PremiumController {
    @Autowired
    private IJs_Down_PremiumService js_down_premiumService;

    @Log("对下保费_列表查询")
    @ResponseBody
    @RequestMapping(value = "/findPremiumPageVO", method = RequestMethod.POST)
    public PageVO findPremiumPageVO(JqGridParamModel jqGridParamModel, String customSearchFilters, HttpSession session) {
        PageVO pageVO = js_down_premiumService.findPremiumPageVO(jqGridParamModel, customSearchFilters);
        return pageVO;
    }

    @Log("对下保费_汇总查询")
    @ResponseBody
    @RequestMapping(value = "/findPremiumGroupPageVO", method = RequestMethod.POST)
    public PageVO findPremiumGroupPageVO(JqGridParamModel jqGridParamModel, String customSearchFilters, HttpSession session) {
        PageVO pageVO = js_down_premiumService.findPremiumGroupPageVO(jqGridParamModel, customSearchFilters);
        return pageVO;
    }

    @Log(value = "对下保费-结算")
    @RequestMapping(value = "/settlement", method = RequestMethod.POST)
    @ResponseBody
    public ResultVO check(@RequestBody List<Js_Vin_Down_PremiumVO> list, HttpSession session) {
        List<Js_Vin_Down_PremiumVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<Js_Vin_Down_PremiumVO>>(){}.getType());
        SysUserVO sysUserVO = SessionUtils.currentSession();
        return js_down_premiumService.settlement(vos,sysUserVO);
    }


    @Log("对下保费_按月份查询_插入台账")
    @ResponseBody
    @RequestMapping(value = "/selectPremiumGroupByMonth", method = RequestMethod.POST)
    public PageVO selectPremiumGroupByMonth(JqGridParamModel jqGridParamModel, String customSearchFilters, HttpSession session) {
        PageVO pageVO = js_down_premiumService.selectPremiumGroupByMonth(jqGridParamModel, customSearchFilters);
        return pageVO;
    }
}