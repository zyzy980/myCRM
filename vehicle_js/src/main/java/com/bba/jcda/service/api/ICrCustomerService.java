package com.bba.jcda.service.api;


import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.jcda.vo.CrCustomerVO;
import com.bba.jcda.vo.Cr_Customer_LinkVO;
import com.bba.util.JqGridParamModel;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 * 基础档案-客户档案
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
public interface ICrCustomerService {

    PageVO findCustomerListPageVO(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO getCusDetail(CrCustomerVO vo);

    PageVO findzdCusLinkPageVO(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO SaveCus(CrCustomerVO vo, HttpSession session);

    ResultVO batchDelete(List<CrCustomerVO> customerVOs);

    void deleteDetail(List<Cr_Customer_LinkVO> vos);

    ResultVO batchRecovery(List<CrCustomerVO> customerVOs);
}
