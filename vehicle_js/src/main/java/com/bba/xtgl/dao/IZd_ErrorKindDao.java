package com.bba.xtgl.dao;

import com.bba.xtgl.vo.copyUser.ZdErrorkindVO;

import java.util.List;

public interface IZd_ErrorKindDao {

	public List<ZdErrorkindVO> findAllList(ZdErrorkindVO vo);

	public void insert(ZdErrorkindVO vo);
}
