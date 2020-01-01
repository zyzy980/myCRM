package com.bba.jcda.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.jcda.dao.ITax_rateDao;
import com.bba.jcda.service.api.ITax_rateService;
import com.bba.jcda.vo.Tax_rateVO;
import com.bba.util.*;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:26
 */
@Service
@Transactional
public class Tax_rateServiceimpl extends ServiceImpl<ITax_rateDao, Tax_rateVO> implements ITax_rateService {

    @Resource
    private ITax_rateDao tax_rateDao;


    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Tax_rateVO> list = tax_rateDao.findListForGrid(jqGridParamModel);
        int count = tax_rateDao.findListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }

    @Override
    public ResultVO saveDetail(Tax_rateVO requestTax_rateVO, SysUserVO sysUserVO) {
        if(requestTax_rateVO.getTax_month()==null){
            return ResultVO.failResult("日期不能为空");
        } else if (requestTax_rateVO.getTax_rate()==null) {
            return ResultVO.failResult("税率不能为空");
        }
        if(requestTax_rateVO.getId()==0){
            Tax_rateVO paramTax_rateVO = new Tax_rateVO();
            paramTax_rateVO.setTax_month(requestTax_rateVO.getTax_month());
            Tax_rateVO dataTax_rateVO = tax_rateDao.selectOne(paramTax_rateVO);
            if(dataTax_rateVO != null){
                return ResultVO.failResult("日期已存在,请勿重复录入"+requestTax_rateVO.getTax_month());
            }
            requestTax_rateVO.setCreate_by(sysUserVO.getRealName());
            tax_rateDao.insert(requestTax_rateVO);
        } else {
            Tax_rateVO param2Tax_rateVO = new Tax_rateVO();
            param2Tax_rateVO.setId(requestTax_rateVO.getId());
            Tax_rateVO data2Tax_rateVO = tax_rateDao.selectOne(param2Tax_rateVO);
            if (data2Tax_rateVO==null) {
                return ResultVO.failResult("该条数据不存在，请核实数据");
            }
            int res = data2Tax_rateVO.getTax_month().compareTo(requestTax_rateVO.getTax_month());
            if (res!=0) {
                param2Tax_rateVO = new Tax_rateVO();
                param2Tax_rateVO.setTax_month(requestTax_rateVO.getTax_month());
                data2Tax_rateVO = tax_rateDao.selectOne(param2Tax_rateVO);
                if (data2Tax_rateVO != null) {
                    return ResultVO.failResult("日期已存在,请勿重复录入"+requestTax_rateVO.getTax_month());
                }
            }
            requestTax_rateVO.setCreate_by(null);
            requestTax_rateVO.setCreate_date(null);
            tax_rateDao.updateById(requestTax_rateVO);
        }
        return ResultVO.successResult("保存成功");
    }

    @Override
    public ResultVO cancel(Tax_rateVO vo, SysUserVO sysUserVO) {
        return null;
    }


    @Override
    public Tax_rateVO getDetail(Tax_rateVO vo) {
        Tax_rateVO paramTax_rateVO = new Tax_rateVO();
        paramTax_rateVO.setId(vo.getId());
        Tax_rateVO dataTax_rateVOO = tax_rateDao.selectOne(paramTax_rateVO);
        if(dataTax_rateVOO == null){
            return null;
        }
        return dataTax_rateVOO;
    }

    @Override
    public ResultVO remove(List<Tax_rateVO> vos) {
        List<String> idList = new ArrayList<String>();
        for (Tax_rateVO vo:vos) {
            idList.add(vo.getId().toString());
        }
        tax_rateDao.deleteBatchIds(idList);
        return ResultVO.successResult("删除成功");
    }
}
