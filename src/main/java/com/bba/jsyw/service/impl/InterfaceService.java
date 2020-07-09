package com.bba.jsyw.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.bba.jsyw.dao.IInterfaceDao;
import com.bba.jsyw.dao.ITr_BusinessDao;
import com.bba.jsyw.service.api.IInterfaceService;
import com.bba.jsyw.vo.InterfaceResultVO;
import com.bba.jsyw.vo.Tr_BusinessVO;
import com.bba.util.JSONUtils;
import com.bba.xtgl.vo.Sys_Dictionary_DataVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
@Transactional
public class InterfaceService<main> implements IInterfaceService {
	@Resource
	private IInterfaceDao interfacedao;
    @Resource
    private ITr_BusinessDao tr_BusinessDao;

	@Override
	public void insertToBusiness(String sql) {
        interfacedao.insertToBusiness(sql);
	}

	@Override
	public String DailyDataToHD(List<Tr_BusinessVO> list) {
		return null;
	}

	@Override
	public Object GetInsertSQL(Tr_BusinessVO item) {
        String	sql=" select ";
        sql+="'"+item.getBegin_datetime()+"',";
        sql+="'"+item.getReceipt_datetime()+"',";
        sql+="'"+item.getVdr_no()+"',";
        sql+="'"+item.getVin()+"',";
        sql+="'"+item.getTrans_mode()+"',";
        sql+="'"+item.getBegin_city()+"',";
        sql+="'"+item.getEnd_city()+"',";
        sql+="'"+item.getEnd_province()+"',";
        sql+="'"+item.getDealer_no()+"',";
        sql+="'"+item.getCar_type()+"',";
        if(null==item.getRemark() || item.getRemark().equals(""))
        {
            sql+="null,";
        }
        else
        {
            sql+="'"+item.getRemark()+"',";
        }
        if(null==item.getDealer_name() || item.getDealer_name().equals(""))
        {
            sql+="null,";
        }
        else
        {
            sql+="'"+item.getDealer_name()+"',";
        }
        sql+="'VL_SYS',sysdate,'1','0','0','0',";

        sql+="'"+item.getCarrier_no()+"',";
        sql+="'"+item.getCarrier_name()+"'";

        sql+=" from dual ";
        return sql;
	}


    /**
     * 数据验证
     * @param tr_list
     * @return
     */
    private String VerificationJsData(List<Tr_BusinessVO> tr_list) {
        // TODO Auto-generated method stub
        String isok = "OK";
        for (Tr_BusinessVO vo : tr_list) {
            int count_1 = interfacedao.findTrBusinessByVin(vo);
            if (count_1>0) {
                isok =vo.getVin()+",";
            }
        }
        return isok.equals("OK")?isok:isok.substring(0,isok.length()-1);
    }

    @Override
    public InterfaceResultVO Vl_BusinessDataToJs(String jsonData) {
        String isok = "OK";
        String message="";
        //解析json数据
        JSONObject json = JSON.parseObject(jsonData);
        jsonData=json.getString("jsonData");
        List<Tr_BusinessVO> business_list = new ArrayList<Tr_BusinessVO>();
        InterfaceResultVO result = InterfaceResultVO.successResult();
        /**解析json*/
        try {
            business_list = JSONUtils.toJSONObjectList(Tr_BusinessVO.class,jsonData);
        } catch (Exception e) {
            isok = "";
            message = "解析JSON数据失败:"+e.getMessage();
            return InterfaceResultVO.failResult(message);
            // TODO: handle exception
        }
        /**验证数据，VIN码不可重复存在于JS系统*/
        isok = VerificationJsData(business_list);
        /**写入bussiness表*/
        if ("OK".equals(isok) && business_list.size()>0) {
            List<Tr_BusinessVO> newList = new ArrayList<Tr_BusinessVO>();
            for(int i = 0; i < business_list.size(); i++){
                newList.add(business_list.get(i));
                /**每80条数据插入一次*/
                if((i+1) % 80 == 0 || i + 1 == business_list.size()){
                    StringBuilder sb=new StringBuilder();
                    sb.append("INSERT INTO TR_BUSSNESS (BEGIN_DATETIME,RECEIPT_DATETIME,VDR_NO,VIN,TRANS_MODE,BEGIN_CITY,END_CITY,END_PROVINCE,DEALER_NO,CAR_TYPE,REMARK,DEALER_NAME,CREATE_BY,CREATE_DATE,VEHICLE_PROJECT,DATA_FROM,DATA_STATE,MASS_LOSS_TYPE,CARRIER_NO,CARRIER_NAME)");
                    for(int j=0,ilen=newList.size();j<ilen;j++)
                    {
                        sb.append(GetInsertSQL(newList.get(j)));
                        if(j<ilen-1)
                            sb.append(" union all ");
                    }
                    try {
                        interfacedao.insertToBusiness(sb.toString());
                    } catch (Exception eFW) {
                        throw new RuntimeException("插入数据失败，请联系管理员");
                    }
                    newList.clear();
                }
            }

        } else {
            message = "VIN码已存在于结算系统："+isok;
            return InterfaceResultVO.failResult(message);
        }
        return result;
    }

