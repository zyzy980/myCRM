package com.bba.nosettlement.controller;


import com.bba.common.constant.NOSETTLEMENT_TAB_Enum;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.dz.controller.SendEMailThread;
import com.bba.dz.vo.Js_Dz_SheetAccountVO;
import com.bba.dz.vo.Js_Dz_Sheet_DetailAccountVO;
import com.bba.jcda.vo.Cr_Customer_LinkVO;
import com.bba.nosettlement.service.api.IJs_Dz_Non_SheetService;
import com.bba.nosettlement.service.api.IJs_Non_VehicleService;
import com.bba.nosettlement.vo.Js_Dz_Non_SheetVO;
import com.bba.nosettlement.vo.Js_Dz_Non_Sheet_DetailVO;
import com.bba.nosettlement.vo.Js_Non_VehicleVO;
import com.bba.util.*;
import com.bba.xtgl.vo.SysUserVO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/*
* Author:LTJ
* Date:2019-07-31
* Description:3.3.3.非商品车对上结算 - 对账单
* */
@RestController
@RequestMapping("/nosettlement/Js_Dz_Non_Sheet")
public class Js_Dz_Non_SheetController {

    @Autowired
    public IJs_Dz_Non_SheetService iJs_dz_non_sheetService;

    @Log(value = "非商品车对上结算-对上账单管理-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        customSearchFilters = MyUtils.decode(customSearchFilters);
        // 0：管理员；1：操作员；2：商务人员；3：财务人员；4：其它；
        // 数据状态：0.正常  1.业务审核.2商务审核.3.财务审核.4.发送邮件.5.客户确认
        //if (sysUserVO.getUserLevel().equals("1")) {
        //    jqGridParamModel.put("state", "in", "'0','1'");
        //} else
        if (sysUserVO.getUserLevel().equals("2")) {
            jqGridParamModel.put("state", "in", "'1','2'");
        } else if (sysUserVO.getUserLevel().equals("3")) {
            jqGridParamModel.put("state", "in", "'2','3'");
        }

        return iJs_dz_non_sheetService.getListForGrid(jqGridParamModel, customSearchFilters);
    }


    @Log(value = "非商品车对上结算-对上账单管理-明细列表查询")
    @RequestMapping("getListForGridDetail")
    public PageVO getListForGridDetail(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        //SysUserVO sysUserVO = SessionUtils.currentSession();
        customSearchFilters = MyUtils.decode(customSearchFilters);
        return iJs_dz_non_sheetService.getListForGridDetail(jqGridParamModel, customSearchFilters);
    }


    //LTJ:2019-07-22 业务审核-商务审核-财务审核
    // 0：管理员；1：操作员；2：商务人员；3：财务人员；4：其它；
    // 数据状态：0.正常  1.业务审核.2商务审核.3.财务审核.4.发送邮件.5.客户确认
    @Log(value = "非商品车对上结算-对上账单管理-审 核数据")
        @RequestMapping(value = "data_check", method = {RequestMethod.POST, RequestMethod.GET})
        public ResultVO data_check(@RequestBody Js_Dz_Non_SheetVO vo, HttpServletRequest request, HttpServletResponse response) {
            SysUserVO sysUserVO = SessionUtils.currentSession();
            String dz_sheet = ENDECodeUtils.URLDecode(vo.getDz_sheet());
            String[] dz_sheet_array = dz_sheet.split(",");
            List<Js_Dz_Non_SheetVO> list = new ArrayList<Js_Dz_Non_SheetVO>();
            for (String item : dz_sheet_array) {
                Js_Dz_Non_SheetVO info = new Js_Dz_Non_SheetVO();
                info.setDz_sheet(item);
                list.add(info);
            }
            //验证状态
            List<Js_Dz_Non_SheetVO> datalist = iJs_dz_non_sheetService.findListByProperty(list);
            if (null == datalist || datalist.size() <= 0) {
                return ResultVO.failResult("没有查询到选中数据，原因：数据已被其他用户删除或处理，请查询数据后再操作。");
            }
            if (datalist.size() != list.size()) {
                return ResultVO.failResult("有部份数据已被其他用户处理，请查询数据后再操作。");
            }
            String error = "";
            for (Js_Dz_Non_SheetVO item : datalist) {
                if (!vo.getJs_batch().equals(item.getState())) {
                    error += item.getDz_sheet() + ",";
                }
                //查询该对账单下是否存在空账单编号的数据，存在不予通过
                Integer count = iJs_dz_non_sheetService.findNullBillNumber(item);
                if (count>0) {
                    return ResultVO.failResult("对账单中存在数据没有账单编号，对账单："+item.getDz_sheet());
                }
            }
            datalist = null;
            if (StringUtils.isNotBlank(error)) {
                return ResultVO.failResult("选中数据状态已发生变化(数据已被其他用户处理)，请查询数据后再操作。单号：" + error);
            }


        String currentDate = DateUtils.nowDate();
        boolean bMail = false;
        for (Js_Dz_Non_SheetVO item : list) {
            item.setState(vo.getState());//更新为这状态值
            if (null != vo.getRemark() && "mail".equalsIgnoreCase(vo.getRemark())) {
                item.setSend_mail_date(currentDate);
                bMail = true;
            }
        }
        //发送邮件 按钮
        if (bMail) {
            ResultVO rvo=SendMail(list,sysUserVO, request, response);
            if(!rvo.getResultCode().equals("0")){
                return rvo;
            }
        }

        iJs_dz_non_sheetService.UpdateJs_Dz_Sheet_state(list);


        return ResultVO.successResult("OK");
    }


