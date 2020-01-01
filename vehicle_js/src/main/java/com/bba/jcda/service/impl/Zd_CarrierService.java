package com.bba.jcda.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.jcda.dao.Cr_CustomerDao;
import com.bba.jcda.dao.Zd_CarrierDao;
import com.bba.jcda.service.api.ICr_CustomerService;
import com.bba.jcda.service.api.IZd_CarrierService;
import com.bba.jcda.vo.Cr_CustomerVO;
import com.bba.jcda.vo.Zd_CarrierVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
@Service
public class Zd_CarrierService extends ServiceImpl<Zd_CarrierDao, Zd_CarrierVO> implements IZd_CarrierService {

    @Autowired
    private Zd_CarrierDao zd_carrierDao;

}
