package com.bba.xtgl.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bba.xtgl.dao.IWsys_SetDao;
import com.bba.xtgl.service.api.IWsys_SetService;
import com.bba.xtgl.vo.Wsys_SetVO;
@Service
@Transactional
public class Wsys_SetService implements IWsys_SetService{
	
	@Resource
	private IWsys_SetDao wsys_SetDao;
	
	
	@Override
	public Wsys_SetVO getByWithinWhcenter(Wsys_SetVO vo) {
		// TODO Auto-generated method stub
		return wsys_SetDao.getByWithinWhcenter(vo);
	}

}
