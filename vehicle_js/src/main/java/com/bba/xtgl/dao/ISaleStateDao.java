package com.bba.xtgl.dao;

import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SaleStateV0;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author bcmaidou
 * @Date 2019/4/8 10:36
 */
@Repository
public interface ISaleStateDao {

    /**
     * 查询列表数据
     * @Author bcmaidou
     * @Date 10:43 2019/4/8
     */
    List<SaleStateV0> getList(JqGridParamModel jqGridParamModel);

    /**
     * 删除数据
     * @Author bcmaidou
     * @Date 17:27 2019/4/8
     */
    void delData(@Param("sn") String sn,@Param("withinCode") String within_code);

    /**
     * 新增
     * @Author bcmaidou
     * @Date 17:28 2019/4/8
     */
    void insertData(SaleStateV0 saleStateV0);

    /**
     * 更新
     * @Author bcmaidou
     * @Date 17:28 2019/4/8
     */
    void updateData(SaleStateV0 saleStateV0);
}
