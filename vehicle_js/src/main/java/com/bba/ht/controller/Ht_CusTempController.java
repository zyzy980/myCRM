package com.bba.ht.controller;


import com.bba.common.constant.Contract_TypeEnum;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.ht.dto.Ht_CusDTO;
import com.bba.ht.service.api.IHt_CusService;
import com.bba.ht.service.api.IHt_Cus_FreightService;
import com.bba.ht.vo.Ht_CusVO;
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
 *  前端控制器
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
@RestController
@RequestMapping("/Ht_CusTempController")
public class Ht_CusTempController {

    @Autowired
    private IHt_CusService ht_cusService;
    @Autowired
    private IHt_Cus_FreightService ht_cus_freightService;


    @Log(value = "合同管理-对上暂定合同-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGridById(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        customSearchFilters = MyUtils.decode(customSearchFilters);
        jqGridParamModel.put("contract_type", Contract_TypeEnum.TEMP.getCode());
        PageVO pageVO = ht_cusService.getListForGrid(jqGridParamModel,customSearchFilters);
        return pageVO;
    }

    @Log(value = "合同管理-对上暂定合同-保存")
    @RequestMapping("saveDetail")
    public ResultVO saveDetail(@RequestBody Ht_CusDTO requestHt_cusDTO) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        requestHt_cusDTO.getHt_cusVO().setContract_type(Contract_TypeEnum.TEMP.getCode());
        ResultVO resultVO = ht_cusService.saveDetail(requestHt_cusDTO, sysUserVO, Contract_TypeEnum.TEMP);
        return resultVO;
    }

    @Log(value = "合同管理-对上暂定合同-审核")
    @RequestMapping("check")
    public ResultVO check(Ht_CusVO ht_cusVO) {
        if(StringUtils.isBlank(ht_cusVO.getSheet_no())){
            return ResultVO.failResult("先保存后在进行操作");
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = ht_cusService.check(ht_cusVO, sysUserVO);
        return resultVO;
    }

    @Log(value = "合同管理-对上暂定合同-反审核")
    @RequestMapping("uncheck")
    public ResultVO uncheck(Ht_CusVO ht_cusVO) {
        if(StringUtils.isBlank(ht_cusVO.getSheet_no())){
            return ResultVO.failResult("先保存后在进行操作");
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = ht_cusService.uncheck(ht_cusVO, sysUserVO);
        return resultVO;
    }

    @Log(value = "合同管理-对上暂定合同-注销")
    @RequestMapping("cancel")
    public ResultVO cancel(Ht_CusVO ht_cusVO) {
        if(StringUtils.isBlank(ht_cusVO.getSheet_no())){
            return ResultVO.failResult("先保存后在进行操作");
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = ht_cusService.cancel(ht_cusVO, sysUserVO);
        return resultVO;
    }


    @Log(value = "合同管理-对上暂定合同-明细查询")
    @RequestMapping("getDetail")
    public ResultVO getDetail(Ht_CusVO ht_cusVO) {
        if(StringUtils.isBlank(ht_cusVO.getSheet_no())){
            return ResultVO.failResult("合同单号不能为空");
        }
        Ht_CusDTO returnHt_cusDTO = ht_cusService.getDetail(ht_cusVO);
        if(returnHt_cusDTO == null){
            return ResultVO.failResult("该合同未查询到任何数据");
        }
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(returnHt_cusDTO);
        return resultVO;
    }

    @Log(value = "合同管理-对上暂定合同-导入明细")
    @RequestMapping("importData")
    public ResultVO importData(@RequestParam("fileList") MultipartFile file) throws Exception {
        ResultVO resultVO = ht_cusService.importData(file, Contract_TypeEnum.TEMP);
        return resultVO;
    }

    @Log(value = "合同管理-对上暂定合同-下载导入明细模板")
    @RequestMapping("exportTemplate")
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response)
            throws IOException, InvalidFormatException {
        POIUtils.exportTemplate(request, response, "对上暂定合同明细导入模板",
                "Resource/excel/contract/对上暂定合同明细导入模板.xlsx");
    
    }
}

