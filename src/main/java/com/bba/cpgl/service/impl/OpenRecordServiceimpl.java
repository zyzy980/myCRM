package com.bba.cpgl.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.common.Sys_sheetidUtil;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.cpgl.dao.IOpenRecordDao;
import com.bba.cpgl.service.api.IOpenRecordService;
import com.bba.cpgl.vo.OpenRecordVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.util.StringUtils;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:26
 */
@Service
@Transactional
public class OpenRecordServiceimpl extends ServiceImpl<IOpenRecordDao, OpenRecordVO> implements IOpenRecordService {

    @Resource
    private IOpenRecordDao openRecordDao;


    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<OpenRecordVO> list = openRecordDao.findListForGrid(jqGridParamModel);
        int count = openRecordDao.findListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }

    @Override
    public ResultVO saveDetail(OpenRecordVO requestOpenRecordVO, SysUserVO sysUserVO) {
        if(StringUtils.isBlank(requestOpenRecordVO.getSheet_no())){
            String sheet_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.BJ_OPEN_RECORD);
            requestOpenRecordVO.setSheet_no(sheet_no);
            openRecordDao.insert(requestOpenRecordVO);
        } else {
            OpenRecordVO paramOpenRecordVO = new OpenRecordVO();
            paramOpenRecordVO.setSheet_no(requestOpenRecordVO.getSheet_no());
            OpenRecordVO dataOpenRecordVO = openRecordDao.selectOne(paramOpenRecordVO);
            if (dataOpenRecordVO == null) {
                return ResultVO.failResult("该条数据异常,请联系管理员" + requestOpenRecordVO.getSheet_no());
            }
          /*  if (!QuestionsEnum.NORMAL.getCode().equals(dataQusetionsVO.getState())) {
                return ResultVO.failResult("只允许正常状态下的发票进行修改操作");
            }*/
            requestOpenRecordVO.setId(dataOpenRecordVO.getId());
            openRecordDao.updateById(requestOpenRecordVO);
        }
        ResultVO resultVO = ResultVO.successResult();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("sheet_no", requestOpenRecordVO.getSheet_no());
        returnMap.put("msg", "保存成功");
        resultVO.setResultDataFull(returnMap);
        return resultVO;
    }

    @Override
    public OpenRecordVO getDetail(OpenRecordVO openRecordVO) {
        OpenRecordVO paramOpenRecordVO = new OpenRecordVO();
        paramOpenRecordVO.setSheet_no(openRecordVO.getSheet_no());
        OpenRecordVO dataOpenRecordVO = openRecordDao.selectOne(paramOpenRecordVO);
        if(dataOpenRecordVO == null){
            return null;
        }
        return dataOpenRecordVO;
    }

    @Override
    public ResultVO batchDelete(List<OpenRecordVO> vos) {
        try {
            openRecordDao.batchDelete(vos);
            return ResultVO.successResult("操作成功");
        } catch (Exception e) {
            throw new RuntimeException("操作失败，请联系管理员，"+e.getMessage());
        }
    }

    @Override
    public ResultVO getmoney(List<OpenRecordVO> vos) {
        try {
            for (OpenRecordVO requestVO: vos) {
                //插入到收款表
                openRecordDao.insertInCome(requestVO.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return ResultVO.successResult("收款成功！");
    }
}
