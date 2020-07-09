package com.bba.xtgl.service.api;

import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SaleStateV0;

import java.util.List;

/**
 * @Author bcmaidou
 * @Date 2019/4/8 10:33
 */
public interface ISaleStateService {

    /**
     * 查询列表数据
     * @Author bcmaidou
     * @Date 10:35 2019/4/8
     */
    PageVO getList(JqGridParamModel jqGridParamModel, String filters);

    /**
     * 删除数据
     * @Author bcmaidou
     * @Date 17:23 2019/4/8
     */
    ResultVO delData(String sn);

    /**
     * 批量保存数据
     * @Author bcmaidou
     * @Date 17:23 2019/4/8
     */
    ResultVO saveData(List<SaleStateV0> list);
}
