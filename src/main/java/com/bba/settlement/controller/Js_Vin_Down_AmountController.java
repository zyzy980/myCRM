package com.bba.settlement.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.bba.common.common.Sys_sheetidUtil;
import com.bba.common.constant.Js_StateEnum;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.dz.vo.Tr_Payment_PlanVO;
import com.bba.ht.service.api.IHt_Carrier_FreightService;
import com.bba.ht.vo.Ht_CarrierVO;
import com.bba.ht.vo.Ht_Carrier_FreightVO;
import com.bba.jcda.service.api.IJs_Tax_RateService;
import com.bba.jcda.vo.Js_Tax_RateVO;
import com.bba.settlement.service.api.IJs_Vin_Down_AmountService;
import com.bba.settlement.vo.*;
import com.bba.util.*;
import com.bba.xtgl.service.api.ISysSheetIdService;
import com.bba.xtgl.vo.SysSheetIdVO;
import com.bba.xtgl.vo.SysUserVO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * 对下结算管理
 */
@RestController
@RequestMapping("/settlement/Js_Vin_Down_AmountController")
public class Js_Vin_Down_AmountController {

    @Autowired
    private IJs_Vin_Down_AmountService iJs_Vin_Down_AmountService;
    @Autowired
    private ISysSheetIdService iSysSheetIdService;
    @Autowired
    private IHt_Carrier_FreightService iHt_Carrier_FreightService;
    @Autowired
    private IJs_Tax_RateService iJs_Tax_RateService;


    /*
    * 正式合同，临时合同，都在Service层处理数据
    * */
    @Log(value = "结算管理-对下结算管理-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters, String contract_type) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        return iJs_Vin_Down_AmountService.getListForGrid(jqGridParamModel,customSearchFilters,contract_type);
    }

