package com.bba.jcda.controller;

import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.jcda.service.api.ICrCustomerService;
import com.bba.jcda.vo.*;
import com.bba.util.*;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("jcda/customer")
public class Cr_CustomerController {
    @Autowired
    private ICrCustomerService customerService;

    @Log("客户档案_列表查询")
    @RequestMapping(value = "/findCustomerPageVO", method = RequestMethod.POST)
    public @ResponseBody
    PageVO findCustomerListPageVO(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        SysUserVO sysUserVO = (SysUserVO) SessionUtils.currentSession();
        PageVO pageVO = customerService.findCustomerListPageVO(jqGridParamModel, customSearchFilters);
        return pageVO;
    }

    @Log(value = "客户档案-查询明细")
    @ResponseBody
    @RequestMapping(value = "/getDetail", method = RequestMethod.POST)
    public ResultVO getDetail(@RequestBody CrCustomerVO vo) {
        return customerService.getCusDetail(vo);
    }

    @Log("客户联系人明细")
    @RequestMapping(value = "/findzdCusLinkPageVO", method = RequestMethod.POST)
    public @ResponseBody PageVO findzdCusLinkPageVO(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        customSearchFilters = MyUtils.decode(customSearchFilters); // 防止中文乱码
        PageVO pageVO = customerService.findzdCusLinkPageVO(jqGridParamModel, customSearchFilters);
        return pageVO;
    }

    @Log(saveOrUpdate = "保存/新增/修改")
    @ResponseBody
    @RequestMapping(value = "/SaveCus", method = {RequestMethod.POST})
    public ResultVO SaveCus(@RequestBody CrCustomerVO vo, HttpSession session) {
        return customerService.SaveCus(vo, session);
    }

    @Log(value = "注销客户")
    @ResponseBody
    @RequestMapping("/batchDelete")
    public ResultVO batchDelete(String jsonData) {
        if (jsonData == null || jsonData.length() == 0) {
            return ResultVO.failResult("要删除的数据为空");
        }
        List<CrCustomerVO> customerVOs = JSONUtils.toJSONObjectList(CrCustomerVO.class, jsonData);
        return customerService.batchDelete(customerVOs);
    }

    @Log(value = "恢复客户")
    @ResponseBody
    @RequestMapping("/batchRecovery")
    public ResultVO batchRecovery(String jsonData) {
        if (jsonData == null || jsonData.length() == 0) {
            return ResultVO.failResult("要恢复的数据为空");
        }
        List<CrCustomerVO> customerVOs = JSONUtils.toJSONObjectList(CrCustomerVO.class, jsonData);
        return customerService.batchRecovery(customerVOs);
    }

    @Log(value = "删除客户联系人")
    @ResponseBody
    @RequestMapping(value = "/deleteLinks")
    public ResultVO delete(String jsonData) {
        if (jsonData == null || jsonData.length() == 0) {
            return ResultVO.failResult("要删除的数据为空");
        }
        List<Cr_Customer_LinkVO> vos = JSONUtils.toJSONObjectList(Cr_Customer_LinkVO.class, jsonData);
        customerService.deleteDetail(vos);
        return ResultVO.successResult();
    }
}