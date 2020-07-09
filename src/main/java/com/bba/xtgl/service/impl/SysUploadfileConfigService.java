package com.bba.xtgl.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bba.xtgl.dao.ISysUploadfileConfigDao;
import com.bba.xtgl.dao.SysDictionaryDao;
import com.bba.xtgl.service.api.ISysUploadfileConfigService;
import com.bba.xtgl.vo.SysUploadfileConfigVO;
import com.bba.xtgl.vo.SysUserVO;
 
@Service
@Transactional
public class SysUploadfileConfigService implements ISysUploadfileConfigService
{
	
	@Resource
	private ISysUploadfileConfigDao iSysUploadfileConfigDao;
	
	@Override
	public SysUploadfileConfigVO GetSysUploadfileConfig(String CODE,SysUserVO sysUserVO)
	{
		List<SysUploadfileConfigVO> list=iSysUploadfileConfigDao.GetSysUploadfileConfig(CODE);
		if(null==list || list.size()<=0)
			return null;
		return list.get(0);
	}
}
