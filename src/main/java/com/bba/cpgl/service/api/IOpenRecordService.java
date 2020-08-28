package com.bba.cpgl.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.cpgl.vo.OpenRecordVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysUserVO;

import java.util.List;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:24
 */
public interface IOpenRecordService extends IService<OpenRecordVO> {
    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO saveDetail(OpenRecordVO vo, SysUserVO sysUserVO);

    OpenRecordVO getDetail(OpenRecordVO vo);

    ResultVO batchDelete(List<OpenRecordVO> vos);

    ResultVO getmoney(List<OpenRecordVO> vos);
}
