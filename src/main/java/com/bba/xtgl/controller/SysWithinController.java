package com.bba.xtgl.controller;

import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.MyUtils;
import com.bba.util.SessionUtils;
import com.bba.xtgl.service.api.ISysWithinService;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SysWithinController
 * @Discription TODO
 * @Author lao li
 * @Date 2019-03-25 11:09
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/within")
public class SysWithinController {

    @Autowired
    private ISysWithinService sysWithinService;

    @PostMapping(value = "/findList")
    @ResponseBody
    public PageVO findList(JqGridParamModel jqGridParamModel, String customSearchFilters){
        customSearchFilters = MyUtils.paramDecode(customSearchFilters); // 防止中文乱码
        SysUserVO sysUserVO = SessionUtils.currentSession();
        jqGridParamModel.put("code", "eq", sysUserVO.getWithin_code());
        PageVO vo= sysWithinService.findList(jqGridParamModel,customSearchFilters);
        return vo;
    }

    @PostMapping(value = "/delete")
    @ResponseBody
    public ResultVO delete(String withinCode) {
        return sysWithinService.delete(withinCode);
    }

    @PostMapping(value = "/query")
    @ResponseBody
    public ResultVO query(String sn) {
        return sysWithinService.query(sn);
    }

    @PostMapping(value = "/save")
    @ResponseBody
    public ResultVO save(String jsonData){
        ResultVO resultVO = sysWithinService.save(jsonData);
        return resultVO;
    }

}
