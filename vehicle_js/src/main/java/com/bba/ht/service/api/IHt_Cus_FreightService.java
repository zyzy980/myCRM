package com.bba.ht.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.vo.PageVO;
import com.bba.ht.vo.Ht_CusVO;
import com.bba.ht.vo.Ht_Cus_FreightVO;
import com.bba.settlement.vo.Js_Vin_AmountVO;
import com.bba.util.JqGridParamModel;

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
public interface IHt_Cus_FreightService extends IService<Ht_Cus_FreightVO> {



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

    Map<String,Object> matchingContract(List<Js_Vin_AmountVO> list);
}
