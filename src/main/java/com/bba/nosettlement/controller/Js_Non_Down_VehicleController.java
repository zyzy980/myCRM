package com.bba.nosettlement.controller;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.bba.common.constant.Js_StateEnum;
import com.bba.common.constant.NOSETTLEMENT_STATE_Enum;
import com.bba.common.constant.NOSETTLEMENT_TAB_Enum;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.dz.vo.Tr_Payment_PlanVO;
import com.bba.ht.service.api.IHt_Carrier_FreightService;
import com.bba.ht.vo.Non_Ht_CarrierVO;
import com.bba.nosettlement.service.api.IJs_Non_Down_VehicleService;
import com.bba.nosettlement.vo.Js_Non_Down_VehicleVO;
import com.bba.nosettlement.vo.Js_Non_VehicleVO;
import com.bba.settlement.vo.Js_Non_DownTemp_VehicleVO;
import com.bba.util.*;
import com.bba.xtgl.service.api.ISysSheetIdService;
import com.bba.xtgl.vo.SysUserVO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
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
* Description:3.3.3.非商品车对下结算操作
* */
@RestController
@RequestMapping("/nosettlement/Js_Non_Down_Vehicle")
public class Js_Non_Down_VehicleController {

    @Autowired
    private IJs_Non_Down_VehicleService iJs_Non_Down_VehicleService;
    @Autowired
    private ISysSheetIdService iSysSheetIdService;
    @Autowired
    private IHt_Carrier_FreightService iHt_Carrier_FreightService;

