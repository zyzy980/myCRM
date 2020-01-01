package com.bba.xtgl.dao;

import java.util.List;

import com.bba.xtgl.vo.ImageVO;

public interface IPngDao {
	/**
	 * 查询ico
	 * @param imageVO
	 * @return
	 */
	public List<ImageVO> findImage(ImageVO imageVO);
	
	/**
	 * 查询ico
	 * @param imageVO
	 * @return
	 */
	public List<ImageVO> findImageCount(ImageVO imageVO);
}
