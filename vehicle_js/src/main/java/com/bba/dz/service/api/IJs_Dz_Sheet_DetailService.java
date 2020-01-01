package com.bba.dz.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.vo.PageVO;
import com.bba.dz.vo.Js_Dz_SheetVO;
import com.bba.dz.vo.Js_Dz_Sheet_DetailVO;
import com.bba.util.JqGridParamModel;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
public interface IJs_Dz_Sheet_DetailService extends IService<Js_Dz_Sheet_DetailVO> {

    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);



}
