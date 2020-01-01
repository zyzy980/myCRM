package com.bba.fpgl.controller;

import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.fpgl.service.api.IReceivable_invoiceService;
import com.bba.fpgl.vo.Receivable_invoiceVO;
import com.bba.util.*;
import com.bba.xtgl.vo.SysUserVO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 收款发票管理
 * @Author:Suwendaidi
 * @Date: 2019/8/1
 */

@RestController
@RequestMapping("/fpgl/ReceivableInvoiceController")
public class Receivable_invoiceController {

    @Autowired
    private IReceivable_invoiceService ireceivable_invoiceService;

    @Log(value = "发票管理-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        PageVO pageVO = ireceivable_invoiceService.getListForGrid(jqGridParamModel,customSearchFilters);
        return pageVO;
    }

    @Log(value = "发票管理-保存")
    @RequestMapping("saveDetail")
    public ResultVO saveDetail(@RequestBody Receivable_invoiceVO requestReceivable_invoiceVO) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = ireceivable_invoiceService.saveDetail(requestReceivable_invoiceVO, sysUserVO);
        return resultVO;
    }


    @Log(value = "发票管理-注销")
    @RequestMapping("cancel")
    public ResultVO cancel(Receivable_invoiceVO receivable_invoiceVO) {
        if(StringUtils.isBlank(receivable_invoiceVO.getSheet_no())){
            return ResultVO.failResult("先保存后在进行操作");
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = ireceivable_invoiceService.cancel(receivable_invoiceVO, sysUserVO);
        return resultVO;
    }

    @Log(value = "发票管理-明细查询")
    @RequestMapping("getDetail")
    public ResultVO getDetail(Receivable_invoiceVO receivable_invoiceVO) {
        if(StringUtils.isBlank(receivable_invoiceVO.getSheet_no())){
            return ResultVO.failResult("发票号不能为空");
        }
        Receivable_invoiceVO returnReceivable_invoiceVO = ireceivable_invoiceService.getDetail(receivable_invoiceVO);
        if(returnReceivable_invoiceVO == null){
            return ResultVO.failResult("该发票未查询到任何数据");
        }
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(returnReceivable_invoiceVO);
        return resultVO;
    }

    @Log(value = "发票管理-导入修改")
    @RequestMapping("/importData")
    public ResultVO importData(@RequestParam("fileList") MultipartFile file) throws Exception {
        ResultVO resultVO = ireceivable_invoiceService.importData(file);
        return resultVO;
    }

    @Log(value = "业务数据--下载导入模板")
    @RequestMapping("exportTemplate")
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response)
            throws IOException, InvalidFormatException {
        POIUtils.exportTemplate(request, response, "非商品车导入模板",
                "Resource/excel/business/non_business.xlsx");
    }

}
