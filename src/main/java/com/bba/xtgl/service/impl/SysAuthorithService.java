package com.bba.xtgl.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bba.util.SessionUtils;
import com.bba.xtgl.dao.ISysAuthorithDao;
import com.bba.xtgl.service.api.ISysAuthorithService;
import com.bba.xtgl.vo.SysAuthorithVO;
import com.bba.xtgl.vo.SysUserVO;

@Service
@Transactional
public class SysAuthorithService implements ISysAuthorithService {

	@Resource
	private ISysAuthorithDao sysAuthorithDao;

	@Override
	public List<SysAuthorithVO> getAuthorityListById(String userId, int moduleId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("moduleId", moduleId);
		paramMap.put("within_code", SessionUtils.currentSession().getWithin_code());
		return sysAuthorithDao.getAuthorityListById(paramMap);
	}
	@Override
	public List<SysAuthorithVO> getAuthorityListByName(String userId, String moduleName) {
		SysUserVO sysUserVO=SessionUtils.currentSession();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("within_code", sysUserVO.getWithin_code());
		paramMap.put("userId", userId);
		paramMap.put("moduleName", moduleName);
		return sysAuthorithDao.getAuthorityListByName(paramMap);
	}
	@Override
	public List<SysAuthorithVO> findAuthorityByPropertyList(List<SysAuthorithVO> sysAuthorithVOList) {
		
		return sysAuthorithDao.findAuthorityByPropertyList(sysAuthorithVOList);
	}
	
	
	

}

