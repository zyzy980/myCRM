package com.bba.xtgl.controller;

import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JSONUtils;
import com.bba.util.JqGridParamModel;
import com.bba.util.SessionUtils;
import com.bba.xtgl.service.api.SysWithinSetService;
import com.bba.xtgl.vo.SysUserVO;
import com.bba.xtgl.vo.Sys_within_setVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author bcmaidou
 * @Date 2019/3/25 10:21
 */
@RestController
@RequestMapping("/sys/withinSet")
public class SysWithinSetController {

    @Autowired
    private SysWithinSetService sysWithinSetService;

    @Log("查询内码设置单个详情")
    @RequestMapping(value = "searchWithinSetDetail",method = RequestMethod.GET)
    public ResultVO searchWithinSetDetail(){
        return sysWithinSetService.searchWithinSetDetail();
    }

    @Log("保存内码设置")
    @RequestMapping(value = "saveWithinSet",method = {RequestMethod.POST,RequestMethod.GET})
    public ResultVO saveWithinSet(@RequestBody String sys_within_setVO){

        Sys_within_setVO sys_within_setVO1 = JSONUtils.toJSONObject(Sys_within_setVO.class, sys_within_setVO);
        SysUserVO sysUserVO = SessionUtils.currentSession();
        sys_within_setVO1.setCode(sysUserVO.getWithin_code());
        return sysWithinSetService.saveWithinSet(sys_within_setVO1);
    }
}
