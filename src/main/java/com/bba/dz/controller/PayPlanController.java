package com.bba.dz.controller;

import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.dz.service.api.IPayPlanService;
import com.bba.dz.vo.Tr_Payment_PlanVO;
import com.bba.util.*;
import com.bba.xtgl.vo.SysUserVO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LTJ
 * @since 2019-08-23
 */
@RestController
@RequestMapping("/dz/payplan")
public class PayPlanController {

    @Autowired
    private IPayPlanService iPayPlanService;

    @Log(value = "结算管理-对上结算管理-清单查询")
    @RequestMapping("/getListForGrid")
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        //String receipt_date=DateUtils.dateFormat(DateUtils.getDateAddByMonth(DateUtils.parseDate(DateUtils.nowDate("yyyy-MM-dd"),"yyyy-MM-dd"),-3),"yyyy-MM-dd");
        //jqGridParamModel.put("receipt_date","le",receipt_date);
       return iPayPlanService.getListForGrid(jqGridParamModel,customSearchFilters);
    }



    //LTJ:2019-07-22 业务审核-商务审核-财务审核
    // 0：管理员；1：操作员；2：商务人员；3：财务人员；4：其它；
    // 数据状态：0.正常  1.业务审核.2商务审核.3.财务审核.4.发送邮件.5.客户确认
    @Log(value = "结算管理-对上账单管理-审核数据")
    @RequestMapping(value = "data_check", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultVO data_check(@RequestBody Tr_Payment_PlanVO vo, HttpServletRequest request, HttpServletResponse response) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        String id = ENDECodeUtils.URLDecode(vo.getId());
        String[] id_array = id.split(",");
        List<Tr_Payment_PlanVO> list = new ArrayList<Tr_Payment_PlanVO>();
        for (String item : id_array) {
            Tr_Payment_PlanVO info = new Tr_Payment_PlanVO();
            info.setId(item);
            list.add(info);
        }
        //验证状态
        List<Tr_Payment_PlanVO> datalist = iPayPlanService.findListByProperty(list);
        if (null == datalist || datalist.size() <= 0) {
            return ResultVO.failResult("没有查询到选中数据，原因：数据已被其他用户删除或处理，请查询数据后再操作。");
        }
        if (datalist.size() != list.size()) {
            return ResultVO.failResult("有部份数据已被其他用户处理，请查询数据后再操作。");
        }
        String error = "";
        String receipt_error="";
        for (Tr_Payment_PlanVO item : datalist) {
            if("2".equals(vo.getState())) {//提报
                if (!"0".equals(item.getState())) {
                    error += item.getVin() + ",";
                }
                /*else {
                    //收车日期(M+3) 才可以提报，
                    if(StringUtils.isBlank(item.getReceipt_date()))
                    {
                        receipt_error+="选中VIN("+item.getVin()+")收车日期为空;";
                    }else {
                        Date receipt_date = DateUtils.parseDate(item.getReceipt_date(),"yyyy-MM-dd");
                        Date now_date=DateUtils.parseDate(DateUtils.nowDate("yyyy-MM-dd"),"yyyy-MM-dd");
                        now_date=DateUtils.getDateAddByMonth(now_date,3);
                        int iEq=DateUtils.compareDate(receipt_date,now_date);
                        if(iEq>0)
                        {
                            receipt_error+="选中VIN("+item.getVin()+")收车日期加3个月要大于或等于当前系统日期;";
                        }
                    }
                }*/
            }else if("3".equals(vo.getState()))
            {//审核
                if (!"2".equals(item.getState())) {
                    error += item.getVin() + ",";
                }
                /*else{
                    //收车日期(M+4)，才可以审核
                    if(StringUtils.isBlank(item.getReceipt_date()))
                    {
                        receipt_error="选中VIN("+item.getVin()+")收车日期为空;";
                    }else{
                        Date receipt_date = DateUtils.getDateAddByMonth(DateUtils.parseDate(item.getReceipt_date(),"yyyy-MM-dd"),4);
                        Date now_date=DateUtils.parseDate(DateUtils.nowDate("yyyy-MM-dd"),"yyyy-MM-dd");
                        if(DateUtils.compareDate(receipt_date,now_date)>0)
                        {
                            receipt_error+="选中VIN("+item.getVin()+")收车日期加4个月要大于或等于当前系统日期;";
                        }
                    }
                }
                */
            }else if("1".equals(vo.getState())){
                //撤回 - 暂时没启用
                if (!"2".equals(item.getState()) && !"3".equals(item.getState())) {
                    error += item.getVin() + ",";
                }
            }
        }
        datalist = null;
        if (StringUtils.isNotBlank(error)) {
            return ResultVO.failResult("选中数据状态已发生变化(数据已被其他用户处理)，请查询数据后再操作。单号：" + error);
        }
        if(StringUtils.isNotBlank(receipt_error))
        {
            return ResultVO.failResult(receipt_error);

        }

        String CurrentDate=DateUtils.nowDate();
        for(Tr_Payment_PlanVO item:list)
        {
            item.setState(vo.getState());
            item.setOperator_by(sysUserVO.getUserId());
            item.setOperator_date(CurrentDate);
        }

        iPayPlanService.update(list);

        return ResultVO.successResult("OK");
    }



    @Log(value = "报表管理-付款计划-导出数据")
    @RequestMapping("exportData")
    public void exportData(Tr_Payment_PlanVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (null == vo) {
            response.getWriter().write("请选择需要导出的数据。");
            return;
        }
        if (StringUtils.isBlank(vo.getId())) {
            response.getWriter().write("请选择需要导出的数据。");
            return;
        }

        String[] id_array = vo.getId().split(",");
        List<Tr_Payment_PlanVO> list = new ArrayList<Tr_Payment_PlanVO>();
        for (String item : id_array) {
            Tr_Payment_PlanVO info = new Tr_Payment_PlanVO();
            info.setId(item);
            list.add(info);

        }
        list=iPayPlanService.findListByProperty(list);
        if (null==list || list.size()<=0) {
            response.getWriter().write("没有查询到数据，原因：数据已被其他用户删除或处理，请查询数据后，再操作导出。");
            return;
        }

        List<Tr_Payment_PlanVO> groupList = iPayPlanService.findSumDataList_expect(list);
        CreateFileExcel(list,groupList, request, response);

    }

    //生成Excel文件
    private ResultVO CreateFileExcel(List<Tr_Payment_PlanVO> list, List<Tr_Payment_PlanVO> groupList, HttpServletRequest request, HttpServletResponse response)
    {
        ResultVO resultVO=new ResultVO();
        resultVO.setResultCode("1");
        String separator= File.separator;
        try {
            //往Excel文件中写入数据 开始
            String url = "static/Resource/excel/baobiao/payplan_template.xlsx";
            InputStream fis = ClassUtils.class.getClassLoader().getResourceAsStream(url);
            Workbook wb = WorkbookFactory.create(fis);

            //排序 倒序
            list.sort((Tr_Payment_PlanVO f, Tr_Payment_PlanVO t) -> Integer.valueOf(t.getId()) - Integer.valueOf(f.getId()));

            //创建单元格样式
            CellStyle xlsxStyle = wb.createCellStyle();
            xlsxStyle.setBorderLeft(BorderStyle.THIN);
            xlsxStyle.setBorderTop(BorderStyle.THIN);
            xlsxStyle.setBorderRight(BorderStyle.THIN);
            xlsxStyle.setBorderBottom(BorderStyle.THIN);
            xlsxStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            xlsxStyle.setAlignment(HorizontalAlignment.CENTER);

            Font font = wb.createFont();
            font.setFontName("微软雅黑");
            font.setFontHeightInPoints((short) 10);
            xlsxStyle.setFont(font);

            //共 4 个工作簿 （汇总/商品车/非商品车/补差）

            //第 1 个工作簿   汇总
            //获取月份
            String data_format = DateUtils.getDate("yyyyMMdd");
            String now_year = data_format.substring(2,4);
            String now_month = data_format.substring(4,6).replace("0","");
            Integer ColumnNumber = 0;
            Integer iRow=0;
            Sheet sheet_group = wb.getSheetAt(0);
            wb.setSheetName(0,now_month+"月");
            Cell title_cell = sheet_group.getRow(0).getCell(0); //6月付款汇总表
            title_cell.setCellValue(now_month+"月付款汇总表");
            Cell year_cell = sheet_group.getRow(1).getCell(5); //2019年
            year_cell.setCellValue(now_year+"年");
            Cell month_cell = sheet_group.getRow(1).getCell(5); //2019年
            month_cell.setCellValue(now_month+"月");
            Double total =0.0;
            for (int i = 0; i < groupList.size(); i++) {
                Tr_Payment_PlanVO vo = groupList.get(i);
                Row row = sheet_group.createRow(iRow + 4);
                iRow++;
                ColumnNumber = 0;
                CreateCell(row, xlsxStyle, ColumnNumber,"");
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber,"整车运输课");
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber,"下包商支付");
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber,vo.getData_type().equals("0")?"正常付款":(vo.getData_type().equals("1")?"预付款":"冲预付"));
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber,vo.getCarrier_name());//下包商单位
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber,"");
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber,"");
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber,String.valueOf(vo.getTax_amount()));
                total+=vo.getTax_amount();
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber,"");
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber,"");
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber,"");
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber,vo.getRemark());//备注
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber,vo.getInvoice_no());//发票
            }
            //底部统计
            Row rowTotal = sheet_group.createRow(groupList.size()+4);
            ColumnNumber=0;
            CreateCell(rowTotal, xlsxStyle, ColumnNumber,"合计");
            ColumnNumber++;
            CreateCell(rowTotal, xlsxStyle, ColumnNumber,"");
            ColumnNumber++;
            CreateCell(rowTotal, xlsxStyle, ColumnNumber,"");
            ColumnNumber++;
            CreateCell(rowTotal, xlsxStyle, ColumnNumber,"");
            ColumnNumber++;
            CreateCell(rowTotal, xlsxStyle, ColumnNumber,"");
            ColumnNumber++;
            CreateCell(rowTotal, xlsxStyle, ColumnNumber,"");
            ColumnNumber++;
            CreateCell(rowTotal, xlsxStyle, ColumnNumber,"");
            ColumnNumber++;
            CreateCell(rowTotal, xlsxStyle, ColumnNumber,String.valueOf(total));
            ColumnNumber++;
            CreateCell(rowTotal, xlsxStyle, ColumnNumber,"");
            ColumnNumber++;
            CreateCell(rowTotal, xlsxStyle, ColumnNumber,"");
            ColumnNumber++;
            CreateCell(rowTotal, xlsxStyle, ColumnNumber,"");
            ColumnNumber++;
            CreateCell(rowTotal, xlsxStyle, ColumnNumber,"");
            ColumnNumber++;
            CreateCell(rowTotal, xlsxStyle, ColumnNumber,"");

            //TYPE = 0 商品车 1非商品车 2 补差
            //第 2 个工作簿   0=商品车
            iRow=0;
            Sheet sheet = wb.getSheetAt(1);
            for (int i = 0; i < list.size(); i++) {
                if (!"0".equals(list.get(i).getType()))
                    continue;

                Row row = sheet.createRow(iRow + 1);
                iRow++;
                ColumnNumber = 0;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getId());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getVdr_no());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getVin());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getBegin_date());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getReceipt_date());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getTrans_mode());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getBegin_address());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getEnd_address());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getDealer_name());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getCar_type());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getCarrier_name());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getDown_price().toString());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getTax_amount().toString());
            }

            //第 3 个工作簿   1=非商品车
            iRow=0;
            Sheet sheet_non = wb.getSheetAt(2);
            for (int i = 0; i < list.size(); i++) {
                if (!"1".equals(list.get(i).getType()))
                    continue;


                Row row = sheet_non.createRow(iRow + 1);
                iRow++;
                ColumnNumber = 0;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getId());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getHandover_no());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getBegin_date());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getReceipt_date());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getReceipt_sheet_date());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getTrans_mode());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getBegin_address());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getEnd_address());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getCar_type());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getChengyun_qty().toString());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getCarrier_name());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getInvoice_tax().toString());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getNot_tax_amount().toString());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getTax_amount().toString());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getRemark());
            }

            //第 4 个工作簿   2=补差
            iRow=0;
            Sheet sheet_ca = wb.getSheetAt(3);
            for (int i = 0; i < list.size(); i++) {
                if (!"2".equals(list.get(i).getType()))
                    continue;

                Row row = sheet_ca.createRow(iRow + 1);
                iRow++;
                ColumnNumber = 0;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getId());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getBegin_date());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getReceipt_date());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getCarrier_name());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getVdr_no());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getVin());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getTrans_mode());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getBegin_address());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getEnd_address());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getDealer_name());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getCar_type());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getNot_tax_amount().toString());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getTax_amount().toString());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getNot_tax_ht_amount().toString());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getTax_ht_amount().toString());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getNot_tax_difference().toString());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getTax_difference().toString());
            }


            //输出流
            String SaveFileName = new String("付款计划.xlsx".getBytes("GBK"), "ISO8859_1");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + SaveFileName);
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            try (OutputStream out = response.getOutputStream()) {
                wb.write(out);
            } catch (Exception e) {
                e.printStackTrace();
            }


            wb.close();
            wb = null;
        } catch (Exception e) {
            resultVO.setResultDataFull("写入Excel文件出错，原因："+e.getMessage());
            return resultVO;
        }

        //写入文件成功，返回 Excel 路径
        resultVO.setResultCode("0");
        return resultVO;
    }

    private void CreateCell(Row row,CellStyle xlsxStyle,Integer ColumnNumber,String value)
    {
        Cell cell_begin_city = row.createCell(ColumnNumber);
        if(null!=xlsxStyle)
            cell_begin_city.setCellStyle(xlsxStyle);
        cell_begin_city.setCellValue(value);
    }



















    @RequestMapping("paysheet")
    public void paysheet(Tr_Payment_PlanVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        response.setCharacterEncoding("UTF-8");
        //String id = ENDECodeUtils.URLDecode(vo.getId());
        String[] id_array = vo.getId().split(",");
        List<Tr_Payment_PlanVO> list = new ArrayList<Tr_Payment_PlanVO>();
        for (String item : id_array) {
            Tr_Payment_PlanVO info = new Tr_Payment_PlanVO();
            info.setId(item);
            list.add(info);
        }
        //验证状态
        List<Tr_Payment_PlanVO> datalist = iPayPlanService.findListByProperty(list);
        if (null == datalist || datalist.size() <= 0) {
            response.getWriter().write("没有查询到选中数据，原因：数据已被其他用户删除或处理，请查询数据后再操作。");
            return;
        }
        if (datalist.size() != list.size()) {
            response.getWriter().write("有部份数据已被其他用户处理，请查询数据后再操作。");
            return;
        }

        String errorString="";
        String data_type="";
        for(Tr_Payment_PlanVO item:datalist){
            if(StringUtils.isBlank(data_type))
            {
                data_type=""+item.getData_type();
            }else{
                if(!data_type.equals(item.getData_type()))
                {
                    errorString+=item.getId()+",";
                }
            }
        }
        if(StringUtils.isNotBlank(errorString)) {
            response.getWriter().write("选中“数据类型”列不是同一种值。");
            return;
        }


        String carrier_no="";
        for(Tr_Payment_PlanVO item:datalist){
            if(StringUtils.isBlank(carrier_no))
            {
                carrier_no=""+item.getCarrier_no();
            }else{
                if(!carrier_no.equals(item.getCarrier_no()))
                {
                    errorString+=item.getId()+",";
                }
            }
        }
        if(StringUtils.isNotBlank(errorString)) {
            response.getWriter().write("选中的数据承运商必须一致");
            return;
        }

      /*  for(Tr_Payment_PlanVO item:datalist) {
            if (!"N".equals(item.getPay_apply())) {
                errorString += item.getId() + ",";
            }
        }
        if(StringUtils.isNotBlank(errorString)) {
            response.getWriter().write("选中数据“付款申请”列需要全部为N");
            return;
        }*/

        //导出Excel文件
        //6、预付的使用预付模板；正常的使用正常付款模板，冲预付栏为空；冲预付的，正常付款栏为空，冲预付栏填入含税金额。
        if ("1".equals(data_type)) {
            //预付 = 5、根据单位，合同号，发票号，月份，税率汇总
            List<Tr_Payment_PlanVO> sumDataList = iPayPlanService.findSumDataList_expect(list);
            CreateFileExcel_PaySheet_Expect(sumDataList,request,response);
        }else if ("2".equals(data_type)) {
            //冲预付
            List<Tr_Payment_PlanVO> sumDataList = iPayPlanService.findSumDataList_normal(list);
            CreateFileExcel_PaySheet_Rushpayment(data_type,sumDataList,request,response);
        } else {//正常0
            List<Tr_Payment_PlanVO> sumDataList = iPayPlanService.findSumDataList_normal(list);
            CreateFileExcel_PaySheet_Normal(data_type,sumDataList,request,response);
        }


        //7、数据导出后，更改PAY_APPLY=Y
        for(Tr_Payment_PlanVO item:list){
            item.setPay_apply("Y");
        }
        iPayPlanService.update(list);
    }

    //生成Excel文件
    //正常
    private void CreateFileExcel_PaySheet_Normal(String data_type,List<Tr_Payment_PlanVO> list, HttpServletRequest request, HttpServletResponse response)
    {
        SysUserVO sysUserVO=SessionUtils.currentSession();
        String separator= File.separator;
        try {
            //往Excel文件中写入数据 开始
            String url = "static/Resource/excel/baobiao/paysheetTemplate.xlsx";
            InputStream fis = ClassUtils.class.getClassLoader().getResourceAsStream(url);
            Workbook wb = WorkbookFactory.create(fis);

            //创建单元格样式
            CellStyle xlsxStyle = wb.createCellStyle();
            xlsxStyle.setBorderLeft(BorderStyle.THIN);
            xlsxStyle.setBorderTop(BorderStyle.THIN);
            xlsxStyle.setBorderRight(BorderStyle.THIN);
            xlsxStyle.setBorderBottom(BorderStyle.THIN);
            xlsxStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            xlsxStyle.setAlignment(HorizontalAlignment.CENTER);

            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 11);
            xlsxStyle.setFont(font);

            //共 3 个工作簿
            Integer ColumnNumber = 0;
            Integer iRow=0;
            Integer size=0;
            short row_height=480;
            String tax_amount_string="";
            String tax_string="";//税额
            wb.removeSheetAt(2);//删除 - 冲预付
            wb.removeSheetAt(1);//删除 - 预付款
            wb.setSheetName(0,list.get(0).getCarrier_no());//承运商名称，保费的将保险公司承运商编号改为保费明细
            Sheet sheet = wb.getSheetAt(0);//使用 - 正常付款
            Row carrier_row = sheet.getRow(2);
            carrier_row.getCell(0).setCellValue("单位名称："+list.get(0).getCarrier_name());//单位名称
            Row contract_row = sheet.getRow(4);
            contract_row.getCell(7).setCellValue(list.get(0).getContract_no());//合同
            for (int i = 0; i < list.size(); i++) {
                sheet.shiftRows(size+7,sheet.getLastRowNum(),1,true,false);
                Row row = sheet.createRow(size+7);
                row.setHeight(row_height);
                iRow++;
                tax_amount_string+="G"+(iRow+7)+"+";
                tax_string+="E"+(iRow+7)+"+";
                ColumnNumber = 0;
                CreateCell(row, xlsxStyle, ColumnNumber,"");//摘要
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, "");
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, "");
               // POIUtils.mergeCell(sheet,iRow+7,iRow+7,0,2);
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, "RMB");
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber,"");//不含税金额
                row.getCell(ColumnNumber).setCellValue(Double.parseDouble((list.get(i).getNot_tax_amount().toString()).trim()));
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, String.valueOf(list.get(i).getInvoice_tax()*100).replace(".0",""));
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, "");//含税金额String.valueOf(list.get(i).getTax_amount())
                row.getCell(ColumnNumber).setCellValue(Double.parseDouble((list.get(i).getTax_amount().toString()).trim()));
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getJs_no());//结算号
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getInvoice_no());//发票号
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, "");
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, "");
                //POIUtils.mergeCell(sheet,iRow+7,iRow+7,8,10);
                addMergedRegion(size+7, size+7, 0, 2,row_height,"",sheet,row,xlsxStyle);
                addMergedRegion(size+7, size+7, 8, 10,row_height,"",sheet,row,xlsxStyle);
                size++;
            }

            //合计：含税金额
            Row sum_row = sheet.getRow(list.size()+7);
            Cell tax_amount_cell = sum_row.createCell(6);
            tax_amount_cell.setCellStyle(xlsxStyle);
            tax_amount_cell.setCellType(CellType.FORMULA);
            tax_amount_cell.setCellFormula("SUM("+tax_amount_string.substring(0,tax_amount_string.length()-1)+")");//("SUM(G6+G7+G8)");

            //合计
            Row heji_row = sheet.getRow(list.size()+10);
            addMergedRegion(size+10, size+11, 0, 0,row_height,"合计",sheet,heji_row,xlsxStyle);
            addMergedRegion(size+10, size+10, 2, 4,row_height,"",sheet,heji_row,xlsxStyle);
            Cell heji_cell = heji_row.createCell(2);
            heji_cell.setCellStyle(xlsxStyle);
            heji_cell.setCellType(CellType.FORMULA);
            heji_cell.setCellFormula("G"+String.valueOf((list.size()+8)));
            POIUtils.mergeCell(sheet,size+10,size+10,8,10);
            POIUtils.mergeCell(sheet,size+11,size+11,1,10);

            //税额
            Row tax_row = sheet.getRow(list.size()+12);
            addMergedRegion(size+12, size+12, 2, 4,row_height,"",sheet,tax_row,xlsxStyle);
            Cell tax_cell = tax_row.createCell(2);
            tax_cell.setCellStyle(xlsxStyle);
            tax_cell.setCellType(CellType.FORMULA);
            tax_cell.setCellFormula("G"+String.valueOf((list.size()+8))+"-SUM("+tax_string.substring(0,tax_amount_string.length()-1)+")");//=G8
            //输出流

            //合并备注
            Row remark_row = sheet.getRow(list.size()+13);
            addMergedRegion(list.size()+13, list.size()+13, 1, 10,row_height,"",sheet,remark_row,xlsxStyle);

            //合并预付款
            Row yu_pay_row = sheet.getRow(list.size()+14);
            addMergedRegion(list.size()+14, list.size()+14, 1, 10,row_height,"",sheet,yu_pay_row,xlsxStyle);

            String SaveFileName = new String("付款申请单.xlsx".getBytes("GBK"), "ISO8859_1");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + SaveFileName);
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            try (OutputStream out = response.getOutputStream()) {
                wb.write(out);
            } catch (Exception e) {
                e.printStackTrace();
            }

            wb.close();
            wb = null;
        } catch (Exception e) {
            try {
                response.getWriter().write(e.getMessage());
            }catch (Exception eres)
            {}
        }
    }


    private void addMergedRegion(int firstRow, int lastRow, int firstCol, int lastCol,short height,String value,Sheet sheet,Row row,CellStyle style)
    {
        CellRangeAddress range_row = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        sheet.addMergedRegion(range_row);
        row.setHeight(height);
        Cell cell=row.createCell(firstCol);
        cell.setCellStyle(style);
        cell.setCellValue(value);

        for(int i=firstCol+1;i<=lastCol;i++)
        {
            row.createCell(i).setCellStyle(style);
        }
    }
    private void CreateSignRowCell(Row row,int rowNumber,String value)
    {
        Cell cell = row.createCell(rowNumber);
        cell.setCellValue(value);
    }











    //生成预付Excel文件
    //预付
    private void CreateFileExcel_PaySheet_Expect(List<Tr_Payment_PlanVO> list, HttpServletRequest request, HttpServletResponse response)
    {
        SysUserVO sysUserVO=SessionUtils.currentSession();
        String separator= File.separator;
        try {
            //往Excel文件中写入数据 开始
            String url = "static/Resource/excel/baobiao/paysheetTemplate.xlsx";
            InputStream fis = ClassUtils.class.getClassLoader().getResourceAsStream(url);
            Workbook wb = WorkbookFactory.create(fis);
            wb.setForceFormulaRecalculation(true);

            //创建单元格样式
            CellStyle xlsxStyle = wb.createCellStyle();
            xlsxStyle.setBorderLeft(BorderStyle.THIN);
            xlsxStyle.setBorderTop(BorderStyle.THIN);
            xlsxStyle.setBorderRight(BorderStyle.THIN);
            xlsxStyle.setBorderBottom(BorderStyle.THIN);
            xlsxStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            xlsxStyle.setAlignment(HorizontalAlignment.CENTER);

            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 11);
            xlsxStyle.setFont(font);

            //共 2 个工作簿
            Integer ColumnNumber = 0;
            Integer iRow=0;
            short row_height=480;
            String tax_amount_string="";
            Sheet sheet = wb.getSheetAt(1);//使用 - 正常付款
            wb.removeSheetAt(2);//删除 - 冲预付
            wb.removeSheetAt(0);//删除 - 正常付款
            for (int i = 0; i < list.size(); i++) {
                Row row = sheet.createRow(iRow + 5);
                row.setHeight(row_height);
                iRow++;
                tax_amount_string+="F"+(iRow+5)+"+";
                ColumnNumber = 0;
                CreateCell(row, xlsxStyle, ColumnNumber, String.valueOf(i+1));
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getCarrier_name());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getContract_no());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getPay_expect());

                ColumnNumber++;
                //币种 - 固定值
                CreateCell(row, xlsxStyle, ColumnNumber, "RMB");
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, "");
                row.getCell(ColumnNumber).setCellValue(Double.parseDouble((list.get(i).getTax_amount().toString()).trim()));
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getInvoice_no());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getRemark());
            }
            //合计
            iRow+=5;
            Row rmb_row = sheet.createRow(iRow);
            addMergedRegion(iRow, iRow, 0, 4,row_height,"RMB合计",sheet,rmb_row,xlsxStyle);

            //合计：含税金额
            Cell tax_amount_cell = rmb_row.createCell(5);
            tax_amount_cell.setCellStyle(xlsxStyle);
            tax_amount_cell.setCellType(CellType.FORMULA);
            tax_amount_cell.setCellFormula("SUM("+tax_amount_string.substring(0,tax_amount_string.length()-1)+")");//("SUM(G6+G7+G8)");

            //大写金额
            addMergedRegion(iRow, iRow, 6, 7,row_height,"",sheet,rmb_row,xlsxStyle);
            Cell upper_cell = rmb_row.createCell(6);
            upper_cell.setCellStyle(xlsxStyle);
            upper_cell.setCellType(CellType.FORMULA);
            upper_cell.setCellFormula("RIGHT(TEXT(F"+(iRow+1)+"*100,\"[DBNUM2]0仟0佰0拾0万0仟0佰0拾0元0角0分整\"),LEN(F"+(iRow+1)+")*2+IF(ISERR(FIND(\".\",F"+(iRow+1)+")),5,IF(LEN(F"+(iRow+1)+")-FIND(\".\",F"+(iRow+1)+")=1,1,-1)))");



            iRow++;
            Row usd_row = sheet.createRow(iRow);
            addMergedRegion(iRow, iRow, 0, 4,row_height,"USD合计",sheet,usd_row,xlsxStyle);

            //合计：含税金额
            Cell usd_tax_cell = usd_row.createCell(5);
            usd_tax_cell.setCellStyle(xlsxStyle);
            usd_tax_cell.setCellValue("0.00");

            addMergedRegion(iRow, iRow, 6, 7,row_height,"零元零角零分整",sheet,usd_row,xlsxStyle);


            //部门，审核签字
            iRow+=4;
            Row sign_row = sheet.createRow(iRow);
            CreateSignRowCell(sign_row,0,"业务部门：");
            CreateSignRowCell(sign_row,2,"结算审核：");
            CreateSignRowCell(sign_row,5,"会计审核：");
            CreateSignRowCell(sign_row,7,"制表："+sysUserVO.getRealName());

            iRow+=1;
            Row date_row = sheet.createRow(iRow);
            CreateSignRowCell(date_row,7,"日期："+DateUtils.nowDate("yyyy-MM-dd"));

            iRow+=2;
            Row hrow = sheet.createRow(iRow);
            CreateSignRowCell(hrow,0,"财务部负责人：");
            CreateSignRowCell(hrow,2,"分管副总：");
            CreateSignRowCell(hrow,5,"总经理：");
            CreateSignRowCell(hrow,7,"出纳：");



            //输出流
            String SaveFileName = new String("预付款申请单.xlsx".getBytes("GBK"), "ISO8859_1");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + SaveFileName);
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            try (OutputStream out = response.getOutputStream()) {
                wb.write(out);
            } catch (Exception e) {
                e.printStackTrace();
            }

            wb.close();
            wb = null;
        } catch (Exception e) {
            try {
                response.getWriter().write(e.getMessage());
            }catch (Exception eres)
            {}
        }
    }



    //生成Excel文件
    //冲预付申请2
    private void CreateFileExcel_PaySheet_Rushpayment(String data_type,List<Tr_Payment_PlanVO> list, HttpServletRequest request, HttpServletResponse response)
    {
        SysUserVO sysUserVO=SessionUtils.currentSession();
        String separator= File.separator;
        try {
            //往Excel文件中写入数据 开始
            String url = "static/Resource/excel/baobiao/paysheetTemplate.xlsx";
            InputStream fis = ClassUtils.class.getClassLoader().getResourceAsStream(url);
            Workbook wb = WorkbookFactory.create(fis);
            // 读取Excel文档
            wb.setForceFormulaRecalculation(true);
            //创建单元格样式
            CellStyle xlsxStyle = wb.createCellStyle();
            xlsxStyle.setBorderLeft(BorderStyle.THIN);
            xlsxStyle.setBorderTop(BorderStyle.THIN);
            xlsxStyle.setBorderRight(BorderStyle.THIN);
            xlsxStyle.setBorderBottom(BorderStyle.THIN);
            xlsxStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            xlsxStyle.setAlignment(HorizontalAlignment.CENTER);

            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 11);
            xlsxStyle.setFont(font);

            //共 3 个工作簿
            Integer ColumnNumber = 0;
            Integer iRow=0;
            short row_height=480;
            String tax_amount_string="";
            Sheet sheet = wb.getSheetAt(2);//使用 - 正常付款
            wb.removeSheetAt(1);//删除 - 预付款
            wb.removeSheetAt(0);//删除 - 正常
            Row contract_no_row = sheet.getRow(3);
            contract_no_row.createCell(9).setCellValue(list.get(0).getContract_no());
            for (int i = 0; i < list.size(); i++) {
                Row row = sheet.createRow(iRow + 5);
                row.setHeight(row_height);
                iRow++;
                tax_amount_string+="G"+(iRow+5)+"+";
                ColumnNumber = 0;
                CreateCell(row, xlsxStyle, ColumnNumber, String.valueOf(i+1));
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getCarrier_name());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getJs_no());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getInvoice_no());
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, list.get(i).getRemark());//摘要
                ColumnNumber++;
                //币种 - 固定值
                CreateCell(row, xlsxStyle, ColumnNumber, "RMB");
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, "");
                row.getCell(ColumnNumber).setCellValue(Double.parseDouble((list.get(i).getTax_amount().toString()).trim()));
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, String.valueOf(list.get(i).getInvoice_tax()*100).replace(".0",""));
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, "");
                row.getCell(ColumnNumber).setCellValue(Double.parseDouble((list.get(i).getNot_tax_amount().toString()).trim()));
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber,"" );
                row.getCell(ColumnNumber).setCellValue(Double.parseDouble((list.get(i).getTax_difference().toString()).trim()));
                ColumnNumber++;
                CreateCell(row, xlsxStyle, ColumnNumber, "");
                row.getCell(ColumnNumber).setCellValue(Double.parseDouble((list.get(i).getTax_amount().toString()).trim()));
            }
            //合计
            iRow+=5;
            Row rmb_row = sheet.createRow(iRow);
            addMergedRegion(iRow, iRow, 0, 5,row_height,"RMB合计",sheet,rmb_row,xlsxStyle);

            //合计：含税金额
            Cell tax_amount_cell = rmb_row.createCell(6);
            tax_amount_cell.setCellStyle(xlsxStyle);
            tax_amount_cell.setCellType(CellType.FORMULA);
            tax_amount_cell.setCellFormula("SUM("+tax_amount_string.substring(0,tax_amount_string.length()-1)+")");//("SUM(G6+G7+G8)");

            //大写金额
            addMergedRegion(iRow, iRow, 7, 10,row_height,"",sheet,rmb_row,xlsxStyle);
            Cell upper_cell = rmb_row.createCell(7);
            upper_cell.setCellStyle(xlsxStyle);
            upper_cell.setCellType(CellType.FORMULA);
            upper_cell.setCellFormula("RIGHT(TEXT(G"+(iRow+1)+"*100,\"[DBNUM2]0仟0佰0拾0万0仟0佰0拾0元0角0分整\"),LEN(G"+(iRow+1)+")*2+IF(ISERR(FIND(\".\",G"+(iRow+1)+")),5,IF(LEN(G"+(iRow+1)+")-FIND(\".\",G"+(iRow+1)+")=1,1,-1)))");



            iRow++;
            Row usd_row = sheet.createRow(iRow);
            addMergedRegion(iRow, iRow, 0, 5,row_height,"USD合计",sheet,usd_row,xlsxStyle);

            //合计：含税金额
            Cell usd_tax_cell = usd_row.createCell(6);
            usd_tax_cell.setCellStyle(xlsxStyle);
            usd_tax_cell.setCellValue("0.00");

            addMergedRegion(iRow, iRow, 7, 10,row_height,"零元零角零分整",sheet,usd_row,xlsxStyle);


            //部门，审核签字
            iRow+=3;
            Row sign_row = sheet.createRow(iRow);
            CreateSignRowCell(sign_row,1,"业务部门：");
            CreateSignRowCell(sign_row,3,"结算审核：");
            CreateSignRowCell(sign_row,7,"会计审核：");
            CreateSignRowCell(sign_row,9,"制表：");
            CreateSignRowCell(sign_row,10,sysUserVO.getRealName());

            iRow+=3;
            Row hrow = sheet.createRow(iRow);
            CreateSignRowCell(hrow,1,"财务部负责人：");
            CreateSignRowCell(hrow,3,"分管副总：");
            CreateSignRowCell(hrow,7,"总经理：");
            CreateSignRowCell(hrow,9,"日期：");
            CreateSignRowCell(hrow,10,DateUtils.nowDate("yyyy/MM/dd"));


            //输出流
            String SaveFileName = new String("付款申请单.xlsx".getBytes("GBK"), "ISO8859_1");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + SaveFileName);
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            try (OutputStream out = response.getOutputStream()) {
                wb.write(out);
            } catch (Exception e) {
                e.printStackTrace();
            }

            wb.close();
            wb = null;
        } catch (Exception e) {
            try {
                response.getWriter().write(e.getMessage());
            }catch (Exception eres)
            {}
        }
    }
}