    //发送邮件
    private ResultVO SendMail(List<Js_Dz_Non_SheetVO> list,SysUserVO sysUserVO,HttpServletRequest request,HttpServletResponse response)
    {
        //检查用户帐号是否有邮箱账号和密码
        if(StringUtils.isBlank(sysUserVO.getMail())){
            return ResultVO.failResult("检查到你的登录账号，没有录入邮箱地址，请到用户管理里面录入，或联系管理员。");
        }
        if(StringUtils.isBlank(sysUserVO.getMailpassword())){
            return ResultVO.failResult("检查到你的登录账号，没有录入邮箱密码，请到用户管理里面录入，或联系管理员。");
        }
        if(StringUtils.isBlank(sysUserVO.getMailhost())){
            return ResultVO.failResult("检查到你的登录账号，没有录入邮箱主机地址，请到用户管理里面录入，或联系管理员。");
        }
        if(StringUtils.isBlank(sysUserVO.getMailport())){
            return ResultVO.failResult("检查到你的登录账号，没有录入邮箱主机端口，请到用户管理里面录入，或联系管理员。");
        }
        Properties properties=new Properties();
        properties.setProperty("mailUsername",sysUserVO.getMail());
        properties.setProperty("mailPassword", sysUserVO.getMailpassword());
        properties.setProperty("mailHost",sysUserVO.getMailhost());
        properties.setProperty("mailPort",sysUserVO.getMailport());
        properties.setProperty("mailFrom",sysUserVO.getMail());
        properties.setProperty("mailPersonal",sysUserVO.getRealName());
        properties.setProperty("mailTimeout","20000");


        //读取明细数据，并过滤重复客户编号
        List<Js_Dz_Non_Sheet_DetailVO> detailList=new ArrayList<Js_Dz_Non_Sheet_DetailVO>();
        for(Js_Dz_Non_SheetVO item:list){
            Js_Dz_Non_Sheet_DetailVO vo=new Js_Dz_Non_Sheet_DetailVO();
            vo.setDz_sheet(item.getDz_sheet());
            detailList.add(vo);
        }
        detailList=iJs_dz_non_sheetService.findListByProperty(detailList);
        List<String> cusList=new ArrayList<String>();
        for(Js_Dz_Non_Sheet_DetailVO item:detailList){
            if(!cusList.contains(item.getCus_no())){
                cusList.add(item.getCus_no());
            }
        }


        for(String cus_code:cusList) {
            //过滤要发送数据
            List<Js_Dz_Non_Sheet_DetailVO> sendDataList=new ArrayList<Js_Dz_Non_Sheet_DetailVO>();
            for(Js_Dz_Non_Sheet_DetailVO item:detailList){
                if(cus_code.equals(item.getCus_no())){
                    sendDataList.add(item);
                }
            }
            if(sendDataList.size()<=0)
                continue;

            //组织接收邮件的用户
            Cr_Customer_LinkVO linkVO = new Cr_Customer_LinkVO();
            linkVO.setCus_code(cus_code);
            linkVO.setState("1");
            linkVO.setDui_flag("1");
            List<Cr_Customer_LinkVO> linkList = iJs_dz_non_sheetService.findListByProperty(linkVO);
            if(null==linkList || linkList.size()<=0){
                continue;
            }
            String emailString="";
            String emailUserName="";
            for(Cr_Customer_LinkVO item:linkList){
                if(StringUtils.isNotBlank(item.getMail())) {
                    emailString += item.getMail() + ",";
                    emailUserName += item.getRealname() + "、";
                }
            }
            if(StringUtils.isBlank(emailString))
                continue;
            emailString=emailString.substring(0,emailString.length()-1);
            emailUserName=emailUserName.substring(0,emailUserName.length()-1);

            //创建附件文件
            ResultVO resultVO=CreateDZ_SHEET_To_Excel(1,sendDataList,sysUserVO, request, response);
            if(!resultVO.getResultCode().equals("0"))
                return resultVO;

            //发送附件路径
            String attachmentFilePath =resultVO.getResultDataFull().toString();
            //发送附件名称
            String attachmentFilename=attachmentFilePath.substring(attachmentFilePath.lastIndexOf(File.separator));
            //发送邮件
            //邮件标题： 2019-07-23结算表
            String js_date=sendDataList.get(0).getCreate_date().indexOf(" ")!=-1?sendDataList.get(0).getCreate_date().split(" ")[0]:sendDataList.get(0).getCreate_date();
            String subject=js_date+"结算表";

            //邮件内容
            String html="<div style='width:100%;height:auto;'>";
            html+="<div style='width:100%;color:#0091d4;'>"+emailUserName+"：</div>";
            html+="<div style='width:70%;padding-left:20px;color:#0091d4;'>您们好！</div>";
            html+="<div style='width:70%;padding-left:20px;color:#0091d4;'>附件为 "+subject+"</div>";
            html+="<div style='width:70%;padding-left:20px;color:#0091d4;'>汇总如下，请审核，谢谢。</div>";
            //表格开始
            html+="<div style='width:100%;'>";

            html+="<table border='1' cellspacing='0' cellpadding='1'>";
            html+="<tr>";
            html+="<td align='center' colspan='4' style='font-weight:bold'>"+subject+"</td>";
            html+="</tr>";
            //表格需要根据税率分开
            Set<String> tax_set = new HashSet();
            //数据需要汇总统计 - 数量 \ 金额
            List<String> distinctList=new ArrayList<String>();
            List<Js_Dz_Non_Sheet_DetailVO> mailBodyList=new ArrayList<Js_Dz_Non_Sheet_DetailVO>();
            for(int i=0,ilen=sendDataList.size();i<ilen;i++) {
                if (!distinctList.contains(sendDataList.get(i).getBill_number()+sendDataList.get(i).getTax_rate())) {
                    if(null==sendDataList.get(i).getJs_qty())
                        sendDataList.get(i).setJs_qty("0");
                    if(null==sendDataList.get(i).getNot_tax_amount())
                        sendDataList.get(i).setNot_tax_amount("0");
                    if(null==sendDataList.get(i).getTax_amount())
                        sendDataList.get(i).setTax_amount("0");
                    mailBodyList.add(sendDataList.get(i));
                    distinctList.add(sendDataList.get(i).getBill_number()+sendDataList.get(i).getTax_rate());
                } else {
                    for (Js_Dz_Non_Sheet_DetailVO bodyItem : mailBodyList) {
                        if (bodyItem.getBill_number().equals(sendDataList.get(i).getBill_number()) && bodyItem.getTax_rate().equals(sendDataList.get(i).getTax_rate())) {
                            bodyItem.setJs_qty(String.valueOf(Integer.valueOf(bodyItem.getJs_qty()) + (null==sendDataList.get(i).getJs_qty()?0:Integer.valueOf(sendDataList.get(i).getJs_qty()))));
                            bodyItem.setNot_tax_amount(String.valueOf(Double.valueOf(bodyItem.getNot_tax_amount()) + (null==sendDataList.get(i).getNot_tax_amount()?0d:Double.valueOf(sendDataList.get(i).getNot_tax_amount()))));
                            bodyItem.setTax_amount(String.valueOf(Integer.valueOf(bodyItem.getTax_amount()) + (null==sendDataList.get(i).getTax_amount()?0d:Double.valueOf(sendDataList.get(i).getTax_amount()))));
                            break;
                        }
                    }
                }
            }
            int js_qty=0;
            Double not_tax_amount=0.0;
            Double tax_amount=0.0;
            int index=0;
            for (String tax_rate : tax_set) {
                for(int i=0,ilen=mailBodyList.size();i<ilen;i++) {
                    html+="<tr  style='background:#eff5fb'>";
                    html+="<td align='center' style='width:150px;font-weight:bold'>"+(index==0?"账单编号":"")+"</td><td align='center' style='width:100px;font-weight:bold'>"+(index==0?"运输台数":"")+"</td><td align='center'  style='width:130px;font-weight:bold'>"+(index==0?"不含税金额":"")+"</td><td align='center'   style='width:130px;font-weight:bold'>含税金额("+String.valueOf(Double.valueOf(tax_rate)*100).replace(".0","")+"%"+")</td>";
                    html+="</tr>";
                    js_qty+=StringUtils.isBlank(mailBodyList.get(i).getJs_qty())?0:Integer.valueOf(mailBodyList.get(i).getJs_qty());
                    not_tax_amount+=StringUtils.isBlank(mailBodyList.get(i).getNot_tax_amount())?0.0:Double.valueOf(mailBodyList.get(i).getNot_tax_amount());
                    tax_amount+=StringUtils.isBlank(mailBodyList.get(i).getTax_amount())?0.0:Double.valueOf(mailBodyList.get(i).getTax_amount());
                    html += "<tr>";
                    html += "<td align='center'>" + mailBodyList.get(i).getBill_number() + "</td><td align='center'>" + mailBodyList.get(i).getJs_qty() + "</td><td align='center'>" +MyUtils.formatDouble2(Double.valueOf(mailBodyList.get(i).getNot_tax_amount())) + "</td><td align='center'>" +MyUtils.formatDouble2(Double.valueOf(mailBodyList.get(i).getTax_amount())) + "("+(StringUtils.isBlank(mailBodyList.get(i).getTax_rate())?"0":String.valueOf(Double.valueOf(mailBodyList.get(i).getTax_rate())*100)+"%")+")</td>";
                    html += "</tr>";
                }
                index++;
            }
            html+="<tr>";
            html+="<td align='center' style='width:150px;font-weight:bold'>合计</td><td align='center' style='width:150px;font-weight:bold'>"+js_qty+"</td><td align='center' style='width:150px;font-weight:bold'>"+MyUtils.formatDouble2(not_tax_amount)+"</td><td align='center' style='width:150px;font-weight:bold'>"+MyUtils.formatDouble2(tax_amount)+"</td>";
            html+="</tr>";
            html+="</table>";
            html+="</div>";
            //表格结束
            html+="</div>";

            try {
                Thread thread = new Thread(new SendEMailThread(properties, emailString.split(","), subject, html, attachmentFilename, attachmentFilePath));
                thread.start();
            }catch (Exception e){

            }
        }
        return ResultVO.successResult("OK");
    }