    @Log(value = "非商品车结算管理-对下结算管理-清单查询")
    @RequestMapping(value = "/getListForGrid", method = {RequestMethod.POST, RequestMethod.GET})
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        //承运商确认数据 - 查询列表
        SysUserVO sysUserVO=SessionUtils.currentSession();
        if(sysUserVO.getUserLevel().equals("5")) {
            //承运商角色
            //USERLEVEL = 0：管理员；1：操作员/业务员；2：商务人员；3：财务人员；4：风险；5：承运商
            // 数据状态：0.正常  1.已结算.2业务审核.3.财务审核.4.承运商审核.5.台账，6.开票
            jqGridParamModel.put("carrier_no","eq", sysUserVO.getContractorCode());
            jqGridParamModel.put("js_state","in","'3','4','5','6'");
        }
        return iJs_Non_Down_VehicleService.getListForGrid(jqGridParamModel, customSearchFilters);
    }


    @Log(value = "非商品车结算管理-对下结算管理-结算")
    @RequestMapping(value = "settlement", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultVO settlement(@RequestBody Js_Non_Down_VehicleVO vo, HttpServletRequest request, HttpServletResponse response) {

        SysUserVO sysUserVO = SessionUtils.currentSession();
        String id = ENDECodeUtils.URLDecode(vo.getId());
        String[] id_array = id.split(",");
        String error = "";

        List<Js_Non_Down_VehicleVO> list = new ArrayList<Js_Non_Down_VehicleVO>();
        for (String item : id_array) {
            Js_Non_Down_VehicleVO info = new Js_Non_Down_VehicleVO();
            info.setId(item);
            list.add(info);
        }
        List<Js_Non_Down_VehicleVO> datalist = iJs_Non_Down_VehicleService.findListByProperty(list);
        if (null == datalist || datalist.size() <= 0) {
            return ResultVO.failResult("数据已被其他用户删除或处理，请查询数据后再操作。");
        }
        if (datalist.size() != list.size()) {
            return ResultVO.failResult("有部份数据已被其他用户处理，请查询数据后再操作。");
        }

        for (Js_Non_Down_VehicleVO item : datalist) {
            if (!"0".equals(item.getJs_state())) {
                error += item.getHandover_no() + ",";
            }
        }
        if (StringUtils.isNotBlank(error)) {
            return ResultVO.failResult("选中数据状态已发生变化(数据已被其他用户处理)，请查询数据后再操作。交车单号：" + error);
        }
        return iJs_Non_Down_VehicleService.settlement(datalist);

    }


    @Log(value = "非商品车结算管理-对下结算管理-撤回")
    @RequestMapping("un_settlement")
    public ResultVO un_settlement(@RequestBody List<String> list) {
        if(null==list || list.size() <= 0){
            return ResultVO.failResult("请至少选择一行数据进行操作");
        }
        List<Js_Non_Down_VehicleVO> dataList=new ArrayList<Js_Non_Down_VehicleVO>();
        for(String item:list)
        {
            Js_Non_Down_VehicleVO vo=new Js_Non_Down_VehicleVO();
            vo.setId(item);
            dataList.add(vo);
        }
        return iJs_Non_Down_VehicleService.un_settlement(dataList);
    }


    //LTJ:2019-07-22 业务审核-财务审核-承运商审核
    // 0：管理员；1：操作员；2：商务人员；3：财务人员；4：其它；
    // 数据状态：0.正常  1.已结算.2业务审核.3.财务审核.4.承运商审核.5.台账，6.开票
    @Log(value = "非商品车结算管理-对下结算管理-审核数据")
    @RequestMapping(value = "data_check", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultVO data_check(@RequestBody Js_Non_Down_VehicleVO vo, HttpServletRequest request, HttpServletResponse response) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        String id = ENDECodeUtils.URLDecode(vo.getId());
        String[] id_array = id.split(",");

        List<Js_Non_Down_VehicleVO> list = new ArrayList<Js_Non_Down_VehicleVO>();
        for (String item : id_array) {
            Js_Non_Down_VehicleVO info = new Js_Non_Down_VehicleVO();
            info.setId(item);
            list.add(info);
        }
        //验证状态
        List<Js_Non_Down_VehicleVO> datalist = iJs_Non_Down_VehicleService.findListByProperty(list);
        if (null == datalist || datalist.size() <= 0) {
            return ResultVO.failResult("没有查询到选中数据，原因：数据已被其他用户删除或处理，请查询数据后再操作。");
        }
        if (datalist.size() != list.size()) {
            return ResultVO.failResult("有部份数据已被其他用户处理，请查询数据后再操作。");
        }
        String error = "";
        for (Js_Non_Down_VehicleVO item : datalist) {
            if (!vo.getJs_batch().equals(item.getJs_state())) {
                error += item.getVin() + ",";
            }
        }
        if (StringUtils.isNotBlank(error)) {
            return ResultVO.failResult("选中数据状态已发生变化(数据已被其他用户处理)，请查询数据后再操作。车架号：" + error);
        }

        for (Js_Non_Down_VehicleVO item : list) {
            item.setJs_state(vo.getJs_state());
        }

        return iJs_Non_Down_VehicleService.data_check(vo,list,datalist);
        //iJs_Non_Down_VehicleService.update(list);
        //return ResultVO.successResult("OK");
    }


    //LTJ:2019-07-24 业务审核-财务审核-承运商审核
    // 用户等级：0：管理员；1：操作员；2：商务人员；3：财务人员；4：其它；
    // 数据状态：0.正常  1.业务审核.2商务审核.3.财务审核.4.发送邮件.5.客户确认
    @Log(value = "非商品车结算管理-对下结算管理-驳回数据")
    @RequestMapping(value = "replayData", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultVO replayData(@RequestBody Js_Non_Down_VehicleVO vo) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        String id = ENDECodeUtils.URLDecode(vo.getId());
        String[] id_array = id.split(",");

        List<Js_Non_Down_VehicleVO> list = new ArrayList<Js_Non_Down_VehicleVO>();
        for (String item : id_array) {
            Js_Non_Down_VehicleVO info = new Js_Non_Down_VehicleVO();
            info.setId(item);
            list.add(info);
        }

        List<Js_Non_Down_VehicleVO> datalist = iJs_Non_Down_VehicleService.findListByProperty(list);
        if (null == datalist || datalist.size() <= 0) {
            return ResultVO.failResult("没有查询到选中数据，原因：数据已被其他用户删除或处理，请查询数据后再操作。");
        }
        if (datalist.size() != list.size()) {
            return ResultVO.failResult("有部份数据已被其他用户处理，请查询数据后再操作。");
        }

        //判断状态 - 根据用户等级
        String error_vin = "";
        String stateStr = "";
        for (Js_Non_Down_VehicleVO item : datalist) {
            if (sysUserVO.getUserLevel().equals("1")) //业务操作员 - 0 正常 状态
            {
                if (!item.getJs_state().equals("1")) {
                    error_vin += item.getVin() + ",";
                    stateStr = "已结算";
                }
            } else if (sysUserVO.getUserLevel().equals("3")) //财务操作员 - 2 商务审核 状态
            {
                if (!item.getJs_state().equals("2")) {
                    error_vin += item.getVin() + ",";
                    stateStr = "业务审核";
                }
            }
        }

        if (StringUtils.isNotBlank(error_vin)) {
            return ResultVO.failResult("选中数据“" + stateStr + "”状态已发生变化(数据已被其他用户处理)，请查询数据后再操作。车架号：" + error_vin.substring(0, error_vin.length() - 1));
        }

        //更新 JS_VIN_DOWN_AMOUNT 金额
        iJs_Non_Down_VehicleService.UpdateDataList(list);


        return ResultVO.successResult("OK");
    }



    @Log(value = "非商品车结算管理-对下结算管理-删除保密车数据")
    @RequestMapping(value="/del",method = {RequestMethod.POST,RequestMethod.GET})
    public ResultVO del(@RequestBody List<Js_Non_Down_VehicleVO> list)
    {
        if(null==list || list.size()<=0)
        {
            return ResultVO.failResult("没有选择任何数据，不能删除。");
        }
        for(Js_Non_Down_VehicleVO item:list) {
            item.setType("1");//保密车
        }
        List<Js_Non_Down_VehicleVO> dataList=iJs_Non_Down_VehicleService.findListByProperty(list);
        if(null==dataList || dataList.size()<=0)
        {
            return ResultVO.failResult("没有查询到数据，数据已被其他用户删除或处理，不能删除。");
        }
        String errorString="";
        for(Js_Non_Down_VehicleVO item:dataList) {
            if (item.getJs_state().equals("0") || item.getJs_state().equals("1")) {

            } else {
                errorString=item.getHandover_no()+",";
            }
        }
        if(StringUtils.isNotBlank(errorString))
        {
            return ResultVO.failResult("选中数据不是“正常”或“已结算”状态，交车单号："+errorString);
        }
        iJs_Non_Down_VehicleService.delete(list);
        return ResultVO.successResult("删除成功。");
    }


    @Log(value = "非商品车对下结算-对下结算-导出对账单")
    @RequestMapping("exportData")
    public void exportData(Js_Non_Down_VehicleVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        if(null==vo) {
            response.getWriter().write("请选择需要导出的数据。");
            return ;
        }
        if(StringUtils.isBlank(vo.getId())){
            response.getWriter().write("请选择需要导出的数据。");
            return ;
        }
        String[] id_array=vo.getId().split(",");
        List<Js_Non_Down_VehicleVO> detailList=new ArrayList<Js_Non_Down_VehicleVO>();
        for(String item:id_array){
            Js_Non_Down_VehicleVO info=new Js_Non_Down_VehicleVO();
            info.setId(item);
            detailList.add(info);
        }
        detailList=iJs_Non_Down_VehicleService.findListByProperty(detailList);
        if(null==detailList || detailList.size()<=0) {
            response.getWriter().write("没有查询到导出选择数据，数据可能被其他用户处理，请查询数据后再操作。");
            return ;
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        CreateDZ_SHEET_To_Excel(detailList,sysUserVO,request,response);
    }

    // 生成对账单Excel文件
    // 参数 list 客户字段必须是相同 - 此方法不区分客户生成文件
    // resKind=0直接输出；=1写入文件
    private ResultVO CreateDZ_SHEET_To_Excel(List<Js_Non_Down_VehicleVO> list, SysUserVO sysUserVO, HttpServletRequest request, HttpServletResponse response) {
        ResultVO resultVO = new ResultVO();
        resultVO.setResultCode("1");
        String separator = File.separator;
        String SaveFilePath = System.getProperty("user.dir") + separator + "Excel" + separator + "dz_sheet" + separator + DateUtils.nowDate("yyyyMMdd") + separator;
        try {
            File file = new File(SaveFilePath);
            if (!file.exists() && !file.isDirectory()) {
                file.mkdirs();
            }
            // 2019年非商品车结算表.xlsx
            String SaveFileName = DateUtils.dateFormat(DateUtils.parseDate(list.get(0).getBegin_date(), "yyyy-MM-dd"), "yy") + "年非商品车对下结算表.xlsx";
            SaveFilePath += SaveFileName;
            //往Excel文件中写入数据 开始
            String url = "static/Resource/excel/dz/dz_non_down_template.xlsx";
            String finalXlsxPath = ClassUtils.class.getClassLoader().getResource(url).getPath();
            file = new File(SaveFilePath);
            if (file.exists()) {
                file.delete();
            }
            // 读取Excel文档
            File finalXlsxFile = new File(finalXlsxPath);
            FileInputStream in = new FileInputStream(finalXlsxFile);
            Workbook wb = null;
            if (finalXlsxFile.getName().endsWith("xls")) { // Excel 2003
                wb = new HSSFWorkbook(in);
            } else if (finalXlsxFile.getName().endsWith("xlsx")) { // Excel 2007/2010
                wb = new XSSFWorkbook(in);
            }

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
            for (int i = NOSETTLEMENT_TAB_Enum.values().length - 1; i >= 0; i--) {
                //过滤数据 == 区分 - VIP，保密车，合同车，救援车
                List<Js_Non_Down_VehicleVO> datalist = new ArrayList<Js_Non_Down_VehicleVO>();
                for (Js_Non_Down_VehicleVO item : list) {
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
                    wb.setSheetName(i, yearStr + "年(" + NOSETTLEMENT_TAB_Enum.values()[i].getTitle() + ")" + datalist.get(0).getCarrier_no());
                } else {
                    wb.setSheetName(i, yearStr + "年" + NOSETTLEMENT_TAB_Enum.values()[i].getTitle() + datalist.get(0).getCarrier_no());
                }

                // sheet 对应 工作簿 - 统计表
                Sheet sheet = wb.getSheetAt(i);
                Cell cell_record = sheet.getRow(0).getCell(0);//标题
                cell_record.setCellValue("南京长安民生住久物流有限公司\r\n" + yearStr + "年非商品车对下运输费用结算表");
                Cell cell_dz_sheet = sheet.getRow(1).getCell(0);//时间
                cell_dz_sheet.setCellValue(DateUtils.dateFormat(begin_date, "yyyyMMdd"));

                int ishipment_qty=0;
                int ijs_qty=0;
                int iclosed_num=0;
                Float inot_tax_freight=0f;
                Float inot_tax_rescue_truck_startoric=0f;
                Float inot_tax_amount=0f;
                Float itax_amount=0f;
                Float inot_tax_other_amount=0f;


                Integer ColumnNumber = 0;
                for (int r = 0, rlen = datalist.size(); r < rlen; r++) {
                    Row row = sheet.createRow(r + 3);
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
                    // 承运商
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getCarrier_name());
                    //11 发运地
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getBegin_address());
                    //12 目的地
                    ColumnNumber++;
                    CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getEnd_address());
                    if (NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals("2") || NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals("3")) {
                        // 合同路线距离
                        ColumnNumber++;
                        CreateCell(row, xlsxStyle, ColumnNumber, datalist.get(r).getMil());
                    }

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
                Row row_total = sheet.createRow(datalist.size() + 3);
                for(int t=0;t<18;t++)
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
                    CreateCell(row_total, xlsxStyleBold, 14,String.valueOf(inot_tax_freight));
                    if (NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals("3")){
                        //救援车起步价 不含税
                        CreateCell(row_total, xlsxStyleBold, 15, String.valueOf(inot_tax_rescue_truck_startoric));
                    }else{
                        //其他费用(不含税)
                        CreateCell(row_total, xlsxStyleBold, 15,String.valueOf(inot_tax_other_amount));
                    }
                    //费用小计 不含税
                    CreateCell(row_total, xlsxStyleBold, 17,String.valueOf(inot_tax_amount));
                    //费用小计 含税
                    CreateCell(row_total, xlsxStyleBold, 18,String.valueOf(itax_amount));
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
                    //其他费用(不含税)
                    CreateCell(row_total, xlsxStyleBold, 15,String.valueOf(inot_tax_other_amount));
                    //费用小计 不含税
                    CreateCell(row_total, xlsxStyleBold, 17,String.valueOf(inot_tax_amount));
                    //费用小计 含税
                    CreateCell(row_total, xlsxStyleBold, 18,String.valueOf(itax_amount));
                }
                else if (NOSETTLEMENT_TAB_Enum.values()[i].getCode().equals("0")) {
                    //承运数量
                    CreateCell(row_total, xlsxStyleBold, 8,String.valueOf(ishipment_qty));
                    //计费数量
                    CreateCell(row_total, xlsxStyleBold, 9,String.valueOf(ijs_qty));
                    //运费小计(不含税)
                    CreateCell(row_total, xlsxStyleBold, 14,String.valueOf(inot_tax_freight));
                    //其他费用(不含税)
                    CreateCell(row_total, xlsxStyleBold, 15,String.valueOf(inot_tax_other_amount));
                    //费用小计 不含税
                    CreateCell(row_total, xlsxStyleBold, 17,String.valueOf(inot_tax_amount));
                    //费用小计 含税
                    CreateCell(row_total, xlsxStyleBold, 18,String.valueOf(itax_amount));
                }
            }


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


            wb.close();
            wb = null;

            // 往Excel文件中写入数据 结束
        } catch (Exception e) {
            resultVO.setResultDataFull("写入Excel文件出错，原因：" + e.getMessage());
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




    /**
     * 对下匹配合同，需要判断如果之前的合同是预估合同，且状态为【生成台账】，则需要往历史结算表插入数据
     * @param ids
     * @return
     */
    @Log(value = "非商品车结算管理-对下结算管理-匹配合同")
    @RequestMapping("/setcontract_no")
    public ResultVO setcontract_no(@RequestBody List<String> ids) {
        if (null == ids || ids.size() <= 0) {
            return ResultVO.failResult("没有选择任何数据。");
        }
        List<Js_Non_Down_VehicleVO> list = new ArrayList<Js_Non_Down_VehicleVO>();
        for (String item : ids) {
            Js_Non_Down_VehicleVO vo = new Js_Non_Down_VehicleVO();
            vo.setId(item);
            list.add(vo);
        }
        List<Js_Non_Down_VehicleVO> Amountlist = iJs_Non_Down_VehicleService.findListByProperty(list);
        if(null==Amountlist || Amountlist.size()<=0)
        {
            return ResultVO.failResult("没有查询到选中的数据，请查询数据后，再操作。");
        }

        String error="";
        for(Js_Non_Down_VehicleVO item : Amountlist )
        {
            if(!((Js_StateEnum.DOWN_CREATE_TZ.getCode().equals(item.getJs_state()) && "2".equals(item.getContract_type()))|| Js_StateEnum.DOWN_NORMAL.getCode().equals(item.getJs_state()))) {
                error+=item.getHandover_no()+",";
            }
        }
        if(StringUtils.isNotBlank(error))
        {
            return ResultVO.failResult("选中如下数据不是“正常”状态或“台账”状态且“预估合同”数据，请查询后再操作，交车单号:"+error);
        }

        //查找合同
        List<Js_Non_Down_VehicleVO> updateList=new ArrayList<Js_Non_Down_VehicleVO>();
        List<Js_Non_Down_VehicleVO> compensationList = new ArrayList<Js_Non_Down_VehicleVO>();//补差
        Map<String ,Object> map = new HashMap<>() ;
        map = iHt_Carrier_FreightService.matchingContract_non_down(Amountlist);

        updateList = (List<Js_Non_Down_VehicleVO>) map.get("updateList");
        compensationList = (List<Js_Non_Down_VehicleVO>) map.get("compensationList");
        List<String> msg_list = (List<String>) map.get("msg_list");
        ResultVO resultVO = new ResultVO();
        if(updateList.size()>0 || compensationList.size()>0) {
            if (updateList.size()>0) {
                for(Js_Non_Down_VehicleVO avo:updateList)
                    avo.setJs_state("0"); //更新状态 = 0
                iJs_Non_Down_VehicleService.update(updateList);
            }
            if (compensationList.size()>0) {
                //插入到暂定结算历史表
                List<Js_Non_Down_VehicleVO> paramList = new ArrayList<Js_Non_Down_VehicleVO>();
                for (Js_Non_Down_VehicleVO vo:compensationList) {
                    Js_Non_Down_VehicleVO paramVO = new Js_Non_Down_VehicleVO();
                    paramVO.setId(vo.getId());
                    paramList.add(paramVO);
                }
                List<Js_Non_Down_VehicleVO> dataList = iJs_Non_Down_VehicleService.findListByProperty(paramList);
                //插入到暂定结算历史表
                List<Js_Non_DownTemp_VehicleVO> insertList=new ArrayList<Js_Non_DownTemp_VehicleVO>();
                for (Js_Non_Down_VehicleVO dataVO:dataList) {
                    Js_Non_DownTemp_VehicleVO tempVO = new Js_Non_DownTemp_VehicleVO();
                    BeanUtils.copyProperties(dataVO, tempVO);
                    insertList.add(tempVO);
                }
                //插入到历史表
                if(insertList.size()>0)
                    iJs_Non_Down_VehicleService.insert(insertList);
                if(compensationList.size()>0)
                    iJs_Non_Down_VehicleService.update(compensationList);
            }
            String message = msg_list.size()==0?"匹配成功":"匹配成功,但部分失败：";
            resultVO = ResultVO.successResult(message,ArrayUtils.join(msg_list, "</br>"));
        } else {
            resultVO = ResultVO.failResult("匹配失败",ArrayUtils.join(msg_list, "</br>"));
        }
        return resultVO;
    }


    @Log(value = "非商品车结算管理-对下结算管理-预付申请")
    @RequestMapping(value = "expect_apply", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultVO expect_apply(@RequestBody Js_Non_Down_VehicleVO vo, HttpServletRequest request, HttpServletResponse response) {
        String id = ENDECodeUtils.URLDecode(vo.getId());
        String[] id_array = id.split(",");
        List<Js_Non_Down_VehicleVO> list = new ArrayList<Js_Non_Down_VehicleVO>();
        for (String item : id_array) {
            Js_Non_Down_VehicleVO info = new Js_Non_Down_VehicleVO();
            info.setId(item);
            list.add(info);
        }
        //验证状态
        List<Js_Non_Down_VehicleVO> datalist = iJs_Non_Down_VehicleService.findListByProperty(list);
        if (null == datalist || datalist.size() <= 0) {
            return ResultVO.failResult("没有查询到选中数据，原因：数据已被其他用户删除或处理，请查询数据后再操作。");
        }
        if (datalist.size() != list.size()) {
            return ResultVO.failResult("有部份数据已被其他用户处理，请查询数据后再操作。");
        }

        //1=保密车，此数据是通过Excel导入，不判断状态等.
        String type_string="1";
        if(!type_string.equals(vo.getType()))
        {
            String data_error = "";
            for (Js_Non_Down_VehicleVO item : datalist) {
                if (!"4".equals(item.getJs_state())) {
                    data_error = "选中交车单号(" + item.getHandover_no() + ")状态不是“承运商审核”；";
                }
                if (!"3".equals(item.getJs_data_type())) {
                    data_error = "选中交车单号(" + item.getHandover_no() + ")数据类型不是“预付”；";
                }
            }
            if (StringUtils.isNotBlank(data_error)) {
                return ResultVO.failResult(data_error);
            }
        }
        String nowDate=DateUtils.nowDate();
        SysUserVO sysUserVO=SessionUtils.currentSession();
        //把数据写入 TR_PAYMENT_PLAN 表
        List<Tr_Payment_PlanVO> insertDataList=new ArrayList<Tr_Payment_PlanVO>();
        for (Js_Non_Down_VehicleVO item : datalist) {
            Tr_Payment_PlanVO planVO=new Tr_Payment_PlanVO();
            planVO.setType("1");//0 商品车 1非商品车 2 补差
            planVO.setState("0");//状态 0正常 1撤回  2已提报 3已完成
            planVO.setCarrier_no(item.getCarrier_no());
            planVO.setCarrier_name(item.getCarrier_name());
            planVO.setInvoice_no(item.getInvoice_no());
            planVO.setHandover_no(item.getHandover_no());//交接单号
            planVO.setVin(item.getVin());
            //planVO.setVdr_no(item.getVdr_no());
            //planVO.setDealer_name(item.getDealer_name());
            planVO.setBegin_date(item.getBegin_date());
            planVO.setReceipt_date(item.getReceipt_date());
            //planVO.setReceipt_sheet_date(item.getrec);
            planVO.setTrans_mode(item.getTrans_mode());
            planVO.setBegin_address(item.getBegin_address());
            planVO.setEnd_address(item.getEnd_address());
            planVO.setCar_type(item.getCar_type());
            planVO.setChengyun_qty(item.getShipment_qty()==null?0:Integer.valueOf(item.getShipment_qty()));
            planVO.setDown_price(item.getNot_tax_price()==null?0d:Double.valueOf(item.getNot_tax_price()));
            planVO.setTax_amount(item.getTax_amount()==null?0d:Double.valueOf(item.getTax_amount()));
            planVO.setNot_tax_amount(item.getNot_tax_amount()==null?0d:Double.valueOf(item.getNot_tax_amount()));
            planVO.setInvoice_tax(item.getTax_rate()==null?0d:Double.valueOf(item.getTax_rate()));
            planVO.setTax_ht_amount(0.0);
            planVO.setNot_tax_ht_amount(0.0);
            planVO.setTax_difference(0.0);
            planVO.setNot_tax_difference(0.0);
            planVO.setOperator_by(sysUserVO.getUserId());
            planVO.setOperator_date(nowDate);
            planVO.setData_type("1");//0正常 1 预付2 冲预付
            planVO.setVin_id(item.getId());
            planVO.setPay_apply("N");//付款申请N，Y
            planVO.setContract_no(item.getContract_no());
            //planVO.setPay_expect(null);//预付号
            planVO.setJs_no(item.getJs_no());

            insertDataList.add(planVO);
        }
        iJs_Non_Down_VehicleService.insert(insertDataList);
        if(type_string.equals(vo.getType()))
        {
            //保密车特殊处理
            //0正常结算，1对上不结（VIP），2对下不结(无合同).3预付
            for (Js_Non_Down_VehicleVO item : list) {
                item.setJs_data_type("3");
            }
            iJs_Non_Down_VehicleService.update(list);
        }
        return ResultVO.successResult("预付申请成功");
    }


    /*

    @RequestMapping("/setcontract_no1")
    public ResultVO setcontract_no1(@RequestBody List<String> ids) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        if (null == ids || ids.size() <= 0) {
            return ResultVO.failResult("没有选择任何数据。");
        }
        List<Js_Non_Down_VehicleVO> list = new ArrayList<Js_Non_Down_VehicleVO>();
        for (String item : ids) {
            Js_Non_Down_VehicleVO vo = new Js_Non_Down_VehicleVO();
            vo.setId(item);
            list.add(vo);
        }
        List<Js_Non_Down_VehicleVO> Amountlist = iJs_Non_Down_VehicleService.findListByProperty(list);
        if(null==Amountlist || Amountlist.size()<=0)
        {
            return ResultVO.failResult("没有查询到选中的数据，请查询数据后，再操作。");
        }

        String error="";
        for(Js_Non_Down_VehicleVO item : Amountlist)
        {
            if(!NOSETTLEMENT_STATE_Enum.NORMAL.getCode().equals(item.getJs_state()))
            {
                error+=item.getHandover_no()+",";
            }
        }
        if(StringUtils.isNotBlank(error))
        {
            return ResultVO.failResult("选中如下数据不是“"+NOSETTLEMENT_STATE_Enum.NORMAL.getName()+"”状态数据，请查询后再操作，交车单号:"+error);
        }

        return iJs_Non_Down_VehicleService.setcontract_no(Amountlist);
    }
    */






    @RequestMapping("/exportTemplate")
    @ResponseBody
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidFormatException {
        POIUtils.exportTemplate(request, response, "非商品车对下结算保密车导入模板","Resource/excel/business/non_down_sec_template.xlsx");
    }


    @Log(value = "非商品车结算管理-对下结算管理-导入数据")
    @RequestMapping("/importData")
    @ResponseBody
    public ResultVO importData(@RequestParam("fileList") MultipartFile file) throws Exception {
        List<Js_Non_Down_VehicleVO> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), Js_Non_Down_VehicleVO.class, new ImportParams());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == list || list.size() <= 0) {
            return ResultVO.failResult("导入失败，上传Excel文件中没有数据");
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        List<Js_Non_Down_VehicleVO> queryList = new ArrayList<Js_Non_Down_VehicleVO>();
        for (Js_Non_Down_VehicleVO item : queryList) {
            Js_Non_Down_VehicleVO info = new Js_Non_Down_VehicleVO();
            info.setHandover_no(item.getHandover_no());
            queryList.add(info);
        }
        //判断状态 = 0 || 1
        queryList = iJs_Non_Down_VehicleService.findListByProperty(queryList);
        if (null != queryList && queryList.size() > 0) {
            String errorString = "";
            for (Js_Non_Down_VehicleVO item : queryList) {
                if (item.getJs_state().equals("0") || item.getJs_state().equals("1")) {

                } else {
                    errorString = item.getHandover_no() + ",";
                }
            }
            if (StringUtils.isNotBlank(errorString)) {
                return ResultVO.failResult("导入失败，检查已存在的交车单号数据不是“正常”，“已结算”状态,交车单号：" + errorString);
            }
        }
        String create_date = DateUtils.nowDate();
        List<Js_Non_Down_VehicleVO> insertList = new ArrayList<Js_Non_Down_VehicleVO>();
        List<Js_Non_Down_VehicleVO> updateList = new ArrayList<Js_Non_Down_VehicleVO>();
        for (Js_Non_Down_VehicleVO item : list) {
            boolean bExists = false;
            item.setType("1"); //保密车
            if (null != queryList && queryList.size() > 0) {
                for (Js_Non_Down_VehicleVO info : queryList) {
                    if (item.getHandover_no().equals(info.getHandover_no())) {
                        item.setId(info.getId());
                        bExists = false;
                        break;
                    }
                }
            }
            //承运商编号
            Non_Ht_CarrierVO non_ht_carrierVO=new Non_Ht_CarrierVO();
            non_ht_carrierVO.setCarrier_name(item.getCarrier_name());
            List<Non_Ht_CarrierVO> carrierList=iJs_Non_Down_VehicleService.findListByProperty(non_ht_carrierVO);
            if(null!=carrierList && carrierList.size()>0)
            {
                item.setCarrier_no(carrierList.get(0).getCarrier_no());
            }
            if (bExists) {
                updateList.add(item);
            } else {
                item.setJs_state("1"); //已结算
                item.setCreate_by(sysUserVO.getUserId());
                item.setCreate_date(create_date);
                insertList.add(item);
            }
        }
        if(insertList.size()>0)
            iJs_Non_Down_VehicleService.insert(insertList);
        if(updateList.size()>0)
            iJs_Non_Down_VehicleService.update(updateList);
        return ResultVO.successResult("导入成功！新增 " + insertList.size() + "笔；更新 " + updateList.size());
    }
}
