package com.bba.xtgl.service.impl;

import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.util.SessionUtils;
import com.bba.xtgl.dao.ISaleStateDao;
import com.bba.xtgl.service.api.ISaleStateService;
import com.bba.xtgl.vo.SaleStateV0;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author bcmaidou
 * @Date 2019/4/8 10:33
 */
@Service
@Transactional
public class SaleStateServiceImpl implements ISaleStateService {

    @Autowired
    private ISaleStateDao iSaleStateDao;

    /**
     * 查询列表数据
     * @Author bcmaidou
     * @Date 10:35 2019/4/8
     */
    @Override
    public PageVO getList(JqGridParamModel jqGridParamModel, String filters) {

        String filter = JqGridSearchParamHandler.processSql( filters, jqGridParamModel);
        jqGridParamModel.setFilters(filter);

        List<SaleStateV0> list = iSaleStateDao.getList(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, list.size());
    }

    /**
     * 删除数据
     * @Author bcmaidou
     * @Date 17:23 2019/4/8
     */
    @Override
    public ResultVO delData(String sn) {

        SysUserVO sysUserVO = SessionUtils.currentSession();
        iSaleStateDao.delData(sn,sysUserVO.getWithin_code());
        return ResultVO.successResult();
    }

    /**
     * 批量保存数据
     * @Author bcmaidou
     * @Date 17:23 2019/4/8
     */
    @Override
    public ResultVO saveData(List<SaleStateV0> list) {

        SysUserVO sysUserVO = SessionUtils.currentSession();

        for (SaleStateV0 saleStateV0 : list) {
            saleStateV0.setWithinCode(sysUserVO.getWithin_code());

            if (saleStateV0.getSn().equals("0")){
                saleStateV0.setCreateBy(sysUserVO.getRealName());
                iSaleStateDao.insertData(saleStateV0);
            }else {
                saleStateV0.setUpdateBy(sysUserVO.getRealName());
                iSaleStateDao.updateData(saleStateV0);
            }
        }
        return ResultVO.successResult();
    }

}
