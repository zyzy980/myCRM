package com.bba.jcda.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.vo.PageVO;
import com.bba.ht.dao.Ht_CusDao;
import com.bba.ht.service.api.IHt_CusService;
import com.bba.ht.vo.Ht_CusVO;
import com.bba.jcda.dao.Cr_CustomerDao;
import com.bba.jcda.service.api.ICr_CustomerService;
import com.bba.jcda.vo.Cr_CustomerVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
@Service
public class Cr_CustomerService extends ServiceImpl<Cr_CustomerDao, Cr_CustomerVO> implements ICr_CustomerService {

    @Autowired
    private Cr_CustomerDao cr_CustomerDao;

}
