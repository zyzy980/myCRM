package com.bba.ht.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.constant.Contract_TypeEnum;
import com.bba.common.vo.PageVO;
import com.bba.ht.dao.Ht_CusDao;
import com.bba.ht.dao.Ht_Cus_FreightDao;
import com.bba.ht.service.api.IHt_CusService;
import com.bba.ht.service.api.IHt_Cus_FreightService;
import com.bba.ht.vo.Ht_CusVO;
import com.bba.ht.vo.Ht_Cus_FreightVO;
import com.bba.jcda.service.api.IJs_Tax_RateService;
import com.bba.jcda.vo.Js_Tax_RateVO;
import com.bba.settlement.vo.Js_Vin_AmountVO;
import com.bba.util.DateUtils;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
@Service
public class Ht_Cus_FreightImpl extends ServiceImpl<Ht_Cus_FreightDao, Ht_Cus_FreightVO> implements IHt_Cus_FreightService {

    @Autowired
    private Ht_Cus_FreightDao ht_Cus_FreightDao;
    @Autowired
    private Ht_CusDao ht_cusDao;
    @Autowired
    private IJs_Tax_RateService iJs_Tax_RateService;

    @Override
    public List<String> processFreightByTempBusiness(List<Js_Vin_AmountVO> list) {


        List<String> msg_list = new ArrayList<String>();

        for(int i = 0,line = 1; i < list.size(); i++,line++){
            Js_Vin_AmountVO vo = list.get(i);
            //1.得到相关的合同规则
            List<Ht_Cus_FreightVO> dataHt_Cus_FreightVOList = ht_Cus_FreightDao.findFreightByBusiness(vo);
            if(dataHt_Cus_FreightVOList.size() == 0){
                msg_list.add("第"+line+"行,未匹配到合同");
//                vo.setJs_state("-1");
                continue;
            }

            Ht_Cus_FreightVO dataHt_Cus_FreightVO = dataHt_Cus_FreightVOList.get(0);

            Ht_CusVO paramHt_CusVO = new Ht_CusVO();
            paramHt_CusVO.setSheet_no(dataHt_Cus_FreightVO.getSheet_no());
            Ht_CusVO dataHt_CusVO = ht_cusDao.selectOne(paramHt_CusVO);
            //取值 - js_tax_rate // LTJ:2019-09-24
            Js_Tax_RateVO rateVO=new Js_Tax_RateVO();
            rateVO.setTax_month(DateUtils.dateFormat(vo.getBegin_date(),"yyyy-MM"));
            List<Js_Tax_RateVO> rateList=iJs_Tax_RateService.GetJs_Tax_RateList(rateVO);
            if(null==rateList || rateList.size()<=0)
            {
                msg_list.add("第"+line+"行,未匹配到税率("+rateVO.getTax_month()+")");
                continue;
            }
            vo.setTax_rate(BigDecimal.valueOf(rateList.get(0).getTax_rate()));

            //2.计算不含税运输费
            BigDecimal not_tax_freight = dataHt_Cus_FreightVO.getFirst_price().multiply(dataHt_Cus_FreightVO.getFirst_mileage()).add(
                    dataHt_Cus_FreightVO.getTwo_price().multiply(dataHt_Cus_FreightVO.getTwo_mileage())).add(
                    dataHt_Cus_FreightVO.getThree_price().multiply(dataHt_Cus_FreightVO.getThree_mileage()));

            vo.setNot_tax_freight(not_tax_freight);
            //设置里程单价
            if (dataHt_Cus_FreightVO.getTrans_mode().indexOf("公路")!=-1) {//公路
                vo.setMil((dataHt_Cus_FreightVO.getFirst_mileage().add(dataHt_Cus_FreightVO.getTwo_mileage()).add(dataHt_Cus_FreightVO.getThree_mileage())).toString());
                vo.setPrice(dataHt_Cus_FreightVO.getFirst_price().add(dataHt_Cus_FreightVO.getTwo_price()).add(dataHt_Cus_FreightVO.getThree_price()));
            } else { //多式联运，多式联运没有单价里程

            }
            //3.设置过海费
            vo.setNot_tax_other_amount(dataHt_Cus_FreightVO.getCross_sea_amount());

            //4.计算保费
            vo.setNot_tax_premium(dataHt_Cus_FreightVO.getPremium());

            //5.设置不含税合计费用
            vo.setNot_tax_amount(vo.getNot_tax_freight().add(vo.getNot_tax_other_amount()).add(vo.getNot_tax_premium()));

            //6.设置含税合计=不含税金额*(1+税率)，税率从合同主表获取
            vo.setTax_amount(vo.getNot_tax_amount().multiply(vo.getTax_rate().add(new BigDecimal("1"))));

            vo.setContract_no(dataHt_CusVO.getContract_no());

            vo.setContract_sheet_no(dataHt_CusVO.getSheet_no());
            //vo.setTax_rate(dataHt_CusVO.getTax_rate()); //使用合同表 - 这样有缺陷,不灵活

            vo.setContract_type(dataHt_CusVO.getContract_type());

        }


        return msg_list;
    }

