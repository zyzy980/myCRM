package com.bba.xtgl.service.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.DictionaryVO;
import com.bba.xtgl.vo.SysDictionaryDataVO;
import com.bba.xtgl.vo.SysDictionaryVO;

public interface ISysDictionaryService {
	
	public ResultVO getDictionaryDataListCache(String dicTypeCode);
	public List<DictionaryVO> getListForGrid(JqGridParamModel jqGridParamModel);
	public PageVO getListForGridSubordinate(JqGridParamModel jqGridParamModel);
	public void add(SysDictionaryVO sysvo);
	public void delete(Integer[] sn);
	public void deleteData(Integer[] sn);
	public void update(SysDictionaryVO sysvo);
	public SysDictionaryVO getDictionary(Integer sn);
	
	public void insertDictionaryVO(List<DictionaryVO> dictionaryVOlist,HttpServletRequest request,HttpServletResponse response);
	
	public List<SysDictionaryDataVO> findDictionaryDataList(String typeCode);
	
	
	/**
	 * 保存数据字典
	 * @param typeCode 目录ID
	 * @param dicText 数据字典文本值
	 * @return dicValue
 	 */
	public String saveDictionaryData(String typeCode, String dicText);
	
	List<DictionaryVO> findDictionaryList();
	
	public List<SysDictionaryDataVO> findDictionaryLists(SysDictionaryDataVO sysDictionaryDataVO);
	
	public List<SysDictionaryDataVO> getState(String tableName);

	/**
	 * @Description 验证密码、用户名是否正确，用来修改字典表
	 * @param password 密码
	 * @param userName 用户名
	 * @Author lao li
	 * @Date
	 * @return
	*/
	ResultVO validPassword(String password,String userName);

	List<SysDictionaryVO> getZdDic(JqGridParamModel jqGridParamModel);

	void insertDicData(List<DictionaryVO> dictionaryVOlist,HttpServletRequest request, HttpServletResponse response);

	void deleteDicData(Integer[] sn);

	void deleteDic(Integer[] array);
	PageVO getDicData(JqGridParamModel jqGridParamModel);

	SysDictionaryVO getCusDictionary(Integer sn);

	void updateDic(SysDictionaryVO sysvo);

	void addDic(SysDictionaryVO sysvo);

}
