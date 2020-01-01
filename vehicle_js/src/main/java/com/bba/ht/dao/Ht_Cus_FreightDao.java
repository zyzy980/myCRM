package com.bba.ht.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.ht.vo.Ht_CusVO;
import com.bba.ht.vo.Ht_Cus_FreightVO;
import com.bba.settlement.vo.Js_Vin_AmountVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
public interface Ht_Cus_FreightDao extends BaseMapper<Ht_Cus_FreightVO> {

    List<Ht_Cus_FreightVO> findFreightByBusiness(Js_Vin_AmountVO vo);
/*

    List<Ht_Cus_FreightVO> findFreightByBusiness(List<Js_Vin_AmountVO> list);
*/


}
