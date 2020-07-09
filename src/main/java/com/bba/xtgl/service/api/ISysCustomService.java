package com.bba.xtgl.service.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bba.xtgl.vo.SysCustomVO;

public interface ISysCustomService {
	public SysCustomVO getCustomDetail(Integer userId);
	
	public void addUserCustom(SysCustomVO sysCustomVO);
	
	public void updateUserCustom(SysCustomVO sysCustomVO);
	
}
