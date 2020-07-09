package com.bba.xtgl.service.api;

import com.bba.xtgl.vo.SysUserVO;
import com.bba.xtgl.vo.SysUsersTokenVO;

public interface ISysUsersTokenService
{
	
	public SysUsersTokenVO getSysUsersTokenVO(SysUsersTokenVO sysUsersTokenVO);
	public Integer getSysUsersTokenCount(SysUsersTokenVO sysUsersTokenVO);
	public void insert(SysUsersTokenVO sysUsersTokenVO);
	public void delete(SysUsersTokenVO sysUsersTokenVO);
	
	public SysUserVO getUserById(String within_code,String userid);
}
