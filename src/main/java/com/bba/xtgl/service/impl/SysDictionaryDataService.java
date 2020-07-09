package com.bba.xtgl.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bba.util.SessionUtils;
import com.bba.xtgl.dao.SysDictionaryDataDao;
import com.bba.xtgl.service.api.ISysDictionaryDataService;
import com.bba.xtgl.vo.SysDictionaryDataVO;
import com.bba.xtgl.vo.SysUserVO;

@Service
@Transactional
public class SysDictionaryDataService implements ISysDictionaryDataService {
	
	@Resource
	private SysDictionaryDataDao sysDictionaryDataDao;
	

	@Override
	public List<SysDictionaryDataVO> findDictionaryDataList(String typeCode) {
		SysUserVO sysUserVO=SessionUtils.currentSession();
		return sysDictionaryDataDao.findList(sysUserVO.getWithin_code(),typeCode);
	}

	@Override
	public Map<String, SysDictionaryDataVO> findDictionaryDataMap(String typeCode) {
		List<SysDictionaryDataVO> sysDictionaryDataVOList = findDictionaryDataList(typeCode);
		Map<String, SysDictionaryDataVO> map = new HashMap<String, SysDictionaryDataVO>();
		for (SysDictionaryDataVO vo : sysDictionaryDataVOList) {
			map.put(vo.getDicValue(), vo);
		}
		return map;
	}

	@Override
	public int batchDelete(List<SysDictionaryDataVO> list) {
		return sysDictionaryDataDao.batchDelete(list);
	}

	@Override
	public int batchDeleteDic(List<SysDictionaryDataVO> list) {
		return sysDictionaryDataDao.batchDeleteDic(list);
	}
	
	

}
