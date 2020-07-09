package com.bba.jcda.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.jcda.dto.Ht_FreightDTO;
import com.bba.jcda.vo.Js_Trans_Mode_ChangeVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysUserVO;

import java.util.List;

public interface IJs_Trans_Mode_ChangeService extends IService<Js_Trans_Mode_ChangeVO> {
    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO saveDetail(Js_Trans_Mode_ChangeVO requestJs_trans_mode_changeVO, SysUserVO sysUserVO);

    Js_Trans_Mode_ChangeVO getDetail(Js_Trans_Mode_ChangeVO js_trans_mode_changeVO);

    ResultVO batchDelete(List<Js_Trans_Mode_ChangeVO> modeChangeLists);

    Ht_FreightDTO exchangeTransMode(Ht_FreightDTO dto,String type);
}
