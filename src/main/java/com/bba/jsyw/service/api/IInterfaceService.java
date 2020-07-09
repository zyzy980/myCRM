package com.bba.jsyw.service.api;

import com.bba.jsyw.vo.InterfaceResultVO;
import com.bba.jsyw.vo.Tr_BusinessVO;

import java.util.List;

/**
 * 业务结算接口
 */
public interface IInterfaceService
{
	/**
	 * 插入到业务表
	 * @param sql
	 */
	void insertToBusiness(String sql);
	/**
	 * 业务数据推送到结结算系统
	 * @param list
	 */
	String DailyDataToHD(List<Tr_BusinessVO> list);

	Object GetInsertSQL(Tr_BusinessVO tr_businessVO);

    InterfaceResultVO Vl_BusinessDataToJs(String jsonData);

	List<Tr_BusinessVO> getVlBusinessList();

	void insertToBusinessAndDetail(List<Tr_BusinessVO> vl_list);

	void updateVLdataToN(List<Tr_BusinessVO> vl_list);

	void updateVLdataToY();

	/**
	 * 插入日志
	 * @param vo
	 */
	/*public void LogInsert(Sys_Interface_LogVO vo);*/
	


	

	

}