    @Override
    @DS("vl_oracle")
    public List<Tr_BusinessVO> getVlBusinessList() {
        return interfacedao.getVlBusinessList();
    }

    @Override
    public void insertToBusinessAndDetail(List<Tr_BusinessVO> list) {
        /**1、批量插入数据*/
       List<Tr_BusinessVO> newList = new ArrayList<Tr_BusinessVO>();
        for(int i = 0; i < list.size(); i++){
            newList.add(list.get(i));
            if((i+1) % 50 == 0 || i + 1 == list.size()){
                //插入主表
                interfacedao.insertToBusinessData(newList);
                //插入明细表
                interfacedao.insertToBusinessCarrierData(newList);
                //插入到保费
                interfacedao.insertJs_vin_down_premium(newList);
                newList.clear();
            }
        }
        /**2、运输方式、城市:业务数据中新的运输方式和城市插入到字典*/
        //获取当前业务数据中运输方式
        List<String> transmodeList = new ArrayList<>();
        List<String> cityList = new ArrayList<>();
        Set set = new HashSet();
        Set city_set = new HashSet();
        for (Tr_BusinessVO transVO:list) {
            if(set.add(transVO.getTrans_mode())) {
                transmodeList.add(transVO.getTrans_mode());
            }
            if (city_set.add(transVO.getBegin_city())) {
                cityList.add(transVO.getBegin_city());
            }
            if (city_set.add(transVO.getEnd_city())) {
                cityList.add(transVO.getEnd_city());
            }
        }

        List<String> existTransTypeList= interfacedao.getZDtransType();
        Integer dicOrder=existTransTypeList.size()+1;
        for (String businessTransType:transmodeList) {
            if (!existTransTypeList.contains(businessTransType)) {
                Sys_Dictionary_DataVO dicVO = new Sys_Dictionary_DataVO();
                dicVO.setSn(0);
                dicVO.setDicvalue(businessTransType);
                dicVO.setDicorder(dicOrder.toString());
                dicVO.setTypecode("TRANS_MODE");
                interfacedao.insertZdictionary_data(dicVO);
                dicOrder++;
            }
        }
        List<String> existCityList= interfacedao.getCityList();
        Integer city_dicOrder=existCityList.size()+1;
        for (String businessCity:cityList) {
            if (!existCityList.contains(businessCity)) {
                Sys_Dictionary_DataVO dicVO = new Sys_Dictionary_DataVO();
                dicVO.setSn(0);
                dicVO.setDicvalue(businessCity);
                dicVO.setDicorder(city_dicOrder.toString());
                dicVO.setTypecode("CITY_ARCHIVE");
                interfacedao.insertZdictionary_data(dicVO);
                city_dicOrder++;
            }
        }
    }

    @Override
    @DS("vl_oracle")
    public void updateVLdataToN(List<Tr_BusinessVO> vl_list) {
        List<String> batchNoList = new ArrayList<>();
        Set set = new HashSet();
        for (Tr_BusinessVO vo:vl_list) {
            if(set.add(vo.getBatch_no())) {
                batchNoList.add(vo.getBatch_no());
            }
        }
        for (String batch_no:batchNoList) {
            interfacedao.updateVLdataToN(batch_no);
        }
    }

    @Override
    @DS("vl_oracle")
    public void updateVLdataToY() {
        interfacedao.updateVLdataToY();
    }
}
