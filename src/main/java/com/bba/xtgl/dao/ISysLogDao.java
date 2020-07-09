package com.bba.xtgl.dao;

import java.util.List;

import com.bba.xtgl.vo.SysLogVO;

public interface ISysLogDao {
	/**
	 * 插入日志
	 * @param syslogList
	 */
	public void insertLog(List<SysLogVO> syslogList);
	
	/**
	 * 查询字段注释信息
	 * @param sysLogVO
	 * @return
	 */
	public List<SysLogVO> getColComments(SysLogVO sysLogVO);
}
