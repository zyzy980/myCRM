package com.bba.xtgl.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bba.xtgl.vo.SysDictionaryDataVO;

public interface SysDictionaryDataDao {

	public List<SysDictionaryDataVO> findList(@Param(value="within_code")String within_code,@Param(value="typeCode")String typeCode);
	
	public int batchDelete(List<SysDictionaryDataVO> list);

	int batchDeleteDic(List<SysDictionaryDataVO> list);
	
}
