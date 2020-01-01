package com.bba.xtgl.service.api;

import java.util.List;

import com.bba.xtgl.vo.SysShortCutLinkVO;

public interface ISysShortCutLinkService {


	public List<SysShortCutLinkVO> findSysShortCutLinkByUserId(String userId);
	
	public SysShortCutLinkVO getSysShortCutLinkById(Integer id);

	
	public Integer updateSysShortCutLink(List<SysShortCutLinkVO> sysShortCutLinkList);

	public Integer saveSysShortCutLink(SysShortCutLinkVO vo);

	public Integer deleteSysShortCutLink(SysShortCutLinkVO vo);


}
