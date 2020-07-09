package com.bba.xtgl.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bba.xtgl.dao.IZdYwLocationDao;
import com.bba.xtgl.service.api.IZdYwLocationService;
import com.bba.xtgl.vo.ZdYwLocationVO;

@Service
@Transactional
public class ZdYwLocationService implements IZdYwLocationService {
	@Resource
	private IZdYwLocationDao zdYwLocationDao;

	@Override
	public List<ZdYwLocationVO> findZdYwLocation(ZdYwLocationVO paramZdYwLocationVO) {
		List<ZdYwLocationVO> zdYwLocationList = zdYwLocationDao
				.findList(paramZdYwLocationVO);
		return zdYwLocationList;
	}

}