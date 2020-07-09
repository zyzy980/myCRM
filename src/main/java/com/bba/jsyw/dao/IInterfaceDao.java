package com.bba.jsyw.dao;

import com.bba.jsyw.vo.Tr_BusinessVO;
import com.bba.xtgl.vo.Sys_Dictionary_DataVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IInterfaceDao
{

	public void insertToBusiness(@Param(value = "sql") String sql);

	int findTrBusinessByVin(Tr_BusinessVO vo);

	List<Tr_BusinessVO> getVlBusinessList();

	void updateVLdataToN(@Param(value = "batch_no") String batch_no);

	List<String> getZDtransType();

	void insertZdictionary_data(Sys_Dictionary_DataVO vo);

	void insertToBusinessData(List<Tr_BusinessVO> list);

    void insertToBusinessCarrierData(List<Tr_BusinessVO> list);

    List<String> getCityList();

    void insertJs_vin_down_premium(List<Tr_BusinessVO> list);

	void updateVLdataToY();
	/*public void LogInsert(Sys_Interface_LogVO vo);*/


}
