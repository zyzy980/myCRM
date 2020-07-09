package com.bba.xtgl.service.impl;

import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bba.xtgl.dao.ISysUsersTokenDao; 
import com.bba.xtgl.service.api.ISysUsersTokenService;
import com.bba.xtgl.vo.SysUserVO;
import com.bba.xtgl.vo.SysUsersTokenVO;

@Service
@Transactional
public class SysUsersTokenService implements ISysUsersTokenService {

	@Resource
	private ISysUsersTokenDao iSysUsersTokenDao;

	@Override
	public SysUsersTokenVO getSysUsersTokenVO(SysUsersTokenVO sysUsersTokenVO)
	{
		// TODO Auto-generated method stub
		return iSysUsersTokenDao.getSysUsersTokenVO(sysUsersTokenVO);
	}

	@Override
	public void insert(SysUsersTokenVO sysUsersTokenVO)
	{
		// TODO Auto-generated method stub
		iSysUsersTokenDao.insert(sysUsersTokenVO);
	}

	@Override
	public void delete(SysUsersTokenVO sysUsersTokenVO)
	{
		// TODO Auto-generated method stub
		iSysUsersTokenDao.delete(sysUsersTokenVO);
	}

	@Override
	public Integer getSysUsersTokenCount(SysUsersTokenVO sysUsersTokenVO)
	{
		// TODO Auto-generated method stub
		return iSysUsersTokenDao.getSysUsersTokenCount(sysUsersTokenVO);
	}

	@Override
	public SysUserVO getUserById(String within_code,String userid)
	{
		// TODO Auto-generated method stub
		return iSysUsersTokenDao.getUserById(within_code,userid);
	}
	  

}
