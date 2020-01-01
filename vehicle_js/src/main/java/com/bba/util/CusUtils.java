package com.bba.util;

import com.bba.xtgl.vo.SysUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Auther: Administrator
 * @Date: 2018/12/21 11:58
 * @Description:
 */
@Component
public class CusUtils {
/*
    @Autowired
    private ZdCusDao zdCusDao;
    private static CusUtils cusUtils;

    @PostConstruct
    public void init(){
        cusUtils = this;
        cusUtils.zdCusDao = this.zdCusDao;
    }

    *//**
     * 功能描述:  自动生成客户编号
     * @return:
     * @auther: laoli
     * @date: 2018/12/21 11:59
     *//*
    public static String autoGeneratorCusNo(){
        SysUserVO sysUserVO = SessionUtils.currentSession();
        String cusNo = "";
        String cusCode = cusUtils.zdCusDao.getCusCodeMax(sysUserVO.getWithin_code());
        if(StringUtils.isEmpty(cusCode)) {
            cusNo = sysUserVO.getWithin_code() + "A1";
        } else {
            int val = Integer.parseInt(cusCode) + 1;
            cusNo = sysUserVO.getWithin_code() + "A" + val;
        }
        return cusNo;
    }*/
}
