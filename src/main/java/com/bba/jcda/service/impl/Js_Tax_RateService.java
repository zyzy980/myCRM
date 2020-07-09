package com.bba.jcda.service.impl;


import com.bba.common.service.impl.BaseService;
import com.bba.jcda.dao.IJs_Tax_RateDao;
import com.bba.jcda.service.api.IJs_Tax_RateService;
import com.bba.jcda.vo.Js_Tax_RateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class Js_Tax_RateService extends BaseService implements IJs_Tax_RateService {

    @Autowired
    private IJs_Tax_RateDao iJs_Tax_RateDao;

@Override
    public List<Js_Tax_RateVO> GetJs_Tax_RateList(Js_Tax_RateVO vo)
    {
        return iJs_Tax_RateDao.GetJs_Tax_RateList(vo);
    }


}
