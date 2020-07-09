package com.bba.settlement.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bba.common.constant.Contract_TypeEnum;
import com.bba.common.constant.Js_StateEnum;
import com.bba.common.constant.SETTLEMENT_StateEnum;
import com.bba.common.service.impl.BaseService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.ht.service.api.IHt_CarrierService;
import com.bba.ht.vo.Ht_CarrierVO;
import com.bba.ht.vo.Ht_Carrier_FreightVO;
import com.bba.jcda.service.api.IJs_Tax_RateService;
import com.bba.jcda.vo.Js_Tax_RateVO;
import com.bba.settlement.dao.IJs_Vin_Down_AmountDao;
import com.bba.settlement.dao.Js_Vin_AmountDao;
import com.bba.settlement.service.api.IJs_Vin_Down_AmountService;
import com.bba.settlement.vo.*;
import com.bba.util.*;
import com.bba.xtgl.service.api.ISysSheetIdService;
import com.bba.xtgl.vo.SysSheetIdVO;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class Js_Vin_Down_AmountServiceImpl extends BaseService implements IJs_Vin_Down_AmountService {

    @Autowired
    private IJs_Vin_Down_AmountDao iJs_Vin_Down_AmountDao;
    @Autowired
    private Js_Vin_AmountDao Js_Vin_AmountDao;
    @Autowired
    private IJs_Tax_RateService iJs_Tax_RateService;
    @Autowired
    private IHt_CarrierService ht_CarrierService;
    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters,String contract_type) {
        SysUserVO sysUserVO = SessionUtils.currentSession();

        if(sysUserVO.getUserLevel().equals("5"))
        {
            //承运商角色
            //USERLEVEL = 0：管理员；1：操作员/业务员；2：商务人员；3：财务人员；4：风险；5：承运商
            // 数据状态：0.正常  1.已结算.2业务审核.3.财务审核.4.承运商审核.5.台账，6.开票
            jqGridParamModel.put("carrier_no","eq", sysUserVO.getContractorCode());
            jqGridParamModel.put("js_state","in","'3','4','5','6'");
        }
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        if("0".equals(contract_type)) {
            //当前数据
            List<Js_Vin_Down_AmountVO> list = iJs_Vin_Down_AmountDao.getListForGrid(jqGridParamModel);
            int records = iJs_Vin_Down_AmountDao.getListForGridCount(jqGridParamModel);
            return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
        }else{
            //历史数据
            List<Js_Vin_DownTemp_AmountVO> list = iJs_Vin_Down_AmountDao.getListForGrid_Temp(jqGridParamModel);
            int records = iJs_Vin_Down_AmountDao.getListForGridCount_Temp(jqGridParamModel);
            return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
        }
    }

    @Override
    public PageVO getCompensationListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Js_Vin_Down_CompensationAmountVO> list = iJs_Vin_Down_AmountDao.getCompensationListForGrid(jqGridParamModel);
        int records = iJs_Vin_Down_AmountDao.getCompensationListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
    }

    @Override
    public void UpdateDataList(List<Js_Vin_Down_AmountVO> list)
    {
        iJs_Vin_Down_AmountDao.UpdateDataList(list);
    }

    @Override
    public void UpdateTempDataList(List<Js_Vin_DownTemp_AmountVO> list)
    {
        iJs_Vin_Down_AmountDao.UpdateTempDataList(list);
    }

    @Override
    public List<Ht_CarrierVO> findHt_CarrierVO(Ht_CarrierVO vo)
    {
        return iJs_Vin_Down_AmountDao.findHt_CarrierVO(vo);
    }

    @Override
    public String findJS_VIN_AMOUNT_DZ_SHEET(Js_Vin_AmountVO vo)
    {
        return iJs_Vin_Down_AmountDao.findJS_VIN_AMOUNT_DZ_SHEET(vo);
    }
    @Override
    public List<Ht_Carrier_FreightVO> findHt_Carrier_FreightVO(Js_Vin_Down_AmountVO vo)
    {
        return iJs_Vin_Down_AmountDao.findHt_Carrier_FreightVO(vo);
    }

    @Override
    public ResultVO two_settlementDetail(List<String> idlist, ISysSheetIdService iSysSheetIdService)
    {
        SysUserVO sysUserVO=SessionUtils.currentSession();
        if(idlist.size() == 0){
            return ResultVO.failResult("请至少选择一行数据进行操作");
        }
        List<Js_Vin_Down_AmountVO> js_vin_amountVOList = iJs_Vin_Down_AmountDao.selectBatchIds(idlist);

        List<Js_Vin_Down_AmountVO> list=new ArrayList<Js_Vin_Down_AmountVO>();
        for(String item:idlist)
        {
            Js_Vin_Down_AmountVO vo=new Js_Vin_Down_AmountVO();
            vo.setId(item);
            list.add(vo);
        }
        list = this.findListByProperty(list);
        List<String> msg_list = new ArrayList<String>();
        List<String> msg_no_contract_no_list = new ArrayList<String>();
        List<String> msg_more_contract_no_list = new ArrayList<String>();
        List<String> rate_more_contract_no_list = new ArrayList<String>();
        //插入到暂定结算历史表
        List<Js_Vin_DownTemp_AmountVO> insertList=new ArrayList<Js_Vin_DownTemp_AmountVO>();


        for(int i = 0,line = 1; i < list.size(); i++,line++){
            Js_Vin_Down_AmountVO item = list.get(i);

            Js_Vin_DownTemp_AmountVO tempVO = new Js_Vin_DownTemp_AmountVO();
            BeanUtils.copyProperties(item, tempVO);
            tempVO.setVin_id(item.getId());
            insertList.add(tempVO);

            if(!StringUtils.equals(item.getJs_state(), Js_StateEnum.DOWN_CREATE_FP.getCode())){
                msg_list.add("第"+line+"行,不处于已开票状态,不能进行补差结算");
                continue;
            }
            EntityWrapper ht_carrierEntityWrapper = new EntityWrapper();
            Ht_CarrierVO paramHt_CarrierVO = new Ht_CarrierVO();
            paramHt_CarrierVO.setContract_no(item.getContract_no());
            ht_carrierEntityWrapper.setEntity(paramHt_CarrierVO);
            Ht_CarrierVO dataHt_CarrierVO = ht_CarrierService.selectOne(ht_carrierEntityWrapper);
            if(!Contract_TypeEnum.TEMP.getCode().equals(dataHt_CarrierVO.getContract_type())){
                msg_list.add("第"+line+"行,合同非暂定合同,不能进行补差结算");
                continue;
            }
            item.setContract_type("1");
            List<Ht_Carrier_FreightVO> ht_carrier_freightList = this.findHt_Carrier_FreightVO(item);
            if (null == ht_carrier_freightList || ht_carrier_freightList.size() <= 0) {
                //改变列表显示背景颜色 - 暂时不处理 2017-07-27 16:18
                msg_no_contract_no_list.add(item.getVin());
                continue;
            } else if (ht_carrier_freightList.size() > 1) {
                //改变列表显示背景颜色 - 暂时不处理 2017-07-27 16:18
                msg_more_contract_no_list.add(item.getVin());
                continue;
            }

            item.setContract_no(ht_carrier_freightList.get(0).getContract_no());
            item.setContract_type(ht_carrier_freightList.get(0).getContract_type());
            item.setJs_state("1");//已结算

            //取值 - js_tax_rate // LTJ:2019-09-24
            Js_Tax_RateVO rateVO=new Js_Tax_RateVO();
            rateVO.setTax_month(DateUtils.dateFormat(DateUtils.parseDate(item.getBegin_date(),"yyyy-MM-dd"),"yyyy-MM"));
            List<Js_Tax_RateVO> rateList=iJs_Tax_RateService.GetJs_Tax_RateList(rateVO);
            if(null==rateList || rateList.size()<=0)
            {
                rate_more_contract_no_list.add(item.getVin());
                continue;
            }
            item.setTax_rate(rateList.get(0).getTax_rate().toString());

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

                tax_amount=not_tax_amount+not_tax_amount* Float.valueOf(item.getTax_rate());
            }


            item.setNot_tax_freight(String.valueOf(not_tax_freight));//运费 - 计算不含税运输费
            item.setNot_tax_other_amount(String.valueOf(not_tax_other_amount));//其它费用 - 设置过海费 - 没有保费因为保险对一家承运商保
            item.setNot_tax_amount(String.valueOf(not_tax_amount)); //合计 - 设置不含税合计费用
            item.setTax_amount(String.valueOf(tax_amount));//设置含税合计=不含税金额*(1+税率)，税率从合同主表获取
            //item.setJs_batch(js_batch); //结算批次
            //item.setJs_no(js_no); //结算号
            Js_Vin_AmountVO vin_amountVO = new Js_Vin_AmountVO();
            vin_amountVO.setVin(item.getVin());
            List<Js_Vin_AmountVO> vinList=this.findListByProperty(vin_amountVO);
            if(null!=vinList && vinList.size()>0){
                item.setDz_sheet(vinList.get(0).getDz_sheet());//通过VIN关联对上数据表 js_vin_amount.dz_sheet
                item.setBill_number(vinList.get(0).getBill_number());
            }

            /*
            Js_Vin_AmountVO vin_amountVO = new Js_Vin_AmountVO();
            vin_amountVO.setVin(item.getVin());
            item.setDz_sheet(this.findJS_VIN_AMOUNT_DZ_SHEET(vin_amountVO));//通过VIN关联对上数据表 js_vin_amount.dz_sheet
            */
        }
     /*   if(msg_list.size() > 0){
            return ResultVO.failResult("补差失败,不是“已开票”状态。", "VIN:"+ArrayUtils.join(msg_list, ","));
        }*/
        if(msg_list.size() > 0){
            return ResultVO.failResult("补差结算失败", ArrayUtils.join(msg_list, "</br>"));
        }
        if(msg_no_contract_no_list.size() > 0 || msg_more_contract_no_list.size()>0){
            return ResultVO.failResult("补差失败，原因："+ (msg_no_contract_no_list.size() > 0?"</br>没有匹配到合同号，VIN:"+ArrayUtils.join(msg_no_contract_no_list, ",")+"；</br>":"")+(msg_more_contract_no_list.size() > 0?"匹配到多个正式合同号，VIN:"+ArrayUtils.join(msg_more_contract_no_list, ","):""));
        }
        if(rate_more_contract_no_list.size() > 0){
            return ResultVO.failResult("补差失败，原因：没有匹配到税率，VIN:"+ArrayUtils.join(rate_more_contract_no_list, ","));
        }

        //写入历史表存放
        this.insert(insertList);

      /*  SysSheetIdVO sysSheetIdVO=new SysSheetIdVO();
        sysSheetIdVO.setP_within_code("TMS");
        sysSheetIdVO.setP_whcenter("NO");
        sysSheetIdVO.setP_tablename("JS_VIN_DOWN_AMOUNT");*/
        //String js_no = iSysSheetIdService.USP_WS_SHEETID_GET(sysSheetIdVO);
      /*  SysSheetIdVO sysSheetIdVO1=new SysSheetIdVO();
        sysSheetIdVO1.setP_within_code("TMS");
        sysSheetIdVO1.setP_whcenter("BH");
        sysSheetIdVO1.setP_tablename("JS_VIN_DOWN_AMOUNT");*/
        //String js_batch = iSysSheetIdService.USP_WS_SHEETID_GET(sysSheetIdVO1);
        List<Js_Vin_Down_AmountVO> updateList=new ArrayList<Js_Vin_Down_AmountVO>();
        for(Js_Vin_Down_AmountVO vo: list){
            Js_Vin_Down_AmountVO info=new Js_Vin_Down_AmountVO();
            info.setId(vo.getId());
            //info.setJs_batch(js_batch);
            //info.setJs_no(js_no);
            info.setHis_flag("Y");
            info.setBill_number(vo.getBill_number()+"BC");//二次结算要清空账单编号?
            info.setContract_no(vo.getContract_no());
            info.setContract_type(vo.getContract_type());
            info.setTax_rate(vo.getTax_rate().toString());
            info.setJs_state("1");//已结算 //重新跑流程

            info.setNot_tax_freight(vo.getNot_tax_freight());//运费 - 计算不含税运输费
            info.setNot_tax_other_amount(vo.getNot_tax_other_amount());//其它费用 - 设置过海费 - 没有保费因为保险对一家承运商保
            info.setNot_tax_amount(vo.getNot_tax_amount()); //合计 - 设置不含税合计费用
            info.setTax_amount(vo.getTax_amount());//设置含税合计=不含税金额*(1+税率)，税率从合同主表获取
            info.setDz_sheet(vo.getDz_sheet());

            updateList.add(info);
        }
        this.update(updateList);

        return ResultVO.successResult("结算成功");
    }

    @Override
    public ResultVO un_settlement(List<String> list, SysUserVO sysUserVO) {
        if(list.size() == 0){
            return ResultVO.failResult("请至少选择一行数据进行操作");
        }
        List<Js_Vin_Down_AmountVO> js_vin_amountVOList = iJs_Vin_Down_AmountDao.selectBatchIds(list);

        List<String> msg_list = new ArrayList<String>();
        for(int i = 0; i < js_vin_amountVOList.size(); i++){
            Js_Vin_Down_AmountVO vo = js_vin_amountVOList.get(i);
            if(!SETTLEMENT_StateEnum.SETTLEMENT.getCode().equals(vo.getJs_state())){
                msg_list.add("该数据非结算状态不能进行撤回"+vo.getVdr_no());
                continue;
            }
        }
        if(msg_list.size() > 0){
            return ResultVO.failResult(ArrayUtils.join(msg_list, "</br>"));
        }
        for(int i = 0; i < js_vin_amountVOList.size(); i++) {
            Js_Vin_Down_AmountVO updateVO = new Js_Vin_Down_AmountVO();
            updateVO.setId( js_vin_amountVOList.get(i).getId());
            updateVO.setJs_state(SETTLEMENT_StateEnum.NORMAL.getCode());
            updateVO.setBill_number("");
            updateVO.setNot_tax_freight("0");
            updateVO.setNot_tax_other_amount("0");
            updateVO.setNot_tax_amount("0");
            updateVO.setTax_rate("0");
            updateVO.setTax_amount("0");
            updateVO.setJs_data_type("0");
            updateVO.setContract_no("");
            updateVO.setContract_type("");
            this.update(updateVO);
        }
        return ResultVO.successResult("撤回成功");
    }


    /**
     * list 上传参数
     * datalist 查询数据
     * */
    String vehicle_project="1";//车辆项目 1商品车，2非商品车3其他
    //String type="2"; //1对上补差 2对下补差 3预估补差   ???
    @Override
    public ResultVO data_check(Js_Vin_Down_AmountVO vo,List<Js_Vin_Down_AmountVO> list,List<Js_Vin_Down_AmountVO> datalist)
    {
        //补差 承运商审核 参数 js_state = 4
        String bucha="4";
        if(bucha.equals(vo.getJs_state()))
        {
            //补差功能
            String errorString="";
            List<Js_Vin_Down_AmountVO> insertList=new ArrayList<Js_Vin_Down_AmountVO>();
            List<Js_Vin_DownTemp_AmountVO> insertTempList=new ArrayList<Js_Vin_DownTemp_AmountVO>();
            for(Js_Vin_Down_AmountVO item:datalist) {
                if (("Y".equalsIgnoreCase(item.getHis_flag()) && StringUtils.isNotBlank(item.getJs_no())) || "Y".equalsIgnoreCase(item.getYugu_flag())) {
                    //暂定合同转正式
                    //1、先判断：如果HIS_FLAG=’Y’且JS_NO不为空--（整体条件，说明这个是暂定合同转正式）
                    //先查询历史结算表JS_VIN_DOWNTEMP_AMOUNT,合同为【暂定合同】查出，如果没有数据报出数据异常通知管理员；
                    // 然后用当前【正式合同】结算的数据减去【暂定合同】结算的数据，即当前减历史，将差额插入到JS_COMPENSATION 结算补差表，
                    // 此时插入补差表【类型】为‘2’（对下补差）。

                    //2、其次判断：YUGU_FLAG=’Y’
                    //如果审核的数据YUGU_FLAG=’Y’，（说明此数据是预估合同转签订合同，历史表中只存在【预估合同】的数据）。
                    // 先查询历史结算表JS_VIN_DOWNTEMP_AMOUNT,合同为【预估合同】查出，如果没有数据报出数据异常通知管理员；
                    // 当前结算数据减去历史数据，此时类型为‘3’（预估补差）。
                    Js_Vin_DownTemp_AmountVO tempvo = new Js_Vin_DownTemp_AmountVO();
                    tempvo.setVin_id(item.getId());
                    if ("Y".equalsIgnoreCase(item.getHis_flag()) && StringUtils.isNotBlank(item.getJs_no()))
                        tempvo.setContract_type("0");//1、优先使用   临时合同
                    else
                        tempvo.setContract_type("2");//2、预估合同
                    List<Js_Vin_DownTemp_AmountVO> temp_amountList = this.findListByProperty(tempvo);
                    if (null==temp_amountList || temp_amountList.size()<=0)
                    {
                        errorString+=item.getVin()+",";
                        continue;
                    }
                    insertList.add(item);
                    insertTempList.add(temp_amountList.get(0));
                }
            }
            if(StringUtils.isNotBlank(errorString))
            {
                return ResultVO.failResult("没有查询到历史结算表相关数据，补差出错，VIN:"+errorString);
            }

            for(int i=0,ilen=insertList.size();i<ilen;i++)
            {
                insertJs_CompensationVO(insertList.get(i),insertTempList.get(i));
                Js_Vin_DownTemp_AmountVO tempAmountVO=new Js_Vin_DownTemp_AmountVO();
                tempAmountVO.setId(insertTempList.get(i).getId());
                tempAmountVO.setJs_state(vo.getJs_state());
                this.update(tempAmountVO); //更新历史表
            }
        }


        //反审核
        //补差 承运商反审核 删除表的补差数据
        if(bucha.equals(vo.getJs_batch()))
        {
            // 补差
            List<Js_CompensationVO> compensationList=new ArrayList<Js_CompensationVO>();
            List<Js_Vin_DownTemp_AmountVO> temp_amountVOList=new ArrayList<Js_Vin_DownTemp_AmountVO>();
            for (Js_Vin_Down_AmountVO item : datalist) {
                Js_CompensationVO info=new Js_CompensationVO();
                info.setVin_id(item.getId());
                info.setVehicle_project(vehicle_project); //车辆项目 1商品车，2非商品车3其他
                //info.setType(type);            //1对上补差 2对下补差 3预估补差
                compensationList.add(info);

                Js_Vin_DownTemp_AmountVO tempAmountVO=new Js_Vin_DownTemp_AmountVO();
                tempAmountVO.setVin_id(item.getId());
                tempAmountVO.setJs_state(vo.getJs_state());
                temp_amountVOList.add(tempAmountVO);
            }
            this.delete(compensationList);
            iJs_Vin_Down_AmountDao.updateJs_Vin_DownTemp_Amount(temp_amountVOList);
        }


        //只更新状态一个条件
        this.update(list);

        return ResultVO.successResult("OK");
    }


    /**
     * 写入补差表
     * */
    private void insertJs_CompensationVO(Js_Vin_Down_AmountVO item,Js_Vin_DownTemp_AmountVO info)
    {
        //1、删除原来补差数据2019-11-5注释
        /*Js_CompensationVO compensationVO=new Js_CompensationVO();
        compensationVO.setVin_id(item.getId());
        compensationVO.setVehicle_project(vehicle_project);
        this.delete(compensationVO);*/

        //2、写补差表
        Js_CompensationVO insertVO=new Js_CompensationVO();
        insertVO.setVehicle_project(vehicle_project);
        insertVO.setBegin_city(item.getBegin_city());
        insertVO.setEnd_city(item.getEnd_city());
        insertVO.setBegin_date(item.getBegin_date());
        insertVO.setReceipt_date(item.getReceipt_date());
        insertVO.setCar_type(item.getCar_type());
        insertVO.setTrans_mode(item.getTrans_mode());
        //insertVO.getCus_no(item.getcu)  //对下没有客户
        if("0".equals(info.getContract_type()))
            insertVO.setType("2");//1对上补差，2对下补差，3预估补差
        else if("2".equals(info.getContract_type()))
            insertVO.setType("3");//3预估补差
        insertVO.setDz_sheet(item.getDz_sheet());
        insertVO.setBill_number(item.getBill_number());
        insertVO.setBefor_contract_no(info.getContract_no());
        insertVO.setBefor_contract_type(info.getContract_type());
        insertVO.setAfter_contract_no(item.getContract_no());
        insertVO.setAfter_contract_type(item.getContract_type());
        insertVO.setJs_no(item.getJs_no());
        insertVO.setJs_batch(item.getJs_batch());
        insertVO.setVin_id(item.getId());  //???????????
        insertVO.setVin(item.getVin());
        insertVO.setCarrier_no(item.getCarrier_no());
        insertVO.setCarrier_name(item.getCarrier_name());
        insertVO.setTax_rate(BigDecimal.valueOf(Double.valueOf(item.getTax_rate())));
        insertVO.setTax_up_total(new BigDecimal(0));
        insertVO.setNtax_up_total(new BigDecimal(0));

        Double tax_down_total=Double.valueOf(item.getTax_amount())-Double.valueOf(info.getTax_amount());
        insertVO.setTax_down_total(BigDecimal.valueOf(tax_down_total));//应付运费RMB（含税）
        Double ntax_down_total=Double.valueOf(item.getNot_tax_amount())-Double.valueOf(info.getNot_tax_amount());
        insertVO.setNtax_down_total(BigDecimal.valueOf(ntax_down_total));//应付运费RMB（不含税）

        insertVO.setTax_amount(new BigDecimal(0));
        insertVO.setState("0");
        insertVO.setInvoice_no(item.getInvoice_no());
        if(null!=item.getInvoice_date())
            insertVO.setInvoice_date(DateUtils.dateFormat(item.getInvoice_date(),"yyyy-MM-dd HH:mm:ss"));

        insertVO.setDealer_name(item.getDealer_name());
        insertVO.setVdr_no(item.getVdr_no());
        insertVO.setOld_tax_amount(BigDecimal.valueOf(Double.valueOf(info.getTax_amount())));
        insertVO.setOld_ntax_amount(BigDecimal.valueOf(Double.valueOf(info.getNot_tax_amount())));
        insertVO.setNow_tax_amount(BigDecimal.valueOf(Double.valueOf(item.getTax_amount())));
        insertVO.setNow_ntax_amount(BigDecimal.valueOf(Double.valueOf(item.getNot_tax_amount())));

        //大于0的才补差
        this.insert(insertVO);
    }








    @Override
    public List<Js_Vin_Down_AmountVO> GetJs_Vin_Down_AmountVO(List<Js_Vin_Down_AmountVO> list)
    {
        return iJs_Vin_Down_AmountDao.GetJs_Vin_Down_AmountVO(list);
    }
    @Override
    public List<Js_Vin_DownTemp_AmountVO> GetJs_Vin_DownTemp_AmountVO(List<Js_Vin_DownTemp_AmountVO> list)
    {
        return iJs_Vin_Down_AmountDao.GetJs_Vin_DownTemp_AmountVO(list);
    }

}
