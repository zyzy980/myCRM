package com.bba.dz.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.dz.vo.Js_Dz_SheetVO;
import com.bba.dz.vo.Js_Dz_Sheet_DetailVO;
import com.bba.ht.vo.Ht_CusVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
public interface Js_Dz_SheetDao extends BaseMapper<Js_Dz_SheetVO> {

    List<Js_Dz_SheetVO> getListForGrid(JqGridParamModel jqGridParamModel);

    int getListForGridCount(JqGridParamModel jqGridParamModel);

}
