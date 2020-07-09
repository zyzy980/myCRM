package com.bba.nosettlement.service.impl;


import com.bba.common.constant.NOSETTLEMENT_TAB_Enum;
import com.bba.common.constant.SETTLEMENT_StateEnum;
import com.bba.common.service.impl.BaseService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.ht.vo.Non_Ht_CarrierVO;
import com.bba.ht.vo.Non_Ht_Carrier_FreightVO;
import com.bba.jcda.service.api.IJs_Tax_RateService;
import com.bba.jcda.vo.Js_Tax_RateVO;
import com.bba.nosettlement.dao.IJs_Non_Down_VehicleDao;
import com.bba.nosettlement.service.api.IJs_Non_Down_VehicleService;
import com.bba.nosettlement.vo.Js_Non_Down_VehicleVO;
import com.bba.nosettlement.vo.Js_Non_VehicleVO;
import com.bba.settlement.vo.Js_CompensationVO;
import com.bba.settlement.vo.Js_Non_DownTemp_VehicleVO;
import com.bba.util.*;
import com.bba.xtgl.service.api.ISysSheetIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/*
 * Author:LTJ
 * Date:2019-07-31
 * Description:3.3.3.非商品车对下结算操作
 * */
@Transactional
@Service
public class Js_Non_Down_VehicleService extends BaseService implements IJs_Non_Down_VehicleService {

    @Resource
    private IJs_Non_Down_VehicleDao iJs_Non_Down_VehicleDao;
    @Autowired
    private ISysSheetIdService iSysSheetIdService;
    @Autowired
    private IJs_Tax_RateService iJs_Tax_RateService;

