package com.bba.xtgl.service.api;

import java.util.List;
import java.util.Map;

import com.bba.xtgl.vo.SysDictionaryDataVO;


public interface ISysDictionaryDataService {

	public List<SysDictionaryDataVO> findDictionaryDataList(String typeCode);
	
	public Map<String, SysDictionaryDataVO> findDictionaryDataMap(String typeCode);
	
	
	public int batchDelete(List<SysDictionaryDataVO> list);

	int batchDeleteDic(List<SysDictionaryDataVO> list);

}
