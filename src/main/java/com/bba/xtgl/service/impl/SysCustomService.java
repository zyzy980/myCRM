package com.bba.xtgl.service.impl;


import com.bba.xtgl.dao.ISysCustomDao;
import com.bba.xtgl.dao.ISysLanguageDao;
import com.bba.xtgl.service.api.ISysCustomService;
import com.bba.xtgl.vo.SysCustomVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class SysCustomService implements ISysCustomService {

	@Resource
	private ISysCustomDao sysCustomDao;

	@Autowired
	private ISysLanguageDao iSysLanguageDao;
	
	@Override
	public SysCustomVO getCustomDetail(Integer userId) {
		List<SysCustomVO> sysCustomVOList = sysCustomDao.getCustomDetail(userId);
		return sysCustomVOList.size() > 0 ? sysCustomVOList.get(0) : null;
	}

	@Override
	public void addUserCustom(SysCustomVO sysCustomVO) {
		this.sysCustomDao.addUserCustom(sysCustomVO);
	}

	@Override
	public void updateUserCustom(SysCustomVO sysCustomVO) {
		sysCustomDao.updateUserCustom(sysCustomVO);
	}

	
}
