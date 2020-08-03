package com.bba.cpgl.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.cpgl.vo.ReceiveRecordVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysUserVO;

import java.util.List;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:24
 */
public interface IPurchaseManagerService extends IService<ReceiveRecordVO> {
    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO saveDetail(ReceiveRecordVO vo, SysUserVO sysUserVO);

    ReceiveRecordVO getDetail(ReceiveRecordVO vo);

    ResultVO batchDelete(List<ReceiveRecordVO> vos);
}
