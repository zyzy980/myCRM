package com.bba.xtgl.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bba.common.interceptor.Sys_BaseOperator_LogVO;
import com.bba.util.JqGridParamModel;

public interface ISys_BaseOperator_LogDao {

	List<Map<String, Object>> findMapListBySql(@Param("sql") String sql);

	public void batchInsertForSqlServer(List<Sys_BaseOperator_LogVO> list);

	public void batchInsertForOracle(List<Sys_BaseOperator_LogVO> list);

	public List<Sys_BaseOperator_LogVO> getListForGrid(JqGridParamModel jqGridParamModel);

	public int getListForGridCount(JqGridParamModel jqGridParamModel);

	public List<Sys_BaseOperator_LogVO> getDetail(Sys_BaseOperator_LogVO vo);
}
