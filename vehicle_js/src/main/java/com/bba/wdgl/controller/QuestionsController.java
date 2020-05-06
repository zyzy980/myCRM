package com.bba.wdgl.controller;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.MyUtils;
import com.bba.util.SessionUtils;
import com.bba.util.StringUtils;
import com.bba.wdgl.service.api.IQuestionsService;
import com.bba.wdgl.vo.QusetionsVO;
import com.bba.xtgl.vo.SysUserVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
/**
 * 问题收录管理
 * @Author:Suwendaidi
 * @Date: 2020/5/6
 */
@RestController
@RequestMapping("/wdgl/QuestionsController")
public class QuestionsController {

    @Autowired
    private IQuestionsService questionsService;

    @Log(value = "问题管理-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        PageVO pageVO = questionsService.getListForGrid(jqGridParamModel,customSearchFilters);
        return pageVO;
    }

    @Log(value = "问题管理-保存")
    @RequestMapping("saveDetail")
    public ResultVO saveDetail(@RequestBody QusetionsVO requestQusetionsVO) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = questionsService.saveDetail(requestQusetionsVO, sysUserVO);
        return resultVO;
    }

    @Log(value = "问题管理-注销")
    @RequestMapping("cancel")
    public ResultVO cancel(QusetionsVO qusetionsVO) {
        if(StringUtils.isBlank(qusetionsVO.getSheet_no())){
            return ResultVO.failResult("先保存后在进行操作");
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = questionsService.cancel(qusetionsVO, sysUserVO);
        return resultVO;
    }

    @Log(value = "问题管理-明细查询")
    @RequestMapping("getDetail")
    public ResultVO getDetail(QusetionsVO qusetionsVO) {
        if(StringUtils.isBlank(qusetionsVO.getSheet_no())){
            return ResultVO.failResult("单号不能为空");
        }
        QusetionsVO returnQusetionsVO = questionsService.getDetail(qusetionsVO);
        if(returnQusetionsVO == null){
            return ResultVO.failResult("该问题未查询到任何数据");
        }
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(returnQusetionsVO);
        return resultVO;
    }

    @Log(value = "问题管理-批量审核")
    @RequestMapping("check")
    public ResultVO check(@RequestBody List<QusetionsVO> list) {
        List<QusetionsVO> vos = new Gson().fromJson(new Gson().toJson(list), new TypeToken<List<QusetionsVO>>(){}.getType());
        SysUserVO sysUserVO = SessionUtils.currentSession();
        return questionsService.check(vos,sysUserVO);
    }
}
