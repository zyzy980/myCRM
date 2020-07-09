package com.bba.xtgl.dao;

import java.util.List;

import com.bba.xtgl.vo.ZdYwLocationVO;

public interface IZdYwLocationDao {
	public List<ZdYwLocationVO> findList(ZdYwLocationVO paramZdYwLocationVO);
	public void insert(ZdYwLocationVO vo);
}
