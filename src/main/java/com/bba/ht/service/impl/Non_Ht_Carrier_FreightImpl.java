package com.bba.ht.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.ht.dao.Non_Ht_Carrier_FreightDao;
import com.bba.ht.service.api.INon_Ht_Carrier_FreightService;
import com.bba.ht.vo.Non_Ht_Carrier_FreightVO;
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
public class Non_Ht_Carrier_FreightImpl extends ServiceImpl<Non_Ht_Carrier_FreightDao, Non_Ht_Carrier_FreightVO> implements INon_Ht_Carrier_FreightService {

    @Autowired
    private Non_Ht_Carrier_FreightDao ht_Cus_FreightDao;

}
