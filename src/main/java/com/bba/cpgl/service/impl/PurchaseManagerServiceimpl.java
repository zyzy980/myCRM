package com.bba.cpgl.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.common.Sys_sheetidUtil;
import com.bba.common.constant.QuestionsEnum;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.cpgl.dao.ICpDao;
import com.bba.cpgl.dao.IPurchaseManagerDao;
import com.bba.cpgl.service.api.ICpService;
import com.bba.cpgl.service.api.IPurchaseManagerService;
import com.bba.cpgl.vo.CpVO;
import com.bba.cpgl.vo.ReceiveRecordVO;
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
public class PurchaseManagerServiceimpl extends ServiceImpl<IPurchaseManagerDao, ReceiveRecordVO> implements IPurchaseManagerService {

    @Resource
    private IPurchaseManagerDao purchaseManagerDao;


    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<ReceiveRecordVO> list = purchaseManagerDao.findListForGrid(jqGridParamModel);
        int count = purchaseManagerDao.findListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }

    @Override
    public ResultVO saveDetail(ReceiveRecordVO requestReceiveRecordVO, SysUserVO sysUserVO) {
        if(StringUtils.isBlank(requestReceiveRecordVO.getSheet_no())){
            String sheet_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.QUESTIONS);
            requestReceiveRecordVO.setSheet_no(sheet_no);
            purchaseManagerDao.insert(requestReceiveRecordVO);
        } else {
            ReceiveRecordVO paramReceiveRecordVO = new ReceiveRecordVO();
            paramReceiveRecordVO.setSheet_no(requestReceiveRecordVO.getSheet_no());
            ReceiveRecordVO dataReceiveRecordVO = purchaseManagerDao.selectOne(paramReceiveRecordVO);
            if (dataReceiveRecordVO == null) {
                return ResultVO.failResult("该条数据异常,请联系管理员" + requestReceiveRecordVO.getSheet_no());
            }
          /*  if (!QuestionsEnum.NORMAL.getCode().equals(dataQusetionsVO.getState())) {
                return ResultVO.failResult("只允许正常状态下的发票进行修改操作");
            }*/
            requestReceiveRecordVO.setId(dataReceiveRecordVO.getId());
            purchaseManagerDao.updateById(requestReceiveRecordVO);
        }
        ResultVO resultVO = ResultVO.successResult();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("sheet_no", requestReceiveRecordVO.getSheet_no());
        returnMap.put("msg", "保存成功");
        resultVO.setResultDataFull(returnMap);
        return resultVO;
    }


    @Override
    public ReceiveRecordVO getDetail(ReceiveRecordVO receiveRecordVO) {
        ReceiveRecordVO paramReceiveRecordVO = new ReceiveRecordVO();
        paramReceiveRecordVO.setSheet_no(receiveRecordVO.getSheet_no());
        ReceiveRecordVO dataReceiveRecordVO = purchaseManagerDao.selectOne(paramReceiveRecordVO);
        if(dataReceiveRecordVO == null){
            return null;
        }
        return dataReceiveRecordVO;
    }

    @Override
    public ResultVO batchDelete(List<ReceiveRecordVO> vos) {
        try {
            purchaseManagerDao.batchDelete(vos);
            return ResultVO.successResult("操作成功");
        } catch (Exception e) {
            throw new RuntimeException("操作失败，请联系管理员，"+e.getMessage());
        }
    }
}
