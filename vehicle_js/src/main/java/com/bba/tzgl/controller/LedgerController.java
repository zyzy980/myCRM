package com.bba.tzgl.controller;

import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.settlement.vo.Js_Vin_Down_PremiumVO;
import com.bba.tzgl.dto.LedgerDTO;
import com.bba.tzgl.service.api.ILedgerService;
import com.bba.tzgl.service.api.ILedger_DetailService;
import com.bba.tzgl.vo.LedgerVO;
import com.bba.tzgl.vo.Ledger_DetailVO;
import com.bba.util.*;
import com.bba.xtgl.vo.SysUserVO;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.ClassUtils;
/**
 * 台账管理
 * @Author:Suwendaidi
 * @Date: 2019/7/24 11:51
 */

@RestController
@RequestMapping("/tzgl/LedgerController")
public class LedgerController {

    @Autowired
    private ILedgerService iledgerService;

    @Autowired
    private ILedger_DetailService iledger_DetailService;

    @Log(value = "台账管理-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        PageVO pageVO = iledgerService.getListForGrid(jqGridParamModel,customSearchFilters);
        return pageVO;
    }

    @Log(value = "台账管理-保存")
    @RequestMapping("saveDetail")
    public ResultVO saveDetail(@RequestBody LedgerDTO requestLedgerDTO) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = iledgerService.saveDetail(requestLedgerDTO, sysUserVO);
        return resultVO;
    }

    @Log(value = "台账管理-审核")
    @RequestMapping("check")
    public ResultVO check(LedgerVO ledgerVO) {
        if(StringUtils.isBlank(ledgerVO.getSheet_no())){
            return ResultVO.failResult("先保存后在进行操作");
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = iledgerService.check(ledgerVO, sysUserVO);
        return resultVO;
    }

    @Log(value = "台账管理-反审核")
    @RequestMapping("uncheck")
    public ResultVO uncheck(LedgerVO ledgerVO) {
        if(StringUtils.isBlank(ledgerVO.getSheet_no())){
            return ResultVO.failResult("先保存后在进行操作");
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = iledgerService.uncheck(ledgerVO, sysUserVO);
        return resultVO;
    }

    @Log(value = "台账管理-注销")
    @RequestMapping("cancel")
    public ResultVO cancel(LedgerVO ledgerVO) {
        if(StringUtils.isBlank(ledgerVO.getSheet_no())){
            return ResultVO.failResult("先保存后在进行操作");
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = iledgerService.cancel(ledgerVO, sysUserVO);
        return resultVO;
    }

    @Log(value = "台账管理-明细查询")
    @RequestMapping("getDetail")
    public ResultVO getDetail(LedgerVO ledgerVO) {
        if(StringUtils.isBlank(ledgerVO.getSheet_no())){
            return ResultVO.failResult("台账单号不能为空");
        }
        LedgerDTO returnLedgerDTO = iledgerService.getDetail(ledgerVO);
        if(returnLedgerDTO == null){
            return ResultVO.failResult("该台账未查询到任何数据");
        }
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(returnLedgerDTO);
        return resultVO;
    }

    @Log(value = "台账管理-查询结算批次下拉框")
    @RequestMapping(value = "/findJsProject", method = {RequestMethod.GET,RequestMethod.POST})
    public List<Ledger_DetailVO> findJsProject(Ledger_DetailVO vo){
        List<Ledger_DetailVO> list = iledger_DetailService.findJsProject(vo);
        return list;
    }

    @Log(value = "台账管理-查询结算批次下拉框(客户)")
    @RequestMapping(value = "/findCusJsProject", method = {RequestMethod.GET,RequestMethod.POST})
    public List<LedgerVO> findCusJsProject(LedgerVO vo){
        List<LedgerVO> list = iledgerService.findCusJsProject(vo);
        return list;
    }

    @Log(value = "台账管理-添加月保费")
    @RequestMapping(value = "/addPremium", method = RequestMethod.POST)
    public ResultVO addPremium(@RequestBody List<Js_Vin_Down_PremiumVO> list, HttpSession session) {
        List<Js_Vin_Down_PremiumVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<Js_Vin_Down_PremiumVO>>(){}.getType());
        return iledgerService.addPremium(vos);
    }

    @Log(value = "台账管理-批量删除保费")
    @RequestMapping("/batchRrmovePremium")
    public ResultVO batchDelete(String jsonData) {
        if (jsonData == null || jsonData.length() == 0) {
            return ResultVO.failResult("要删除的数据为空");
        }
        List<Ledger_DetailVO> ledger_detailVOs = JSONUtils.toJSONObjectList(Ledger_DetailVO.class, jsonData);
        ResultVO resultVO = iledgerService.batchRrmovePremium(ledger_detailVOs);
        return resultVO;
    }

    @Log(value = "台账管理-生成发票")
    @RequestMapping("createInvoice")
    public ResultVO createInvoice(@RequestBody List<LedgerVO> list) {
        List<LedgerVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<LedgerVO>>(){}.getType());
        SysUserVO sysUserVO = SessionUtils.currentSession();
        return iledgerService.createInvoice(vos,sysUserVO);
    }

    @Log(value = "台账管理-根据模板导出")
    @RequestMapping(value ="/exportExcel")
    public void exportExcel(LedgerVO vo,HttpServletResponse response,HttpServletRequest request) throws Exception{
        if(null==vo) {
            response.getWriter().write("请选择需要导出的数据。");
            return ;
        }
        if(StringUtils.isBlank(vo.getSheet_no())){
            response.getWriter().write("请选择需要导出的数据。");
            return ;
        }
        String[] sheet_no_array=vo.getSheet_no().split(",");
        List<LedgerDTO> ledgerDTOList = new ArrayList<>();
        for(String item:sheet_no_array){
            LedgerVO requestLedgerVO = new LedgerVO();
            requestLedgerVO.setSheet_no(item);
            LedgerDTO returnLedgerDTO = iledgerService.getDetail(requestLedgerVO);
            ledgerDTOList.add(returnLedgerDTO);
        }
        //根据模板excel,创建新附件
        SysUserVO sysUserVO = SessionUtils.currentSession();
        exportExcels(ledgerDTOList,sysUserVO,response,request);
    }

    public ResultVO exportExcels (List<LedgerDTO> LedgerDTOList,SysUserVO sysUserVO,HttpServletResponse response,HttpServletRequest request) throws Exception {
        try {
            //根据模板excel,创建新附件
            String url = "Resource/excel/ledger/Ledger_export.xlsx";
            InputStream fis = ClassUtils.class.getClassLoader().getResourceAsStream("static/"+url);
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            Sheet sheet =  wb.getSheetAt(0);
            //单位名称，后面看是否需要判断只允许导出一家的台账
            String cusName = LedgerDTOList.get(0).getLedgerVO().getCus_name();
            //合同号
            String contract_no = LedgerDTOList.get(0).getLedgerVO().getContract_no();
            //应收运费总合计(含税)
            BigDecimal tax_up_total = new BigDecimal(0);
            //税额总
            BigDecimal tax_amount_total = new BigDecimal(0);
            //应付运费总
            BigDecimal tax_down_total = new BigDecimal(0);
            //不含税应付总
            BigDecimal ntax_down_total = new BigDecimal(0);
            //1、设置单位名称
            Row row = sheet.getRow(2);
            row.getCell(0).setCellValue("单位名称："+cusName);
            //2、设置合同号
            row.getCell(9).setCellValue(contract_no);
            //循环开始
            //创建单元格样式
            CellStyle xlsxStyle = POIUtils.getTextStype(wb);
            CellStyle numDoublexlsxStyle =POIUtils.getNumDoubleStype(wb);
            Font font = wb.createFont();
            font.setFontName("微软雅黑");
            font.setFontHeightInPoints((short)10);
            xlsxStyle.setFont(font);
            numDoublexlsxStyle.setFont(font);
            Integer ColumnNumber=0;
            Integer size=0;
            Integer num=1;
            int start_row = 5;
            int end_row = 4;
            for (int i = 0; i < LedgerDTOList.size(); i++) {
                LedgerVO ledgerVO = LedgerDTOList.get(i).getLedgerVO();
                List<Ledger_DetailVO> ledger_detailVOList = LedgerDTOList.get(i).getLedger_DetailVOList();
                //如果合同号存在多个，填入到第二个合同栏，有个弊端：后边的合同都只会在第二栏
                if (StringUtils.notEquals(ledgerVO.getContract_no(),contract_no)) {
                    row = sheet.getRow(3);
                    row.getCell(9).setCellValue(ledgerVO.getContract_no());
                }
                for (int j = 0; j < ledger_detailVOList.size(); j++) {
                    //首先排除保费信息
                    if (StringUtils.notEquals(ledger_detailVOList.get(j).getPremium_flag(),"Y")) {
                        //写入台账信息汇总，结算号等
                        sheet.shiftRows(size+5,sheet.getLastRowNum(),1,true,false);
                        Row newRow = sheet.createRow(size+5);
                        newRow.setHeightInPoints(30);
                        if (j == 0) {
                            //合并单元格起始行
                            start_row = start_row+size;
                            ColumnNumber=0;
                            //序号
                            POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,String.valueOf(num));
                            ColumnNumber++;
                            //结算项目
                            POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,ledgerVO.getJs_project());
                            ColumnNumber++;
                            //结算号
                            POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,ledgerVO.getJs_no());
                            ColumnNumber++;
                            //应收运费RMB
                            POIUtils.CreateNumCell(newRow,numDoublexlsxStyle,ColumnNumber,Double.parseDouble(ledgerVO.getTax_up_total().toString()));
                            tax_up_total = tax_up_total.add(ledgerVO.getTax_up_total());
                            ColumnNumber++;
                            //税额
                            POIUtils.CreateNumCell(newRow,numDoublexlsxStyle,ColumnNumber,Double.parseDouble(ledgerVO.getTax_amount().toString()));
                            tax_amount_total = tax_amount_total.add(ledgerVO.getTax_amount());
                            ColumnNumber++;
                            //发票号
                            POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,ledgerVO.getInvoice_no());
                            ColumnNumber++;
                            //对下结算对象
                            POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,ledger_detailVOList.get(j).getCarrier_name());
                            ColumnNumber++;
                            //应付费用含税
                            POIUtils.CreateNumCell(newRow,numDoublexlsxStyle,ColumnNumber,Double.parseDouble(ledger_detailVOList.get(j).getTax_down_total().toString()));
                            tax_down_total = tax_down_total.add(ledger_detailVOList.get(j).getTax_down_total());
                            ColumnNumber++;
                            //应付费用不含税
                            POIUtils.CreateNumCell(newRow,numDoublexlsxStyle,ColumnNumber,Double.parseDouble(ledger_detailVOList.get(j).getNtax_down_total().toString()));
                            ntax_down_total = ntax_down_total.add(ledger_detailVOList.get(j).getNtax_down_total());
                            ColumnNumber++;
                            //税率
                            POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,(ledger_detailVOList.get(j).getTax_rate().multiply(new BigDecimal(100))).stripTrailingZeros().toPlainString()+"%");
                            ColumnNumber++;
                            //利润
                            POIUtils.CreateNumCell(newRow,numDoublexlsxStyle,ColumnNumber,Double.parseDouble(ledgerVO.getNot_tax_profit().toString()));
                            num++;
                        } else {
                            ColumnNumber=0;
                            //序号
                            POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,"");
                            ColumnNumber++;
                            //结算项目 --保费需要显示
                            POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,"");
                            ColumnNumber++;
                            //结算号 --保费需要显示
                            POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,"");
                            ColumnNumber++;
                            //应收运费RMB
                            POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,"");
                            ColumnNumber++;
                            //税额
                            POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,"");
                            ColumnNumber++;
                            //发票号
                            POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,"");
                            ColumnNumber++;
                            //对下结算对象
                            POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,ledger_detailVOList.get(j).getCarrier_name());
                            ColumnNumber++;
                            //应付费用含税
                            POIUtils.CreateNumCell(newRow,numDoublexlsxStyle,ColumnNumber,Double.parseDouble(ledger_detailVOList.get(j).getTax_down_total().toString()));
                            tax_down_total = tax_down_total.add(ledger_detailVOList.get(j).getTax_down_total());
                            ColumnNumber++;
                            //应付费用不含税
                            POIUtils.CreateNumCell(newRow,numDoublexlsxStyle,ColumnNumber,Double.parseDouble(ledger_detailVOList.get(j).getNtax_down_total().toString()));
                            ntax_down_total = ntax_down_total.add(ledger_detailVOList.get(j).getNtax_down_total());
                            ColumnNumber++;
                            //税率
                            POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,(ledger_detailVOList.get(i).getTax_rate().multiply(new BigDecimal(100))).stripTrailingZeros().toPlainString()+"%");
                            ColumnNumber++;
                            //利润
                            POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,"");
                        }
                        size++;
                        end_row++;
                    }
                }
                //插入对下保费信息
                for(int x = 0; x < ledger_detailVOList.size(); x++) {
                    if (StringUtils.equals(ledger_detailVOList.get(x).getPremium_flag(),"Y")) {
                        sheet.shiftRows(size+5,sheet.getLastRowNum(),1,true,false);
                        Row newRow = sheet.createRow(size+5);
                        newRow.setHeightInPoints(30);
                        ColumnNumber=0;
                        //序号
                        POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,String.valueOf(num));
                        ColumnNumber++;
                        //结算项目
                        POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,ledger_detailVOList.get(x).getJs_project());
                        ColumnNumber++;
                        //结算号
                        POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,ledger_detailVOList.get(x).getJs_no());
                        ColumnNumber++;
                        //应收运费RMB
                        POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,"");
                        ColumnNumber++;
                        //税额
                        POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,"");
                        ColumnNumber++;
                        //发票号
                        POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,"");
                        ColumnNumber++;
                        //对下结算对象
                        POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,ledger_detailVOList.get(x).getCarrier_name());
                        ColumnNumber++;
                        //应付费用含税
                        POIUtils.CreateNumCell(newRow,numDoublexlsxStyle,ColumnNumber,Double.parseDouble(ledger_detailVOList.get(x).getTax_down_total().toString()));
                        ColumnNumber++;
                        //应付费用不含税
                        POIUtils.CreateNumCell(newRow,numDoublexlsxStyle,ColumnNumber,Double.parseDouble(ledger_detailVOList.get(x).getNtax_down_total().toString()));
                        ColumnNumber++;
                        //税率
                        POIUtils.CreateCell(newRow,xlsxStyle,ColumnNumber,(ledger_detailVOList.get(x).getTax_rate().multiply(new BigDecimal(100))).stripTrailingZeros().toPlainString()+"%");
                        ColumnNumber++;
                        //利润
                        POIUtils.CreateNumCell(newRow,numDoublexlsxStyle,ColumnNumber,Double.parseDouble(ledgerVO.getNot_tax_profit().toString()));
                        num++;
                        size++;
                    }
                }
                /**合并单元格*/
                if (start_row<end_row) {
                    POIUtils.mergeCell(sheet,start_row,end_row,10,10);
                }
            }
            //设置合计
            row = sheet.getRow(5+size);
            row.getCell(3).setCellValue(tax_up_total.toString());
            row.getCell(4).setCellValue(tax_amount_total.toString());
            row.getCell(7).setCellValue(tax_down_total.toString());
            row.getCell(8).setCellValue(ntax_down_total.toString());
            //制表
            row = sheet.getRow(7+size);
            row.getCell(10).setCellValue(sysUserVO.getRealName());
            //日期
            row = sheet.getRow(8+size);
            row.getCell(10).setCellValue(DateUtils.nowDate("yyyy-MM-dd"));

            //导出
            response.setCharacterEncoding("UTF-8");
            String fileName = "台账";
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859_1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            response.setCharacterEncoding("UTF-8");
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            try (OutputStream out = response.getOutputStream()) {
                wb.write(out);
            }catch (Exception e){
                e.printStackTrace();
            }
            wb.close();
            wb=null;
        } catch (Exception e) {
            return ResultVO.failResult("导出台账失败，原因："+e.getMessage());
        }
        return ResultVO.successResult("导出台账成功");
    }

}