    @Override
    public List<String> processFreightByBusiness(List<Js_Vin_AmountVO> list) {
        List<String> msg_list = new ArrayList<String>();

        for(int i = 0,line = 1; i < list.size(); i++,line++){
            Js_Vin_AmountVO vo = list.get(i);
            vo.setContract_type(Contract_TypeEnum.FORMAL.getCode());
            //1.得到相关的合同规则
            List<Ht_Cus_FreightVO> dataHt_Cus_FreightVOList = ht_Cus_FreightDao.findFreightByBusiness(vo);
            if(dataHt_Cus_FreightVOList.size() == 0){

                msg_list.add("第"+line+"行,未匹配到正式合同");
                continue;
            }
            if(dataHt_Cus_FreightVOList.size() > 1){
                msg_list.add("第"+line+"行,匹配到多条正式合同");
                continue;
            }
            Ht_Cus_FreightVO dataHt_Cus_FreightVO = dataHt_Cus_FreightVOList.get(0);
          /*  Ht_CusVO paramHt_CusVO = new Ht_CusVO();
            paramHt_CusVO.setSheet_no(dataHt_Cus_FreightVO.getSheet_no());
            Ht_CusVO dataHt_CusVO = ht_cusDao.selectOne(paramHt_CusVO);*/
    /*        if(!Contract_TypeEnum.FORMAL.getCode().equals(dataHt_Cus_FreightVO.getContract_type())){
                msg_list.add("第"+line+"行,未匹配到正式合同");
                continue;
            }*/
            //取值 - js_tax_rate // LTJ:2019-09-24
            Js_Tax_RateVO rateVO=new Js_Tax_RateVO();
            rateVO.setTax_month(DateUtils.dateFormat(vo.getBegin_date(),"yyyy-MM"));
            List<Js_Tax_RateVO> rateList=iJs_Tax_RateService.GetJs_Tax_RateList(rateVO);
            if(null==rateList || rateList.size()<=0)
            {
                msg_list.add("第"+line+"行,未匹配到税率("+rateVO.getTax_month()+")");
                continue;
            }
            vo.setTax_rate(BigDecimal.valueOf(rateList.get(0).getTax_rate()));

            //2.计算不含税运输费
            BigDecimal not_tax_freight = dataHt_Cus_FreightVO.getFirst_price().multiply(dataHt_Cus_FreightVO.getFirst_mileage()).add(
                dataHt_Cus_FreightVO.getTwo_price().multiply(dataHt_Cus_FreightVO.getTwo_mileage())).add(
                    dataHt_Cus_FreightVO.getThree_price().multiply(dataHt_Cus_FreightVO.getThree_mileage()));

            vo.setNot_tax_freight(not_tax_freight);

            //3.设置过海费
            vo.setNot_tax_other_amount(dataHt_Cus_FreightVO.getCross_sea_amount());

            //4.计算保费
            vo.setNot_tax_premium(dataHt_Cus_FreightVO.getPremium());

            //5.设置不含税合计费用
            vo.setNot_tax_amount(vo.getNot_tax_freight().add(vo.getNot_tax_other_amount()).add(vo.getNot_tax_premium()));

            //6.设置含税合计=不含税金额*(1+税率)，税率从合同主表获取
            vo.setTax_amount(vo.getNot_tax_amount().multiply(vo.getTax_rate().add(new BigDecimal("1"))));

            vo.setContract_no(dataHt_Cus_FreightVO.getContract_no());
            vo.setContract_sheet_no(dataHt_Cus_FreightVO.getSheet_no());

            vo.setContract_type(dataHt_Cus_FreightVO.getContract_type());

        }


        return msg_list;
    }

    @Override
    public Map<String, Object> matchingContract(List<Js_Vin_AmountVO> list) {
        List<String> msg_list = new ArrayList<String>();
        List<Js_Vin_AmountVO> updateList=new ArrayList<Js_Vin_AmountVO>();
        for(int i = 0,line = 1; i < list.size(); i++,line++){
            Js_Vin_AmountVO vo = list.get(i);
            //1.得到相关的合同规则
            List<Ht_Cus_FreightVO> dataHt_Cus_FreightVOList = ht_Cus_FreightDao.findFreightByBusiness(vo);
            if(dataHt_Cus_FreightVOList.size() == 0){
                msg_list.add("第"+line+"行,未匹配到合同");
                continue;
            }
            /*2019-09-05 操作员看到“匹配到多条合同”提示，他们愿意去注释其它的合同，只保留一条*/
            if(dataHt_Cus_FreightVOList.size() > 1){
                msg_list.add("第"+line+"行,匹配到多条合同");
                continue;
            }
            Js_Vin_AmountVO info=new Js_Vin_AmountVO();
            info.setId(vo.getId());
            info.setContract_no(dataHt_Cus_FreightVOList.get(0).getContract_no());
            info.setContract_type(dataHt_Cus_FreightVOList.get(0).getContract_type());
            updateList.add(info);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("msg_list",msg_list);
        map.put("updateList",updateList);
        return map;
    }


}
