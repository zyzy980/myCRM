package com.bba.fpgl.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.fpgl.dao.ITr_Payment_PlanDao;
import com.bba.fpgl.service.api.ITr_Payment_PlanService;
import com.bba.fpgl.vo.Tr_Payment_PlanVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:26
 */
@Service
@Transactional
public class Tr_Payment_PlanServiceimpl extends ServiceImpl<ITr_Payment_PlanDao, Tr_Payment_PlanVO> implements ITr_Payment_PlanService {


}
