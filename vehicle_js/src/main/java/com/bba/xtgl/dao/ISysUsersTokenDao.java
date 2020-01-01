package com.bba.xtgl.dao;

import org.apache.ibatis.annotations.Param;
import com.bba.xtgl.vo.SysUserVO;
import com.bba.xtgl.vo.SysUsersTokenVO;

public interface ISysUsersTokenDao
{
	public SysUsersTokenVO getSysUsersTokenVO(SysUsersTokenVO sysUsersTokenVO);
	public Integer getSysUsersTokenCount(SysUsersTokenVO sysUsersTokenVO);
	public void insert(SysUsersTokenVO sysUsersTokenVO);
	public void delete(SysUsersTokenVO sysUsersTokenVO);
	
	public SysUserVO getUserById(@Param(value="within_code")String within_code,@Param(value="userid")String userid);
}
