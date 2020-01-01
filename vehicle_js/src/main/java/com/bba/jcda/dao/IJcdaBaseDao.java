package com.bba.jcda.dao;

import com.bba.util.JqGridParamModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;




/**
 * 通用dao层
 * 
 * @author Administrator
 * 
 */
public interface IJcdaBaseDao<T> {
	/**
	 * 组合查询数据集
	 * @param 条件集合
	 * @return
	 */
	public List<T> findListByPropertys(List<T> list);

	/**
	 * 获取详情
	 * @param vo
	 * @return
	 */
	public T getDetail(T vo);
	/**
	 * 分页查询
	 * @param vo
	 * @return
	 */
	public List<T> findListForGrid(T vo);
	public List<T> findListForGrid(JqGridParamModel jqGridParamModel);
	/**
	 * 批量修改数据
	 * @param list
	 * @return
	 */
	public int batchUpdate(List<T> list);
	
	/**
	 * 固定列修改
	 * @param vo  固定列值
	 * @param where_list 条件集
	 * @return
	 */
	public int batchFixedUpdate(@Param("vo") T vo, @Param("where_list") List<T> where_list);
	
	/**
	 * 批量新增数据
	 * @param list
	 * @return
	 */
	public int batchInsert(List<T> list);  
	
	/**
	 * 批量删除数据,不删除，只修改状态
	 * @param list
	 * @return
	 */
	public void batchDelete(List<T> list);
	/**
	 * 分页查询数据数量
	 * @param bill_TranferVO
	 * @return
	 */
	public String findListForGridCount(T vo);
	public int findListForGridCount(JqGridParamModel jqGridParamModel);
	
	/**
	 * 批量修改属性为Y
	 * @param list
	 * @return
	 */
	public int batchEdit2Y(List<T> list);


	void insertData(T t);

	void updateData(T t);
}
