package com.bba.cpgl.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.common.Sys_sheetidUtil;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.cpgl.dao.IBjCostDao;
import com.bba.cpgl.dao.IPurchaseManagerDao;
import com.bba.cpgl.service.api.IBjCostService;
import com.bba.cpgl.service.api.IPurchaseManagerService;
import com.bba.cpgl.vo.BjCostVO;
import com.bba.cpgl.vo.ReceiveRecordVO;
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
public class BjCostServiceimpl extends ServiceImpl<IBjCostDao, BjCostVO> implements IBjCostService {

    @Resource
    private IBjCostDao iBjCostDao;


    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<BjCostVO> list = iBjCostDao.findListForGrid(jqGridParamModel);
        int count = iBjCostDao.findListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }

    @Override
    public ResultVO saveDetail(BjCostVO requestBjCostVO, SysUserVO sysUserVO) {
        if(StringUtils.isBlank(requestBjCostVO.getSheet_no())){
            String sheet_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.BJ_COST);
            requestBjCostVO.setSheet_no(sheet_no);
            iBjCostDao.insert(requestBjCostVO);
        } else {
            BjCostVO paramBjCostVO = new BjCostVO();
            paramBjCostVO.setSheet_no(requestBjCostVO.getSheet_no());
            BjCostVO dataBjCostVO = iBjCostDao.selectOne(paramBjCostVO);
            if (dataBjCostVO == null) {
                return ResultVO.failResult("该条数据异常,请联系管理员" + requestBjCostVO.getSheet_no());
            }
          /*  if (!QuestionsEnum.NORMAL.getCode().equals(dataQusetionsVO.getState())) {
                return ResultVO.failResult("只允许正常状态下的发票进行修改操作");
            }*/
            requestBjCostVO.setId(dataBjCostVO.getId());
            iBjCostDao.updateById(requestBjCostVO);
        }
        ResultVO resultVO = ResultVO.successResult();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("sheet_no", requestBjCostVO.getSheet_no());
        returnMap.put("msg", "保存成功");
        resultVO.setResultDataFull(returnMap);
        return resultVO;
    }


    @Override
    public BjCostVO getDetail(BjCostVO bjCostVO) {
        BjCostVO paramBjCostVO = new BjCostVO();
        paramBjCostVO.setId(bjCostVO.getId());
        BjCostVO dataBjCostVO = iBjCostDao.selectOne(paramBjCostVO);
        if(dataBjCostVO == null){
            return null;
        }
        return dataBjCostVO;
    }

    @Override
    public ResultVO batchDelete(List<BjCostVO> vos) {
        try {
            iBjCostDao.batchDelete(vos);
            return ResultVO.successResult("操作成功");
        } catch (Exception e) {
            throw new RuntimeException("操作失败，请联系管理员，"+e.getMessage());
        }
    }
}
