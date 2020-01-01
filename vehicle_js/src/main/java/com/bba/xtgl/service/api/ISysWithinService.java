package com.bba.xtgl.service.api;


import javax.servlet.http.HttpServletRequest;

import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysWithinVO;
import com.bba.xtgl.vo.Wsys_SetVO;

public interface ISysWithinService {
	public SysWithinVO getDetailBySessionWithinCode(String withinCode);
	
	/**
	 * 修改明细
	 * @param sysWithinCodeVO
	 * @param request
	 * @return
	 */
	public ResultVO updateWithin(SysWithinVO sysWithinVO, Wsys_SetVO wsys_SetVO, HttpServletRequest request);


	/**
	 * @Description 查询公司明细
	 * @param
	 * @Author lao li
	 * @Date

	/**
	 * 查询grid清单数据
	 * @param sysWithinCodeVO

    /*	*//**
	 * 查询grid清单数据
	 * @param sysWithinCodeVO
	 * @return
	*/
	PageVO findList(JqGridParamModel jqGridParamModel,String customSearchFilters);

	/**
	 * @Description 删除公司数据
	 * @param withinCode 内码
	 * @Author lao li
	 * @Date
	 * @return
	*/
	ResultVO delete(String withinCode);

	/**
	 * @Description 修改公司明细
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	ResultVO save(String jsonData);

	ResultVO query(String sn);



}
