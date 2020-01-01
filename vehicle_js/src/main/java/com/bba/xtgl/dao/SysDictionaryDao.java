package com.bba.xtgl.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.DictionaryVO;
import com.bba.xtgl.vo.SysDictionaryDataVO;
import com.bba.xtgl.vo.SysDictionaryVO;

public interface SysDictionaryDao {
	public List<DictionaryVO> findList(JqGridParamModel jqGridParamModel);
	public List<DictionaryVO> getListForGridSubordinate(JqGridParamModel jqGridParamModel);
	public int getListForGridSubordinateCount(JqGridParamModel jqGridParamModel);
	public void add(SysDictionaryVO sysvo);
	public void delete(Integer[] sn);
	public void deleteData(Integer[] sn);
	public void update(SysDictionaryVO sysvo);
	public SysDictionaryVO getDictionary(Integer sn);
	public void insertDictionary(List<DictionaryVO> list);
	public void updateDictionary(List<DictionaryVO> list);
	
	/**
	 *  根据属性 删除目录下数据 
	 */
	public void deleteDataByProperty(SysDictionaryDataVO sysDictionaryDataVO);
	
	public List<SysDictionaryDataVO> findDictionaryDataList(@Param(value="within_code")String within_code,@Param(value="typeCode")String typeCode);
	
	List<DictionaryVO> findDictionaryList();
	
	public List<SysDictionaryDataVO> findDictionaryLists(SysDictionaryDataVO sysDictionaryDataVO);
	
	
	public List<Map<String, Object>> findAllDictionaryList();
	public List<Map<String, Object>> findAllDictionaryDataList();
	
	/**
	 * 通过传进来的参数查询数据字典
	 * @param params
	 * @return
	 */
	List<SysDictionaryDataVO> getDicInfo(Map<String, Object> params);

	List<SysDictionaryVO> getZdDic(JqGridParamModel jqGridParamModel);

	void insertZdDicData(List<DictionaryVO> list);

	void updateZdDicData(List<DictionaryVO> list);

	List<SysDictionaryDataVO> queryZdDicData(@Param(value="within_code")String within_code,@Param(value="typeCode")String typeCode);

	void deleteDicData(Integer[] sn);
	void deleteDic(Integer[] sn);

	List<DictionaryVO> getDicData(JqGridParamModel jqGridParamModel);

	SysDictionaryVO getCusDictionary(Integer sn);

	void updateDic(SysDictionaryVO sysvo);

	void addDic(SysDictionaryVO sysvo);


}
