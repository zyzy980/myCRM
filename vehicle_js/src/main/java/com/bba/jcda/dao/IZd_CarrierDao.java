package com.bba.jcda.dao;

import com.bba.ht.vo.Ht_CarrierVO;
import com.bba.jcda.vo.Zd_CarrierVO;

import java.util.List;

public interface IZd_CarrierDao extends IJcdaBaseDao<Zd_CarrierVO> {

    List<Ht_CarrierVO> selectHtList(Zd_CarrierVO removeVo);
}
