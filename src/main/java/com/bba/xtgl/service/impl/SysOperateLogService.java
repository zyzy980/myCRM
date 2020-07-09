package com.bba.xtgl.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bba.common.vo.PageVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.xtgl.dao.ISysOperateLogDao;
import com.bba.xtgl.service.api.ISysOperateLogService;
import com.bba.xtgl.vo.SysOperateLogVO;

@Service
@Transactional
public class SysOperateLogService implements ISysOperateLogService {

	@Resource
	private ISysOperateLogDao sysOperateLogDao;

	@Override
	public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
		String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		List<SysOperateLogVO> list = sysOperateLogDao.getListForGrid(jqGridParamModel);
		int records = sysOperateLogDao.getListForGridCount(jqGridParamModel);
		return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
	}
	@Override
	public SysOperateLogVO getDetail(SysOperateLogVO vo) {
		List<SysOperateLogVO> vos = sysOperateLogDao.findListByProperty(vo);
		if (vos != null && vos.size() > 1) {
			throw new RuntimeException("查询单条数据失败，匹配结果数量为多条");
		}
		return vos.size() == 0 ? null : vos.get(0);
	}
	@Override
	public void save(SysOperateLogVO vo) {
		// 字符长度大于3800 就截取
		try
		{
			if (StringUtils.isNotEmpty(vo.getParams()) && vo.getParams().length() > 3800) {
				vo.setParams(vo.getParams().substring(0, 3800) + "...");
			}
			sysOperateLogDao.insert(vo);
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			
		}
	}

}