package com.bba.ht.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.ht.vo.Ht_Carrier_FreightVO;
import com.bba.ht.vo.Ht_Cus_FreightVO;
import com.bba.nosettlement.vo.Js_Non_Down_VehicleVO;
import com.bba.settlement.vo.Js_Vin_Down_AmountVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
public interface IHt_Carrier_FreightService extends IService<Ht_Carrier_FreightVO> {

    Map<String, Object> matchingContract(List<Js_Vin_Down_AmountVO> amountlist);

    Map<String, Object> matchingContract_non_down(List<Js_Non_Down_VehicleVO> amountlist);
}
