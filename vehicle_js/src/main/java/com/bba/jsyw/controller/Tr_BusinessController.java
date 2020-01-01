package com.bba.jsyw.controller;
import com.bba.common.constant.BusinessData_projectEnum;
import com.bba.common.constant.UserLevelEnum;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.jsyw.dto.Tr_BusinessDTO;
import com.bba.jsyw.service.api.ITr_BusinessService;
import com.bba.jsyw.vo.Tr_BusinessVO;
import com.bba.util.*;
import com.bba.xtgl.vo.SysUserVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;

/**
 * 业务数据管理
 *@Author:Suwendaidi
 *2019/7/11
 */
@Controller
@RequestMapping("jsyw/business")
public class Tr_BusinessController {
    @Autowired
    private ITr_BusinessService businessService;

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
        if (sysUserVO.getUserLevel().equals(UserLevelEnum.USERLEVEL_FXY.getCode())) {
            //风险员
            jqGridParamModel.put("data_state", "eq", "0");
        } else if (sysUserVO.getUserLevel().equals(UserLevelEnum.USERLEVEL_YWY.getCode())) {
            //业务员
            jqGridParamModel.put("mass_loss_type", "eq", "0");
            jqGridParamModel.put("data_state", "eq", "1");
        }
        PageVO pageVO = businessService.findBusinessPageVO(jqGridParamModel, customSearchFilters);
        return pageVO;
    }

    @Log(value = "业务数据-查询明细")
    @ResponseBody
    @RequestMapping(value = "/getDetail",method = RequestMethod.POST)
    public ResultVO getDetail(@RequestBody Tr_BusinessVO vo) {
        if(vo.getId()==null){
            return ResultVO.failResult("无效的ID");
        }
        Tr_BusinessDTO tr_BusinessDTO = businessService.getDetail(vo);
        if(tr_BusinessDTO == null){
            return ResultVO.failResult("未查询到任何数据");
        }
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(tr_BusinessDTO);
        return resultVO;
    }

    @Log(value = "业务数据--保存")
    @RequestMapping(value="saveDetail",method = RequestMethod.POST)
    public @ResponseBody ResultVO saveDetail(HttpServletRequest request) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        MultipartFile commonsMultipartFile =  multiRequest.getFile("contract_file");
        String jsonData = request.getParameter("jsonData");
        Tr_BusinessDTO requestTr_BusinessDTO = JSONUtils.toJSONObject(Tr_BusinessDTO.class, jsonData);
        ResultVO resultVO = businessService.saveDetail(requestTr_BusinessDTO, sysUserVO,commonsMultipartFile);
        return resultVO;
    }

    @Log(value = "业务数据-批量注销")
    @RequestMapping("/batchCancel")
    @ResponseBody
    public ResultVO batchDelete(String jsonData, HttpSession session) {
        if (jsonData == null || jsonData.length() == 0) {
            return ResultVO.failResult("要删除的数据为空!");
        }
        List<Tr_BusinessVO> tr_BusinessVOs = JSONUtils.toJSONObjectList(Tr_BusinessVO.class, jsonData);
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = businessService.batchCancel(tr_BusinessVOs,sysUserVO);
        return resultVO;
    }

    @Log(value = "业务数据-标识数据")
    @RequestMapping(value = "/updateJsState", method = RequestMethod.POST)
    @ResponseBody
    public ResultVO UpdateJsState(@RequestBody List<Tr_BusinessVO> list) {
        List<Tr_BusinessVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<Tr_BusinessVO>>(){}.getType());
        return businessService.updateJsState(vos);
    }

    @Log(value = "业务数据-审核")
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    @ResponseBody
    public ResultVO check(@RequestBody List<Tr_BusinessVO> list) {
        List<Tr_BusinessVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<Tr_BusinessVO>>(){}.getType());
        SysUserVO sysUserVO = SessionUtils.currentSession();
        return businessService.check(vos,sysUserVO);
    }

    @Log(value = "业务数据-导入数据")
    @RequestMapping("/importData")
    @ResponseBody
    public ResultVO importData(@RequestParam("fileList") MultipartFile file) throws Exception {
        ResultVO resultVO = businessService.importData(file, BusinessData_projectEnum.COMMODITY_CAR);
        return resultVO;
    }

    @Log(value = "业务数据--下载导入模板")
    @RequestMapping("exportTemplate")
    @ResponseBody
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response)
            throws IOException, InvalidFormatException {
        POIUtils.exportTemplate(request, response, "业务数据导入模板",
                "Resource/excel/business/业务数据导入模板.xlsx");
    }

    @Log(value = "业务数据-多承运商-导入数据")
    @RequestMapping("importMoreCarrier")
    public ResultVO importMoreCarrier(@RequestParam("fileList") MultipartFile file) throws Exception {
        ResultVO resultVO = businessService.importMoreCarrier(file);
        return resultVO;
    }

    @Log(value = "业务数据-多承运商-下载导入明细模板")
    @RequestMapping("exportMoreCarrier")
    public void exportMoreCarrier(HttpServletRequest request, HttpServletResponse response)
            throws IOException, InvalidFormatException {
        POIUtils.exportTemplate(request, response, "多承运商导入模板",
                "Resource/excel/business/more_carrier.xlsx");
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
        downloadFile(request, response,"原始文件", dataPath);
    }

    /**
     * 功能描述: 下载文件
     * @param dataPath 文件路径
     * @param fileName 文件名称
     * @return:
     * @auther: laoli
     * @date: 2019/1/7 14:17
     */
    public static void downloadFile(HttpServletRequest request, HttpServletResponse response, String fileName, String dataPath) throws IOException, InvalidFormatException{
        fileName = new String(fileName.getBytes("GBK"), "ISO8859_1");
        response.setCharacterEncoding("UTF-8");

        String fileWith = dataPath.substring(dataPath.lastIndexOf(".")+1);
        if(fileWith.equals("txt")){
            response.setContentType("application/octet-stream");
            FileInputStream ins=new FileInputStream(dataPath);
            //InputStream ins = ClassUtils.class.getClassLoader().getResourceAsStream(dataPath);
            byte[] buffer = new byte[ins.available()];
            ins.read(buffer);
            ins.close();

            File file = new File(dataPath);
            response.setContentType("text/plain");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ".txt");
            response.addHeader("Content-Length", "" + file.length());

            try (OutputStream ous = new BufferedOutputStream(response.getOutputStream());){
                ous.write(buffer);
            } catch (Exception e) {
                throw e;
            }
        }else if(fileWith.equals("xls") || fileWith.equals("xlsx")){
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + "." + fileWith);
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            FileInputStream fis = new FileInputStream(dataPath);
            Workbook wb = WorkbookFactory.create(fis);
            try (OutputStream out = response.getOutputStream();){
                wb.write(out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // dataPath 文件路径
            // fileName 文件名称
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition","attachment;filename=" + fileName + "." + fileWith);
            OutputStream outp = response.getOutputStream();
            FileInputStream in =  new FileInputStream(dataPath);
            byte[] b = new byte[1024];
            int i = 0;
            while((i = in.read(b)) > 0)  {
                outp.write(b, 0, i);
            }
            outp.flush();
            in.close();
            outp.close();
        }
    }

}