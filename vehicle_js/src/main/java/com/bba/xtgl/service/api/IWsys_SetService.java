package com.bba.xtgl.service.api;

import com.bba.xtgl.vo.Wsys_SetVO;

public interface IWsys_SetService {
	/**
	 * 查询明细
	 * @param vo
	 * @return
	 */
	public Wsys_SetVO getByWithinWhcenter(Wsys_SetVO vo);
}
