package com.bba.xtgl.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bba.xtgl.dao.ISysRoleDao;
import com.bba.xtgl.service.api.ISysRoleService;
import com.bba.xtgl.vo.SysRoleVO;


@Service
@Transactional
public class SysRoleService implements ISysRoleService {

	@Resource
	private ISysRoleDao sysRoleDao;

	@Override
	public List<SysRoleVO> findByUserId(String within_code,String userId) {
		return sysRoleDao.findByUserId(within_code,userId+"");
	}

}