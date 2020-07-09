package com.bba.xtgl.dao;

import java.util.List;

import com.bba.xtgl.vo.Wsys_SetVO;

public interface IWsys_SetDao {
	public Wsys_SetVO getByWithinWhcenter(Wsys_SetVO vo);
	public int batchUpdate(List<Wsys_SetVO> list);
	//出库库存减扣 判断
	public int isOutStock(Wsys_SetVO vo);
}
