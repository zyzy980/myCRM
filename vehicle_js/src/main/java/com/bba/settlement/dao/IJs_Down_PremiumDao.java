package com.bba.settlement.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.jcda.vo.Zd_CarrierVO;
import com.bba.settlement.vo.Js_Vin_Down_PremiumVO;
import com.bba.util.JqGridParamModel;
import java.util.List;

public interface IJs_Down_PremiumDao extends BaseMapper<Js_Vin_Down_PremiumVO> {
    List<Js_Vin_Down_PremiumVO> findListForGrid(JqGridParamModel jqGridParamModel);
    int findListForGridCount(JqGridParamModel jqGridParamModel);

    List<Js_Vin_Down_PremiumVO> findListGroupForGrid(JqGridParamModel jqGridParamModel);
    int findListGroupForGridCount(JqGridParamModel jqGridParamModel);
    List<Js_Vin_Down_PremiumVO> getCarPremiumList();

    List<Zd_CarrierVO> findzd_carrierVOListByProperty(Zd_CarrierVO requestZd_carrierVO);

    List<Js_Vin_Down_PremiumVO> selectPremiumGroupByMonth(JqGridParamModel jqGridParamModel);

    int selectPremiumGroupByMonthCount(JqGridParamModel jqGridParamModel);

    void updateDataState(Js_Vin_Down_PremiumVO updateVin_down_premiumVO);
}
