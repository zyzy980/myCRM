package com.bba.xtgl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bba.xtgl.vo.SysRoleVO;

public interface ISysRoleDao {
	public List<SysRoleVO> findByUserId(@Param(value="within_code")String within_code,@Param(value="userId")String userId);

}
