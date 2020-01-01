package com.bba.jcda.dao;

import com.bba.ht.vo.Ht_CusVO;
import com.bba.jcda.vo.CrCustomerVO;
import com.bba.jcda.vo.Cr_Customer_LinkVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

public interface ICr_CustomerDao extends IJcdaBaseDao<CrCustomerVO> {

    List<Cr_Customer_LinkVO> findCusLinkListForGrid(JqGridParamModel jqGridParamModel);
    int findCusLinkListForGridCount(JqGridParamModel jqGridParamModel);
    void batchInsertLinks(List<Cr_Customer_LinkVO> insert);
    void updateLink(Cr_Customer_LinkVO vo);
    void deleteLinks(List<Cr_Customer_LinkVO> vos);
    List<Ht_CusVO> selectHtList(CrCustomerVO removeVO);
}
