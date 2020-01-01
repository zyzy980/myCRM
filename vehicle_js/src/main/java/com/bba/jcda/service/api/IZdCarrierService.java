package com.bba.jcda.service.api;


import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.jcda.vo.Zd_CarrierVO;
import com.bba.util.JqGridParamModel;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 * 基础档案-承运商档案
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
public interface IZdCarrierService {

    PageVO findCarrierListPageVO(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO getCarDetail(Zd_CarrierVO vo);

    ResultVO SaveCus(Zd_CarrierVO vo, HttpSession session);

    ResultVO batchDelete(List<Zd_CarrierVO> vos);

    ResultVO batchRecovery(List<Zd_CarrierVO> vos);
}
