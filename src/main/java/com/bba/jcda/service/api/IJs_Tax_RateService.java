package com.bba.jcda.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.service.api.IBaseService;
import com.bba.jcda.vo.Js_Tax_RateVO;
import com.bba.jcda.vo.Zd_CarrierVO;

import java.util.List;

public interface IJs_Tax_RateService extends IBaseService {

    public List<Js_Tax_RateVO> GetJs_Tax_RateList(Js_Tax_RateVO vo);

}
