package com.bba.ht.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.ht.vo.Ht_Carrier_FreightVO;
import com.bba.ht.vo.Non_Ht_Carrier_FreightVO;
import com.bba.nosettlement.vo.Js_Non_Down_VehicleVO;
import com.bba.settlement.vo.Js_Vin_Down_AmountVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
public interface Ht_Carrier_FreightDao extends BaseMapper<Ht_Carrier_FreightVO> {

    List<Ht_Carrier_FreightVO> findFreightByBusiness(Js_Vin_Down_AmountVO vo);

    List<Non_Ht_Carrier_FreightVO> findFreightByBusiness_non_down(Js_Non_Down_VehicleVO vo);
}
