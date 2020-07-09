package com.bba.ht.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.ht.vo.Non_Ht_Cus_FreightVO;
import com.bba.settlement.vo.Js_Vin_AmountVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
public interface INon_Ht_Cus_FreightService extends IService<Non_Ht_Cus_FreightVO> {



    /**
     * 根据业务数据获取合同规则
     * @param list
     * @return
     */
    List<String> processFreightByTempBusiness(List<Js_Vin_AmountVO> list);

    /**
     * 根据业务数据获取合同规则
     * @param list
     * @return
     */
    List<String> processFreightByBusiness(List<Js_Vin_AmountVO> list);

}
