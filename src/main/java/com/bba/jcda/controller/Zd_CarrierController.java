package com.bba.jcda.controller;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.jcda.service.api.IZdCarrierService;
import com.bba.jcda.vo.Zd_CarrierVO;
import com.bba.util.JSONUtils;
import com.bba.util.JqGridParamModel;
import com.bba.util.SessionUtils;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 承运商档案
 */
@Controller
@RequestMapping("jcda/carrier")
public class Zd_CarrierController {
    @Autowired
    private IZdCarrierService zdCarrierService;

    @Log("承运商档案_列表查询")
    @RequestMapping(value = "/findCustomerPageVO", method = RequestMethod.POST)
    public @ResponseBody
    PageVO findCustomerListPageVO(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        SysUserVO sysUserVO = (SysUserVO) SessionUtils.currentSession();
        PageVO pageVO = zdCarrierService.findCarrierListPageVO(jqGridParamModel, customSearchFilters);
        return pageVO;
    }

    @Log(value = "承运商档案-查询明细")
    @ResponseBody
    @RequestMapping(value = "/getDetail", method = RequestMethod.POST)
    public ResultVO getDetail(@RequestBody Zd_CarrierVO vo) {
        return zdCarrierService.getCarDetail(vo);
    }


    @Log(saveOrUpdate = "保存/新增/修改")
    @ResponseBody
    @RequestMapping(value = "/SaveCus", method = {RequestMethod.POST})
    public ResultVO SaveCus(@RequestBody Zd_CarrierVO vo, HttpSession session) {
        return zdCarrierService.SaveCus(vo, session);
    }

    @Log(value = "承运商档案-注销")
    @ResponseBody
    @RequestMapping("/batchDelete")
    public ResultVO batchDelete(String jsonData) {
        if (jsonData == null || jsonData.length() == 0) {
            return ResultVO.failResult("要删除的数据为空");
        }
        List<Zd_CarrierVO> vos = JSONUtils.toJSONObjectList(Zd_CarrierVO.class, jsonData);
        return zdCarrierService.batchDelete(vos);
    }

    @Log(value = "承运商档案-恢复")
    @ResponseBody
    @RequestMapping("/batchRecovery")
    public ResultVO batchRecovery(String jsonData) {
        if (jsonData == null || jsonData.length() == 0) {
            return ResultVO.failResult("要恢复的数据为空");
        }
        List<Zd_CarrierVO> vos = JSONUtils.toJSONObjectList(Zd_CarrierVO.class, jsonData);
        return zdCarrierService.batchRecovery(vos);
    }
}