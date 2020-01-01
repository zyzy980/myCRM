package com.bba.ht.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.constant.Js_StateEnum;
import com.bba.ht.dao.Ht_Carrier_FreightDao;
import com.bba.ht.service.api.IHt_Carrier_FreightService;
import com.bba.ht.vo.Ht_Carrier_FreightVO;
import com.bba.ht.vo.Non_Ht_Carrier_FreightVO;
import com.bba.nosettlement.vo.Js_Non_Down_VehicleVO;
import com.bba.settlement.vo.Js_Vin_Down_AmountVO;
import com.bba.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 */
@Service
public class Ht_Carrier_FreightImpl extends ServiceImpl<Ht_Carrier_FreightDao, Ht_Carrier_FreightVO> implements IHt_Carrier_FreightService {

    @Autowired
    private Ht_Carrier_FreightDao ht_Carrier_FreightDao;

    /**
     * 获取合同 优先顺序
     * 1=正式合同
     * 0=临时合同
     * 2=预估合同
     * */
    private Ht_Carrier_FreightVO GetContractFilter( List<Ht_Carrier_FreightVO> list)
    {
        for(Ht_Carrier_FreightVO item:list)
        {
            if("1".equals(item.getContract_type()))
            {
                return item;
            }
        }
        for(Ht_Carrier_FreightVO item:list)
        {
            if("0".equals(item.getContract_type()))
            {
                return item;
            }
        }
        for(Ht_Carrier_FreightVO item:list)
        {
            if("2".equals(item.getContract_type()))
            {
                return item;
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> matchingContract(List<Js_Vin_Down_AmountVO> list) {
        List<String> msg_list = new ArrayList<String>();
        List<Js_Vin_Down_AmountVO> updateList=new ArrayList<Js_Vin_Down_AmountVO>();//直接匹配合同
        List<Js_Vin_Down_AmountVO> compensationList = new ArrayList<Js_Vin_Down_AmountVO>();//需要补差的
        for(int i = 0,line = 1; i < list.size(); i++,line++){
            Js_Vin_Down_AmountVO vo = list.get(i);
            String old_contract_type = vo.getContract_type();
            String old_js_state = vo.getJs_state();
            //1.得到相关的合同规则
            List<Ht_Carrier_FreightVO> dataHt_Carrier_FreightVOList = ht_Carrier_FreightDao.findFreightByBusiness(vo);
            if(dataHt_Carrier_FreightVOList.size() == 0){

                msg_list.add("第"+line+"行,未匹配到合同");
                continue;
            }
            /*2019-09-05 操作员看到“匹配到多条合同”提示，他们愿意去注释其它的合同，只保留一条*/
            if(dataHt_Carrier_FreightVOList.size() > 1){
                msg_list.add("第"+line+"行,匹配到多条合同");
                continue;
            }

            //Ht_Carrier_FreightVO contractVO=GetContractFilter(dataHt_Carrier_FreightVOList);
            Ht_Carrier_FreightVO contractVO=dataHt_Carrier_FreightVOList.get(0);
            //如果是临时转临时，不允许匹配
            if(StringUtils.equals(old_contract_type,"2") && StringUtils.equals(contractVO.getContract_type(),"2")){
                msg_list.add("第"+line+"行,预估合同不可重复匹配");
                continue;
            }
            Js_Vin_Down_AmountVO info=new Js_Vin_Down_AmountVO();
            //预估转签订，重置结算内容
            info.setId(vo.getId());
            info.setContract_no(contractVO.getContract_no());
            info.setContract_type(contractVO.getContract_type());
            info.setJs_state(Js_StateEnum.DOWN_NORMAL.getCode());
            info.setTax_rate("0");
            info.setNot_tax_freight("0");
            info.setNot_tax_other_amount("0");
            info.setNot_tax_price("0");
            info.setTax_amount("0");
            info.setNot_tax_amount("0");
            info.setBill_number("");

            if ((StringUtils.equals(old_contract_type,"2") && StringUtils.equals(old_js_state, Js_StateEnum.DOWN_CREATE_TZ.getCode())) && StringUtils.notEquals(info.getContract_type(),"2")) { //预估合同转签订合同且已经台账，需要往历史插入数据
                info.setYugu_flag("Y");
                compensationList.add(info);
            } else {
                updateList.add(info);
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("msg_list",msg_list);
        map.put("compensationList",compensationList);
        map.put("updateList",updateList);
        return map;
    }














































    /**
     * 获取合同 优先顺序
     * 1=正式合同
     * 0=临时合同
     * 2=预估合同
     * */
    private Non_Ht_Carrier_FreightVO GetContractFilter_non_down( List<Non_Ht_Carrier_FreightVO> list)
    {
        for(Non_Ht_Carrier_FreightVO item:list)
        {
            if("1".equals(item.getContract_type()))
            {
                return item;
            }
        }
        for(Non_Ht_Carrier_FreightVO item:list)
        {
            if("0".equals(item.getContract_type()))
            {
                return item;
            }
        }
        for(Non_Ht_Carrier_FreightVO item:list)
        {
            if("2".equals(item.getContract_type()))
            {
                return item;
            }
        }
        return null;
    }
    @Override
    public Map<String, Object> matchingContract_non_down(List<Js_Non_Down_VehicleVO> list) {
        List<String> msg_list = new ArrayList<String>();
        List<Js_Non_Down_VehicleVO> updateList=new ArrayList<Js_Non_Down_VehicleVO>();//直接匹配合同
        List<Js_Non_Down_VehicleVO> compensationList = new ArrayList<Js_Non_Down_VehicleVO>();//需要补差的
        for(int i = 0,line = 1; i < list.size(); i++,line++){
            Js_Non_Down_VehicleVO vo = list.get(i);
            String old_contract_type = vo.getContract_type();
            String old_js_state = vo.getJs_state();
            //1.得到相关的合同规则
            List<Non_Ht_Carrier_FreightVO> dataHt_Carrier_FreightVOList=ht_Carrier_FreightDao.findFreightByBusiness_non_down(vo);

            if(dataHt_Carrier_FreightVOList.size() == 0){

                msg_list.add("第"+line+"行,未匹配到合同");
                continue;
            }
            /*2019-09-05 操作员看到“匹配到多条合同”提示，他们愿意去注释其它的合同，只保留一条*/
            if(dataHt_Carrier_FreightVOList.size() > 1){
                msg_list.add("第"+line+"行,匹配到多条合同");
                continue;
            }
            //Non_Ht_Carrier_FreightVO non_carrierVO=GetContractFilter_non_down(dataHt_Carrier_FreightVOList);
            Non_Ht_Carrier_FreightVO non_carrierVO=dataHt_Carrier_FreightVOList.get(0);
            //如果是临时转临时，不允许匹配
            if(StringUtils.equals(old_contract_type,"2") && StringUtils.equals(vo.getContract_type(),"2")){
                msg_list.add("第"+line+"行,临时合同不可重复匹配");
                continue;
            }
            Js_Non_Down_VehicleVO info=new Js_Non_Down_VehicleVO();
            info.setId(vo.getId());
            info.setContract_no(non_carrierVO.getContract_no());
            info.setContract_type(non_carrierVO.getContract_type());
            info.setTax_rate("0");
            info.setNot_tax_freight("0");
            info.setNot_tax_other_amount("0");
            info.setNot_tax_price("0");
            info.setTax_amount("0");
            info.setNot_tax_amount("0");
            info.setBill_number("");
            if ((StringUtils.equals(old_contract_type,"2") && StringUtils.equals(old_js_state, Js_StateEnum.DOWN_CREATE_TZ.getCode())) && StringUtils.notEquals(info.getContract_type(),"2")) { //预估合同转签订合同且已经台账，需要往历史插入数据
                info.setYugu_flag("Y");
                compensationList.add(info);
            } else {
                updateList.add(info);
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("msg_list",msg_list);
        map.put("compensationList",compensationList);
        map.put("updateList",updateList);
        return map;
    }
}
