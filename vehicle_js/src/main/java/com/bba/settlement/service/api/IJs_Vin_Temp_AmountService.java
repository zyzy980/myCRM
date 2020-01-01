package com.bba.settlement.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.settlement.vo.Js_Vin_AmountVO;
import com.bba.settlement.vo.Js_Vin_Temp_AmountVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysUserVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
public interface IJs_Vin_Temp_AmountService extends IService<Js_Vin_Temp_AmountVO> {

    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

}
