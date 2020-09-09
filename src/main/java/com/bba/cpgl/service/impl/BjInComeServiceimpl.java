package com.bba.cpgl.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.common.Sys_sheetidUtil;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.cpgl.dao.IBjCostDao;
import com.bba.cpgl.dao.IBjInComeDao;
import com.bba.cpgl.dto.BjKanbanDTO;
import com.bba.cpgl.service.api.IBjCostService;
import com.bba.cpgl.service.api.IBjInComeService;
import com.bba.cpgl.vo.BjCostVO;
import com.bba.cpgl.vo.BjInComeVO;
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
public class BjInComeServiceimpl extends ServiceImpl<IBjInComeDao, BjInComeVO> implements IBjInComeService {

    @Resource
    private IBjInComeDao iBjInComeDao;


    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<BjInComeVO> list = iBjInComeDao.findListForGrid(jqGridParamModel);
        int count = iBjInComeDao.findListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }

    @Override
    public ResultVO saveDetail(BjInComeVO requestBjInComeVO, SysUserVO sysUserVO) {
        if(StringUtils.isBlank(requestBjInComeVO.getSheet_no())){
            String sheet_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.BJ_INCOME);
            requestBjInComeVO.setSheet_no(sheet_no);
            iBjInComeDao.insert(requestBjInComeVO);
        } else {
            BjInComeVO paramBjInComeVO = new BjInComeVO();
            paramBjInComeVO.setSheet_no(requestBjInComeVO.getSheet_no());
            BjInComeVO dataBjInComeVO = iBjInComeDao.selectOne(paramBjInComeVO);
            if (dataBjInComeVO == null) {
                return ResultVO.failResult("该条数据异常,请联系管理员" + requestBjInComeVO.getSheet_no());
            }
          /*  if (!QuestionsEnum.NORMAL.getCode().equals(dataQusetionsVO.getState())) {
                return ResultVO.failResult("只允许正常状态下的发票进行修改操作");
            }*/
            requestBjInComeVO.setId(dataBjInComeVO.getId());
            iBjInComeDao.updateById(requestBjInComeVO);
        }
        ResultVO resultVO = ResultVO.successResult();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("sheet_no", requestBjInComeVO.getSheet_no());
        returnMap.put("msg", "保存成功");
        resultVO.setResultDataFull(returnMap);
        return resultVO;
    }


    @Override
    public BjInComeVO getDetail(BjInComeVO bjInComeVO) {
        BjInComeVO paramBjInComeVO = new BjInComeVO();
        paramBjInComeVO.setId(bjInComeVO.getId());
        BjInComeVO dataBjInComeVO = iBjInComeDao.selectOne(paramBjInComeVO);
        if(dataBjInComeVO == null){
            return null;
        }
        return dataBjInComeVO;
    }

    @Override
    public ResultVO batchDelete(List<BjInComeVO> vos) {
        try {
            iBjInComeDao.batchDelete(vos);
            return ResultVO.successResult("操作成功");
        } catch (Exception e) {
            throw new RuntimeException("操作失败，请联系管理员，"+e.getMessage());
        }
    }

    @Override
    public ResultVO toCompany(List<BjInComeVO> vos) {
        try {
            iBjInComeDao.updateState(vos);
            return ResultVO.successResult("操作成功");
        } catch (Exception e) {
            throw new RuntimeException("操作失败，请联系管理员，"+e.getMessage());
        }
    }

    @Override
    public ResultVO getKanbanreportInfo(OpenRecordVO vo) {
        //货物分类统计
        List<OpenRecordVO> goodsTypeList = iBjInComeDao.getGoodsTypeReport();
        //价值占比
        List<OpenRecordVO> valueList = iBjInComeDao.getValueReport();
        //销售方式金额占比
        List<OpenRecordVO> sellModeList = iBjInComeDao.getSellModeReport();
        //销售额趋势图
        List<OpenRecordVO> salesTrendList = iBjInComeDao.getSalesTrendReport();
        //销售排行榜
        List<OpenRecordVO> salesRankingList = iBjInComeDao.getSalesRankingReport();
        //滚动
        List<OpenRecordVO> rollList = iBjInComeDao.getRollReport();

        BjKanbanDTO kanbanDTO = new BjKanbanDTO();
        kanbanDTO.setGoodsTypeList(goodsTypeList);
        kanbanDTO.setValueList(valueList);
        kanbanDTO.setSellModeList(sellModeList);
        kanbanDTO.setSalesTrendList(salesTrendList);
        kanbanDTO.setSalesRankingList(salesRankingList);
        kanbanDTO.setRollList(rollList);
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(kanbanDTO);
        return resultVO;
    }
}
