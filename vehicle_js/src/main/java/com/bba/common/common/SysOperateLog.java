package com.bba.common.common;

import org.springframework.beans.factory.annotation.Autowired;

import com.bba.util.SessionUtils;
import com.bba.xtgl.service.api.ISysOperateLogService;
import com.bba.xtgl.vo.SysOperateLogVO;
import com.bba.xtgl.vo.SysUserVO;

public class SysOperateLog {
	@Autowired
	private ISysOperateLogService iSysOperateLogService;

	public SysOperateLogVO logVO;

	/**
	 * 设置操作日志对象
	 */
	public void setSysOperateLog(SysOperateLogVO logVO) {
		this.logVO = logVO;
	}

	/**
	 * 获取操作日志对象
	 */
	public SysOperateLogVO getSysOperateLog() {
		return this.logVO;
	}

	/**
	 * 写入操作日志数据
	 */
	public void insert() {
		if (null == logVO) {
			return;
		}
		try {
			SysUserVO sysUserVO = (SysUserVO) SessionUtils.currentSession();
			logVO.setOperator(sysUserVO.getUserId() + sysUserVO.getRealName());
			logVO.setWithinCode(sysUserVO.getWithin_code());
			logVO.setYwLocation(sysUserVO.getWhcenter());
			
			iSysOperateLogService.save(logVO);
		} catch (Exception e) {

		}
	}

}