    // 生成对账单Excel文件
    // 参数 list 客户字段必须是相同 - 此方法不区分客户生成文件
    // resKind=0直接输出；=1写入文件
    private ResultVO CreateDZ_SHEET_To_Excel(int resKind,List<Js_Dz_Non_Sheet_DetailVO> list,SysUserVO sysUserVO,HttpServletRequest request,HttpServletResponse response)
    {
        ResultVO resultVO=new ResultVO();
        resultVO.setResultCode("1");
        String separator=File.separator;
        String SaveFilePath = System.getProperty("user.dir") + separator+"Excel"+separator+"dz_sheet"+separator+ DateUtils.nowDate("yyyyMMdd") + separator;
        try {
            File file = new File(SaveFilePath);
            if (!file.exists() && !file.isDirectory()) {
                file.mkdirs();
            }
            // 2019年非商品车结算表.xlsx
            String SaveFileName= DateUtils.dateFormat(DateUtils.parseDate(list.get(0).getBegin_date(),"yyyy-MM-dd"),"yy") + "年非商品车结算表.xlsx";
            SaveFilePath+=SaveFileName;
            //往Excel文件中写入数据 开始
            String url = "static/Resource/excel/dz/dz_non_sheet_template.xlsx";
            InputStream fis = ClassUtils.class.getClassLoader().getResourceAsStream(url);
            file = new File(SaveFilePath);
            if(file.exists())
            {
                file.delete();
            }
            // 读取Excel文档
            Workbook wb =  new XSSFWorkbook(fis);;

            // 创建单元格样式
            CellStyle xlsxStyle = wb.createCellStyle();
            xlsxStyle.setBorderLeft(BorderStyle.THIN);
            xlsxStyle.setBorderTop(BorderStyle.THIN);
            xlsxStyle.setBorderRight(BorderStyle.THIN);
            xlsxStyle.setBorderBottom(BorderStyle.THIN);
            xlsxStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            xlsxStyle.setAlignment(HorizontalAlignment.CENTER);

            // 创建单元格样式
            CellStyle xlsxStyleBold = wb.createCellStyle();
            xlsxStyleBold.setBorderLeft(BorderStyle.THIN);
            xlsxStyleBold.setBorderTop(BorderStyle.THIN);
            xlsxStyleBold.setBorderRight(BorderStyle.THIN);
            xlsxStyleBold.setBorderBottom(BorderStyle.THIN);
            xlsxStyleBold.setVerticalAlignment(VerticalAlignment.CENTER);
            xlsxStyleBold.setAlignment(HorizontalAlignment.CENTER);

            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short)11);
            font.setBold(true);
            xlsxStyleBold.setFont(font);



