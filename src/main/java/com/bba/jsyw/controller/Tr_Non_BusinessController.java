package com.bba.jsyw.controller;
import com.bba.common.constant.BusinessData_projectEnum;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.jsyw.dto.Tr_Non_BusinessDTO;
import com.bba.jsyw.service.api.ITr_Non_BusinessService;
import com.bba.jsyw.vo.Tr_Non_BusinessVO;
import com.bba.util.*;
import com.bba.xtgl.vo.SysUserVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 业务数据管理
 *@Author:Suwendaidi
 *2019/7/11
 */
@Controller
@RequestMapping("jsyw/non_business")
public class Tr_Non_BusinessController {
    @Autowired
    private ITr_Non_BusinessService non_businessService;

    @Log("业务数据_列表查询")
    @ResponseBody
    @RequestMapping(value = "/findBusinessPageVO", method = RequestMethod.POST)
    public PageVO findBusinessPageVO(JqGridParamModel jqGridParamModel, String customSearchFilters, HttpSession session) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        customSearchFilters = MyUtils.decode(customSearchFilters);
        /**
         * 0：管理员；1：操作员(业务)；2：商务人员；3：财务人员；4：风险 5：承运；
         * 1、业务数据到结算系统后状态为0，先是风险组才能看到，风险组标注重损普损后，审核数据
         * 2、审核数据后状态状态为1，业务可以看到
         * */
        if (sysUserVO.getUserLevel().equals("4")) {
            jqGridParamModel.put("mass_loss_type", "in", "'0','1','2'");
            jqGridParamModel.put("data_state", "in", "'0'");
        } else if (sysUserVO.getUserLevel().equals("2")) {
            jqGridParamModel.put("mass_loss_type", "in", "'1'");
            jqGridParamModel.put("data_state", "ni", "'0'");
        }
        PageVO pageVO = non_businessService.findBusinessPageVO(jqGridParamModel, customSearchFilters);
        return pageVO;
    }

    @Log(value = "业务数据-查询明细")
    @ResponseBody
    @RequestMapping(value = "/getDetail",method = RequestMethod.POST)
    public ResultVO getDetail(@RequestBody Tr_Non_BusinessVO vo) {
        if(vo.getId()==null){
            return ResultVO.failResult("无效的ID");
        }
        Tr_Non_BusinessDTO tr_non_BusinessDTO = non_businessService.getDetail(vo);
        if(tr_non_BusinessDTO == null){
            return ResultVO.failResult("未查询到任何数据");
        }
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(tr_non_BusinessDTO);
        return resultVO;
    }

    @Log(value = "业务数据--保存")
    @RequestMapping(value="saveDetail",method = RequestMethod.POST)
    public @ResponseBody ResultVO saveDetail(HttpServletRequest request) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        MultipartFile commonsMultipartFile =  multiRequest.getFile("contract_file");
        String jsonData = request.getParameter("jsonData");
        Tr_Non_BusinessDTO requestTr_Non_BusinessDTO = JSONUtils.toJSONObject(Tr_Non_BusinessDTO.class, jsonData);
        ResultVO resultVO = non_businessService.saveDetail(requestTr_Non_BusinessDTO, sysUserVO,commonsMultipartFile);
        return resultVO;
    }

    @Log(value = "业务数据-批量注销")
    @RequestMapping("/batchCancel")
    @ResponseBody
    public ResultVO batchDelete(String jsonData, HttpSession session) {
        if (jsonData == null || jsonData.length() == 0) {
            return ResultVO.failResult("要删除的数据为空!");
        }
        List<Tr_Non_BusinessVO> tr_Non_BusinessVOs = JSONUtils.toJSONObjectList(Tr_Non_BusinessVO.class, jsonData);
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = non_businessService.batchCancel(tr_Non_BusinessVOs,sysUserVO);
        return resultVO;
    }

    @Log(value = "业务数据-标识数据")
    @RequestMapping(value = "/updateJsState", method = RequestMethod.POST)
    @ResponseBody
    public ResultVO UpdateJsState(@RequestBody List<Tr_Non_BusinessVO> list, HttpSession session) {
        List<Tr_Non_BusinessVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<Tr_Non_BusinessVO>>(){}.getType());
        return non_businessService.updateJsState(vos);
    }

    @Log(value = "业务数据-审核")
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    @ResponseBody
    public ResultVO check(@RequestBody List<Tr_Non_BusinessVO> list, HttpSession session) {
        List<Tr_Non_BusinessVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<Tr_Non_BusinessVO>>(){}.getType());
        SysUserVO sysUserVO = SessionUtils.currentSession();
        return non_businessService.check(vos,sysUserVO);
    }

    @Log(value = "业务数据-导入数据")
    @RequestMapping("/importData")
    @ResponseBody
    public ResultVO importData(@RequestParam("fileList") MultipartFile file) throws Exception {
        ResultVO resultVO = non_businessService.importData(file, BusinessData_projectEnum.COMMODITY_CAR);
        return resultVO;
    }

    @Log(value = "业务数据--下载导入模板")
    @RequestMapping("exportTemplate")
    @ResponseBody
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response)
            throws IOException, InvalidFormatException {
        POIUtils.exportTemplate(request, response, "非商品车导入模板",
                "Resource/excel/business/non_business.xlsx");
    }

    /***
     * 下载上传文件
     * @param request
     * @param response
     * @param dataPath
     * @throws IOException
     * @throws InvalidFormatException
     */
    @RequestMapping("downloadFile")
    public @ResponseBody void downloadFile(HttpServletRequest request, HttpServletResponse response, String dataPath) throws IOException, InvalidFormatException {
        POIUtils.downloadFile(request, response,"上传文件", dataPath);
    }

}