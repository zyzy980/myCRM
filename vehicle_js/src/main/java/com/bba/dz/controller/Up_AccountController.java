package com.bba.dz.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.dz.service.api.IJs_Dz_SheetAccountService;
import com.bba.dz.service.api.IJs_Dz_SheetService;
import com.bba.dz.service.api.IJs_Dz_Sheet_DetailService;
import com.bba.dz.service.impl.Js_Dz_SheetService;
import com.bba.dz.vo.Js_Dz_SheetAccountVO;
import com.bba.dz.vo.Js_Dz_SheetExportVO;
import com.bba.dz.vo.Js_Dz_SheetVO;
import com.bba.dz.vo.Js_Dz_Sheet_DetailAccountVO;
import com.bba.jcda.vo.Cr_Customer_LinkVO;
import com.bba.util.*;
import com.bba.xtgl.vo.SysUserVO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
@RestController
@RequestMapping("dz/Up_AccountController")
public class Up_AccountController {

    @Autowired
    private IJs_Dz_SheetService js_dz_sheetService;
    @Autowired
    private IJs_Dz_Sheet_DetailService js_dz_sheet_detailService;

    @Autowired
    private IJs_Dz_SheetAccountService iJs_Dz_SheetAccountService;

    @Autowired
    private Js_Dz_SheetService js_Dz_SheetService;

    @Log(value = "结算管理-对上账单管理-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        customSearchFilters = MyUtils.decode(customSearchFilters);
        // 0：管理员；1：操作员；2：商务人员；3：财务人员；4：其它；
        // 数据状态：0.正常  1.业务审核.2商务审核.3.财务审核.4.发送邮件.5.客户确认
//        if (sysUserVO.getUserLevel().equals("1")) {
//            jqGridParamModel.put("state", "in", "'0','1'");
//        } else
        if (sysUserVO.getUserLevel().equals("2")) {
            jqGridParamModel.put("state", "in", "'1','2'");
        } else if (sysUserVO.getUserLevel().equals("3")) {
            jqGridParamModel.put("state", "in", "'2','3'");
        }
        PageVO pageVO = js_dz_sheetService.getListForGrid(jqGridParamModel, customSearchFilters);
        return pageVO;
    }