    /**
     * 查询
     * */
    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters)
    {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Js_Non_Down_VehicleVO> list = iJs_Non_Down_VehicleDao.getListForGrid(jqGridParamModel);
        int records = iJs_Non_Down_VehicleDao.getListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
    }
    /**
     * 驳回
     * */
    @Override
    public void UpdateDataList(List<Js_Non_Down_VehicleVO> list)
    {
        iJs_Non_Down_VehicleDao.UpdateDataList(list);
    }


    /**
     * 非商品车对下结算
     * */
    @Override
    public ResultVO settlement(List<Js_Non_Down_VehicleVO> dataList)
    {
        // 属性	ATTRIBUTE	0 体系内，1 体系外		varchar2(40)		FALSE	FALSE
        // TYPE	TYPE	0 VIP, 1 保密，2 合同价	3 救援车	varchar2(40)		FALSE	FALSE

        //匹配合同计算费用
        Integer iRows=0;
        Integer isucc=0;
        Integer ierror=0;
        String errorString="";
        for(Js_Non_Down_VehicleVO item:dataList) {
            iRows++;
            //前端有4个界面分别使用 ATTRIBUTE=0 且 type=0,1,2,3 区分
            if (NOSETTLEMENT_TAB_Enum.VIP.getCode().equals(item.getType())) {
                //VIP界面
                ResultVO res=updateVehicleContractData_settlement(item);
                if(!"0".equals(res.getResultCode()))
                {
                    errorString+="交车单号 "+item.getHandover_no()+" "+((ResultDataFullMore)res.getResultDataFull()).getSimpleMessage()+"；";
                    ierror++;
                }
                else {
                    isucc++;
                }
            } else if (NOSETTLEMENT_TAB_Enum.SEC.getCode().equals(item.getType())) {
                //保密车
                //保密车，不走合同规则，人工结算后导入系统。
            } else if (NOSETTLEMENT_TAB_Enum.CON.getCode().equals(item.getType())) {
                //合同车
                ResultVO res=updateVehicleContractData_settlement(item);
                if(!"0".equals(res.getResultCode()))
                {
                    errorString+="交车单号 "+item.getHandover_no()+" "+((ResultDataFullMore)res.getResultDataFull()).getSimpleMessage()+"；";
                    ierror++;
                }
                else {
                    isucc++;
                }
            } else if (NOSETTLEMENT_TAB_Enum.SAV.getCode().equals(item.getType())) {
                //救援车
                ResultVO res=updateVehicleContractData_settlement(item);
                if(!"0".equals(res.getResultCode()))
                {
                    errorString+="交车单号 "+item.getHandover_no()+" "+((ResultDataFullMore)res.getResultDataFull()).getSimpleMessage()+"；";
                    ierror++;
                }
                else {
                    isucc++;
                }
            }
        }
        return ResultVO.successResult("结算完成；成功："+isucc.toString()+"条；失败："+ierror.toString()+"条，原因："+errorString);
    }

    private ResultVO updateVehicleContractData_contract(Js_Non_Down_VehicleVO item)
    {
        //匹配合同
        return updateVehicleContractData(item,0);
    }
    private ResultVO updateVehicleContractData_settlement(Js_Non_Down_VehicleVO item)
    {
        //结算
        return updateVehicleContractData(item,1);
    }
    private ResultVO updateVehicleContractData(Js_Non_Down_VehicleVO item,int iKind)
    {
        Js_Non_Down_VehicleVO updateVO=new Js_Non_Down_VehicleVO();
        updateVO.setId(item.getId());
        //查找合同号
        //查找合同号对应运输合同规则
        Non_Ht_Carrier_FreightVO freightVO=iJs_Non_Down_VehicleDao.findContractFreightVO(item);
        if(null==freightVO || null==freightVO.getSn())
            return ResultVO.failResult("没有找到合同号运费规则。");

        updateVO.setContract_no(freightVO.getContract_no());
        updateVO.setContract_type(freightVO.getContract_type());
        //取值 - js_tax_rate // LTJ:2019-09-24
        Js_Tax_RateVO rateVO=new Js_Tax_RateVO();
        rateVO.setTax_month(DateUtils.dateFormat(DateUtils.parseDate(item.getBegin_date(),"yyyy-MM-dd"),"yyyy-MM"));
        List<Js_Tax_RateVO> rateList=iJs_Tax_RateService.GetJs_Tax_RateList(rateVO);
        if(null==rateList || rateList.size()<=0)
        {
            return ResultVO.failResult("没有找到税率（"+rateVO.getTax_month()+"）。");
        }
        updateVO.setTax_rate(rateList.get(0).getTax_rate().toString());
        //updateVO.setTax_rate(contractVO.getTax_rate().toString());

        if(iKind==1) {//结算
            //赋值费用
            //updateVO.setNot_tax_premium(freightVO.getPremium().toString()); //不含税保费  对下没有保费
            Float not_tax_freight = freightVO.getFirst_mileage().floatValue() * freightVO.getFirst_price().floatValue() + freightVO.getTwo_mileage().floatValue() * freightVO.getTwo_price().floatValue() + freightVO.getThree_mileage().floatValue() * freightVO.getThree_price().floatValue();
            updateVO.setNot_tax_freight(not_tax_freight.toString());//运费
            updateVO.setNot_tax_other_amount(freightVO.getCross_sea_amount().toString());//其它费用
            Float not_tax_amount=not_tax_freight+freightVO.getCross_sea_amount().floatValue();
            Float tax_amount=not_tax_amount+not_tax_amount*Float.valueOf(updateVO.getTax_rate());
            updateVO.setNot_tax_amount(not_tax_amount.toString());//不含税合计
            updateVO.setTax_amount(tax_amount.toString());//含税合计
            updateVO.setJs_state("1");
        }
        //匹配  账单编号 字段
        //需求文档 3.3.5
        Js_Non_VehicleVO vechicleVO=new Js_Non_VehicleVO();
        vechicleVO.setBus_id(item.getBus_id());
        List<Js_Non_VehicleVO> vechicleList=this.findListByProperty(vechicleVO);
        if(null!=vechicleList && vechicleList.size()>0)
        {
            updateVO.setBill_number(vechicleList.get(0).getBill_number());
        }
        if(StringUtils.isBlank(updateVO.getBill_number())){
            updateVO.setJs_data_type("3");//0正常结算，1对上不结），2对下（VIP不结(无合同).3预付
        }

        this.update(updateVO);
        return ResultVO.successResult("OK");
    }

    /**
     * 暂时没用
     * */
    @Override
    public ResultVO setcontract_no(List<Js_Non_Down_VehicleVO> dataList)
    {
        Integer iRows=0;
        Integer isucc=0;
        Integer ierror=0;
        String errorString="";
        for(Js_Non_Down_VehicleVO item:dataList) {
            //前端有4个界面分别使用 ATTRIBUTE=0 且 type=0,1,2,3 区分
            if (NOSETTLEMENT_TAB_Enum.VIP.getCode().equals(item.getType())) {
                //VIP界面
                ResultVO res = updateVehicleContractData_contract(item);
                if (!"0".equals(res.getResultCode())) {
                    errorString += "交车单号 " + item.getHandover_no() + " " + ((ResultDataFullMore) res.getResultDataFull()).getSimpleMessage() + "；";
                    ierror++;
                } else {
                    isucc++;
                }

            } else if (NOSETTLEMENT_TAB_Enum.SEC.getCode().equals(item.getType())) {
                //保密车
                //问题描述：不匹配，因为保密车是手工结算。
            } else if (NOSETTLEMENT_TAB_Enum.CON.getCode().equals(item.getType())) {
                //合同车
                ResultVO res=updateVehicleContractData_contract(item);
                if(!"0".equals(res.getResultCode()))
                {
                    errorString+="交车单号 "+item.getHandover_no()+" "+((ResultDataFullMore)res.getResultDataFull()).getSimpleMessage()+"；";
                    ierror++;
                }
                else {
                    isucc++;
                }
            } else if (NOSETTLEMENT_TAB_Enum.SAV.getCode().equals(item.getType())) {
                //救援车
                ResultVO res=updateVehicleContractData_contract(item);
                if(!"0".equals(res.getResultCode()))
                {
                    errorString+="交车单号 "+item.getHandover_no()+" "+((ResultDataFullMore)res.getResultDataFull()).getSimpleMessage()+"；";
                    ierror++;
                }
                else {
                    isucc++;
                }
            }
        }
        return ResultVO.successResult("匹配合同完成；成功："+isucc.toString()+"条；失败："+ierror.toString()+"条，原因："+errorString);
    }


    /**
     * 结算数据 - 撤回
     * */
    @Override
    public ResultVO  un_settlement(List<Js_Non_Down_VehicleVO> list)
    {
        List<Js_Non_Down_VehicleVO> js_vin_amountVOList = this.findListByProperty(list);
        List<String> msg_list = new ArrayList<String>();
        for(int i = 0; i < js_vin_amountVOList.size(); i++){
            Js_Non_Down_VehicleVO vo = js_vin_amountVOList.get(i);
            if(!SETTLEMENT_StateEnum.SETTLEMENT.getCode().equals(vo.getJs_state())){
                msg_list.add("该数据非结算状态不能进行撤回,交车单号:"+vo.getHandover_no());
                continue;
            }
        }
        if(msg_list.size() > 0){
            return ResultVO.failResult(ArrayUtils.join(msg_list, "</br>"));
        }
        for(int i = 0; i < js_vin_amountVOList.size(); i++) {
            Js_Non_Down_VehicleVO updateVO = new Js_Non_Down_VehicleVO();
            updateVO.setId(js_vin_amountVOList.get(i).getId());
            updateVO.setJs_state(SETTLEMENT_StateEnum.NORMAL.getCode());
            updateVO.setBill_number("");
            updateVO.setNot_tax_freight("0");
            updateVO.setNot_tax_other_amount("0");
            updateVO.setNot_tax_amount("0");
            updateVO.setTax_amount("0");
            updateVO.setTax_rate("");
            /*updateVO.setContract_no("");
            updateVO.setContract_type("");*/
            this.update(updateVO);
        }
        return ResultVO.successResult("撤回成功");
    }


    String vehicle_project="2";//车辆项目 1商品车，2非商品车, 3其他
    //String type="2"; //1对上补差 2对下补差 3预估补差   ???
    @Override
    public ResultVO  data_check(Js_Non_Down_VehicleVO vo,List<Js_Non_Down_VehicleVO> list,List<Js_Non_Down_VehicleVO> datalist) {
        //补差 承运商审核 参数 js_state = 4
        String bucha = "4";
        if (bucha.equals(vo.getJs_state())) {
            //补差功能
            String errorString = "";
            List<Js_Non_Down_VehicleVO> insertList = new ArrayList<Js_Non_Down_VehicleVO>();
            List<Js_Non_DownTemp_VehicleVO> insertTempList = new ArrayList<Js_Non_DownTemp_VehicleVO>();
            for (Js_Non_Down_VehicleVO item : datalist) {
                if ("Y".equalsIgnoreCase(item.getYugu_flag())) {
                    //1、如果审核的数据YUGU_FLAG是否为‘Y’（说明此数据需要补差）。由于非商品车没有暂定和正式合同之分，
                    // 所以非商品车补差只能是预估补差。从历史表查询预估合同的数据，生成差额插入到补差表。补差类型为‘3’
                    Js_Non_DownTemp_VehicleVO tempvo = new Js_Non_DownTemp_VehicleVO();
                    tempvo.setVin_id(item.getId());
                    //tempvo.setContract_type("2");//2、预估合同
                    List<Js_Non_DownTemp_VehicleVO> temp_amountList = this.findListByProperty(tempvo);
                    if (null == temp_amountList || temp_amountList.size() <= 0) {
                        errorString += item.getVin() + ",";
                        continue;
                    }
                    insertList.add(item);
                    insertTempList.add(temp_amountList.get(0));
                }
            }
            if (StringUtils.isNotBlank(errorString)) {
                return ResultVO.failResult("没有查询到历史结算表相关数据补差出错，VIN:" + errorString);
            }

            for (int i = 0, ilen = insertList.size(); i < ilen; i++) {
                insertJs_CompensationVO(insertList.get(i), insertTempList.get(i));

                Js_Non_DownTemp_VehicleVO tempAmountVO=new Js_Non_DownTemp_VehicleVO();
                tempAmountVO.setId(insertTempList.get(i).getId());
                tempAmountVO.setJs_state(vo.getJs_state());
                this.update(tempAmountVO); //更新历史表
            }
        }


        //反审核
        //补差 承运商反审核 删除表的补差数据
        if (bucha.equals(vo.getJs_batch())) {
            // 补差
            List<Js_CompensationVO> compensationList = new ArrayList<Js_CompensationVO>();
            List<Js_Non_DownTemp_VehicleVO> temp_vehicleList=new ArrayList<Js_Non_DownTemp_VehicleVO>();
            for (Js_Non_Down_VehicleVO item : datalist) {
                Js_CompensationVO info = new Js_CompensationVO();
                info.setVin_id(item.getId());
                info.setVehicle_project(vehicle_project); //车辆项目 1商品车，2非商品车3其他
                //info.setType(type);            //1对上补差 2对下补差 3预估补差
                compensationList.add(info);

                Js_Non_DownTemp_VehicleVO temp_vehicleVO=new Js_Non_DownTemp_VehicleVO();
                temp_vehicleVO.setVin_id(item.getId());
                temp_vehicleVO.setJs_state(vo.getJs_state());
                temp_vehicleList.add(temp_vehicleVO);
            }
            this.delete(compensationList);
            iJs_Non_Down_VehicleDao.updateJs_Non_DownTemp_Vehicle(temp_vehicleList);
        }


        //只更新状态一个条件
        this.update(list);

        return ResultVO.successResult("OK");
    }

    /**
     * 写入补差表
     * */
    private void insertJs_CompensationVO(Js_Non_Down_VehicleVO item, Js_Non_DownTemp_VehicleVO info)
    {
        //1、删除原来补差数据
        Js_CompensationVO compensationVO=new Js_CompensationVO();
        compensationVO.setVin_id(item.getId());
        compensationVO.setVehicle_project(vehicle_project);
        this.delete(compensationVO);

        //2、写补差表
        Js_CompensationVO insertVO=new Js_CompensationVO();
        insertVO.setVehicle_project(vehicle_project);
        insertVO.setBegin_city(item.getBegin_address());
        insertVO.setEnd_city(item.getEnd_address());
        insertVO.setBegin_date(item.getBegin_date());
        insertVO.setReceipt_date(item.getReceipt_date());
        insertVO.setCar_type(item.getCar_type());
        insertVO.setTrans_mode(item.getTrans_mode());
        //insertVO.getCus_no(item.getCus_no())  //对下没有客户
        insertVO.setType("3");//3预估补差
        //insertVO.setDz_sheet(item.getDz_sheet());
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

        //insertVO.setDealer_name();
        //insertVO.setVdr_no();
        insertVO.setOld_tax_amount(BigDecimal.valueOf(Double.valueOf(info.getTax_amount())));
        insertVO.setOld_ntax_amount(BigDecimal.valueOf(Double.valueOf(info.getNot_tax_amount())));
        insertVO.setNow_tax_amount(BigDecimal.valueOf(Double.valueOf(item.getTax_amount())));
        insertVO.setNow_ntax_amount(BigDecimal.valueOf(Double.valueOf(item.getNot_tax_amount())));

        this.insert(insertVO);
    }
}
