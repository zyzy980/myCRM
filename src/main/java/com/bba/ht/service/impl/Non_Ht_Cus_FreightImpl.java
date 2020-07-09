package com.bba.ht.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.constant.Contract_TypeEnum;
import com.bba.ht.dao.Non_Ht_CusDao;
import com.bba.ht.dao.Non_Ht_Cus_FreightDao;
import com.bba.ht.service.api.INon_Ht_Cus_FreightService;
import com.bba.ht.vo.Non_Ht_CusVO;
import com.bba.ht.vo.Non_Ht_Cus_FreightVO;
import com.bba.settlement.vo.Js_Vin_AmountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
@Service
public class Non_Ht_Cus_FreightImpl extends ServiceImpl<Non_Ht_Cus_FreightDao, Non_Ht_Cus_FreightVO> implements INon_Ht_Cus_FreightService {

    @Autowired
    private Non_Ht_Cus_FreightDao ht_Cus_FreightDao;
    @Autowired
    private Non_Ht_CusDao ht_cusDao;

    @Override
    public List<String> processFreightByTempBusiness(List<Js_Vin_AmountVO> list) {


        List<String> msg_list = new ArrayList<String>();

        for(int i = 0,line = 1; i < list.size(); i++,line++){
            Js_Vin_AmountVO vo = list.get(i);
            //1.得到相关的合同规则
            List<Non_Ht_Cus_FreightVO> dataHt_Cus_FreightVOList = ht_Cus_FreightDao.findFreightByBusiness(vo);
            if(dataHt_Cus_FreightVOList.size() == 0){
                msg_list.add("第"+line+"行,未匹配到合同");
//                vo.setJs_state("-1");
                continue;
            }

            Non_Ht_Cus_FreightVO dataHt_Cus_FreightVO = dataHt_Cus_FreightVOList.get(0);

            Non_Ht_CusVO paramHt_CusVO = new Non_Ht_CusVO();
            paramHt_CusVO.setSheet_no(dataHt_Cus_FreightVO.getSheet_no());
            Non_Ht_CusVO dataHt_CusVO = ht_cusDao.selectOne(paramHt_CusVO);


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
            vo.setTax_amount(vo.getNot_tax_amount().multiply(dataHt_CusVO.getTax_rate().add(new BigDecimal("1"))));

            vo.setContract_no(dataHt_CusVO.getContract_no());


            vo.setContract_type(dataHt_CusVO.getContract_type());

        }


        return msg_list;
    }

    @Override
    public List<String> processFreightByBusiness(List<Js_Vin_AmountVO> list) {


        List<String> msg_list = new ArrayList<String>();

        for(int i = 0,line = 1; i < list.size(); i++,line++){
            Js_Vin_AmountVO vo = list.get(i);
            //1.得到相关的合同规则
            List<Non_Ht_Cus_FreightVO> dataHt_Cus_FreightVOList = ht_Cus_FreightDao.findFreightByBusiness(vo);
            if(dataHt_Cus_FreightVOList.size() == 0){

                msg_list.add("第"+line+"行,未匹配到合同");
//                vo.setJs_state("-1");
                continue;
            }

            Non_Ht_Cus_FreightVO dataHt_Cus_FreightVO = dataHt_Cus_FreightVOList.get(0);


            Non_Ht_CusVO paramHt_CusVO = new Non_Ht_CusVO();
            paramHt_CusVO.setSheet_no(dataHt_Cus_FreightVO.getSheet_no());
            Non_Ht_CusVO dataHt_CusVO = ht_cusDao.selectOne(paramHt_CusVO);
            if(!Contract_TypeEnum.FORMAL.getCode().equals(dataHt_CusVO.getContract_type())){
                msg_list.add("第"+line+"行,未匹配到正式合同");
                continue;
            }

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
            vo.setTax_amount(vo.getNot_tax_amount().multiply(dataHt_CusVO.getTax_rate().add(new BigDecimal("1"))));

            vo.setContract_no(dataHt_CusVO.getContract_no());


            vo.setContract_type(dataHt_CusVO.getContract_type());

        }


        return msg_list;
    }



}
