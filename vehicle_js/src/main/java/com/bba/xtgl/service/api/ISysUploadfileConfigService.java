package com.bba.xtgl.service.api;

import com.bba.xtgl.vo.SysUploadfileConfigVO;
import com.bba.xtgl.vo.SysUserVO;

public interface ISysUploadfileConfigService
{
	public SysUploadfileConfigVO GetSysUploadfileConfig(String CODE,SysUserVO sysUserVO);
}
