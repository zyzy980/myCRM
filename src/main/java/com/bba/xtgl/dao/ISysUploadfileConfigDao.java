package com.bba.xtgl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bba.xtgl.vo.SysUploadfileConfigVO;
 

public interface ISysUploadfileConfigDao {
	 
	
	/**
	 *  获取数据列表
	 */
	public List<SysUploadfileConfigVO> GetSysUploadfileConfig(@Param(value="CODE")String CODE);
	
	 
}
