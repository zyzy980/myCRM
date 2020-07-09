package com.bba.settlement.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.bba.common.constant.BusinessData_projectEnum;
import com.bba.common.constant.Contract_TypeEnum;
import com.bba.common.interceptor.Log;
import com.bba.common.service.api.IBaseService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.ht.service.api.IHt_CusService;
import com.bba.ht.service.api.IHt_Cus_FreightService;
import com.bba.ht.vo.Ht_CusVO;
import com.bba.settlement.service.api.IJs_Vin_AmountService;
import com.bba.settlement.service.api.IJs_Vin_Down_AmountService;
import com.bba.settlement.service.api.IJs_Vin_Temp_AmountService;
import com.bba.settlement.vo.Js_Vin_AmountVO;
import com.bba.settlement.vo.Tr_Statistical_ImportVO;
import com.bba.settlement.vo.VehicleTotalVO;
import com.bba.util.*;
import com.bba.xtgl.vo.SysUserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
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
@RequestMapping("/settlement/Js_Vin_AmountController")
public class Js_Vin_AmountController {

    @Autowired
    private IJs_Vin_AmountService js_vin_amountService;
    @Autowired
    private IJs_Vin_Temp_AmountService js_vin_temp_amountService;
    @Autowired
    private IJs_Vin_Down_AmountService iJs_Vin_Down_AmountService;
    @Autowired
    private IHt_Cus_FreightService ht_cus_freightService;

    @Log(value = "结算管理-对上结算管理-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters, String contract_type) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        customSearchFilters = MyUtils.decode(customSearchFilters);
        PageVO pageVO = null;
        if("0".equals(contract_type)){
            pageVO = js_vin_amountService.getListForGrid(jqGridParamModel,customSearchFilters);
        }else{
            pageVO = js_vin_temp_amountService.getListForGrid(jqGridParamModel,customSearchFilters);
        }

        return pageVO;
    }

