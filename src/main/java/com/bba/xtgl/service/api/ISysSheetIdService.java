package com.bba.xtgl.service.api;
 

import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysSheetIdVO;

public interface ISysSheetIdService {


	public String USP_WS_SHEETID_GET(SysSheetIdVO sysSheetIdVO);

	/**
	 * 查询列表数据
	 * @Author bcmaidou
	 * @Date 16:59 2019/3/28
	 */
    PageVO searchList(String filters, JqGridParamModel jqGridParamModel);

    /**
     * 查询单个详情
     * @Author bcmaidou
     * @Date 18:56 2019/3/28
     */
    ResultVO searchDetail(String sn);

    /**
     * 新增或修改单据号
     * @Author bcmaidou
     * @Date 9:32 2019/3/29
     */
	ResultVO saveDetail(SysSheetIdVO sysSheetIdVO);

	/**
	 * 删除单据号
	 * @Author bcmaidou
	 * @Date 9:36 2019/3/29
	 */
	ResultVO delDetail(String snList);
}
