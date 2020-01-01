package com.bba.xtgl.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bba.xtgl.dao.ISysShortCutLinkDao;
import com.bba.xtgl.service.api.ISysShortCutLinkService;
import com.bba.xtgl.vo.SysShortCutLinkVO;

@Service
@Transactional
public class SysShortCutLinkService implements ISysShortCutLinkService {

	@Resource
	private ISysShortCutLinkDao sysShortCutLinkDao;

	@Override
	public List<SysShortCutLinkVO> findSysShortCutLinkByUserId(String userId) {
		SysShortCutLinkVO vo = new SysShortCutLinkVO();
		vo.setUserId(userId);
		return sysShortCutLinkDao.findList(vo);
	}

	@Override
	public SysShortCutLinkVO getSysShortCutLinkById(Integer id) {
		SysShortCutLinkVO vo = new SysShortCutLinkVO();
		vo.setId(id);
		List<SysShortCutLinkVO> list = sysShortCutLinkDao.findList(vo);
		return list.size() == 0 ? null :list.get(0);
	}

	@Override
	public Integer updateSysShortCutLink(List<SysShortCutLinkVO> sysShortCutLinkList) {
		return sysShortCutLinkDao.update(sysShortCutLinkList);
	}

	@Override
	public Integer saveSysShortCutLink(SysShortCutLinkVO vo) {
		return sysShortCutLinkDao.save(vo);
	}

	@Override
	public Integer deleteSysShortCutLink(SysShortCutLinkVO vo) {
		return sysShortCutLinkDao.delete(vo);
	}


	
}
