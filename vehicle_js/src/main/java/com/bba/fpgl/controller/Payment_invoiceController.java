package com.bba.fpgl.controller;

import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.fpgl.dto.Payment_invoiceDTO;
import com.bba.fpgl.service.api.IPayment_invoiceService;
import com.bba.fpgl.service.api.IPayment_invoice_DetailService;
import com.bba.fpgl.vo.Payment_invoiceVO;
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
 * 付款发票管理
 * @Author:Suwendaidi
 * @Date: 2019/8/1
 */

@RestController
@RequestMapping("/fpgl/PaymentInvoiceController")
public class Payment_invoiceController {

    @Autowired
    private IPayment_invoiceService payment_invoiceService;

    @Autowired
    private IPayment_invoice_DetailService payment_invoice_detailService;

    @Log(value = "发票管理-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        SysUserVO sysUserVO = SessionUtils.currentSession();
        /**
         * 0：管理员；1：操作员(业务)；2：商务人员；3：财务人员；4：风险 5：承运；
         * */
        if (sysUserVO.getUserLevel().equals("5")) {
            jqGridParamModel.put("carrier_no", sysUserVO.getContractorCode());
        }
        PageVO pageVO = payment_invoiceService.getListForGrid(jqGridParamModel,customSearchFilters);
        return pageVO;
    }

    @Log(value = "发票明细管理-清单查询")
    @RequestMapping("getListForGridDetail")
    public PageVO getListForGridDetail(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        SysUserVO sysUserVO = SessionUtils.currentSession();
        /**
         * 0：管理员；1：操作员(业务)；2：商务人员；3：财务人员；4：风险 5：承运；
         * */
        if (sysUserVO.getUserLevel().equals("5")) {
            jqGridParamModel.put("carrier_no", sysUserVO.getContractorCode());
        }
        PageVO pageVO = payment_invoice_detailService.getListForGrid(jqGridParamModel,customSearchFilters);
        return pageVO;
    }

    @Log(value = "发票管理-预开票清单查询")
    @RequestMapping("getBeforListForGrid")
    public PageVO getBeforListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters,String type) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        SysUserVO sysUserVO = SessionUtils.currentSession();
        /**
         * 0：管理员；1：操作员(业务)；2：商务人员；3：财务人员；4：风险 5：承运；
         * */
        if (sysUserVO.getUserLevel().equals("5")) {
            jqGridParamModel.put("carrier_no", sysUserVO.getContractorCode());
        }
        PageVO pageVO = null;
        if(type.equals("0")){//正常
            pageVO = payment_invoiceService.getBeforListForGrid(jqGridParamModel,customSearchFilters);
        }else{//补差
            pageVO = payment_invoiceService.getBeforCompensationListForGrid(jqGridParamModel,customSearchFilters);
        }
        return pageVO;
    }

    @Log(value = "发票管理-保存")
    @RequestMapping("saveDetail")
    public ResultVO saveDetail(@RequestBody Payment_invoiceDTO requestPayment_invoiceDTO) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = payment_invoiceService.saveDetail(requestPayment_invoiceDTO, sysUserVO);
        return resultVO;
    }


    @Log(value = "发票管理-注销")
    @RequestMapping("cancel")
    public ResultVO cancel(Payment_invoiceVO payment_invoiceVO) {
        if(StringUtils.isBlank(payment_invoiceVO.getSheet_no())){
            return ResultVO.failResult("先保存后在进行操作");
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = payment_invoiceService.cancel(payment_invoiceVO, sysUserVO);
        return resultVO;
    }


    @Log(value = "发票管理-明细查询")
    @RequestMapping("getDetail")
    public ResultVO getDetail(Payment_invoiceVO payment_invoiceVO) {
        if(StringUtils.isBlank(payment_invoiceVO.getSheet_no())){
            return ResultVO.failResult("单号不能为空");
        }
        Payment_invoiceDTO returnPayment_invoiceDTO = payment_invoiceService.getDetail(payment_invoiceVO);
        if(returnPayment_invoiceDTO == null){
            return ResultVO.failResult("该发票未查询到任何数据");
        }
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(returnPayment_invoiceDTO);
        return resultVO;
    }

    @Log(value = "预开票管理-生成发票")
    @RequestMapping("createInvoice")
    public ResultVO createInvoice(@RequestBody List<Payment_invoiceVO> list,String type) {
        List<Payment_invoiceVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<Payment_invoiceVO>>(){}.getType());
        SysUserVO sysUserVO = SessionUtils.currentSession();
        return payment_invoiceService.createInvoice(vos,sysUserVO,type);
    }

    @Log(value = "发票管理-批量审核（提交付款计划）")
    @RequestMapping("check")
    public ResultVO check(@RequestBody List<Payment_invoiceVO> list) {
        List<Payment_invoiceVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<Payment_invoiceVO>>(){}.getType());
        SysUserVO sysUserVO = SessionUtils.currentSession();
        return payment_invoiceService.check(vos,sysUserVO);
    }

}