    @Log(value = "结算管理-对上账单管理-明细清单查询")
    @RequestMapping("getDetailListForGrid")
    public PageVO getDetailListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        customSearchFilters = MyUtils.decode(customSearchFilters);
        PageVO pageVO = js_dz_sheet_detailService.getListForGrid(jqGridParamModel, customSearchFilters);
        return pageVO;
    }


    @Log(value = "结算管理-对上账单管理-导出账单")
    @RequestMapping("export")
    public void export(Js_Dz_SheetAccountVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(null==vo) {
            response.getWriter().write("请选择需要导出的数据。");
            return ;
        }
        if(StringUtils.isBlank(vo.getDz_sheet())){
            response.getWriter().write("请选择需要导出的数据。");
            return ;
        }
        String[] dz_sheet_array=vo.getDz_sheet().split(",");
        List<Js_Dz_Sheet_DetailAccountVO> detailList=new ArrayList<Js_Dz_Sheet_DetailAccountVO>();
        for(String item:dz_sheet_array){
            Js_Dz_Sheet_DetailAccountVO info=new Js_Dz_Sheet_DetailAccountVO();
            info.setDz_sheet(item);
            detailList.add(info);
        }
        List<Js_Dz_Sheet_DetailAccountVO> bill_number_list = iJs_Dz_SheetAccountService.findBillNumber(detailList);//对账单下billnumber集合
        List<Js_Dz_SheetExportVO> exportVOList = new ArrayList<Js_Dz_SheetExportVO>();//需要导出账单数据集合
        //1、获取billnumber集合
        for (Js_Dz_Sheet_DetailAccountVO bill_vo:bill_number_list ) {
            Js_Dz_SheetExportVO exportVO = new Js_Dz_SheetExportVO();
            detailList=iJs_Dz_SheetAccountService.findListByProperty(bill_vo);
            if(null==detailList || detailList.size()<=0) {
                response.getWriter().write("没有查询到导出选择数据，数据可能被其他用户处理，请查询数据后再操作。");
                return ;
            }
            List<Js_Dz_Sheet_DetailAccountVO> masterList=iJs_Dz_SheetAccountService.GetJs_Dz_Sheet_DetailVO(detailList);
            exportVO.setMasterList(masterList);
            exportVO.setDetailList(detailList);
            exportVOList.add(exportVO);
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        CreateDZ_SHEET_To_Excel(0,exportVOList,sysUserVO,request,response);
    }



    //LTJ:2019-07-22 业务审核-商务审核-财务审核
    // 0：管理员；1：操作员；2：商务人员；3：财务人员；4：其它；
    // 数据状态：0.正常  1.业务审核.2商务审核.3.财务审核.4.发送邮件.5.客户确认
    @Log(value = "结算管理-对上账单管理-审核数据")
    @RequestMapping(value = "data_check", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultVO data_check(@RequestBody Js_Dz_SheetAccountVO vo,HttpServletRequest request,HttpServletResponse response) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        String dz_sheet = ENDECodeUtils.URLDecode(vo.getDz_sheet());
        String[] dz_sheet_array = dz_sheet.split(",");
        List<Js_Dz_SheetAccountVO> list = new ArrayList<Js_Dz_SheetAccountVO>();
        for (String item : dz_sheet_array) {
            Js_Dz_SheetAccountVO info = new Js_Dz_SheetAccountVO();
            info.setDz_sheet(item);
            list.add(info);
        }
        //验证状态
        List<Js_Dz_SheetAccountVO> datalist = iJs_Dz_SheetAccountService.findListByProperty(list);
        if (null == datalist || datalist.size() <= 0) {
            return ResultVO.failResult("没有查询到选中数据，原因：数据已被其他用户删除或处理，请查询数据后再操作。");
        }
        if (datalist.size() != list.size()) {
            return ResultVO.failResult("有部份数据已被其他用户处理，请查询数据后再操作。");
        }
        String error = "";
        for (Js_Dz_SheetAccountVO item : datalist) {
            if (!vo.getJs_batch().equals(item.getState())) {
                error += item.getDz_sheet() + ",";
                continue;
            }
            //查询该对账单下是否存在空账单编号的数据，存在不予通过
            Integer count = iJs_Dz_SheetAccountService.findNullBillNumber(item);
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
        for (Js_Dz_SheetAccountVO item : list) {
            item.setState(vo.getState());//更新为这状态值
            if (null != vo.getRemark() && "mail".equalsIgnoreCase(vo.getRemark())) {
                item.setSend_mail_date(currentDate);
                bMail = true;
            }
            if("5".equals(vo.getState()))
            {
                //客户确认
                item.setReturn_date(currentDate);
            }
        }
        //发送邮件 按钮
        if (bMail) {
            ResultVO rvo=SendMail(list,sysUserVO, request, response);
            if(!rvo.getResultCode().equals("0")){
                return rvo;
            }
            //2、发送邮件时，更新JS_VIN_AMOUNT表中的DELIVERY_DATE  =  iJs_Dz_SheetAccountService.UpdateJs_Dz_Sheet_state(list);
            //iJs_Dz_SheetAccountService.UpdateJs_Vin_Amount(list);
        }
        return iJs_Dz_SheetAccountService.UpdateJs_Dz_Sheet_state(list,vo);
    }

    //发送邮件
    private ResultVO SendMail(List<Js_Dz_SheetAccountVO> list,SysUserVO sysUserVO,HttpServletRequest request,HttpServletResponse response)
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
        List<Js_Dz_Sheet_DetailAccountVO> detailList=new ArrayList<Js_Dz_Sheet_DetailAccountVO>();
        for(Js_Dz_SheetAccountVO item:list){
            Js_Dz_Sheet_DetailAccountVO vo=new Js_Dz_Sheet_DetailAccountVO();
            vo.setDz_sheet(item.getDz_sheet());
            detailList.add(vo);
        }
        List<Js_Dz_Sheet_DetailAccountVO> bill_number_list = iJs_Dz_SheetAccountService.findBillNumber(detailList);//对账单下billnumber集合
        List<Js_Dz_SheetExportVO> exportVOList = new ArrayList<Js_Dz_SheetExportVO>();//需要导出账单数据集合
        //1、获取billnumber集合
        for (Js_Dz_Sheet_DetailAccountVO bill_vo:bill_number_list ) {
            Js_Dz_SheetExportVO exportVO = new Js_Dz_SheetExportVO();
            List<Js_Dz_Sheet_DetailAccountVO> detailsList=iJs_Dz_SheetAccountService.findListByProperty(bill_vo);
            List<Js_Dz_Sheet_DetailAccountVO> masterList=iJs_Dz_SheetAccountService.GetJs_Dz_Sheet_DetailVO(detailsList);
            exportVO.setMasterList(masterList);
            exportVO.setDetailList(detailsList);
            exportVOList.add(exportVO);
        }
        detailList = iJs_Dz_SheetAccountService.findListByProperty(detailList);
        List<String> cusList=new ArrayList<String>();
        for(Js_Dz_Sheet_DetailAccountVO item:detailList){
            if(!cusList.contains(item.getCus_no())){
                cusList.add(item.getCus_no());
            }
        }
        for(String cus_code:cusList) {
            //过滤要发送数据
            List<Js_Dz_Sheet_DetailAccountVO> sendDataList=new ArrayList<Js_Dz_Sheet_DetailAccountVO>();
            for(Js_Dz_Sheet_DetailAccountVO item:detailList){
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
            List<Cr_Customer_LinkVO> linkList = iJs_Dz_SheetAccountService.findListByProperty(linkVO);
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
            //List<Js_Dz_Sheet_DetailAccountVO> masterList=iJs_Dz_SheetAccountService.GetJs_Dz_Sheet_DetailVO(sendDataList);
            ResultVO resultVO=CreateDZ_SHEET_To_Excel(1,exportVOList,sysUserVO, request, response);
            if(!resultVO.getResultCode().equals("0"))
                return resultVO;

            //发送附件路径
            String attachmentFilePath =resultVO.getResultDataFull().toString();
            //发送附件名称
            String attachmentFilename=attachmentFilePath.substring(attachmentFilePath.lastIndexOf(File.separator)+1);
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
            //数据需要汇总统计 - 数量 \ 金额
            //表格需要根据税率分开
            Set<String> tax_set = new HashSet();
            List<String> distinctList=new ArrayList<String>();
            List<Js_Dz_Sheet_DetailAccountVO> mailBodyList=new ArrayList<Js_Dz_Sheet_DetailAccountVO>();
            for(int i=0,ilen=sendDataList.size();i<ilen;i++) {
                //去重税率
                tax_set.add(sendDataList.get(i).getTax_rate());
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
                    for (Js_Dz_Sheet_DetailAccountVO bodyItem : mailBodyList) {
                        if (bodyItem.getBill_number().equals(sendDataList.get(i).getBill_number()) && bodyItem.getTax_rate().equals(sendDataList.get(i).getTax_rate())) {
                            bodyItem.setJs_qty(String.valueOf(Integer.valueOf(bodyItem.getJs_qty()) + (null==sendDataList.get(i).getJs_qty()?0:Integer.valueOf(sendDataList.get(i).getJs_qty()))));
                            bodyItem.setNot_tax_amount(String.valueOf(Double.valueOf(bodyItem.getNot_tax_amount()) + (null==sendDataList.get(i).getNot_tax_amount()?0d:Double.valueOf(sendDataList.get(i).getNot_tax_amount()))));
                            bodyItem.setTax_amount(String.valueOf(Double.valueOf(bodyItem.getTax_amount()) + (null==sendDataList.get(i).getTax_amount()?0d:Double.valueOf(sendDataList.get(i).getTax_amount()))));
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
                html+="<tr  style='background:#eff5fb'>";
                html+="<td align='center' style='width:150px;font-weight:bold'>"+(index==0?"账单编号":"")+"</td><td align='center' style='width:100px;font-weight:bold'>"+(index==0?"运输台数":"")+"</td><td align='center'  style='width:130px;font-weight:bold'>"+(index==0?"不含税金额":"")+"</td><td align='center'   style='width:130px;font-weight:bold'>含税金额("+String.valueOf(Double.valueOf(tax_rate)*100).replace(".0","")+"%"+")</td>";
                html+="</tr>";
                for(int i=0,ilen=mailBodyList.size();i<ilen;i++) {
                    if (StringUtils.equals(mailBodyList.get(i).getTax_rate(),tax_rate)) {
                        js_qty+=StringUtils.isBlank(mailBodyList.get(i).getJs_qty())?0:Integer.valueOf(mailBodyList.get(i).getJs_qty());
                        not_tax_amount+=StringUtils.isBlank(mailBodyList.get(i).getNot_tax_amount())?0.0:Double.valueOf(mailBodyList.get(i).getNot_tax_amount());
                        tax_amount+=StringUtils.isBlank(mailBodyList.get(i).getTax_amount())?0.0:Double.valueOf(mailBodyList.get(i).getTax_amount());
                        html += "<tr>";
                        html += "<td align='center'>" + mailBodyList.get(i).getBill_number() + "</td><td align='center'>" + mailBodyList.get(i).getJs_qty() + "</td><td align='center'>" +MyUtils.formatDouble2(Double.valueOf(mailBodyList.get(i).getNot_tax_amount())) + "</td><td align='center'>" +MyUtils.formatDouble2(Double.valueOf(mailBodyList.get(i).getTax_amount()))+"</td>";
                        html += "</tr>";
                    }
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
    private Js_Dz_Sheet_DetailAccountVO GetCarTypeModel(String CarType,Js_Dz_Sheet_DetailAccountVO vo,List<Js_Dz_Sheet_DetailAccountVO> list)
    {
        Js_Dz_Sheet_DetailAccountVO info=null;
        for(Js_Dz_Sheet_DetailAccountVO item: list)
        {
            if(CarType.equalsIgnoreCase(item.getCar_type()) && vo.getBegin_city().equals(item.getBegin_city()) && vo.getEnd_city().equals(item.getEnd_city()) && vo.getTrans_mode().equals(item.getTrans_mode()))
            {
                info=item;
                break;
            }
        }
        return info;
    }
    //生成对账单Excel文件
    //resKind=0直接输出；=1写入文件
    private ResultVO CreateDZ_SHEET_To_Excel(int resKind,List<Js_Dz_SheetExportVO> exportVOList,SysUserVO sysUserVO,HttpServletRequest request,HttpServletResponse response)
    {
        ResultVO resultVO = new ResultVO();
        resultVO.setResultCode("1");
        int index=0;
        XSSFWorkbook wb = new XSSFWorkbook();//导出的excel
        String separator= File.separator;
        String SaveFilePath=System.getProperty("user.dir") + separator + "Excel" + separator + "dz_sheet" + separator + DateUtils.nowDate("yyyyMMdd") + separator;
        String SaveFileName = "CMAR10-071 0A商品车运输费用结算表" + DateUtils.dateFormat(DateUtils.parseDate(DateUtils.nowDate(), "yyyy-MM-dd HH:mm:ss"), "MM.dd") + ".xlsx";
        SaveFilePath += SaveFileName;
        int iRun = 0;
        try {
            for (int j=0;j< exportVOList.size();j++) {
                Js_Dz_SheetExportVO exportVO = exportVOList.get(j);
                List<Js_Dz_Sheet_DetailAccountVO> list = exportVO.getDetailList();
                List<Js_Dz_Sheet_DetailAccountVO> masterList = exportVO.getMasterList();
                File file = new File(SaveFilePath);
                if (!file.exists() && !file.isDirectory()) {
                    file.mkdirs();
                }

                /**
                 * 获取合同号，判断是公路运输还是多式联运
                 * */
                String contract_no = list.get(0).getContract_no();
                String flag = iJs_Dz_SheetAccountService.transModeSelect(contract_no);
                //获取客户合同号
                String contract_sheet_no = list.get(0).getContract_sheet_no();
                String cus_contract_no = iJs_Dz_SheetAccountService.getCusContractNo(contract_sheet_no);
                String url = null;
                if (flag!=null) {//说明是公路
                    url = "static/Resource/excel/dz/dz_sheet_template.xlsx";
                } else {
                    url = "static/Resource/excel/dz/dz_sheet_template_2.xlsx";
                }
                //往Excel文件中写入数据 读取模板
                InputStream fis = ClassUtils.class.getClassLoader().getResourceAsStream(url);
                file = new File(SaveFilePath);
                if (file.exists()) {
                    file.delete();
                }
                // 读取Excel文档
                XSSFWorkbook model_wb = new XSSFWorkbook(fis);
                String sheetDate = list.get(0).getCreate_date().split(" ")[0].replace("-", "");
                model_wb.setSheetName(0, "统计" + sheetDate);
                model_wb.setSheetName(1, "明细" + sheetDate);
                // 模板页
                XSSFSheet sheetModel = null;
                XSSFSheet sheetModel_detail = null;
                sheetModel = model_wb.getSheetAt(0);
                sheetModel_detail = model_wb.getSheetAt(1);
                //新建的Sheet页
                XSSFSheet newSheet = null;
                XSSFSheet newSheet_detail = null;
                newSheet = wb.createSheet("统计"+exportVO.getMasterList().get(0).getBill_number());
                newSheet_detail = wb.createSheet("明细"+exportVO.getMasterList().get(0).getBill_number());
                newSheet.setDisplayGridlines(false);//去除网格线
                POIUtils.copySheet(wb, sheetModel, newSheet);
                POIUtils.copySheet(wb, sheetModel_detail, newSheet_detail);
                //插入图片
                InputStream imagefis = ClassUtils.class.getClassLoader().getResourceAsStream("static/Resource/excel/dz/mzd.png");
                ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                BufferedImage bufferImg = ImageIO.read(imagefis);
                ImageIO.write(bufferImg,"png",byteArrayOut);
                XSSFDrawing patriarch = newSheet.createDrawingPatriarch();
                XSSFClientAnchor anchor = new XSSFClientAnchor(0,0,0,100,(short) 0,0,(short)3,2);
                patriarch.createPicture(anchor , wb.addPicture(byteArrayOut.toByteArray(),XSSFWorkbook.PICTURE_TYPE_JPEG));
                //创建单元格样式
                CellStyle xlsxStyle = POIUtils.getTextStype(wb);
                CellStyle numDoublexlsxStyle =POIUtils.getNumDoubleStype(wb);
                CellStyle numIntegerxlsxStyle =POIUtils.getNumIntegerStype(wb);
                Font font = wb.createFont();
                font.setFontName("arial");
                font.setFontHeightInPoints((short) 10);
                xlsxStyle.setFont(font);
                numDoublexlsxStyle.setFont(font);
                numIntegerxlsxStyle.setFont(font);

                // sheet 对应 工作簿1 - 统计表
                Sheet sheet = wb.getSheetAt(index);
                index++;
                Integer ColumnNumber = 0;
                /**公路模板*/
                if (flag!=null) {
                    Cell cell_record = sheet.getRow(1).getCell(16); //Record No.:CMAR10-071 0A
                    cell_record.setCellValue("Record No.:CMAR10-071 0A"/*+list.get(0).getCus_no()*/);
                    Cell cell_dz_sheet = sheet.getRow(2).getCell(17); //单据编号：
                    cell_dz_sheet.setCellValue("ZC-"+list.get(0).getBill_number());
                    Cell cell_contract_no = sheet.getRow(3).getCell(17); //客户合同号：
                    cell_contract_no.setCellValue(cus_contract_no);

                    Integer sum_j36_qty = 0;
                    Integer sum_j59r_qty = 0;
                    Integer sum_j72x_qty = 0;
                    Integer sum_j72y_qty = 0;

                    Double sum_total_j36 = 0d;
                    Double sum_total_j59r = 0d;
                    Double sum_total_j72x = 0d;
                    Double sum_total_j72y = 0d;
                    Double sum_total_tax = 0d;

                    List<Js_Dz_Sheet_DetailAccountVO> for_detail = new ArrayList<Js_Dz_Sheet_DetailAccountVO>();
                    List<String> distinctCarTypeList = new ArrayList<String>();
                    for (Js_Dz_Sheet_DetailAccountVO item : masterList) {
                        String con = item.getBegin_city() + item.getEnd_city() + item.getTrans_mode();
                        if (!distinctCarTypeList.contains(con)) {
                            for_detail.add(item);
                            distinctCarTypeList.add(con);
                        }
                    }
                    int iRow = 0;
                    for (Js_Dz_Sheet_DetailAccountVO item : for_detail) {
                        Row row = sheet.createRow(iRow + 7);
                        ColumnNumber = 0;
                        //1序号
                        POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, String.valueOf(iRow + 1));
                        //2起运地
                        ColumnNumber++;
                        POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, item.getBegin_city());
                        //3目的地
                        ColumnNumber++;
                        POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, item.getEnd_city());
                        //4运输方式
                        ColumnNumber++;
                        POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, item.getTrans_mode());
                        Integer j36 = 0;
                        Integer j59r = 0;
                        Integer j72x = 0;
                        Integer j72y = 0;
                        Double j36_amount = 0d;
                        Double j72x_amount = 0d;
                        Double j59r_amount = 0d;
                        Double j72y_amount = 0d;
                        Double mil = 0d;
                        Double sum_not_tax_amount = 0d;
                        Double sum_tax_amount = 0d;
                        Js_Dz_Sheet_DetailAccountVO info = GetCarTypeModel("J36", item, masterList);
                        if (null != info) {
                            j36 = null != info.getJs_qty() ? Integer.valueOf(info.getJs_qty()) : 0;
                            j36_amount = j36 * Double.valueOf(null != info.getNot_tax_amount() ? info.getNot_tax_amount() : "0");
                            sum_not_tax_amount = sum_not_tax_amount + j36_amount;
                            sum_tax_amount = sum_tax_amount + j36 * Double.valueOf(null != info.getTax_amount() ? info.getTax_amount() : "0");
                            mil = mil + Double.valueOf(null != info.getMil() ? info.getMil() : "0");
                            sum_j36_qty = sum_j36_qty + j36;
                            sum_total_j36 = sum_total_j36 + j36_amount;
                        }
                        info = GetCarTypeModel("J59R", item, masterList);
                        if (null != info) {
                            j59r = null != info.getJs_qty() ? Integer.valueOf(info.getJs_qty()) : 0;
                            j59r_amount = j59r * Double.valueOf(null != info.getNot_tax_amount() ? info.getNot_tax_amount() : "0");
                            sum_not_tax_amount = sum_not_tax_amount + j59r_amount;
                            sum_tax_amount = sum_tax_amount + j59r * Double.valueOf(null != info.getTax_amount() ? info.getTax_amount() : "0");
                            mil = mil + Double.valueOf(null != info.getMil() ? info.getMil() : "0");
                            sum_j59r_qty = sum_j59r_qty + j59r;
                            sum_total_j59r = sum_total_j59r + j59r_amount;
                        }
                        info = GetCarTypeModel("J72X", item, masterList);
                        if (null != info) {
                            j72x = null != info.getJs_qty() ? Integer.valueOf(info.getJs_qty()) : 0;
                            j72x_amount = j72x * Double.valueOf(null != info.getNot_tax_amount() ? info.getNot_tax_amount() : "0");
                            sum_not_tax_amount = sum_not_tax_amount + j72x_amount;
                            sum_tax_amount = sum_tax_amount + j72x * Double.valueOf(null != info.getTax_amount() ? info.getTax_amount() : "0");
                            mil = mil + Double.valueOf(null != info.getMil() ? info.getMil() : "0");
                            sum_j72x_qty = sum_j72x_qty + j72x;
                            sum_total_j72x = sum_total_j72x + j72x_amount;
                        }
                        info = GetCarTypeModel("J72Y", item, masterList);
                        if (null != info) {
                            j72y = null != info.getJs_qty() ? Integer.valueOf(info.getJs_qty()) : 0;
                            j72y_amount = j72y * Double.valueOf(null != info.getNot_tax_amount() ? info.getNot_tax_amount() : "0");
                            sum_not_tax_amount = sum_not_tax_amount + j72y_amount;
                            sum_tax_amount = sum_tax_amount + j72y * Double.valueOf(null != info.getTax_amount() ? info.getTax_amount() : "0");
                            mil = mil + Double.valueOf(null != info.getMil() ? info.getMil() : "0");
                            sum_j72y_qty = sum_j72y_qty + j72y;
                            sum_total_j72y = sum_total_j72y + j72y_amount;
                        }
                        sum_total_tax += sum_tax_amount;
                        //5计费数量 - J36
                        ColumnNumber++;
                        POIUtils.CreateNumCell(row, numIntegerxlsxStyle, ColumnNumber, Double.parseDouble(j36.toString()));
                        //6计费数量 - J59R
                        ColumnNumber++;
                        POIUtils.CreateNumCell(row, numIntegerxlsxStyle, ColumnNumber, Double.parseDouble(j59r.toString()));
                        //7计费数量 - J72X
                        ColumnNumber++;
                        POIUtils.CreateNumCell(row, numIntegerxlsxStyle, ColumnNumber, Double.parseDouble(j72x.toString()));
                        //8计费数量 - J72Y
                        ColumnNumber++;
                        POIUtils.CreateNumCell(row, numIntegerxlsxStyle, ColumnNumber,Double.parseDouble(j72y.toString()));
                        //9里程
                        ColumnNumber++;
                        POIUtils.CreateNumCell(row, numIntegerxlsxStyle, ColumnNumber, Double.parseDouble(mil.toString()));
                        //9运费单价
                        ColumnNumber++;
                        POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber,  Double.parseDouble(item.getNot_tax_price()));
                        //10保费单价
                        ColumnNumber++;
                        POIUtils.CreateNumCell(row, numIntegerxlsxStyle, ColumnNumber, Double.parseDouble(item.getNot_tax_premium()));
               /* //11IT费单价
                ColumnNumber++;
                CreateCell(row,xlsxStyle,ColumnNumber,"0");*/
                        //12其它费用单价
                        ColumnNumber++;
                        POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber,  Double.parseDouble(item.getNot_tax_other_amount()));
                        //13费用小计 - J36
                        ColumnNumber++;
                        POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber,j36_amount);
                        //13费用小计 - J59R
                        ColumnNumber++;
                        POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber, j59r_amount);
                        //14费用小计 - J72X
                        ColumnNumber++;
                        POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber, j72x_amount);
                        //15费用小计 - J72Y
                        ColumnNumber++;
                        POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber, j72y_amount);
                        //16费用小计 - 合计
                        ColumnNumber++;
                        POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber, sum_not_tax_amount);
                        //17费用 （含税）
                        ColumnNumber++;
                        POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber, sum_tax_amount);
                        //18备注
                        ColumnNumber++;
                        POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, item.getRemark());
                        //19备注1
                        ColumnNumber++;
                        POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, "");
                        //20备注2
                        ColumnNumber++;
                        POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, "");

                        iRow++;
                    }
                    //底部统计
                    Row rowTotal = sheet.createRow(for_detail.size()+7);
                    ColumnNumber = 0;
                    POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "总计");
                    ColumnNumber++;
                    POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
                    ColumnNumber++;
                    POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
                    ColumnNumber++;
                    POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
                    ColumnNumber++;
                    POIUtils.CreateNumCell(rowTotal, numIntegerxlsxStyle, ColumnNumber, Double.parseDouble(String.valueOf(sum_j36_qty)));
                    ColumnNumber++;
                    POIUtils.CreateNumCell(rowTotal, numIntegerxlsxStyle, ColumnNumber, Double.parseDouble(String.valueOf(sum_j59r_qty)));
                    ColumnNumber++;
                    POIUtils.CreateNumCell(rowTotal, numIntegerxlsxStyle, ColumnNumber, Double.parseDouble(String.valueOf(sum_j72x_qty)));
                    ColumnNumber++;
                    POIUtils.CreateNumCell(rowTotal, numIntegerxlsxStyle, ColumnNumber, Double.parseDouble(String.valueOf(sum_j72y_qty)));
                    ColumnNumber++;
                    POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
           /* ColumnNumber++;
            CreateCell(rowTotal,xlsxStyle,ColumnNumber,"");*/
                    ColumnNumber++;
                    POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
                    ColumnNumber++;
                    POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
                    ColumnNumber++;
                    POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
                    ColumnNumber++;
                    POIUtils.CreateNumCell(rowTotal, numDoublexlsxStyle, ColumnNumber,MyUtils.formatDouble2(sum_total_j36));
                    ColumnNumber++;
                    POIUtils.CreateNumCell(rowTotal, numDoublexlsxStyle, ColumnNumber,MyUtils.formatDouble2(sum_total_j59r));
                    ColumnNumber++;
                    POIUtils.CreateNumCell(rowTotal, numDoublexlsxStyle, ColumnNumber,MyUtils.formatDouble2(sum_total_j72x));
                    ColumnNumber++;
                    POIUtils.CreateNumCell(rowTotal, numDoublexlsxStyle, ColumnNumber,MyUtils.formatDouble2(sum_total_j72y));
                    ColumnNumber++;
                    POIUtils.CreateNumCell(rowTotal, numDoublexlsxStyle, ColumnNumber,MyUtils.formatDouble2(sum_total_j36 + sum_total_j59r + sum_total_j72x + sum_total_j72y));
                    ColumnNumber++;
                    POIUtils.CreateNumCell(rowTotal, numDoublexlsxStyle, ColumnNumber, MyUtils.formatDouble2(sum_total_tax));
                    ColumnNumber++;
                    POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
                    ColumnNumber++;
                    POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
                    ColumnNumber++;
                    POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");

                    Row rowNull_1 = sheet.createRow(for_detail.size() + 7 + 1);
                    Row rowNull_2 = sheet.createRow(for_detail.size() + 7 + 2);
                    Row rowBuildTable = sheet.createRow(for_detail.size() + 7 + 3);
                    POIUtils.CreateCell(rowBuildTable, null, 0, "物流部制表：");
                    CellStyle style = wb.createCellStyle();
                    style.setBorderBottom(BorderStyle.THIN);//下边框
                    POIUtils.CreateCell(rowBuildTable, null, 1, "");
                    POIUtils.CreateCell(rowBuildTable, style, 2, "");
                    POIUtils.CreateCell(rowBuildTable, style, 3, "");
                    POIUtils.CreateCell(rowBuildTable, style, 4, "");
                    POIUtils.CreateCell(rowBuildTable, style, 5, "");
                    POIUtils.CreateCell(rowBuildTable, style, 6, "");
                    POIUtils.CreateCell(rowBuildTable, style, 7, "");
                    POIUtils.CreateCell(rowBuildTable, style, 8, "");
                    POIUtils.CreateCell(rowBuildTable, style, 9, "");
                    POIUtils.mergeCell(sheet,for_detail.size()+10,for_detail.size()+10,0,1);
                } else {
                    /**多式联运模板*/
                    sheet = createSheetMultimodalTransport(sheet,list,masterList,wb,cus_contract_no);
                }
                //合并单元格，物流部制表

                // sheet 对应 工作簿2 - 明细表
                CellStyle xlsxdetailStyle = wb.createCellStyle();
                xlsxdetailStyle.setBorderLeft(BorderStyle.THIN);
                xlsxdetailStyle.setBorderTop(BorderStyle.THIN);
                xlsxdetailStyle.setBorderRight(BorderStyle.THIN);
                xlsxdetailStyle.setBorderBottom(BorderStyle.THIN);
                xlsxdetailStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                xlsxdetailStyle.setAlignment(HorizontalAlignment.CENTER);
                Font detail_font = wb.createFont();
                detail_font.setFontName("宋体");
                detail_font.setFontHeightInPoints((short) 10);
                xlsxdetailStyle.setFont(detail_font);

                //明细
                Sheet detail_sheet = wb.getSheetAt(index);
                index++;
                for (int i = 0; i < list.size(); i++) {
                    Row row = detail_sheet.createRow(i + 2);
                    ColumnNumber = 0;
                    //1序号
                    POIUtils.CreateCell(row, xlsxdetailStyle, ColumnNumber, String.valueOf(i + 1));
                    //2运单号
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxdetailStyle, ColumnNumber, list.get(i).getVdr_no());
                    //2VIN
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxdetailStyle, ColumnNumber, list.get(i).getVin());
                    //2发运日期
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxdetailStyle, ColumnNumber, list.get(i).getBegin_date().indexOf(" ") != -1 ? list.get(i).getBegin_date().split(" ")[0] : list.get(i).getBegin_date());
                    //2收车日期
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxdetailStyle, ColumnNumber, list.get(i).getReceipt_date().indexOf(" ") != -1 ? list.get(i).getReceipt_date().split(" ")[0] : list.get(i).getReceipt_date());
                    //2经销商名称
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxdetailStyle, ColumnNumber, list.get(i).getDealer_name());
                    //2运输方式
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxdetailStyle, ColumnNumber, list.get(i).getTrans_mode());
                    //2起点
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxdetailStyle, ColumnNumber, list.get(i).getBegin_city());
                    //2终点
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxdetailStyle, ColumnNumber, list.get(i).getEnd_city());
                    //2运输车型
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxdetailStyle, ColumnNumber, list.get(i).getCar_type());
                    //单车费用小计（不含税）
                    ColumnNumber++;
                    POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber, MyUtils.formatDouble2(Double.valueOf(list.get(i).getNot_tax_amount())));
                    //备注1
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxdetailStyle, ColumnNumber, list.get(i).getRemark());
                    //备注2
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxdetailStyle, ColumnNumber, "");
                }
            }
            if (resKind == 1) {
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
            } else {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            wb.close();
            wb = null;
                //往Excel文件中写入数据 结束
            } catch (Exception e) {
                resultVO.setResultDataFull("写入Excel文件出错，原因：iRun=" + String.valueOf(iRun) + "；" + e.getMessage());
                return resultVO;
            }
            //写入文件成功，返回 Excel 路径
            resultVO.setResultCode("0");
            resultVO.setResultDataFull(SaveFilePath);
            return resultVO;
    }


    //LTJ:2019-07-23 业务审核-商务审核-财务审核
    // 用户等级：0：管理员；1：操作员；2：商务人员；3：财务人员；4：其它；
    // 数据状态：0.正常  1.业务审核.2商务审核.3.财务审核.4.发送邮件.5.客户确认
    @Log(value = "结算管理-对上账单管理-驳回数据")
    @RequestMapping(value = "replayData", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultVO replayData(@RequestBody Js_Dz_Sheet_DetailAccountVO vo) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        String id = ENDECodeUtils.URLDecode(vo.getId());
        String[] id_array = id.split(",");
        List<Js_Dz_Sheet_DetailAccountVO> list = new ArrayList<Js_Dz_Sheet_DetailAccountVO>();
        for (String item : id_array) {
            Js_Dz_Sheet_DetailAccountVO info = new Js_Dz_Sheet_DetailAccountVO();
            info.setId(item);
            list.add(info);
        }

        List<Js_Dz_Sheet_DetailAccountVO> datalist = iJs_Dz_SheetAccountService.findListByProperty(list);
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
        for (Js_Dz_Sheet_DetailAccountVO item : datalist) {
            //客户确认 ||  生成台账 不能驳回
            if ("5".equals(item.getData_state()) || "6".equals(item.getData_state())) {
                error_vin += item.getVin() + ",";
            }
        }
        if (StringUtils.isNotBlank(error_vin)) {
            return ResultVO.failResult("选中数据“客户确认”或“生成台账”状态不能驳回，请查询数据后再操作。VIN：" + error_vin.substring(0, error_vin.length() - 1));
        }

        for (Js_Dz_Sheet_DetailAccountVO item : datalist) {
            if(!dz_sheet_list.contains(item.getDz_sheet()))
            {
                dz_sheet_list.add(item.getDz_sheet());
            }
            if (sysUserVO.getUserLevel().equals("1")) //业务操作员 - 0=正常 或 4=发送邮件 状态
            {
                if ("0".equals(item.getData_state()) || "4".equals(item.getData_state())) {
                    //OK
                } else {
                    error_vin += item.getVin() + ",";
                    stateStr = "“正常”、“发送邮件”";
                }
            } else if (sysUserVO.getUserLevel().equals("2")) //商务操作员 - 1 审核 状态
            {
                if (!item.getData_state().equals("1")) {
                    error_vin += item.getVin() + ",";
                    stateStr = "“审核”";
                }
            } else if (sysUserVO.getUserLevel().equals("3")) //财务操作员 - 2 商务审核 状态
            {
                if (!item.getData_state().equals("2")) {
                    error_vin += item.getVin() + ",";
                    stateStr = "“商务审核”";
                }
            }
        }

        if (StringUtils.isNotBlank(error_vin)) {
            return ResultVO.failResult("选中数据" + stateStr + "状态已发生变化(数据已被其他用户处理)，请查询数据后再操作。VIN：" + error_vin.substring(0, error_vin.length() - 1));
        }



        iJs_Dz_SheetAccountService.replayUpdateData(datalist);
        //明细表数据是否存在，不存在删除主表,否则重新统计金额
        for(String item:dz_sheet_list)
        {
            Js_Dz_Sheet_DetailAccountVO info=new Js_Dz_Sheet_DetailAccountVO();
            info.setDz_sheet(item);
            List<Js_Dz_Sheet_DetailAccountVO> templist = iJs_Dz_SheetAccountService.findListByProperty(info);
            if(null==templist || templist.size()<=0) {
                //删除主表
                iJs_Dz_SheetAccountService.deleteJs_Dz_Sheet(info);
            }
            else{
                //重新统计金额
                iJs_Dz_SheetAccountService.updateJs_Dz_Sheet(info);
            }
            templist=null;
        }

        return ResultVO.successResult("OK");
    }


    @Log(value = "结算管理-对上账单管理-撤消数据")
    @RequestMapping(value = "reback", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultVO reback(@RequestBody Js_Dz_SheetAccountVO vo) {
        //1、首先判断状态
        Js_Dz_SheetVO paramJs_Dz_SheetVO = new Js_Dz_SheetVO();
        paramJs_Dz_SheetVO.setDz_sheet(vo.getDz_sheet());
        EntityWrapper dzWrapper = new EntityWrapper();
        dzWrapper.setEntity(paramJs_Dz_SheetVO);
        Js_Dz_SheetVO dataJs_Dz_SheetVO = js_Dz_SheetService.selectOne(dzWrapper);
        if (dataJs_Dz_SheetVO==null) {
            return ResultVO.failResult("操作的对账单不存在，请核实数据");
        } else if (StringUtils.notEquals(dataJs_Dz_SheetVO.getState(),"0")) {
            return ResultVO.failResult("非‘正常’状态，无法进行此操作");
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        String dz_sheet = ENDECodeUtils.URLDecode(vo.getDz_sheet());
        String[] dz_sheet_array = dz_sheet.split(",");
        List<Js_Dz_SheetAccountVO> list = new ArrayList<Js_Dz_SheetAccountVO>();
        for (String item : dz_sheet_array) {
            Js_Dz_SheetAccountVO info = new Js_Dz_SheetAccountVO();
            info.setDz_sheet(item);
            list.add(info);
        }
        return iJs_Dz_SheetAccountService.reback(list);
    }

    @Log(value = "结算管理-对上账单管理-新增明细保存")
    @RequestMapping(value = "save", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultVO save(@RequestBody Js_Dz_SheetAccountVO vo) {
        return iJs_Dz_SheetAccountService.save(vo);
    }

    @Log(value = "结算管理-对上账单管理-一健账单号")
    @RequestMapping(value = "buildSheet", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultVO buildSheet(@RequestBody Js_Dz_Sheet_DetailAccountVO vo) {
        return iJs_Dz_SheetAccountService.buildSheet(vo);
    }


    /***
     * 多式联运模板生成
     * @param sheet
     * @param list
     * @param masterList
     * @return
     */
    public Sheet createSheetMultimodalTransport (Sheet sheet,List<Js_Dz_Sheet_DetailAccountVO> list,List<Js_Dz_Sheet_DetailAccountVO> masterList,XSSFWorkbook wb,String cus_contract_no) {
        Cell cell_record = sheet.getRow(1).getCell(22); //Record No.:CMAR10-071 0A
        cell_record.setCellValue("Record No.:CMAR10-071 0A"/*+list.get(0).getCus_no()*/);
        Cell cell_dz_sheet = sheet.getRow(2).getCell(23); //单据编号：
        cell_dz_sheet.setCellValue("ZC-"+list.get(0).getBill_number());
        Cell cell_contract_no = sheet.getRow(3).getCell(23); //合同号：
        cell_contract_no.setCellValue(cus_contract_no);
        //创建单元格样式
        CellStyle xlsxStyle = POIUtils.getTextStype(wb);
        CellStyle numDoublexlsxStyle =POIUtils.getNumDoubleStype(wb);
        CellStyle numIntegerxlsxStyle =POIUtils.getNumIntegerStype(wb);
        Font font = wb.createFont();
        font.setFontName("arial");
        font.setFontHeightInPoints((short) 10);
        xlsxStyle.setFont(font);
        numDoublexlsxStyle.setFont(font);
        numIntegerxlsxStyle.setFont(font);

        Integer sum_j36_qty = 0;
        Integer sum_j59r_qty = 0;
        Integer sum_j72x_qty = 0;
        Integer sum_j72y_qty = 0;

        Double sum_total_j36 = 0d;
        Double sum_total_j59r = 0d;
        Double sum_total_j72x = 0d;
        Double sum_total_j72y = 0d;
        Double sum_total_tax = 0d;
        Integer ColumnNumber = 0;
        List<Js_Dz_Sheet_DetailAccountVO> for_detail = new ArrayList<Js_Dz_Sheet_DetailAccountVO>();
        List<String> distinctCarTypeList = new ArrayList<String>();
        for (Js_Dz_Sheet_DetailAccountVO item : masterList) {
            String con = item.getBegin_city() + item.getEnd_city() + item.getTrans_mode();
            if (!distinctCarTypeList.contains(con)) {
                for_detail.add(item);
                distinctCarTypeList.add(con);
            }
        }
        int iRow = 0;
        for (Js_Dz_Sheet_DetailAccountVO item : for_detail) {
            Row row = sheet.createRow(iRow + 7);
            ColumnNumber = 0;
            //1序号
            POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, String.valueOf(iRow + 1));
            //2起运地
            ColumnNumber++;
            POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, item.getBegin_city());
            //3目的地
            ColumnNumber++;
            POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, item.getEnd_city());
            //4运输方式
            ColumnNumber++;
            POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, item.getTrans_mode());
            Integer j36 = 0;
            Integer j59r = 0;
            Integer j72x = 0;
            Integer j72y = 0;
            Double j36_amount = 0d;
            Double j72x_amount = 0d;
            Double j59r_amount = 0d;
            Double j72y_amount = 0d;
            Double mil = 0d;
            Double sum_not_tax_amount = 0d;
            Double sum_tax_amount = 0d;
            Js_Dz_Sheet_DetailAccountVO info = GetCarTypeModel("J36", item, masterList);
            if (null != info) {
                j36 = null != info.getJs_qty() ? Integer.valueOf(info.getJs_qty()) : 0;
                j36_amount = j36 * Double.valueOf(null != info.getNot_tax_amount() ? info.getNot_tax_amount() : "0");
                sum_not_tax_amount = sum_not_tax_amount + j36_amount;
                sum_tax_amount = sum_tax_amount + j36 * Double.valueOf(null != info.getTax_amount() ? info.getTax_amount() : "0");
                mil = mil + Double.valueOf(null != info.getMil() ? info.getMil() : "0");
                sum_j36_qty = sum_j36_qty + j36;
                sum_total_j36 = sum_total_j36 + j36_amount;
            }
            info = GetCarTypeModel("J59R", item, masterList);
            if (null != info) {
                j59r = null != info.getJs_qty() ? Integer.valueOf(info.getJs_qty()) : 0;
                j59r_amount = j59r * Double.valueOf(null != info.getNot_tax_amount() ? info.getNot_tax_amount() : "0");
                sum_not_tax_amount = sum_not_tax_amount + j59r_amount;
                sum_tax_amount = sum_tax_amount + j59r * Double.valueOf(null != info.getTax_amount() ? info.getTax_amount() : "0");
                mil = mil + Double.valueOf(null != info.getMil() ? info.getMil() : "0");
                sum_j59r_qty = sum_j59r_qty + j59r;
                sum_total_j59r = sum_total_j59r + j59r_amount;
            }
            info = GetCarTypeModel("J72X", item, masterList);
            if (null != info) {
                j72x = null != info.getJs_qty() ? Integer.valueOf(info.getJs_qty()) : 0;
                j72x_amount = j72x * Double.valueOf(null != info.getNot_tax_amount() ? info.getNot_tax_amount() : "0");
                sum_not_tax_amount = sum_not_tax_amount + j72x_amount;
                sum_tax_amount = sum_tax_amount + j72x * Double.valueOf(null != info.getTax_amount() ? info.getTax_amount() : "0");
                mil = mil + Double.valueOf(null != info.getMil() ? info.getMil() : "0");
                sum_j72x_qty = sum_j72x_qty + j72x;
                sum_total_j72x = sum_total_j72x + j72x_amount;
            }
            info = GetCarTypeModel("J72Y", item, masterList);
            if (null != info) {
                j72y = null != info.getJs_qty() ? Integer.valueOf(info.getJs_qty()) : 0;
                j72y_amount = j72y * Double.valueOf(null != info.getNot_tax_amount() ? info.getNot_tax_amount() : "0");
                sum_not_tax_amount = sum_not_tax_amount + j72y_amount;
                sum_tax_amount = sum_tax_amount + j72y * Double.valueOf(null != info.getTax_amount() ? info.getTax_amount() : "0");
                mil = mil + Double.valueOf(null != info.getMil() ? info.getMil() : "0");
                sum_j72y_qty = sum_j72y_qty + j72y;
                sum_total_j72y = sum_total_j72y + j72y_amount;
            }
            sum_total_tax += sum_tax_amount;
            //5计费数量 - J36
            ColumnNumber++;
            POIUtils.CreateNumCell(row, numIntegerxlsxStyle, ColumnNumber, Double.parseDouble(String.valueOf(j36)));
            //6计费数量 - J59R
            ColumnNumber++;
            POIUtils.CreateNumCell(row, numIntegerxlsxStyle, ColumnNumber,Double.parseDouble(String.valueOf(j59r)));
            //7计费数量 - J72X
            ColumnNumber++;
            POIUtils.CreateNumCell(row, numIntegerxlsxStyle, ColumnNumber,Double.parseDouble(String.valueOf(j72x)));
            //8计费数量 - J72Y
            ColumnNumber++;
            POIUtils.CreateNumCell(row, numIntegerxlsxStyle, ColumnNumber, Double.parseDouble(String.valueOf(j72y)));
            //发运场至港口，第一段路线
            ColumnNumber++;
            POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, item.getFirst_route());
            //前端公路里程
            ColumnNumber++;
            POIUtils.CreateNumCell(row, numIntegerxlsxStyle, ColumnNumber, item.getFirst_mileage()==null?0:Double.parseDouble(item.getFirst_mileage()));
            //前端运费单价
            ColumnNumber++;
            POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber, item.getFirst_price()==null?0:Double.parseDouble(item.getFirst_price()));
            //上船港口-下船港口/发运场至中转站点，第二段路线
            ColumnNumber++;
            POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, item.getTwo_route());
            //中段运费单价
            ColumnNumber++;
            POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber,item.getTwo_price()==null?0:Double.parseDouble(item.getTwo_price()));

            //末端（港口/站点-终点）,第三段路线
            ColumnNumber++;
            POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, item.getThree_route());
            //末端公路里程
            ColumnNumber++;
            POIUtils.CreateNumCell(row, numIntegerxlsxStyle, ColumnNumber,item.getThree_mileage()==null?0:Double.parseDouble(item.getThree_mileage()));
            //11前端运费单价
            ColumnNumber++;
            POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber,item.getThree_price()==null?0:Double.parseDouble(item.getThree_price()));

            //10保费单价
            ColumnNumber++;
            POIUtils.CreateNumCell(row, numIntegerxlsxStyle, ColumnNumber,item.getNot_tax_premium()==null?0:Double.parseDouble(item.getNot_tax_premium()));
            //12其它费用单价
            ColumnNumber++;
            POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber,item.getNot_tax_other_amount()==null?0:Double.parseDouble(item.getNot_tax_other_amount()));
            //13费用小计 - J36
            ColumnNumber++;
            POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber, j36_amount);
            //13费用小计 - J59R
            ColumnNumber++;
            POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber, j59r_amount);
            //14费用小计 - J72X
            ColumnNumber++;
            POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber, j72x_amount);
            //15费用小计 - J72Y
            ColumnNumber++;
            POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber, j72y_amount);
            //16费用小计 - 合计
            ColumnNumber++;
            POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber, sum_not_tax_amount);
            //17费用 （含税）
            ColumnNumber++;
            POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber, sum_tax_amount);
            //18备注
            ColumnNumber++;
            POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, item.getRemark());
            //19备注1
            ColumnNumber++;
            POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, "");
            //20备注2
            ColumnNumber++;
            POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, "");

            iRow++;
        }

        //底部统计
        Row rowTotal = sheet.createRow(for_detail.size() + 7);
        ColumnNumber = 0;
        POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "总计");
        ColumnNumber++;
        POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
        ColumnNumber++;
        POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
        ColumnNumber++;
        POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
        ColumnNumber++;
        POIUtils.CreateNumCell(rowTotal, numIntegerxlsxStyle, ColumnNumber, Double.parseDouble(String.valueOf(sum_j36_qty)));
        ColumnNumber++;
        POIUtils.CreateNumCell(rowTotal, numIntegerxlsxStyle, ColumnNumber, Double.parseDouble(String.valueOf(sum_j59r_qty)));
        ColumnNumber++;
        POIUtils.CreateNumCell(rowTotal, numIntegerxlsxStyle, ColumnNumber, Double.parseDouble(String.valueOf(sum_j72x_qty)));
        ColumnNumber++;
        POIUtils.CreateNumCell(rowTotal, numIntegerxlsxStyle, ColumnNumber, Double.parseDouble(String.valueOf(sum_j72y_qty)));
        ColumnNumber++;
        POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
        ColumnNumber++;
        POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
        ColumnNumber++;
        POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
        ColumnNumber++;
        POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
        ColumnNumber++;
        POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
        ColumnNumber++;
        POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
        ColumnNumber++;
        POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
        ColumnNumber++;
        POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
        ColumnNumber++;
        POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
        ColumnNumber++;
        POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
        ColumnNumber++;
        POIUtils.CreateNumCell(rowTotal, numDoublexlsxStyle, ColumnNumber,MyUtils.formatDouble2(sum_total_j36));
        ColumnNumber++;
        POIUtils.CreateNumCell(rowTotal, numDoublexlsxStyle, ColumnNumber,MyUtils.formatDouble2(sum_total_j59r));
        ColumnNumber++;
        POIUtils.CreateNumCell(rowTotal, numDoublexlsxStyle, ColumnNumber,MyUtils.formatDouble2(sum_total_j72x));
        ColumnNumber++;
        POIUtils.CreateNumCell(rowTotal, numDoublexlsxStyle, ColumnNumber,MyUtils.formatDouble2(sum_total_j72y));
        ColumnNumber++;
        POIUtils.CreateNumCell(rowTotal, numDoublexlsxStyle, ColumnNumber,MyUtils.formatDouble2(sum_total_j36 + sum_total_j59r + sum_total_j72x + sum_total_j72y));
        ColumnNumber++;
        POIUtils.CreateNumCell(rowTotal, numDoublexlsxStyle, ColumnNumber, MyUtils.formatDouble2(sum_total_tax));
        ColumnNumber++;
        POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
        ColumnNumber++;
        POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");
        ColumnNumber++;
        POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "");

        Row rowNull_1 = sheet.createRow(for_detail.size() + 7 + 1);
        Row rowNull_2 = sheet.createRow(for_detail.size() + 7 + 2);
        Row rowBuildTable = sheet.createRow(for_detail.size() + 7 + 3);
        POIUtils.CreateCell(rowBuildTable, null, 0, "物流部制表：");
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);//下边框
        POIUtils.CreateCell(rowBuildTable, null, 1, "");
        POIUtils.CreateCell(rowBuildTable, style, 2, "");
        POIUtils.CreateCell(rowBuildTable, style, 3, "");
        POIUtils.CreateCell(rowBuildTable, style, 4, "");
        POIUtils.CreateCell(rowBuildTable, style, 5, "");
        POIUtils.CreateCell(rowBuildTable, style, 6, "");
        POIUtils.CreateCell(rowBuildTable, style, 7, "");
        POIUtils.CreateCell(rowBuildTable, style, 8, "");
        POIUtils.CreateCell(rowBuildTable, style, 9, "");
        POIUtils.mergeCell(sheet,for_detail.size()+10,for_detail.size()+10,0,1);
        return sheet;
    }

}

