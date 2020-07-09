package com.bba.ht.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
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
public interface Ht_CusDao extends BaseMapper<Ht_CusVO> {

    List<Ht_CusVO> getListForGrid(JqGridParamModel jqGridParamModel);

    int getListForGridCount(JqGridParamModel jqGridParamModel);
}
