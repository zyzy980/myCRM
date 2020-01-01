package com.bba.xtgl.dao;

import java.util.List;

import com.bba.xtgl.vo.ModuleVO;
import com.bba.xtgl.vo.SysCustomVO;

public interface ISysCustomDao {

	public List<SysCustomVO> getCustomDetail(Integer userId);
	
	public int addUserCustom(SysCustomVO sysCustomVO);
	
	public int updateUserCustom(SysCustomVO sysCustomVO);

	public List<ModuleVO> GetSysModuleVo();
}