    @Log(value = "结算管理-对下结算补差-清单查询")
    @RequestMapping("getCompensationListForGrid")
    public PageVO getCompensationListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        return iJs_Vin_Down_AmountService.getCompensationListForGrid(jqGridParamModel,customSearchFilters);
    }


    @Log(value = "结算管理-对下结算管理-结算")
    @RequestMapping(value="settlement",method = {RequestMethod.POST,RequestMethod.GET})
    public ResultVO settlement(@RequestBody Js_Vin_Down_AmountVO vo,HttpServletRequest request,HttpServletResponse response) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        String id = ENDECodeUtils.URLDecode(vo.getId());
        String[] id_array = id.split(",");
        String error = "";


        List<Js_Vin_Down_AmountVO> list = new ArrayList<Js_Vin_Down_AmountVO>();
        for (String item : id_array) {
            Js_Vin_Down_AmountVO info = new Js_Vin_Down_AmountVO();
            info.setId(item);
            list.add(info);
        }
        List<Js_Vin_Down_AmountVO> datalist = iJs_Vin_Down_AmountService.findListByProperty(list);
        if (null == datalist || datalist.size() <= 0) {
            return ResultVO.failResult("数据已被其他用户删除或处理，请查询数据后再操作。");
        }
        if (datalist.size() != list.size()) {
            return ResultVO.failResult("有部份数据已被其他用户处理，请查询数据后再操作。");
        }
        List<String> msg_list = new ArrayList<String>();
        for(int i = 0, line = 1; i < datalist.size(); i++, line ++){
            Js_Vin_Down_AmountVO vo2 = datalist.get(i);
            if(!StringUtils.equals(vo2.getJs_state(), "0")){
                msg_list.add("第"+line+"行,已结算请勿重复结算");
            }
          /*  if(StringUtils.equals(vo2.getHis_flag(), "Y")){
                msg_list.add("第"+line+"行，已经结算过的数据请使用‘二次结算’进行操作");
            }*/
        }
        if(msg_list.size() > 0){
            return ResultVO.failResult("结算失败", ArrayUtils.join(msg_list, "</br>"));
        }
        //匹配合同号
        list.clear();
        List<String> msg_no_contract_no_list = new ArrayList<String>();
        List<String> msg_more_contract_no_list = new ArrayList<String>();
        List<String> rate_more_contract_no_list = new ArrayList<String>();
        for (Js_Vin_Down_AmountVO item : datalist) {
            //这里要重新修改，用运输方式+起运地+目的地去匹配合同
            List<Ht_Carrier_FreightVO> ht_carrier_freightList = iJs_Vin_Down_AmountService.findHt_Carrier_FreightVO(item);
            if (null == ht_carrier_freightList || ht_carrier_freightList.size() <= 0) {
                //改变列表显示背景颜色 - 暂时不处理 2017-07-27 16:18
                msg_no_contract_no_list.add(item.getVin());
                continue;
            } else if (ht_carrier_freightList.size() > 1) {
                //改变列表显示背景颜色 - 暂时不处理 2017-07-27 16:18
                msg_more_contract_no_list.add(item.getVin());
                continue;
            }
            Js_Vin_Down_AmountVO downVO = new Js_Vin_Down_AmountVO();
            downVO.setId(item.getId());
            downVO.setContract_no(ht_carrier_freightList.get(0).getContract_no());
            downVO.setContract_type(ht_carrier_freightList.get(0).getContract_type());
            //取值 - js_tax_rate // LTJ:2019-09-24
            Js_Tax_RateVO rateVO=new Js_Tax_RateVO();
            rateVO.setTax_month(DateUtils.dateFormat(DateUtils.parseDate(item.getBegin_date(),"yyyy-MM-dd"),"yyyy-MM"));
            List<Js_Tax_RateVO> rateList=iJs_Tax_RateService.GetJs_Tax_RateList(rateVO);
            if(null==rateList || rateList.size()<=0)
            {
                rate_more_contract_no_list.add(item.getVin());
                continue;
            }
            downVO.setTax_rate(rateList.get(0).getTax_rate().toString());
            //downVO.setTax_rate(ht_carrierVO.get(0).getTax_rate().toString());
            downVO.setJs_state("1");//已结算

            Float not_tax_freight=0f;
            Float not_tax_other_amount=0f;
            Float not_tax_amount=0f;
            Float tax_amount=0f;
            if(null!=ht_carrier_freightList && ht_carrier_freightList.size()>0) {
                not_tax_freight = ht_carrier_freightList.get(0).getFirst_price().floatValue()*ht_carrier_freightList.get(0).getFirst_mileage().floatValue()
                +ht_carrier_freightList.get(0).getTwo_price().floatValue()*ht_carrier_freightList.get(0).getTwo_mileage().floatValue()
                +ht_carrier_freightList.get(0).getThree_price().floatValue()*ht_carrier_freightList.get(0).getThree_mileage().floatValue();

                not_tax_other_amount=ht_carrier_freightList.get(0).getCross_sea_amount().floatValue();

                //not_tax_amount=not_tax_freight+not_tax_other_amount;
                //运费为第一段单价*第一段里程+第二段单价*第二段里程+第三段单价*第三段里程，与【运输段】无关。
                not_tax_amount=ht_carrier_freightList.get(0).getFirst_mileage().floatValue() * ht_carrier_freightList.get(0).getFirst_price().floatValue()+
                        ht_carrier_freightList.get(0).getTwo_mileage().floatValue() * ht_carrier_freightList.get(0).getTwo_price().floatValue()+
                        ht_carrier_freightList.get(0).getThree_mileage().floatValue() * ht_carrier_freightList.get(0).getThree_price().floatValue()+not_tax_other_amount;

                tax_amount=not_tax_amount+not_tax_amount* Float.valueOf(downVO.getTax_rate());
            }
            downVO.setNot_tax_freight(String.valueOf(not_tax_freight));//运费 - 计算不含税运输费
            downVO.setNot_tax_other_amount(String.valueOf(not_tax_other_amount));//其它费用 - 设置过海费 - 没有保费因为保险对一家承运商保
            downVO.setNot_tax_amount(String.valueOf(not_tax_amount)); //合计 - 设置不含税合计费用
            downVO.setTax_amount(String.valueOf(tax_amount));//设置含税合计=不含税金额*(1+税率)，税率从合同主表获取
            //downVO.setJs_batch(js_batch); //结算批次
            //downVO.setJs_no(js_no); //结算号

            Js_Vin_AmountVO vin_amountVO = new Js_Vin_AmountVO();
            vin_amountVO.setVin(item.getVin());
            List<Js_Vin_AmountVO> vinList=iJs_Vin_Down_AmountService.findListByProperty(vin_amountVO);
            if(null!=vinList && vinList.size()>0){
                downVO.setDz_sheet(vinList.get(0).getDz_sheet());//通过VIN关联对上数据表 js_vin_amount.dz_sheet
                downVO.setBill_number(vinList.get(0).getBill_number());
            }
            if(StringUtils.isBlank(downVO.getBill_number()))
            {
                //3.3.5 预付 - 需求
                downVO.setJs_data_type("3"); //0正常结算，1对上不结（VIP），2对下不结(无合同).3预付
            }

            list.add(downVO);
        }
        if(msg_no_contract_no_list.size() > 0 || msg_more_contract_no_list.size()>0){
            return ResultVO.failResult("结算失败，原因："+ (msg_no_contract_no_list.size() > 0?"</br>没有匹配到合同号，VIN:"+ArrayUtils.join(msg_no_contract_no_list, ",")+"；</br>":"")+(msg_more_contract_no_list.size() > 0?"匹配到多个合同号，VIN:"+ArrayUtils.join(msg_more_contract_no_list, ","):""));
        }
        if(rate_more_contract_no_list.size() > 0){
            return ResultVO.failResult("结算失败，原因：没有匹配到税率，VIN:"+ArrayUtils.join(rate_more_contract_no_list, ","));
        }
        if (list.size() > 0) {
            iJs_Vin_Down_AmountService.update(list);
        }

        return ResultVO.successResult("OK");
    }

    @Log(value = "结算管理-对下结算管理-二次结算")
    @RequestMapping("two_settlementDetail")
    public ResultVO two_settlementDetail(@RequestBody List<String> list) {
        return iJs_Vin_Down_AmountService.two_settlementDetail(list,iSysSheetIdService);
    }



    @Log(value = "结算管理-对下结算管理-预付申请")
    @RequestMapping(value = "expect_apply", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultVO expect_apply(@RequestBody Js_Vin_Down_AmountVO vo, HttpServletRequest request, HttpServletResponse response) {
        String id = ENDECodeUtils.URLDecode(vo.getId());
        String[] id_array = id.split(",");
        List<Js_Vin_Down_AmountVO> list = new ArrayList<Js_Vin_Down_AmountVO>();
        for (String item : id_array) {
            Js_Vin_Down_AmountVO info = new Js_Vin_Down_AmountVO();
            info.setId(item);
            list.add(info);
        }
        //验证状态
        List<Js_Vin_Down_AmountVO> datalist = iJs_Vin_Down_AmountService.findListByProperty(list);
        if (null == datalist || datalist.size() <= 0) {
            return ResultVO.failResult("没有查询到选中数据，原因：数据已被其他用户删除或处理，请查询数据后再操作。");
        }
        if (datalist.size() != list.size()) {
            return ResultVO.failResult("有部份数据已被其他用户处理，请查询数据后再操作。");
        }
        String data_error="";
        for (Js_Vin_Down_AmountVO item : datalist) {
             if(!"4".equals(item.getJs_state()))
             {
                 data_error="选中运单号("+item.getVdr_no()+")状态不是“承运商审核”；";
             }
            if(!"3".equals(item.getJs_data_type()))
            {
                data_error="选中运单号("+item.getVdr_no()+")数据类型不是“预付”；";
            }
        }
        if(StringUtils.isNotBlank(data_error))
        {
            return ResultVO.failResult(data_error);
        }
        String nowDate=DateUtils.nowDate();
        SysUserVO sysUserVO=SessionUtils.currentSession();
        //把数据写入 TR_PAYMENT_PLAN 表
        List<Tr_Payment_PlanVO> insertDataList=new ArrayList<Tr_Payment_PlanVO>();
        for (Js_Vin_Down_AmountVO item : datalist) {
            Tr_Payment_PlanVO planVO=new Tr_Payment_PlanVO();
            planVO.setType("0");//0 商品车 1非商品车 2 补差
            planVO.setState("0");//状态 0正常 1撤回  2已提报 3已完成
            planVO.setCarrier_no(item.getCarrier_no());
            planVO.setCarrier_name(item.getCarrier_name());
            planVO.setInvoice_no(item.getInvoice_no());
            //planVO.setHandover_no(item.geth);//交接单号
            planVO.setVin(item.getVin());
            planVO.setVdr_no(item.getVdr_no());
            planVO.setDealer_name(item.getDealer_name());
            planVO.setBegin_date(item.getBegin_date());
            planVO.setReceipt_date(item.getReceipt_date());
            //planVO.setReceipt_sheet_date(item.getsh);
            planVO.setTrans_mode(item.getTrans_mode());
            planVO.setBegin_address(item.getBegin_city());
            planVO.setEnd_address(item.getEnd_city());
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
        iJs_Vin_Down_AmountService.insert(insertDataList);
        return ResultVO.successResult("预付申请成功");
    }

    @Log(value = "结算管理-对下结算管理-撤回")
    @RequestMapping("un_settlement")
    public ResultVO un_settlement(@RequestBody List<String> list) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = iJs_Vin_Down_AmountService.un_settlement(list, sysUserVO);
        return resultVO;
    }

    //LTJ:2019-07-22 业务审核-财务审核-承运商审核
    // 0：管理员；1：操作员；2：商务人员；3：财务人员；4：其它；
    // 数据状态：0.正常  1.已结算.2业务审核.3.财务审核.4.承运商审核.5.台账，6.开票
    @Log(value = "结算管理-对下结算管理-审核数据")
    @RequestMapping(value = "data_check", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultVO data_check(@RequestBody Js_Vin_Down_AmountVO vo, HttpServletRequest request, HttpServletResponse response) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        String id = ENDECodeUtils.URLDecode(vo.getId());
        String[] id_array = id.split(",");

        //if("0".equals(vo.getCol_contract_type())) {
        List<Js_Vin_Down_AmountVO> list = new ArrayList<Js_Vin_Down_AmountVO>();
        for (String item : id_array) {
            Js_Vin_Down_AmountVO info = new Js_Vin_Down_AmountVO();
            info.setId(item);
            list.add(info);
        }
        //验证状态
        List<Js_Vin_Down_AmountVO> datalist = iJs_Vin_Down_AmountService.findListByProperty(list);
        if (null == datalist || datalist.size() <= 0) {
            return ResultVO.failResult("没有查询到选中数据，原因：数据已被其他用户删除或处理，请查询数据后再操作。");
        }
        if (datalist.size() != list.size()) {
            return ResultVO.failResult("有部份数据已被其他用户处理，请查询数据后再操作。");
        }
        //判断 参数 Js_batch 与状态列是否相同值，相同才能更新，否则报错提示
        String error = "";
        for (Js_Vin_Down_AmountVO item : datalist) {
            if (!vo.getJs_batch().equals(item.getJs_state())) {
                error += item.getVin() + ",";
            }
        }
        if (StringUtils.isNotBlank(error)) {
            return ResultVO.failResult("选中数据状态已发生变化(数据已被其他用户处理)，请查询数据后再操作。VIN：" + error);
        }

        //把 参数 js_state 值更新到选中数据的状态列
        for (Js_Vin_Down_AmountVO item : list) {
            item.setJs_state(vo.getJs_state());
        }


        return iJs_Vin_Down_AmountService.data_check(vo,list,datalist);

        /*
        }else {
            //Js_Vin_DownTemp_Amount 临时数据历史表，不能审核更新数据
            List<Js_Vin_DownTemp_AmountVO> list = new ArrayList<Js_Vin_DownTemp_AmountVO>();
            for (String item : id_array) {
                Js_Vin_DownTemp_AmountVO info = new Js_Vin_DownTemp_AmountVO();
                info.setId(item);
                list.add(info);
            }
            //验证状态
            List<Js_Vin_DownTemp_AmountVO> datalist = iJs_Vin_Down_AmountService.findListByProperty(list);
            if (null == datalist || datalist.size() <= 0) {
                return ResultVO.failResult("没有查询到选中数据，原因：数据已被其他用户删除或处理，请查询数据后再操作。");
            }
            if (datalist.size() != list.size()) {
                return ResultVO.failResult("有部份数据已被其他用户处理，请查询数据后再操作。");
            }
            String error = "";
            for (Js_Vin_DownTemp_AmountVO item : datalist) {
                if (!vo.getJs_batch().equals(item.getJs_state())) {
                    error += item.getVin() + ",";
                }
            }
            if (StringUtils.isNotBlank(error)) {
                return ResultVO.failResult("选中数据状态已发生变化(数据已被其他用户处理)，请查询数据后再操作。VIN：" + error);
            }

            for (Js_Vin_DownTemp_AmountVO item : list) {
                item.setJs_state(vo.getJs_state());
            }

            iJs_Vin_Down_AmountService.update(list);
        }


        return ResultVO.successResult("OK");  */
    }




    //LTJ:2019-07-24 业务审核-财务审核-承运商审核
    // 用户等级：0：管理员；1：操作员；2：商务人员；3：财务人员；4：其它；
    // 数据状态：0.正常  1.业务审核.2商务审核.3.财务审核.4.发送邮件.5.客户确认
    // 数据状态：0.正常  1.业务审核.2商务审核.3.财务审核.4.发送邮件.5.客户确认
    //可驳回：业务审核、财务审核
    @Log(value = "结算管理-对下结算管理-驳回数据")
    @RequestMapping(value = "replayData", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultVO replayData(@RequestBody Js_Vin_Down_AmountVO vo) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        String id = ENDECodeUtils.URLDecode(vo.getId());
        String[] id_array = id.split(",");

        //if("0".equals(vo.getCol_contract_type())) {
            List<Js_Vin_Down_AmountVO> list = new ArrayList<Js_Vin_Down_AmountVO>();
            for (String item : id_array) {
                Js_Vin_Down_AmountVO info = new Js_Vin_Down_AmountVO();
                info.setId(item);
                list.add(info);
            }

            List<Js_Vin_Down_AmountVO> datalist = iJs_Vin_Down_AmountService.findListByProperty(list);
            if (null == datalist || datalist.size() <= 0) {
                return ResultVO.failResult("没有查询到选中数据，原因：数据已被其他用户删除或处理，请查询数据后再操作。");
            }
            if (datalist.size() != list.size()) {
                return ResultVO.failResult("有部份数据已被其他用户处理，请查询数据后再操作。");
            }

            //判断状态 - 根据用户等级
            String error_vin = "";
            String stateStr = "";
            for (Js_Vin_Down_AmountVO item : datalist) {
                //1、首先判断，只允许业务审核和财务审核才允许驳回
                if (!(item.getJs_state().equals("1") ||item.getJs_state().equals("2") || item.getJs_state().equals("3") )) {
                    return ResultVO.failResult("选中的数据必须是‘已结算’或‘业务审核’或‘财务审核’");
                }
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
                return ResultVO.failResult("选中数据“" + stateStr + "”状态已发生变化(数据已被其他用户处理)，请查询数据后再操作。VIN：" + error_vin.substring(0, error_vin.length() - 1));
            }

            //更新 JS_VIN_DOWN_AMOUNT 金额
            //禅道 =  3425    对下【撤回】，【驳回】按钮，需要更新JS_DATA_TYPE=0
            for(Js_Vin_Down_AmountVO item:list)
            {
                item.setJs_data_type("0");
            }
            iJs_Vin_Down_AmountService.UpdateDataList(list);
        //}
        /*else{
            //临时合同
            List<Js_Vin_DownTemp_AmountVO> list = new ArrayList<Js_Vin_DownTemp_AmountVO>();
            for (String item : id_array) {
                Js_Vin_DownTemp_AmountVO info = new Js_Vin_DownTemp_AmountVO();
                info.setId(item);
                list.add(info);
            }

            List<Js_Vin_DownTemp_AmountVO> datalist = iJs_Vin_Down_AmountService.findListByProperty(list);
            if (null == datalist || datalist.size() <= 0) {
                return ResultVO.failResult("没有查询到选中数据，原因：数据已被其他用户删除或处理，请查询数据后再操作。");
            }
            if (datalist.size() != list.size()) {
                return ResultVO.failResult("有部份数据已被其他用户处理，请查询数据后再操作。");
            }

            //判断状态 - 根据用户等级
            String error_vin = "";
            String stateStr = "";
            for (Js_Vin_DownTemp_AmountVO item : datalist) {
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
                return ResultVO.failResult("选中数据“" + stateStr + "”状态已发生变化(数据已被其他用户处理)，请查询数据后再操作。VIN：" + error_vin.substring(0, error_vin.length() - 1));
            }

            //更新 JS_VIN_DOWN_AMOUNT 金额
            //禅道 =  3425    对下【撤回】，【驳回】按钮，需要更新JS_DATA_TYPE=0
            for(Js_Vin_DownTemp_AmountVO item:list)
            {
                item.setJs_data_type("0");
            }
            iJs_Vin_Down_AmountService.UpdateTempDataList(list);
        }*/

        return ResultVO.successResult("OK");
    }

/*    @Log(value = "结算管理-对上账单管理-导出账单")
    @RequestMapping("exportData")
    public void exportData(Js_Vin_Down_AmountVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (null == vo) {
            response.getWriter().write("请选择需要导出的数据。");
            return;
        }
        if (StringUtils.isBlank(vo.getId())) {
            response.getWriter().write("请选择需要导出的数据。");
            return;
        }

        SysUserVO sysUserVO = SessionUtils.currentSession();
        String[] id_array = vo.getId().split(",");
        if ("0".equals(vo.getCol_contract_type())) {
            //正式合同
            List<Js_Vin_Down_AmountVO> detailList = new ArrayList<Js_Vin_Down_AmountVO>();
            for (String item : id_array) {
                Js_Vin_Down_AmountVO info = new Js_Vin_Down_AmountVO();
                info.setId(item);
                detailList.add(info);
            }
            detailList = iJs_Vin_Down_AmountService.findListByProperty(detailList);
            if (null == detailList || detailList.size() <= 0) {
                response.getWriter().write("没有查询到导出选择数据，数据可能被其他用户处理，请查询数据后再操作。");
                return;
            }
            List<Js_Vin_Down_AmountVO> masterList=iJs_Vin_Down_AmountService.GetJs_Vin_Down_AmountVO(detailList);
            CreateDZ_SHEET_To_Excel(0, masterList,null,detailList, null, vo.getCol_contract_type(),sysUserVO, request, response);
        } else {
            //临时合同
            List<Js_Vin_DownTemp_AmountVO> detailList = new ArrayList<Js_Vin_DownTemp_AmountVO>();
            for (String item : id_array) {
                Js_Vin_DownTemp_AmountVO info = new Js_Vin_DownTemp_AmountVO();
                info.setId(item);
                detailList.add(info);
            }
            detailList = iJs_Vin_Down_AmountService.findListByProperty(detailList);
            if (null == detailList || detailList.size() <= 0) {
                response.getWriter().write("没有查询到导出选择数据，数据可能被其他用户处理，请查询数据后再操作。");
                return;
            }
            List<Js_Vin_DownTemp_AmountVO> masterTempList=iJs_Vin_Down_AmountService.GetJs_Vin_DownTemp_AmountVO(detailList);
            CreateDZ_SHEET_To_Excel(0, null,masterTempList,null, detailList,vo.getCol_contract_type(), sysUserVO, request, response);
        }
    }*/

    @Log(value = "结算管理-对下导出账单")
    @RequestMapping("exportData")
    public void exportData(JqGridParamModel jqGridParamModel, String customSearchFilters, String contract_type, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        customSearchFilters = MyUtils.decode(customSearchFilters);
        List<Js_Vin_Down_AmountVO> list = (List<Js_Vin_Down_AmountVO>) iJs_Vin_Down_AmountService.getListForGrid(jqGridParamModel,customSearchFilters,contract_type).getRows();
        List<Js_Vin_Down_AmountVO> yufu_list = new ArrayList<Js_Vin_Down_AmountVO>();//预付单独处理
        Set set = new HashSet();
        List<String> bill_number_list = new ArrayList<>();
        //获取bill_number
        for (Js_Vin_Down_AmountVO down_vo:list) {
            if (StringUtils.notEquals(down_vo.getJs_data_type(),"3") && StringUtils.isNotBlank(down_vo.getBill_number())) {
                if (set.add(down_vo.getBill_number())) {
                    bill_number_list.add(down_vo.getBill_number());
                }
            } else {
                yufu_list.add(down_vo);
            }
        }
        List<Js_Down_ExportVO> exportVOList = new ArrayList<Js_Down_ExportVO>();//需要导出账单数据集合
        for (String bill_number:bill_number_list) {
            Js_Down_ExportVO js_Down_ExportVO = new Js_Down_ExportVO();
            List<Js_Vin_Down_AmountVO> detailList = new ArrayList<Js_Vin_Down_AmountVO>();
            for (Js_Vin_Down_AmountVO down_vo:list) {
                if (StringUtils.equals(bill_number,down_vo.getBill_number()) && StringUtils.notEquals(down_vo.getJs_data_type(),"3")) {
                    detailList.add(down_vo);
                }
            }
            if (detailList.size()>0) {
                List<Js_Vin_Down_AmountVO> masterList=iJs_Vin_Down_AmountService.GetJs_Vin_Down_AmountVO(detailList);
                js_Down_ExportVO.setDetailList(detailList);
                js_Down_ExportVO.setMasterList(masterList);
                exportVOList.add(js_Down_ExportVO);
            }

        }
        //将预付的放入
        if (yufu_list.size()>0) {
            Js_Down_ExportVO js_Down_ExportVO = new Js_Down_ExportVO();
            List<Js_Vin_Down_AmountVO> masterList=iJs_Vin_Down_AmountService.GetJs_Vin_Down_AmountVO(yufu_list);
            js_Down_ExportVO.setDetailList(yufu_list);
            js_Down_ExportVO.setMasterList(masterList);
            exportVOList.add(js_Down_ExportVO);
        }
        //导出数据
        CreateDZ_SHEET_To_Excel(0,exportVOList,sysUserVO, request, response);
    }

    private Js_Vin_Down_AmountVO GetCarTypeModel(String CarType,Js_Vin_Down_AmountVO vo,List<Js_Vin_Down_AmountVO> list)
    {
        Js_Vin_Down_AmountVO info=null;
        for(Js_Vin_Down_AmountVO item: list)
        {
            if(CarType.equalsIgnoreCase(item.getCar_type()) && vo.getCarrier_no().equals(item.getCarrier_no()) && vo.getBegin_city().equals(item.getBegin_city()) && vo.getEnd_city().equals(item.getEnd_city()) && vo.getTrans_mode().equals(item.getTrans_mode()))
            {
                info=item;
                break;
            }
        }
        return info;
    }

    private Js_Vin_DownTemp_AmountVO GetCarTypeTempModel(String CarType,Js_Vin_DownTemp_AmountVO vo,List<Js_Vin_DownTemp_AmountVO> list)
    {
        Js_Vin_DownTemp_AmountVO info=null;
        for(Js_Vin_DownTemp_AmountVO item: list)
        {
            if(CarType.equalsIgnoreCase(item.getCar_type()) && vo.getCarrier_no().equals(item.getCarrier_no()) && vo.getBegin_city().equals(item.getBegin_city()) && vo.getEnd_city().equals(item.getEnd_city()) && vo.getTrans_mode().equals(item.getTrans_mode()))
            {
                info=item;
                break;
            }
        }
        return info;
    }
    //生成对账单Excel文件
    //resKind=0直接输出；=1 写入文件
    private ResultVO CreateDZ_SHEET_To_Excel(int resKind,List<Js_Down_ExportVO> exportVOList,SysUserVO sysUserVO, HttpServletRequest request, HttpServletResponse response)
    {
        ResultVO resultVO=new ResultVO();
        resultVO.setResultCode("1");
        int index=0;
        XSSFWorkbook wb = new XSSFWorkbook();//导出的excel
        String separator= File.separator;
        String Create_date = DateUtils.dateFormat(new Date(),"yyyy-MM-dd");
        String SaveFilePath=System.getProperty("user.dir") + separator + "Excel" + separator + "dz_sheet" + separator + DateUtils.nowDate("yyyyMMdd") + separator;
        String SaveFileName =  Create_date +"对下结算格式" + ".xlsx";
        SaveFilePath += SaveFileName;
        try {
            for (int j=0;j< exportVOList.size();j++) {
                Js_Down_ExportVO exportVO = exportVOList.get(j);
                List<Js_Vin_Down_AmountVO> downList = exportVO.getDetailList();
                List<Js_Vin_Down_AmountVO> masterList = exportVO.getMasterList();
                File file = new File(SaveFilePath);
                if (!file.exists() && !file.isDirectory()) {
                    file.mkdirs();
                }
                SaveFilePath += SaveFileName;
                //往Excel文件中写入数据 开始
                String url = "static/Resource/excel/dz/down_amount_template.xlsx";
                InputStream fis = ClassUtils.class.getClassLoader().getResourceAsStream(url);
                file = new File(SaveFilePath);
                if (file.exists()) {
                    file.delete();
                }
                // 读取Excel文档
                XSSFWorkbook model_wb = new XSSFWorkbook(fis);
                String sheetDate = downList.get(0).getCreate_date().split(" ")[0].replace("-", "");
                model_wb.setSheetName(0, "对下账单" + sheetDate);
                model_wb.setSheetName(1, "明细" + sheetDate);
                // 模板页
                XSSFSheet sheetModel = null;
                XSSFSheet sheetModel_detail = null;
                sheetModel = model_wb.getSheetAt(0);
                sheetModel_detail = model_wb.getSheetAt(1);
                //新建的Sheet页
                XSSFSheet newSheet = null;
                XSSFSheet newSheet_detail = null;
                newSheet = wb.createSheet(exportVO.getMasterList().get(0).getBill_number()!=null?"对下账单"+exportVO.getMasterList().get(0).getBill_number():"预付汇总");
                newSheet_detail = wb.createSheet(exportVO.getMasterList().get(0).getBill_number()!=null?"明细"+exportVO.getMasterList().get(0).getBill_number():"预付明细");
                newSheet.setDisplayGridlines(false);//去除网格线
                POIUtils.copySheet(wb, sheetModel, newSheet);
                POIUtils.copySheet(wb, sheetModel_detail, newSheet_detail);
                //创建单元格样式
                CellStyle xlsxStyle = POIUtils.getTextStype(wb);
                CellStyle numDoublexlsxStyle =POIUtils.getNumDoubleStype(wb);
                CellStyle numIntegerxlsxStyle =POIUtils.getNumIntegerStype(wb);
                Font font = wb.createFont();
                font.setFontName("微软雅黑");
                font.setFontHeightInPoints((short) 10);
                xlsxStyle.setFont(font);
                numDoublexlsxStyle.setFont(font);
                numIntegerxlsxStyle.setFont(font);
                // sheet 对应 工作簿1 - 统计表
                Sheet sheet = wb.getSheetAt(index);
                index++;

                Cell cell_title_two = sheet.getRow(1).getCell(0);
                Integer ColumnNumber = 0;
                cell_title_two.setCellValue("商品车运输费用结算表（" + Create_date + " " + (Double.valueOf(null != masterList.get(0).getTax_rate() ? masterList.get(0).getTax_rate() : "0") * 100) + "%税率）");
                //正式合同
                List<Js_Vin_Down_AmountVO> for_detail = new ArrayList<Js_Vin_Down_AmountVO>();
                List<String> distinctCarTypeList = new ArrayList<String>();
                for (Js_Vin_Down_AmountVO item : masterList) {
                    String con = item.getCarrier_no() + item.getBegin_city() + item.getEnd_city() + item.getTrans_mode();
                    if (!distinctCarTypeList.contains(con)) {
                        for_detail.add(item);
                        distinctCarTypeList.add(con);
                    }
                }

                Double sum_total_tax = 0d;
                Double sum_total_not_tax = 0d;
                int iRow = 0;
                for (Js_Vin_Down_AmountVO item : for_detail) {

                    Row row = sheet.createRow(iRow + 4);
                    ColumnNumber = 0;
                    //1序号
                    POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, String.valueOf(iRow + 1));
                    //2承运商
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, item.getCarrier_name());
                    //2起运地
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, item.getBegin_city());
                    //3目的地
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, item.getEnd_city());
                    //4运输方式
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, item.getTrans_mode());
                    //4运费单价
                    ColumnNumber++;
                    POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber,item.getNot_tax_price()==null?0:Double.parseDouble(item.getNot_tax_price()));
                    //4其它费用
                    ColumnNumber++;
                    POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber,item.getNot_tax_price()==null?0:Double.parseDouble(item.getNot_tax_other_amount()));

                    Integer j36 = 0;
                    Integer j59r = 0;
                    Integer j72x = 0;
                    Integer j72y = 0;
                    Double j36_amount = 0d;
                    Double j59r_amount = 0d;
                    Double j72x_amount = 0d;
                    Double j72y_amount = 0d;
                    Double sum_not_tax_amount = 0d;
                    Double sum_tax_amount = 0d;
                    Js_Vin_Down_AmountVO info = GetCarTypeModel("J36", item, masterList);
                    if (null != info) {
                        //数量
                        j36 = null != info.getJs_qty() ? Integer.valueOf(info.getJs_qty()) : 0;
                        //J36金额
                        j36_amount = j36 * Double.valueOf(null != info.getNot_tax_amount() ? info.getNot_tax_amount() : "0");
                        sum_not_tax_amount += j36_amount;
                        sum_tax_amount = sum_tax_amount + j36 * Double.valueOf(null != info.getTax_amount() ? info.getTax_amount() : "0");
                    }

                    info = GetCarTypeModel("J59R", item, masterList);
                    if (null != info) {
                        //数量
                        j59r = null != info.getJs_qty() ? Integer.valueOf(info.getJs_qty()) : 0;
                        //j59r
                        j59r_amount = j59r * Double.valueOf(null != info.getNot_tax_amount() ? info.getNot_tax_amount() : "0");
                        sum_not_tax_amount += j59r_amount;
                        sum_tax_amount = sum_tax_amount + j59r * Double.valueOf(null != info.getTax_amount() ? info.getTax_amount() : "0");
                    }

                    info = GetCarTypeModel("J72X", item, masterList);
                    if (null != info) {
                        j72x = null != info.getJs_qty() ? Integer.valueOf(info.getJs_qty()) : 0;
                        j72x_amount = j72x * Double.valueOf(null != info.getNot_tax_amount() ? info.getNot_tax_amount() : "0");
                        sum_not_tax_amount += j72x_amount;
                        sum_tax_amount = sum_tax_amount + j72x * Double.valueOf(null != info.getTax_amount() ? info.getTax_amount() : "0");
                    }
                    info = GetCarTypeModel("J72Y", item, masterList);
                    if (null != info) {
                        j72y = null != info.getJs_qty() ? Integer.valueOf(info.getJs_qty()) : 0;
                        j72y_amount = j72y * Double.valueOf(null != info.getNot_tax_amount() ? info.getNot_tax_amount() : "0");
                        sum_not_tax_amount += j72y_amount;
                        sum_tax_amount = sum_tax_amount + j72y * Double.valueOf(null != info.getTax_amount() ? info.getTax_amount() : "0");
                    }
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
                    iRow++;
                }

                //底部统计
                //CMSCL财务部
                Row rowNull_1 = sheet.createRow(downList.size() + 4 + 2);
                POIUtils.CreateCell(rowNull_1, null, 1, "CMSCL财务部：");
                POIUtils.CreateCell(rowNull_1, null, 7, "整车物流部");
                //CMSCL商务部
                Row rowNull_2 = sheet.createRow(downList.size() + 4 + 4);
                POIUtils.CreateCell(rowNull_2, null, 1, "CMSCL商务部：");
                POIUtils.CreateCell(rowNull_2, null, 7, sysUserVO.getRealName());
                //CMSCL整车物流部
                Row rowNull_3 = sheet.createRow(downList.size() + 4 + 6);
                POIUtils.CreateCell(rowNull_3, null, 1, "CMSCL整车物流部：");
                POIUtils.CreateCell(rowNull_3, null, 7, DateUtils.nowDate("yyyy/MM/dd"));

                // sheet 对应 工作簿2 - 明细表
                Sheet detail_sheet = wb.getSheetAt(index);
                index++;
                for (int i = 0; i < downList.size(); i++) {
                    Row row = detail_sheet.createRow(i + 1);
                    ColumnNumber = 0;
                    //1序号
                    POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, String.valueOf(i + 1));
                    //2运单号
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, downList.get(i).getVdr_no());
                    //2VIN
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, downList.get(i).getVin());
                    //2发运日期
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, downList.get(i).getBegin_date().indexOf(" ") != -1 ? downList.get(i).getBegin_date().split(" ")[0] : downList.get(i).getBegin_date());
                    //2收车日期
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, downList.get(i).getReceipt_date().indexOf(" ") != -1 ? downList.get(i).getReceipt_date().split(" ")[0] : downList.get(i).getReceipt_date());
                    //2运输方式
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, downList.get(i).getTrans_mode());
                    //2起点
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, downList.get(i).getBegin_city());
                    //2终点
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, downList.get(i).getEnd_city());
                    //2经销商名称
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, downList.get(i).getDealer_name());
                    //2运输车型
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, downList.get(i).getCar_type());
                    //2分包商
                    ColumnNumber++;
                    POIUtils.CreateCell(row, xlsxStyle, ColumnNumber, downList.get(i).getCarrier_name());
                    //单车费用小计（不含税）
                    ColumnNumber++;
                    POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber, MyUtils.formatDouble2(Double.valueOf(downList.get(i).getNot_tax_amount())));
                    //单车费用小计（含税）
                    ColumnNumber++;
                    POIUtils.CreateNumCell(row, numDoublexlsxStyle, ColumnNumber, MyUtils.formatDouble2(Double.valueOf(downList.get(i).getTax_amount())));
                    sum_total_tax += Double.valueOf(downList.get(i).getTax_amount());
                    sum_total_not_tax += Double.valueOf(downList.get(i).getNot_tax_amount());
                }
                //对下底部统计
                Row rowTotal = detail_sheet.createRow(for_detail.size()+2);
                ColumnNumber = 0;
                POIUtils.CreateCell(rowTotal, xlsxStyle, ColumnNumber, "总计");
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
                POIUtils.CreateNumCell(rowTotal, numDoublexlsxStyle, ColumnNumber,MyUtils.formatDouble2(sum_total_not_tax));
                ColumnNumber++;
                POIUtils.CreateNumCell(rowTotal, numDoublexlsxStyle, ColumnNumber,MyUtils.formatDouble2(sum_total_tax));
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
                SaveFileName = new String(SaveFileName.getBytes("GBK"), "ISO8859_1");
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

            //往Excel文件中写入数据 结束
        } catch (Exception e) {
            resultVO.setResultDataFull("写入Excel文件出错，原因："+e.getMessage());
            return resultVO;
        }

        //写入文件成功，返回 Excel 路径
        resultVO.setResultCode("0");
        resultVO.setResultDataFull(SaveFilePath);
        return resultVO;
    }

    /**
     * 对下匹配合同，需要判断如果之前的合同是预估合同，且状态为【生成台账】，则需要往历史结算表插入数据
     * @param ids
     * @return
     */
    @Log(value = "结算管理-对下结算管理-匹配合同")
    @RequestMapping("/setcontract_no")
    public ResultVO setcontract_no(@RequestBody List<String> ids) {
        if (null == ids || ids.size() <= 0) {
            return ResultVO.failResult("没有选择任何数据。");
        }
        List<Js_Vin_Down_AmountVO> list = new ArrayList<Js_Vin_Down_AmountVO>();
        for (String item : ids) {
            Js_Vin_Down_AmountVO vo = new Js_Vin_Down_AmountVO();
            vo.setId(item);
            list.add(vo);
        }
        List<Js_Vin_Down_AmountVO> Amountlist = iJs_Vin_Down_AmountService.findListByProperty(list);
        if(null==Amountlist || Amountlist.size()<=0)
        {
            return ResultVO.failResult("没有查询到选中的数据，请查询数据后，再操作。");
        }

        String error="";
        for(Js_Vin_Down_AmountVO item : Amountlist )
        {
            if(!((Js_StateEnum.DOWN_CREATE_TZ.getCode().equals(item.getJs_state()) && "2".equals(item.getContract_type()))|| Js_StateEnum.DOWN_NORMAL.getCode().equals(item.getJs_state()))) {
                error+=item.getVin()+",";
            }
        }
        if(StringUtils.isNotBlank(error))
        {
            return ResultVO.failResult("选中如下数据不是“正常”状态或“台账”且“预估合同”数据，请查询后再操作，VIN:"+error);
        }
        //要注意一下，是预估变成了其他合同，合同号+合同类型变化，才会清除数据。
        // 只清除 = 运费 + 其他费用+ 税率+ 合计

        //查找合同
        List<Js_Vin_Down_AmountVO> updateList=new ArrayList<Js_Vin_Down_AmountVO>();
        List<Js_Vin_Down_AmountVO> compensationList = new ArrayList<Js_Vin_Down_AmountVO>();//补差
        Map<String ,Object> map = new HashMap<>() ;
        map = iHt_Carrier_FreightService.matchingContract(Amountlist);
       /* for(Js_Vin_Down_AmountVO item : Amountlist )
        {
            Ht_CarrierVO query_ht_carrierVO = new Ht_CarrierVO();
            query_ht_carrierVO.setCarrier_no(item.getCarrier_no());
            query_ht_carrierVO.setState("1");
            query_ht_carrierVO.setContract_type("1");//正式合同号
            query_ht_carrierVO.setBegin_date(DateUtils.parseDate(item.getBegin_date(), "yyyy-MM-dd HH:mm:ss"));
            List<Ht_CarrierVO> ht_carrierVO = iJs_Vin_Down_AmountService.findHt_CarrierVO(query_ht_carrierVO);
            if(null!=ht_carrierVO && ht_carrierVO.size()>0)
            {
                Js_Vin_Down_AmountVO info=new Js_Vin_Down_AmountVO();
                info.setId(item.getId());
                info.setContract_no(ht_carrierVO.get(0).getContract_no());
                info.setContract_type(ht_carrierVO.get(0).getContract_type());
                updateList.add(info);
            }
        }*/
        updateList = (List<Js_Vin_Down_AmountVO>) map.get("updateList");
        compensationList = (List<Js_Vin_Down_AmountVO>) map.get("compensationList");
        List<String> msg_list = (List<String>) map.get("msg_list");
        ResultVO resultVO = new ResultVO();
        if(updateList.size()>0 || compensationList.size()>0) {
            if (updateList.size()>0) {
                iJs_Vin_Down_AmountService.update(updateList);
            }
            if (compensationList.size()>0) {
                //插入到暂定结算历史表
                List<Js_Vin_Down_AmountVO> paramList = new ArrayList<Js_Vin_Down_AmountVO>();
                for (Js_Vin_Down_AmountVO vo:compensationList) {
                    Js_Vin_Down_AmountVO paramVO = new Js_Vin_Down_AmountVO();
                    paramVO.setId(vo.getId());
                    paramList.add(paramVO);
                }
                List<Js_Vin_Down_AmountVO> dataList = iJs_Vin_Down_AmountService.findListByProperty(paramList);
                //插入到暂定结算历史表
                List<Js_Vin_DownTemp_AmountVO> insertList=new ArrayList<Js_Vin_DownTemp_AmountVO>();
                for (Js_Vin_Down_AmountVO dataVO:dataList) {
                    Js_Vin_DownTemp_AmountVO tempVO = new Js_Vin_DownTemp_AmountVO();
                    BeanUtils.copyProperties(dataVO, tempVO);
                    tempVO.setVin_id(dataVO.getId());
                    insertList.add(tempVO);
                }
                //插入到历史表
                if(insertList.size()>0)
                    iJs_Vin_Down_AmountService.insert(insertList);
                if(compensationList.size()>0)
                    iJs_Vin_Down_AmountService.update(compensationList);
            }
            String message = msg_list.size()==0?"匹配成功":"匹配成功,但部分失败：";
            resultVO = ResultVO.successResult(message,ArrayUtils.join(msg_list, "</br>"));
        } else {
            resultVO = ResultVO.failResult("匹配失败",ArrayUtils.join(msg_list, "</br>"));
        }
        return resultVO;
    }


}
