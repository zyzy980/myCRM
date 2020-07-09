package com.bba.xtgl.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.vo.PageVO;
import com.bba.ht.dao.Ht_CusDao;
import com.bba.ht.service.api.IHt_CusService;
import com.bba.ht.vo.Ht_CusVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.xtgl.dao.Sys_Dictionary_DataDao;
import com.bba.xtgl.service.api.ISys_Dictionary_DataService;
import com.bba.xtgl.vo.Sys_Dictionary_DataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  数据字典-服务实现类
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
@Service
public class Sys_Dictionary_DataService extends ServiceImpl<Sys_Dictionary_DataDao, Sys_Dictionary_DataVO>
        implements ISys_Dictionary_DataService {

    @Autowired
    private Sys_Dictionary_DataDao sys_Dictionary_DataDao;

}