    @Log(value = "结算管理-对上结算管理-结算")
    @RequestMapping("settlementDetail")
    public ResultVO settlementDetail(@RequestBody List<String> list) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = js_vin_amountService.settlementDetail(list, sysUserVO);
        return resultVO;
    }

    @Log(value = "结算管理-对上结算管理-正式补差")
    @RequestMapping("two_settlementDetail")
    public ResultVO two_settlementDetail(@RequestBody List<String> list) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = js_vin_amountService.two_settlementDetail(list, sysUserVO);
        return resultVO;
    }

    @Log(value = "结算管理-对上结算管理-撤回")
    @RequestMapping("un_settlement")
    public ResultVO un_settlement(@RequestBody List<String> list) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = js_vin_amountService.un_settlement(list, sysUserVO);
        return resultVO;
    }

    @Log(value = "结算管理-对上结算管理-生成对账单")
    @RequestMapping("create_bill")
    public ResultVO create_bill(@RequestBody List<String> list,String type) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = js_vin_amountService.create_bill(list, sysUserVO,type);
        return resultVO;
    }


    @Log(value = "业务数据-导入数据")
    @RequestMapping("/importData")
    @ResponseBody
    public ResultVO importData(@RequestParam("fileList") MultipartFile file) throws Exception {
        ResultVO resultVO = js_vin_amountService.importData(file);
        return resultVO;
    }








    @Log(value = "结算管理-对上结算管理-匹配合同")
    @RequestMapping("/setcontract_no")
    public ResultVO setcontract_no(@RequestBody List<String> ids) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        if (null == ids || ids.size() <= 0) {
            return ResultVO.failResult("没有选择任何数据。");
        }
        List<Js_Vin_AmountVO> list = new ArrayList<Js_Vin_AmountVO>();
        for (String item : ids) {
            Js_Vin_AmountVO vo = new Js_Vin_AmountVO();
            vo.setId(Long.valueOf(item));
            list.add(vo);
        }
        List<Js_Vin_AmountVO> Amountlist = iJs_Vin_Down_AmountService.findListByProperty(list);
        if(null==Amountlist || Amountlist.size()<=0)
        {
            return ResultVO.failResult("没有查询到选中的数据，请查询数据后，再操作。");
        }

        String error="";
        for(Js_Vin_AmountVO item : Amountlist )
        {
            if(!"0".equals(item.getJs_state()))
            {
                error+=item.getVin()+",";
            }
        }
        if(StringUtils.isNotBlank(error))
        {
            return ResultVO.failResult("选中如下数据不是“正常”状态数据，请查询后再操作，VIN:"+error);
        }

        //查找合同
        //匹配合同需要考虑时间、起运地目的地和规则
        List<Js_Vin_AmountVO> updateList=new ArrayList<Js_Vin_AmountVO>();
        Map<String ,Object> map = new HashMap<>() ;
        map = ht_cus_freightService.matchingContract(Amountlist);
 /*       for(Js_Vin_AmountVO item : Amountlist )
        {
            Ht_CusVO ht_cusVO=new Ht_CusVO();
            ht_cusVO.setCus_no(item.getCus_no());
            ht_cusVO.setState("1");//审核
            ht_cusVO.setBegin_date(item.getBegin_date());
            List<Ht_CusVO> ht_cusList = js_vin_amountService.findHt_cusVO(ht_cusVO);
            if(null!=ht_cusList && ht_cusList.size()>0)
            {
                Js_Vin_AmountVO info=new Js_Vin_AmountVO();
                info.setId(item.getId());
                info.setContract_no(ht_cusList.get(0).getContract_no());
                info.setContract_type(ht_cusList.get(0).getContract_type());
                updateList.add(info);
            }
        }*/
        updateList = (List<Js_Vin_AmountVO>) map.get("updateList");
        List<String> msg_list = (List<String>) map.get("msg_list");
        ResultVO resultVO = new ResultVO();
        if(updateList.size()>0) {
            iJs_Vin_Down_AmountService.update(updateList);
            String message = msg_list.size()==0?"匹配成功":"匹配成功,但部分失败：";
            resultVO = ResultVO.successResult(message,ArrayUtils.join(msg_list, "</br>"));
        } else {
            resultVO = ResultVO.failResult("匹配失败",ArrayUtils.join(msg_list, "</br>"));
        }
        return resultVO;
    }











    /**
     * 报表功能
     * */
    @Log(value = "报表管理-商品车管理-清单查询")
    @RequestMapping("/getListForGridBaobiao")
    public PageVO getListForGridBaobiao(JqGridParamModel jqGridParamModel, String customSearchFilters, String contract_type) {
        //SysUserVO sysUserVO = SessionUtils.currentSession();
        customSearchFilters = MyUtils.decode(customSearchFilters);
        return js_vin_amountService.getListForGridBaobiao(jqGridParamModel,customSearchFilters);
    }


    @Log(value = "报表管理-商品车管理-删除数据")
    @RequestMapping("/delBaobiao")
    public ResultVO delBaobiao(@RequestBody List<Integer> id) {
        if(null==id || id.size()<=0)
        {
            return ResultVO.failResult("没有选择数据，不能删除。");
        }
        List<Tr_Statistical_ImportVO> list = new ArrayList<Tr_Statistical_ImportVO>();
        for(Integer item:id)
        {
            Tr_Statistical_ImportVO vo=new Tr_Statistical_ImportVO();
            vo.setId(item.toString());
            list.add(vo);
        }
        iJs_Vin_Down_AmountService.delete(list);
        return ResultVO.successResult("删除成功。");
    }


    @RequestMapping("/exportTemplateBaobiao")
    @ResponseBody
    public void exportTemplateBaobiao(HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidFormatException {
        POIUtils.exportTemplate(request, response, "商品车统计表导入模板","Resource/excel/baobiao/商品车统计表.xlsx");
    }


    @Log(value = "报表管理-商品车管理-导入数据")
    @RequestMapping("/importBaobiaoData")
    @ResponseBody
    public ResultVO importBaobiaoData(@RequestParam("fileList") MultipartFile file) throws Exception {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        List<Tr_Statistical_ImportVO> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), Tr_Statistical_ImportVO.class, new ImportParams());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null==list || list.size()<=0)
        {
            return ResultVO.failResult("导入失败，导入Excel文件没有数据。");
        }

        String CurrentDate=DateUtils.nowDate();

        for(Tr_Statistical_ImportVO item:list)
        {
            item.setMil(null==item.getMil()?"0":item.getMil());
            item.setPrice(null==item.getPrice()?0d:item.getPrice());
            item.setNot_tax_freight(null==item.getNot_tax_freight()?0d:item.getNot_tax_freight());
            item.setNot_tax_premium(null==item.getNot_tax_premium()?0d:item.getNot_tax_premium());
            item.setNot_tax_other_amount(null==item.getNot_tax_other_amount()?0d:item.getNot_tax_other_amount());
            item.setNot_tax_amount(null==item.getNot_tax_amount()?0d:item.getNot_tax_amount());
            item.setNot_tax_down_price(null==item.getNot_tax_down_price()?0d:item.getNot_tax_down_price());
            item.setNot_tax_other_down_amount(null==item.getNot_tax_other_down_amount()?0d:item.getNot_tax_other_down_amount());
            item.setNot_tax_down_premium(null==item.getNot_tax_down_premium()?0d:item.getNot_tax_down_premium());
            item.setNot_tax_down_amount(null==item.getNot_tax_down_amount()?0d:item.getNot_tax_down_amount());
            item.setReal_tax_amount(null==item.getReal_tax_amount()?0d:item.getReal_tax_amount());
            item.setReal_ntax_amount(null==item.getReal_ntax_amount()?0d:item.getReal_ntax_amount());
            item.setReal_cost(null==item.getReal_cost()?0d:item.getReal_cost());
            item.setTax__real_cost(null==item.getTax__real_cost()?0d:item.getTax__real_cost());


            item.setImport_by(sysUserVO.getUserId());
            item.setImport_date(CurrentDate);
        }

        iJs_Vin_Down_AmountService.insert(list);

        return ResultVO.successResult("导入成功！");
    }

}
