package com.bba.dz.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.common.Sys_sheetidUtil;
import com.bba.common.constant.Contract_StateEnum;
import com.bba.common.constant.Contract_TypeEnum;
import com.bba.common.constant.SysDictionaryDataConstant;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.dz.dao.Js_Dz_SheetDao;
import com.bba.dz.dao.Js_Dz_Sheet_DetailDao;
import com.bba.dz.service.api.IJs_Dz_SheetService;
import com.bba.dz.vo.Js_Dz_SheetVO;
import com.bba.ht.dao.Ht_CusDao;
import com.bba.ht.dao.Ht_Cus_FreightDao;
import com.bba.ht.dto.Ht_CusDTO;
import com.bba.ht.service.api.IHt_CusService;
import com.bba.ht.vo.Ht_CusVO;
import com.bba.ht.vo.Ht_Cus_FreightVO;
import com.bba.util.ArrayUtils;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.util.StringUtils;
import com.bba.xtgl.dao.Sys_Dictionary_DataDao;
import com.bba.xtgl.vo.SysUserVO;
import com.bba.xtgl.vo.Sys_Dictionary_DataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
@Service
public class Js_Dz_SheetService extends ServiceImpl<Js_Dz_SheetDao, Js_Dz_SheetVO> implements IJs_Dz_SheetService {

    @Autowired
    private Js_Dz_SheetDao js_dz_sheetDao;
    @Autowired
    private Js_Dz_Sheet_DetailDao js_dz_sheet_detailDao;

    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(
                customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Js_Dz_SheetVO> list = js_dz_sheetDao.getListForGrid(jqGridParamModel);
        int records = js_dz_sheetDao.getListForGridCount(jqGridParamModel);
        // 获取用户所有角色
        return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
    }

    @Override
    public ResultVO business_check(Js_Dz_SheetVO vo, SysUserVO sysUserVO) {
        return null;
    }

    @Override
    public ResultVO finance_check(Js_Dz_SheetVO vo, SysUserVO sysUserVO) {
        return null;
    }

    @Override
    public ResultVO send_mail(Js_Dz_SheetVO vo, SysUserVO sysUserVO) {
        return null;
    }

    @Override
    public ResultVO cus_check(Js_Dz_SheetVO vo, SysUserVO sysUserVO) {
        return null;
    }

    @Override
    public ResultVO uncheck(List<String> list, SysUserVO sysUserVO) {
        return null;
    }

}
