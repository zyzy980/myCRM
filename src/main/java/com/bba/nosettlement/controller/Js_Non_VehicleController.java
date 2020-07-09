package com.bba.nosettlement.controller;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.bba.common.constant.NOSETTLEMENT_STATE_Enum;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.nosettlement.service.api.IJs_Non_VehicleService;
import com.bba.nosettlement.vo.Js_Non_VehicleVO;
import com.bba.nosettlement.vo.Tr_Non_Statistical_ImportVO;
import com.bba.settlement.vo.Js_Vin_AmountVO;
import com.bba.util.*;
import com.bba.xtgl.vo.SysUserVO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
* Author:LTJ
* Date:2019-07-31
* Description:3.3.3.非商品车对上结算操作
* */
@RestController
@RequestMapping("/nosettlement/Js_Non_Vehicle")
public class Js_Non_VehicleController {


    @Autowired
    private IJs_Non_VehicleService iJs_Non_VehicleService;

    @Log(value = "非商品车结算管理-对上结算管理-清单查询")
    @RequestMapping(value="/getListForGrid",method = {RequestMethod.POST,RequestMethod.GET})
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel,String customSearchFilters){
            return iJs_Non_VehicleService.getListForGrid(jqGridParamModel,customSearchFilters);
    }


    @Log(value = "非商品车结算管理-对上结算管理-结算")
    @RequestMapping(value="/settlement",method = {RequestMethod.POST,RequestMethod.GET})
    public ResultVO settlement(@RequestBody List<Js_Non_VehicleVO> list)
    {
        return iJs_Non_VehicleService.settlement(list);
    }

    @Log(value = "非商品车结算管理-对上结算管理-撤回")
    @RequestMapping(value="/un_settlement",method = {RequestMethod.POST,RequestMethod.GET})
    public ResultVO un_settlement(@RequestBody List<Js_Non_VehicleVO> list)
    {
        return iJs_Non_VehicleService.un_settlement(list);
    }

    @Log(value = "非商品车结算管理-对上结算管理-生成对账单")
    @RequestMapping(value="/create_bill",method = {RequestMethod.POST,RequestMethod.GET})
    public ResultVO create_bill(@RequestBody List<Js_Non_VehicleVO> list)
    {
        return iJs_Non_VehicleService.create_bill(list);
    }

    @Log(value = "非商品车结算管理-对上结算管理-删除保密车数据")
    @RequestMapping(value="/del",method = {RequestMethod.POST,RequestMethod.GET})
    public ResultVO del(@RequestBody List<Js_Non_VehicleVO> list)
    {
        if(null==list || list.size()<=0)
        {
            return ResultVO.failResult("没有选择任何数据，不能删除。");
        }
        for(Js_Non_VehicleVO item:list) {
            item.setType("1");//保密车
        }
        List<Js_Non_VehicleVO> dataList=iJs_Non_VehicleService.findListByProperty(list);
        if(null==dataList || dataList.size()<=0)
        {
            return ResultVO.failResult("没有查询到数据，数据已被其他用户删除或处理，不能删除。");
        }
        String errorString="";
        for(Js_Non_VehicleVO item:dataList) {
            if (item.getJs_state().equals("0") || item.getJs_state().equals("1")) {

            } else {
                errorString=item.getHandover_no()+",";
            }
        }
        if(StringUtils.isNotBlank(errorString))
        {
            return ResultVO.failResult("选中数据不是“正常”或“已结算”状态，交车单号："+errorString);
        }
        iJs_Non_VehicleService.delete(list);
        return ResultVO.successResult("删除成功。");
    }


    @Log(value = "业务数据-导入修改数据")
    @RequestMapping("/importData")
    @ResponseBody
    public ResultVO importData(@RequestParam("fileList") MultipartFile file,String mtype) throws Exception {
        return iJs_Non_VehicleService.importData(file,mtype);
    }

    @RequestMapping("/exportTemplate")
    @ResponseBody
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidFormatException {
        POIUtils.exportTemplate(request, response, "非商品车对上结算保密车导入模板","Resource/excel/business/nonvechile_sec_template.xlsx");
    }



    @Log(value = "非商品车结算管理-对上结算管理-匹配合同")
    @RequestMapping("/setcontract_no")
    public ResultVO setcontract_no(@RequestBody List<String> ids) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        if (null == ids || ids.size() <= 0) {
            return ResultVO.failResult("没有选择任何数据。");
        }
        List<Js_Non_VehicleVO> list = new ArrayList<Js_Non_VehicleVO>();
        for (String item : ids) {
            Js_Non_VehicleVO vo = new Js_Non_VehicleVO();
            vo.setId(item);
            list.add(vo);
        }
        List<Js_Non_VehicleVO> Amountlist = iJs_Non_VehicleService.findListByProperty(list);
        if(null==Amountlist || Amountlist.size()<=0)
        {
            return ResultVO.failResult("没有查询到选中的数据，请查询数据后，再操作。");
        }

        String error="";
        for(Js_Non_VehicleVO item : Amountlist )
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

        return iJs_Non_VehicleService.setcontract_no(Amountlist);
    }


    /**
     * 报表功能
     * */
    @Log(value = "报表管理-非商品车管理-清单查询")
    @RequestMapping("/getListForGridBaobiao")
    public PageVO getListForGridBaobiao(JqGridParamModel jqGridParamModel, String customSearchFilters, String contract_type) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        return iJs_Non_VehicleService.getListForGridBaobiao(jqGridParamModel,customSearchFilters);
    }


    @Log(value = "报表管理-非商品车管理-删除数据")
    @RequestMapping("/delBaobiao")
    public ResultVO delBaobiao(@RequestBody List<Integer> id) {
        if(null==id || id.size()<=0)
        {
            return ResultVO.failResult("没有选择数据，不能删除。");
        }
        List<Tr_Non_Statistical_ImportVO> list = new ArrayList<Tr_Non_Statistical_ImportVO>();
        for(Integer item:id)
        {
            Tr_Non_Statistical_ImportVO vo=new Tr_Non_Statistical_ImportVO();
            vo.setId(item.toString());
            list.add(vo);
        }
        iJs_Non_VehicleService.delete(list);
        return ResultVO.successResult("删除成功。");
    }


    @RequestMapping("/exportTemplateBaobiao")
    @ResponseBody
    public void exportTemplateBaobiao(HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidFormatException {
        POIUtils.exportTemplate(request, response, "非商品车统计表导入模板","Resource/excel/baobiao/非商品车统计表.xlsx");
    }


    @Log(value = "报表管理-非商品车管理-导入数据")
    @RequestMapping("/importBaobiaoData")
    @ResponseBody
    public ResultVO importBaobiaoData(@RequestParam("fileList") MultipartFile file) throws Exception {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        List<Tr_Non_Statistical_ImportVO> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), Tr_Non_Statistical_ImportVO.class, new ImportParams());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null==list || list.size()<=0)
        {
            return ResultVO.failResult("导入失败，导入Excel文件没有数据。");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        String CurrentDate=DateUtils.nowDate();
        for(Tr_Non_Statistical_ImportVO item:list)
        {
            Date itemdate = null;
            if(StringUtils.isNotBlank(item.getBegin_date()))
            {
                itemdate = (Date) sdf.parse(item.getBegin_date());
                item.setBegin_date(DateUtils.dateFormat(itemdate,"yyyy-MM-dd HH:mm:ss"));
            }
            if(StringUtils.isNotBlank(item.getReceipt_date()))
            {
                itemdate = (Date) sdf.parse(item.getReceipt_date());
                item.setReceipt_date(DateUtils.dateFormat(itemdate,"yyyy-MM-dd HH:mm:ss"));
            }
            if(StringUtils.isNotBlank(item.getReceipt_sheet_date()))
            {
                itemdate = (Date) sdf.parse(item.getReceipt_sheet_date());
                item.setReceipt_sheet_date(DateUtils.dateFormat(itemdate,"yyyy-MM-dd HH:mm:ss"));
            }
            if(StringUtils.isNotBlank(item.getDelivery_date()))
            {
                itemdate = (Date) sdf.parse(item.getDelivery_date());
                item.setDelivery_date(DateUtils.dateFormat(itemdate,"yyyy-MM-dd HH:mm:ss"));
            }
            if(StringUtils.isNotBlank(item.getReturn_date()))
            {
                itemdate = (Date) sdf.parse(item.getReturn_date());
                item.setReturn_date(DateUtils.dateFormat(itemdate,"yyyy-MM-dd HH:mm:ss"));
            }
            if(StringUtils.isNotBlank(item.getInvoice_date()))
            {
                itemdate = (Date) sdf.parse(item.getInvoice_date());
                item.setInvoice_date(DateUtils.dateFormat(itemdate,"yyyy-MM-dd HH:mm:ss"));
            }
            if(StringUtils.isNotBlank(item.getDown_invoice_month()))
            {
                itemdate = (Date) sdf.parse(item.getDown_invoice_month());
                item.setDown_invoice_month(DateUtils.dateFormat(itemdate,"yyyy-MM-dd HH:mm:ss"));
            }
            if(StringUtils.isNotBlank(item.getDown_pay_plan_date()))
            {
                itemdate = (Date) sdf.parse(item.getDown_pay_plan_date());
                item.setDown_pay_plan_date(DateUtils.dateFormat(itemdate,"yyyy-MM-dd HH:mm:ss"));
            }

            item.setMil(null==item.getMil()?0:item.getMil());
            item.setHt_price(null==item.getHt_price()?0d:item.getHt_price());
            item.setChengyun_qty(null==item.getChengyun_qty()?0:item.getChengyun_qty());
            item.setJifei_qty(null==item.getJifei_qty()?0:item.getJifei_qty());
            item.setNot_tax_freight(null==item.getNot_tax_freight()?0d:item.getNot_tax_freight());
            item.setNot_tax_premium_price(null==item.getNot_tax_premium_price()?0d:item.getNot_tax_premium_price());
            item.setNot_tax_premium(null==item.getNot_tax_premium()?0d:item.getNot_tax_premium());
            item.setNot_tax_other_amount(null==item.getNot_tax_other_amount()?0d:item.getNot_tax_other_amount());
            item.setNot_tax_amount(null==item.getNot_tax_amount()?0d:item.getNot_tax_amount());
            item.setHt_jifei_biaozhun(null==item.getHt_jifei_biaozhun()?0d:item.getHt_jifei_biaozhun());
            item.setDown_jifei_qty(null==item.getDown_jifei_qty()?0d:item.getDown_jifei_qty());
            item.setNot_tax_down_freight(null==item.getNot_tax_down_freight()?0d:item.getNot_tax_down_freight());
            item.setDown_premium_price(null==item.getDown_premium_price()?0d:item.getDown_premium_price());
            item.setDown_premium_total(null==item.getDown_premium_total()?0d:item.getDown_premium_total());
            item.setNot_tax_other_down_amount(null==item.getNot_tax_other_down_amount()?0d:item.getNot_tax_other_down_amount());
            item.setDown_amount(null==item.getDown_amount()?0d:item.getDown_amount());
            item.setReal_tax_amount(null==item.getReal_tax_amount()?0d:item.getReal_tax_amount());
            item.setReal_ntax_amount(null==item.getReal_ntax_amount()?0d:item.getReal_ntax_amount());
            item.setTax__real_cost(null==item.getTax__real_cost()?0d:item.getTax__real_cost());



            item.setImport_by(sysUserVO.getUserId());
            item.setImport_date(CurrentDate);
        }

        iJs_Non_VehicleService.insert(list);

        return ResultVO.successResult("导入成功！");
    }

}
