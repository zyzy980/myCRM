package com.bba.xtgl.service.api;

import com.bba.common.vo.PageVO;
import com.bba.xtgl.vo.ImageVO;


public interface IPngService {
	/**
	 * 查询ico
	 * @param imageVO
	 * @return
	 */
	public PageVO findImage(ImageVO imageVO);
}
