package com.bba.ht.controller;


import com.bba.common.constant.Contract_TypeEnum;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.ht.dto.Ht_CusDTO;
import com.bba.ht.dto.Non_Ht_CusDTO;
import com.bba.ht.service.api.IHt_CusService;
import com.bba.ht.service.api.IHt_Cus_FreightService;
import com.bba.ht.service.api.INon_Ht_CusService;
import com.bba.ht.service.api.INon_Ht_Cus_FreightService;
import com.bba.ht.vo.Ht_CusVO;
import com.bba.ht.vo.Non_Ht_CusVO;
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

/***
 * 非商品车对上合同管理
 *
 */
@RestController
@RequestMapping("/Non_Ht_CusController")
public class Non_Ht_CusController {

    @Autowired
    private INon_Ht_CusService ht_cusService;
    @Autowired
    private INon_Ht_Cus_FreightService ht_cus_freightService;


    @Log(value = "非商品车合同管理-对上合同-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGridById(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        customSearchFilters = MyUtils.decode(customSearchFilters);
        PageVO pageVO = ht_cusService.getListForGrid(jqGridParamModel,customSearchFilters);
        return pageVO;
    }

    @Log(value = "非商品车合同管理-对上合同-保存")
    @RequestMapping("saveDetail")
    public ResultVO saveDetail(@RequestBody Non_Ht_CusDTO requestHt_cusDTO) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = ht_cusService.saveDetail(requestHt_cusDTO, sysUserVO, Contract_TypeEnum.FORMAL);
        return resultVO;
    }

    @Log(value = "非商品车合同管理-对上合同-审核")
    @RequestMapping("check")
    public ResultVO check(Non_Ht_CusVO ht_cusVO) {
        if(StringUtils.isBlank(ht_cusVO.getSheet_no())){
            return ResultVO.failResult("先保存后在进行操作");
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = ht_cusService.check(ht_cusVO, sysUserVO);
        return resultVO;
    }

    @Log(value = "非商品车合同管理-对上合同-反审核")
    @RequestMapping("uncheck")
    public ResultVO uncheck(Non_Ht_CusVO ht_cusVO) {
        if(StringUtils.isBlank(ht_cusVO.getSheet_no())){
            return ResultVO.failResult("先保存后在进行操作");
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = ht_cusService.uncheck(ht_cusVO, sysUserVO);
        return resultVO;
    }

    @Log(value = "非商品车合同管理-对上合同-注销")
    @RequestMapping("cancel")
    public ResultVO cancel(Non_Ht_CusVO ht_cusVO) {
        if(StringUtils.isBlank(ht_cusVO.getSheet_no())){
            return ResultVO.failResult("先保存后在进行操作");
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = ht_cusService.cancel(ht_cusVO, sysUserVO);
        return resultVO;
    }


    @Log(value = "非商品车合同管理-对上合同-明细查询")
    @RequestMapping("getDetail")
    public ResultVO getDetail(Non_Ht_CusVO ht_cusVO) {
        if(StringUtils.isBlank(ht_cusVO.getSheet_no())){
            return ResultVO.failResult("合同单号不能为空");
        }
        Non_Ht_CusDTO returnHt_cusDTO = ht_cusService.getDetail(ht_cusVO);
        if(returnHt_cusDTO == null){
            return ResultVO.failResult("该合同未查询到任何数据");
        }
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(returnHt_cusDTO);
        return resultVO;
    }

    @Log(value = "非商品车合同管理-对上合同-导入明细")
    @RequestMapping("importData")
    public ResultVO importData(@RequestParam("fileList") MultipartFile file) throws Exception {
        ResultVO resultVO = ht_cusService.importData(file, Contract_TypeEnum.FORMAL);
        return resultVO;
    }

    @Log(value = "非商品车合同管理-对上合同-下载导入明细模板")
    @RequestMapping("exportTemplate")
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response)
            throws IOException, InvalidFormatException {
        POIUtils.exportTemplate(request, response, "对上正式合同明细导入模板",
                "Resource/excel/contract/对上正式合同明细导入模板.xlsx");
    
    }
}

