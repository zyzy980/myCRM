package com.bba.cpgl.service.impl;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.common.Sys_sheetidUtil;
import com.bba.common.constant.*;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.cpgl.dao.ICpDao;
import com.bba.cpgl.service.api.ICpService;
import com.bba.cpgl.vo.CpVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.util.StringUtils;
import com.bba.wdgl.dao.IQuestionsDao;
import com.bba.wdgl.vo.QusetionsVO;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:26
 */
@Service
@Transactional
public class CpServiceimpl extends ServiceImpl<ICpDao, CpVO> implements ICpService {

    @Resource
    private IQuestionsDao questionsDao;


    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<QusetionsVO> list = questionsDao.findListForGrid(jqGridParamModel);
        int count = questionsDao.findListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }

    @Override
    public ResultVO saveDetail(QusetionsVO requestQusetionsVO, SysUserVO sysUserVO) {
        if(StringUtils.isBlank(requestQusetionsVO.getSheet_no())){
            String sheet_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.QUESTIONS);
            requestQusetionsVO.setSheet_no(sheet_no);
            requestQusetionsVO.setCreate_by(sysUserVO.getRealName());
            requestQusetionsVO.setState(QuestionsEnum.NORMAL.getCode());
            questionsDao.insert(requestQusetionsVO);
        } else {
            QusetionsVO paramQusetionsVO = new QusetionsVO();
            paramQusetionsVO.setSheet_no(requestQusetionsVO.getSheet_no());
            QusetionsVO dataQusetionsVO = questionsDao.selectOne(paramQusetionsVO);
            if (dataQusetionsVO == null) {
                return ResultVO.failResult("该条数据异常,请联系管理员" + requestQusetionsVO.getSheet_no());
            }
          /*  if (!QuestionsEnum.NORMAL.getCode().equals(dataQusetionsVO.getState())) {
                return ResultVO.failResult("只允许正常状态下的发票进行修改操作");
            }*/
            requestQusetionsVO.setId(dataQusetionsVO.getId());
            requestQusetionsVO.setState(null);
            requestQusetionsVO.setCreate_by(null);
            questionsDao.updateById(requestQusetionsVO);
        }
        ResultVO resultVO = ResultVO.successResult();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("sheet_no", requestQusetionsVO.getSheet_no());
        returnMap.put("msg", "保存成功");
        resultVO.setResultDataFull(returnMap);
        return resultVO;
    }

    @Override
    public ResultVO cancel(QusetionsVO qusetionsVO, SysUserVO sysUserVO) {
        if(StringUtils.isBlank(qusetionsVO.getSheet_no())){
            return ResultVO.failResult("单号不能为空");
        }
        QusetionsVO paramQusetionsVO = new QusetionsVO();
        paramQusetionsVO.setSheet_no(qusetionsVO.getSheet_no());
        QusetionsVO dataQusetionsVO = questionsDao.selectOne(paramQusetionsVO);
        if(dataQusetionsVO == null){
            return ResultVO.failResult("问题不存在");
        }else if(!QuestionsEnum.NORMAL.getCode().equals(dataQusetionsVO.getState())){
            return ResultVO.failResult("只允许‘未使用’状态下的发票进行注销操作");
        }
        paramQusetionsVO.setState(QuestionsEnum.CANCEL.getCode());
        paramQusetionsVO.setId(dataQusetionsVO.getId());
        questionsDao.updateById(paramQusetionsVO);
        return ResultVO.successResult("注销成功");
    }


    @Override
    public QusetionsVO getDetail(QusetionsVO qusetionsVO) {
        QusetionsVO paramQusetionsVO = new QusetionsVO();
        paramQusetionsVO.setSheet_no(qusetionsVO.getSheet_no());
        QusetionsVO dataQusetionsVO = questionsDao.selectOne(paramQusetionsVO);
        if(dataQusetionsVO == null){
            return null;
        }
        return dataQusetionsVO;
    }


    @Override
    public ResultVO check(List<QusetionsVO> vos, SysUserVO sysUserVO) {
        /**设置已使用问题状态
         * 1、修改问题状态
         * */
        try {
            for (QusetionsVO requestQusetionsVO:vos) {
                requestQusetionsVO.setState(QuestionsEnum.USED.getCode());
                requestQusetionsVO.setUsed_by(sysUserVO.getRealName());
                requestQusetionsVO.setUsed_date(new Date());
            }
            questionsDao.updateQuestionState(vos);
        } catch (Exception e) {
            throw new RuntimeException("操作失败，请联系管理员，"+e.getMessage());
        }
        return ResultVO.successResult("操作成功");
    }

    @Override
    public ResultVO reset(List<QusetionsVO> vos, SysUserVO sysUserVO) {
        try {
            questionsDao.updateQuestionState(vos);
            return ResultVO.successResult("操作成功");
        } catch (Exception e) {
            throw new RuntimeException("操作失败，请联系管理员，"+e.getMessage());
        }
    }

    @Override
    public ResultVO batchDelete(List<QusetionsVO> vos) {
        try {
            questionsDao.batchDelete(vos);
            return ResultVO.successResult("操作成功");
        } catch (Exception e) {
            throw new RuntimeException("操作失败，请联系管理员，"+e.getMessage());
        }
    }
}
