package com.bba.xtgl.dao;

import java.util.List;

import com.bba.xtgl.vo.SysShortCutLinkVO;

public interface ISysShortCutLinkDao {


	public List<SysShortCutLinkVO> findList(SysShortCutLinkVO vo);

	public int update(List<SysShortCutLinkVO> list);

	public int save(SysShortCutLinkVO vo);

	public Integer delete(SysShortCutLinkVO vo);


}
