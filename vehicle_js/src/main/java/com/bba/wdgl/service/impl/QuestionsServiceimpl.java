package com.bba.wdgl.service.impl;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.common.Sys_sheetidUtil;
import com.bba.common.constant.*;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.util.StringUtils;
import com.bba.wdgl.dao.IQuestionsDao;
import com.bba.wdgl.service.api.IQuestionsService;
import com.bba.wdgl.vo.QusetionsVO;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:26
 */
@Service
@Transactional
public class QuestionsServiceimpl extends ServiceImpl<IQuestionsDao, QusetionsVO> implements IQuestionsService {

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
        if(requestQusetionsVO.getId()==0){
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
            requestQusetionsVO.setCreate_by(null);
            questionsDao.updateById(requestQusetionsVO);
        }
        return ResultVO.successResult("保存成功");
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
        paramQusetionsVO.setSheet_no(paramQusetionsVO.getSheet_no());
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
                //1 验证
                QusetionsVO paramQusetionsVO = new QusetionsVO();
                paramQusetionsVO.setSheet_no(requestQusetionsVO.getSheet_no());
                QusetionsVO dataQusetionsVO = questionsDao.selectOne(paramQusetionsVO);
                if (dataQusetionsVO==null) {
                    throw new RuntimeException("该问题数据存在异常，单号："+requestQusetionsVO.getSheet_no());
                } else if (StringUtils.notEquals(InvoiceEnum.NORMAL.getCode(),dataQusetionsVO.getState())) {
                    throw new RuntimeException("非‘未使用’状态无法进行此操作，单号："+requestQusetionsVO.getSheet_no());
                }
                //2 修改状态
                QusetionsVO updateQusetionsVO = new QusetionsVO();
                updateQusetionsVO.setState(QuestionsEnum.USED.getCode());
                updateQusetionsVO.setUsed_by(sysUserVO.getRealName());
                updateQusetionsVO.setUsed_date(new Date());
                EntityWrapper questionsEntityWrapper = new EntityWrapper();
                questionsEntityWrapper.setEntity(paramQusetionsVO);
                questionsDao.update(updateQusetionsVO,questionsEntityWrapper);
            }
        } catch (Exception e) {
            throw new RuntimeException("操作失败，请联系管理员，"+e.getMessage());
        }
        return ResultVO.successResult("操作成功");
    }

}
