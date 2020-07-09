package com.bba.xtgl.service.api;

import java.util.List;

import com.bba.xtgl.vo.SysRoleVO;

public interface ISysRoleService {

	public List<SysRoleVO> findByUserId(String within_code,String userId);

}