            //区分 - VIP，保密车，合同车，救援车  对应Excel文件中工作簿
            for(int i = NOSETTLEMENT_TAB_Enum.values().length-1;i>=0;i--) {
                //过滤数据 == 区分 - VIP，保密车，合同车，救援车
                List<Js_Dz_Non_Sheet_DetailVO> datalist = new ArrayList<Js_Dz_Non_Sheet_DetailVO>();
                for (Js_Dz_Non_Sheet_DetailVO item : list) {
                    if (NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals(item.getType())) {
                        datalist.add(item);
                    }
                }
                if (null == datalist || datalist.size() <= 0) {
                    wb.removeSheetAt(i);
                    continue;
                }

                Date begin_date = DateUtils.parseDate(datalist.get(0).getBegin_date(), "yyyy-MM-dd");
                String yearStr = DateUtils.dateFormat(begin_date, "yy");

                if (NOSETTLEMENT_TAB_Enum.values()[i].getName().equals("VIP")) {
                    wb.setSheetName(i, yearStr + "年(" + NOSETTLEMENT_TAB_Enum.values()[i].getTitle() + ")" + datalist.get(0).getCus_no());
                } else {
                    wb.setSheetName(i, yearStr + "年" + NOSETTLEMENT_TAB_Enum.values()[i].getTitle() + datalist.get(0).getCus_no());
                }

                // sheet 对应 工作簿 - 统计表
                Sheet sheet = wb.getSheetAt(i);
                Cell cell_record = sheet.getRow(0).getCell(0);//标题
                cell_record.setCellValue("南京长安民生住久物流有限公司\r\n" + yearStr + "年非商品车运输费用结算表-" + datalist.get(0).getCus_no());
                Cell cell_dz_sheet = sheet.getRow(1).getCell(0);//时间
                cell_dz_sheet.setCellValue(DateUtils.dateFormat(begin_date, "yyyyMMdd"));
                Cell cell_contract_no = sheet.getRow(2).getCell(0); //合同号：
                cell_contract_no.setCellValue("合同号：" + list.get(0).getContract_no());

                int ishipment_qty=0;
                int ijs_qty=0;
                int iclosed_num=0;
                Float inot_tax_freight=0f;
                Float inot_tax_premium=0f;
                Float inot_tax_rescue_truck_startoric=0f;
                Float inot_tax_amount=0f;
                Float itax_amount=0f;
                Float inot_tax_other_amount=0f;



                Integer ColumnNumber = 0;
                for (int r = 0, rlen = datalist.size(); r < rlen; r++) {
                    Row row = sheet.createRow(r + 4);
                    ColumnNumber = 0;
                    //1 序号
                    CreateCell(row, xlsxStyle, ColumnNumber, String.valueOf(r + 1));
                    //2 交接单号
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getHandover_no());
                    if (NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals("0")) {
                        //3 车架号
                        ColumnNumber++;
                        CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getVin());
                    }
                    //4 发运日期
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getBegin_date());
                    //5 收车日期
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getReceipt_date());
                    //6 服务类型
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getService_type());
                    //7 运输方式
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getTrans_mode());
                    //8 运输车型
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getCar_type());
                    //9 承运数量
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getShipment_qty());
                    ishipment_qty+=Integer.valueOf(datalist.get(r).getShipment_qty());
                    //10 计费数量
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getJs_qty());
                    ijs_qty+=Integer.valueOf(datalist.get(r).getJs_qty());
                    //11 发运地
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getBegin_address());
                    //12 目的地
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getEnd_address());
                    if (NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals("2") || NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals("3")) {
                        // 合同线距离
                        ColumnNumber++;
                        CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getMil());
                    }
                    //13需求部门
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getDemand_sector());
                    //15计费标准
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getFreight_basis());

                    if (NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals("3")) {
                        //救援车元/台/KM（不含税）
                        ColumnNumber++;
                        CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getNot_tax_rescue_truck_price());
                    }
                    if (NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals("2")) {
                        //计费标准元/公里/台（不含税）
                        ColumnNumber++;
                        CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getNot_tax_price());
                    }
                    if (NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals("1")) {
                        //密闭式运输车元/车(不含税)
                        ColumnNumber++;
                        CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getNot_tax_losed_car_price());
                        //密闭式运输车台数
                        ColumnNumber++;
                        CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getClosed_num());
                        iclosed_num+=Integer.valueOf(datalist.get(r).getClosed_num());
                    }
                    if (NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals("0")) {
                        //16计费标准元/台（不含税）
                        ColumnNumber++;
                        CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getPrice());
                    }
                    //17运费小计（不含税）
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getNot_tax_freight());
                    inot_tax_freight+=Float.valueOf(datalist.get(r).getNot_tax_freight());
                    //18保费/台（不含税）
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getNot_tax_premium());
                    //19保费小计（不含税）  每台保费 * 结算台数
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, String.valueOf(Float.valueOf(datalist.get(r).getNot_tax_premium()) * Float.valueOf(datalist.get(r).getJs_qty())));
                    inot_tax_premium+=Float.valueOf(datalist.get(r).getNot_tax_premium()) * Float.valueOf(datalist.get(r).getJs_qty());

                    if (NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals("3")) {
                        //救援车起步价（不含税）
                        ColumnNumber++;
                        CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getNot_tax_rescue_truck_startoric());
                        inot_tax_rescue_truck_startoric+=Float.valueOf(datalist.get(r).getNot_tax_rescue_truck_startoric());
                    }
                    if (NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals("2") || NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals("1") || NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals("0")) {
                        //20其它费用（不含税）
                        ColumnNumber++;
                        CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getNot_tax_other_amount());
                        inot_tax_other_amount+=Float.valueOf(datalist.get(r).getNot_tax_other_amount());
                    }
                    //21备注
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getRemark());
                    //22费用小计 不含税
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getNot_tax_amount());
                    inot_tax_amount+=Float.valueOf(datalist.get(r).getNot_tax_amount());
                    //23费用小计 含税
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getTax_amount());
                    itax_amount+=Float.valueOf(datalist.get(r).getTax_amount());
                }
                //合计行
                Row row_total = sheet.createRow(datalist.size() + 4);
                for(int t=0;t<22;t++)
                {
                    CreateCell(row_total, xlsxStyleBold, t,"");
                }
                CreateCell(row_total, xlsxStyleBold, 0, "合计");
                if (NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals("3") || NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals("2")) {
                    //承运数量
                    CreateCell(row_total, xlsxStyleBold, 7,String.valueOf(ishipment_qty));
                    //计费数量
                    CreateCell(row_total, xlsxStyleBold, 8,String.valueOf(ijs_qty));
                    //运费小计 不含税
                    CreateCell(row_total, xlsxStyleBold, 15,String.valueOf(inot_tax_freight));
                    //保费小计 不含税
                    CreateCell(row_total, xlsxStyleBold, 17,String.valueOf(inot_tax_premium));
                    if (NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals("3")){
                        //救援车起步价 不含税
                        CreateCell(row_total, xlsxStyleBold, 18, String.valueOf(inot_tax_rescue_truck_startoric));
                    }else{
                        //其他费用(不含税)
                        CreateCell(row_total, xlsxStyleBold, 18,String.valueOf(inot_tax_other_amount));
                    }
                    //费用小计 不含税
                    CreateCell(row_total, xlsxStyleBold, 20,String.valueOf(inot_tax_amount));
                    //费用小计 含税
                    CreateCell(row_total, xlsxStyleBold, 21,String.valueOf(itax_amount));
                }
                else if (NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals("1")) {
                    //承运数量
                    CreateCell(row_total, xlsxStyleBold, 7,String.valueOf(ishipment_qty));
                    //计费数量
                    CreateCell(row_total, xlsxStyleBold, 8,String.valueOf(ijs_qty));
                    //密闭式运输车台数
                    CreateCell(row_total, xlsxStyleBold, 14,String.valueOf(iclosed_num));
                    //运费小计(不含税)
                    CreateCell(row_total, xlsxStyleBold, 15,String.valueOf(inot_tax_freight));
                    //保费小计 不含税
                    CreateCell(row_total, xlsxStyleBold, 17,String.valueOf(inot_tax_premium));
                    //其他费用(不含税)
                    CreateCell(row_total, xlsxStyleBold, 18,String.valueOf(inot_tax_other_amount));
                    //费用小计 不含税
                    CreateCell(row_total, xlsxStyleBold, 20,String.valueOf(inot_tax_amount));
                    //费用小计 含税
                    CreateCell(row_total, xlsxStyleBold, 21,String.valueOf(itax_amount));
                }
                else if (NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals("0")) {
                    //承运数量
                    CreateCell(row_total, xlsxStyleBold, 8,String.valueOf(ishipment_qty));
                    //计费数量
                    CreateCell(row_total, xlsxStyleBold, 9,String.valueOf(ijs_qty));
                    //运费小计(不含税)
                    CreateCell(row_total, xlsxStyleBold, 15,String.valueOf(inot_tax_freight));
                    //保费小计 不含税
                    CreateCell(row_total, xlsxStyleBold, 17,String.valueOf(inot_tax_premium));
                    //其他费用(不含税)
                    CreateCell(row_total, xlsxStyleBold, 18,String.valueOf(inot_tax_other_amount));
                    //费用小计 不含税
                    CreateCell(row_total, xlsxStyleBold, 20,String.valueOf(inot_tax_amount));
                    //费用小计 含税
                    CreateCell(row_total, xlsxStyleBold, 21,String.valueOf(itax_amount));
                }
                Row row_create_by = sheet.createRow(datalist.size() + 4+2);
                CreateCell(row_create_by, null, 0, "物流部制表：");
                CellStyle style = wb.createCellStyle();
                style.setBorderBottom(BorderStyle.THIN);//下边框
                CreateCell(row_create_by, null, 1, "");
                CreateCell(row_create_by, style, 2, "");
                CreateCell(row_create_by, style, 3, "");
                CreateCell(row_create_by, style, 4, "");
                CreateCell(row_create_by, style, 5, "");
                CreateCell(row_create_by, style, 6, "");
                CreateCell(row_create_by, style, 7, "");
                CreateCell(row_create_by, style, 8, "");
                CreateCell(row_create_by, style, 9, "");
                POIUtils.mergeCell(sheet,datalist.size()+10,datalist.size()+10,0,1);
            }

            if(resKind==1) {
                //写入文件
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                wb.write(outStream);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(outStream.toByteArray());
                FileOutputStream fos = new FileOutputStream(SaveFilePath);
                byte[] buf = new byte[1024];
                int bufLen;
                while ((bufLen = inputStream.read(buf)) > 0) {
                    fos.write(buf, 0, bufLen);
                }
                fos.flush();
                fos.close();
                inputStream.close();
                outStream.close();
            }else{
                //输出流
                //SaveFileName = new String(SaveFileName.getBytes("GBK"), "ISO8859_1");
                SaveFileName = new String(SaveFileName.getBytes("UTF-8"), "ISO8859_1");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + SaveFileName);
                response.setDateHeader("Expires", 0);
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Pragma", "no-cache");
                try (OutputStream out = response.getOutputStream()) {
                    wb.write(out);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            wb.close();
            wb=null;

            // 往Excel文件中写入数据 结束
        } catch (Exception e) {
            resultVO.setResultDataFull("写入Excel文件出错，原因："+e.getMessage());
            return resultVO;
        }

        //写入文件成功，返回 Excel 路径
        resultVO.setResultCode("0");
        resultVO.setResultDataFull(SaveFilePath);
        return resultVO;
    }

    private void CreateCell(Row row,CellStyle xlsxStyle,Integer ColumnNumber,String value)
    {
        Cell cell_begin_city = row.createCell(ColumnNumber);
        if(null!=xlsxStyle)
            cell_begin_city.setCellStyle(xlsxStyle);
        cell_begin_city.setCellValue(value);
    }




    @Log(value = "非商品车对上结算-对上账单管理-导出账单")
    @RequestMapping("exportData")
    public void exportData(Js_Dz_Non_SheetVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        if(null==vo) {
            response.getWriter().write("请选择需要导出的数据。");
            return ;
        }
        if(StringUtils.isBlank(vo.getDz_sheet())){
            response.getWriter().write("请选择需要导出的数据。");
            return ;
        }
        String[] dz_sheet_array=vo.getDz_sheet().split(",");
        List<Js_Dz_Non_Sheet_DetailVO> detailList=new ArrayList<Js_Dz_Non_Sheet_DetailVO>();
        for(String item:dz_sheet_array){
            Js_Dz_Non_Sheet_DetailVO info=new Js_Dz_Non_Sheet_DetailVO();
            info.setDz_sheet(item);
            detailList.add(info);
        }
        detailList=iJs_dz_non_sheetService.findListByProperty(detailList);
        if(null==detailList || detailList.size()<=0) {
            response.getWriter().write("没有查询到导出选择数据，数据可能被其他用户处理，请查询数据后再操作。");
            return ;
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        CreateDZ_SHEET_To_Excel(0,detailList,sysUserVO,request,response);
    }



    //LTJ:2019-08-06 业务审核-商务审核-财务审核
    // 用户等级：0：管理员；1：操作员；2：商务人员；3：财务人员；4：其它；
    // 数据状态：0.正常  1.业务审核.2商务审核.3.财务审核.4.发送邮件.5.客户确认
    @Log(value = "非商品车对上结算-对上账单管理-驳回数据")
    @RequestMapping(value = "replayData", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultVO replayData(@RequestBody Js_Dz_Non_Sheet_DetailVO vo) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        String id = ENDECodeUtils.URLDecode(vo.getId());
        String[] id_array = id.split(",");
        List<Js_Dz_Non_Sheet_DetailVO> list = new ArrayList<Js_Dz_Non_Sheet_DetailVO>();
        for (String item : id_array) {
            Js_Dz_Non_Sheet_DetailVO info = new Js_Dz_Non_Sheet_DetailVO();
            info.setId(item);
            list.add(info);
        }

        List<Js_Dz_Non_Sheet_DetailVO> datalist = iJs_dz_non_sheetService.findListByProperty(list);
        if (null == datalist || datalist.size() <= 0) {
            return ResultVO.failResult("没有查询到选中数据，原因：数据已被其他用户删除或处理，请查询数据后再操作。");
        }
        if (datalist.size() != list.size()) {
            return ResultVO.failResult("有部份数据已被其他用户处理，请查询数据后再操作。");
        }

        //判断状态 - 根据用户等级
        List<String> dz_sheet_list=new ArrayList<String>();
        String error_vin = "";
        String stateStr = "";
        for (Js_Dz_Non_Sheet_DetailVO item : datalist) {
            if(!dz_sheet_list.contains(item.getDz_sheet()))
            {
                dz_sheet_list.add(item.getDz_sheet());
            }
            if (sysUserVO.getUserLevel().equals("1")) //业务操作员 - 0 正常 状态
            {
                if (!item.getJs_state().equals("0")) {
                    error_vin += item.getVin() + ",";
                    stateStr = "正常";
                }
            } else if (sysUserVO.getUserLevel().equals("2")) //商务操作员 - 1 审核 状态
            {
                if (!item.getJs_state().equals("1")) {
                    error_vin += item.getVin() + ",";
                    stateStr = "审核";
                }
            } else if (sysUserVO.getUserLevel().equals("3")) //财务操作员 - 2 商务审核 状态
            {
                if (!item.getJs_state().equals("2")) {
                    error_vin += item.getVin() + ",";
                    stateStr = "商务审核";
                }
            }
        }

        if (StringUtils.isNotBlank(error_vin)) {
            return ResultVO.failResult("选中数据“" + stateStr + "”状态已发生变化(数据已被其他用户处理)，请查询数据后再操作。VIN：" + error_vin.substring(0, error_vin.length() - 1));
        }



        iJs_dz_non_sheetService.replayUpdateData(datalist);
        //明细表数据是否存在，不存在删除主表,否则重新统计金额
        for(String item:dz_sheet_list)
        {
            Js_Dz_Non_Sheet_DetailVO info=new Js_Dz_Non_Sheet_DetailVO();
            info.setDz_sheet(item);
            List<Js_Dz_Non_Sheet_DetailVO> templist = iJs_dz_non_sheetService.findListByProperty(info);
            if(null==templist || templist.size()<=0) {
                //删除主表
                iJs_dz_non_sheetService.deleteJs_Dz_Sheet(info);
            }
            else{
                //重新统计金额
                iJs_dz_non_sheetService.updateJs_Dz_Sheet(info);
            }
            templist=null;
        }
        return ResultVO.successResult("OK");
    }


    @Log(value = "非商品车对上结算-对上账单管理-撤消数据")
    @RequestMapping(value = "reback", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultVO reback(@RequestBody Js_Dz_Non_SheetVO vo) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        String dz_sheet = ENDECodeUtils.URLDecode(vo.getDz_sheet());
        String[] dz_sheet_array = dz_sheet.split(",");
        List<Js_Dz_Non_SheetVO> list = new ArrayList<Js_Dz_Non_SheetVO>();
        for (String item : dz_sheet_array) {
            Js_Dz_Non_SheetVO info = new Js_Dz_Non_SheetVO();
            info.setDz_sheet(item);
            list.add(info);
        }
        return iJs_dz_non_sheetService.reback(list);
    }

    @Log(value = "非商品车对上结算-对上账单管理-新增明细保存")
    @RequestMapping(value = "save", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultVO save(@RequestBody Js_Dz_Non_SheetVO vo) {
        return iJs_dz_non_sheetService.save(vo);
    }


    @Log(value = "非商品车对上结算-对上账单管理-一健账单号")
    @RequestMapping(value = "buildSheet", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultVO buildSheet(@RequestBody Js_Dz_Non_Sheet_DetailVO vo) {
        return iJs_dz_non_sheetService.buildSheet(vo);
    }

}


