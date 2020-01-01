package com.bba.xtgl.dao;

import com.bba.xtgl.vo.SysRoleVO;
import com.bba.xtgl.vo.SysUserDetailRolesVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ISys_userdetailrolesDao {
	public List<SysUserDetailRolesVO> findAllList(SysUserDetailRolesVO vo);

	public void batchInsert(List<SysUserDetailRolesVO> list);

}
