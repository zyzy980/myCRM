package com.bba.bbgl.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.bbgl.dao.Js_Analysis_Summary_DetailDao;
import com.bba.bbgl.service.api.IJs_Analysis_Summary_DetailService;
import com.bba.bbgl.vo.Js_Analysis_Summary_DetailVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:26
 */
@Service
@Transactional
public class Js_Analysis_Summary_DetailServiceimpl extends ServiceImpl<Js_Analysis_Summary_DetailDao, Js_Analysis_Summary_DetailVO> implements IJs_Analysis_Summary_DetailService {

    @Resource
    private Js_Analysis_Summary_DetailDao js_Analysis_Summary_DetailDao;

}
