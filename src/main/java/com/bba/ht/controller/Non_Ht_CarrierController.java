package com.bba.ht.controller;


import com.bba.common.constant.Contract_TypeEnum;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.ht.dto.Non_Ht_CarrierDTO;
import com.bba.ht.service.api.INon_Ht_CarrierService;
import com.bba.ht.service.api.INon_Ht_Carrier_FreightService;
import com.bba.ht.vo.Non_Ht_CarrierVO;
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
 * <p>
 *  非商品出对下
 * </p>
 *
 * @author Suwendaidi
 * @since 2019-07-02
 */
@RestController
@RequestMapping("/Non_Ht_CarrierController")
public class Non_Ht_CarrierController {

    @Autowired
    private INon_Ht_CarrierService non_ht_carrierService;
    @Autowired
    private INon_Ht_Carrier_FreightService non_ht_carrier_freightService;


    @Log(value = "合同管理-对下正式合同-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGridById(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        customSearchFilters = MyUtils.decode(customSearchFilters);
        PageVO pageVO = non_ht_carrierService.getListForGrid(jqGridParamModel,customSearchFilters);
        return pageVO;
    }

    @Log(value = "合同管理-对下正式合同-保存")
    @RequestMapping("saveDetail")
    public ResultVO saveDetail(@RequestBody Non_Ht_CarrierDTO requestHt_carrierDTO) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = non_ht_carrierService.saveDetail(requestHt_carrierDTO, sysUserVO, Contract_TypeEnum.FORMAL);
        return resultVO;
    }

    @Log(value = "合同管理-对下正式合同-审核")
    @RequestMapping("check")
    public ResultVO check(Non_Ht_CarrierVO ht_carrierVO) {
        if(StringUtils.isBlank(ht_carrierVO.getSheet_no())){
            return ResultVO.failResult("先保存后在进行操作");
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = non_ht_carrierService.check(ht_carrierVO, sysUserVO);
        return resultVO;
    }

    @Log(value = "合同管理-对下正式合同-反审核")
    @RequestMapping("uncheck")
    public ResultVO uncheck(Non_Ht_CarrierVO ht_carrierVO) {
        if(StringUtils.isBlank(ht_carrierVO.getSheet_no())){
            return ResultVO.failResult("先保存后在进行操作");
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = non_ht_carrierService.uncheck(ht_carrierVO, sysUserVO);
        return resultVO;
    }

    @Log(value = "合同管理-对下正式合同-注销")
    @RequestMapping("cancel")
    public ResultVO cancel(Non_Ht_CarrierVO ht_carrierVO) {
        if(StringUtils.isBlank(ht_carrierVO.getSheet_no())){
            return ResultVO.failResult("先保存后在进行操作");
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = non_ht_carrierService.cancel(ht_carrierVO, sysUserVO);
        return resultVO;
    }


    @Log(value = "合同管理-对下正式合同-明细查询")
    @RequestMapping("getDetail")
    public ResultVO getDetail(Non_Ht_CarrierVO ht_carrierVO) {
        if(StringUtils.isBlank(ht_carrierVO.getSheet_no())){
            return ResultVO.failResult("合同单号不能为空");
        }
        Non_Ht_CarrierDTO returnHt_carrierDTO = non_ht_carrierService.getDetail(ht_carrierVO);
        if(returnHt_carrierDTO == null){
            return ResultVO.failResult("该合同未查询到任何数据");
        }
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(returnHt_carrierDTO);
        return resultVO;
    }

    @Log(value = "合同管理-对下正式合同-导入明细")
    @RequestMapping("importData")
    public ResultVO importData(@RequestParam("fileList") MultipartFile file) throws Exception {
        ResultVO resultVO = non_ht_carrierService.importData(file, Contract_TypeEnum.FORMAL);
        return resultVO;
    }

    @Log(value = "合同管理-对下正式合同-下载导入明细模板")
    @RequestMapping("exportTemplate")
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response)
            throws IOException, InvalidFormatException {
        POIUtils.exportTemplate(request, response, "对下正式合同明细导入模板",
                "Resource/excel/contract/对下正式合同明细导入模板.xlsx");
    
    }
}

