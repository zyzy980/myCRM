package com.bba.xtgl.service.impl;

import javax.annotation.Resource;

import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.jcda.vo.Zd_Ywlocation_BaseVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.util.SessionUtils;
import com.bba.xtgl.vo.SysUserVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bba.xtgl.dao.ISysSheetIdDao;
import com.bba.xtgl.service.api.ISysSheetIdService;
import com.bba.xtgl.vo.SysSheetIdVO;

import java.util.List;

@Service
@Transactional
public class SysSheetIdService implements ISysSheetIdService {
    @Resource
    private ISysSheetIdDao iSysSheetIdDao;

    @Override
    public String USP_WS_SHEETID_GET(SysSheetIdVO sysSheetIdVO) {
        iSysSheetIdDao.USP_GET_SHEETID(sysSheetIdVO);
        return sysSheetIdVO.getP_output();
    }


    /**
     * 查询列表数据
     *
     * @Author bcmaidou
     * @Date 17:00 2019/3/28
     */
    @Override
    public PageVO searchList(String filters, JqGridParamModel jqGridParamModel) {

        SysUserVO sysUserVO = SessionUtils.currentSession();
        jqGridParamModel.put("within_code", "eq", sysUserVO.getWithin_code());
        String filter = JqGridSearchParamHandler.processSql(filters, jqGridParamModel);

        jqGridParamModel.setFilters(filter);
        List<SysSheetIdVO> list = iSysSheetIdDao.searchList(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, list.size());
    }

    /**
     * 查询单号详情
     *
     * @Author bcmaidou
     * @Date 18:57 2019/3/28
     */
    @Override
    public ResultVO searchDetail(String sn) {
        SysSheetIdVO sysSheetIdVO = iSysSheetIdDao.searchDetail(sn);
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(sysSheetIdVO);
        return resultVO;
    }

    /**
     * 新增或修改单据号
     *
     * @Author bcmaidou
     * @Date 9:32 2019/3/29
     */
    @Override
    public ResultVO saveDetail(SysSheetIdVO sysSheetIdVO) {

        //判断sn是否为空
        if (StringUtils.isEmpty(sysSheetIdVO.getSn())) {
            //为空 新增
            SysUserVO sysUserVO = SessionUtils.currentSession();
            sysSheetIdVO.setP_yw_location(sysUserVO.getWhcenter());
            sysSheetIdVO.setP_within_code(sysUserVO.getWithin_code());
            sysSheetIdVO.setP_within_code("TMS");
            iSysSheetIdDao.insertDetail(sysSheetIdVO);

            Zd_Ywlocation_BaseVO zd_ywlocation_baseVO = new Zd_Ywlocation_BaseVO();
            zd_ywlocation_baseVO.setWithin_code(sysUserVO.getWithin_code());
            zd_ywlocation_baseVO.setKind("SYS_SHEETID");
            zd_ywlocation_baseVO.setBase_sn(sysSheetIdVO.getSn());
            zd_ywlocation_baseVO.setLocation_code(sysUserVO.getWhcenter());
        } else {
            //不为空 修改
            iSysSheetIdDao.updateDetail(sysSheetIdVO);
        }

        return ResultVO.successResult();
    }

    /**
     * 删除单据号
     *
     * @Author bcmaidou
     * @Date 9:32 2019/3/29
     */
    @Override
    public ResultVO delDetail(String sn) {
        iSysSheetIdDao.delDetail(sn);

        Zd_Ywlocation_BaseVO zd_ywlocation_baseVO = new Zd_Ywlocation_BaseVO();
        zd_ywlocation_baseVO.setBase_sn(sn);
        return ResultVO.successResult();
    }

}
