package com.bba.common.interceptor;

import com.bba.util.DateUtils;
import com.bba.util.SessionUtils;
import com.bba.xtgl.dao.ISys_BaseOperator_LogDao;
import com.bba.xtgl.vo.SysUserVO;
//import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LogManagerment {
/*
	@Autowired
	private ISys_BaseOperator_LogDao sys_BaseOperator_LogDao;
	
	public void after(JoinPoint joinpoint) throws ClassNotFoundException, InstantiationException, IllegalAccessException{  
	
		HttpServletRequest request = SessionUtils.currencyHttpServletRequest();
		if(request == null) {
			return;
		}
		SysUserVO sysUserVO = SessionUtils.currentSession();
		List<Map<String,Object>> prev_data_list = (List<Map<String,Object>>)request.getAttribute("pref_data_list");
		if(prev_data_list == null || sysUserVO == null){
			return;
		}
		Map<String,String> columnMap = (Map<String,String>)request.getAttribute("columnMap");
		String after_data_list_sql = (String)request.getAttribute("after_data_list_sql");
		List<Map<String,Object>> after_data_list = sys_BaseOperator_LogDao.findMapListBySql(after_data_list_sql);
		String databaseId = (String)request.getAttribute("databaseId");
		String table_code = (String)request.getAttribute("table_code");
		String primary_field = (String)request.getAttribute("primary_field");
		
        Sys_BaseOperator_LogVO sys_BaseOperator_LogVO = new Sys_BaseOperator_LogVO();
        sys_BaseOperator_LogVO.setCreate_by(sysUserVO.getRealName());
		sys_BaseOperator_LogVO.setCreate_date(DateUtils.nowDate());
		sys_BaseOperator_LogVO.setTable_code(table_code);
		sys_BaseOperator_LogVO.setTable_name(columnMap.get(table_code));
        sys_BaseOperator_LogVO.setPrimary_field(primary_field);
		insertBaseOperator(prev_data_list, after_data_list, sys_BaseOperator_LogVO, columnMap, databaseId);
		request.setAttribute("pref_data_list", null);
		request.setAttribute("columnMap", null);
		request.setAttribute("databaseId", null);
		request.setAttribute("table_code", null);
		request.setAttribute("primary_field", null);
	  } 
		
	private void insertBaseOperator(List<Map<String,Object>> prev_data_list, List<Map<String,Object>> after_data_list,
	    		Sys_BaseOperator_LogVO sys_BaseOperator_LogVO, Map<String,String> columnMap, String databaseId){
		String primary_field = sys_BaseOperator_LogVO.getPrimary_field();
		List<Sys_BaseOperator_LogVO> sys_BaseOperator_LogVOList = new ArrayList<Sys_BaseOperator_LogVO>();
		for (Map<String,Object> prev_dataMap : prev_data_list) {
			Map<String, Object> after_dataMap = null;
			for (Map<String,Object> dataAfter_dataMap : after_data_list) {
				Object after_dataValue = dataAfter_dataMap.get(primary_field);
				Object prev_dataValue = prev_dataMap.get(primary_field);
				if((after_dataValue != null && prev_dataValue == null) || after_dataValue ==null &&prev_dataValue!=null){
					after_dataMap = dataAfter_dataMap;
					break;
				}
				if(after_dataValue != null && prev_dataValue != null){
					if(after_dataValue.toString().equals(prev_dataValue.toString())){
						after_dataMap = dataAfter_dataMap;
						break;
					}
				}
			}
			if(after_dataMap == null){
				//修改前数据库有数据,修改后没有就不操作
				continue;
			}
			
			String nowDate = DateUtils.nowDate();
			for (String key : prev_dataMap.keySet()) {
				Object prev_data = prev_dataMap.get(key);
				Object next_data = after_dataMap.get(key);
				
				
				boolean exits = false;
				if((prev_data != null && next_data == null)||(prev_data == null && next_data != null)){
					exits = true;					
				}else{
					if(prev_data !=null && !prev_data.equals(next_data)){
						exits = true;					
					}
				}
				if(exits){
					Sys_BaseOperator_LogVO paramSys_BaseOperator_LogVO = new Sys_BaseOperator_LogVO();
					paramSys_BaseOperator_LogVO.setCreate_by(sys_BaseOperator_LogVO.getCreate_by());
					paramSys_BaseOperator_LogVO.setCreate_date(nowDate);
					paramSys_BaseOperator_LogVO.setPrimary_field(sys_BaseOperator_LogVO.getPrimary_field());
					paramSys_BaseOperator_LogVO.setPrimary_field_value(""+prev_dataMap.get(sys_BaseOperator_LogVO.getPrimary_field()));
					paramSys_BaseOperator_LogVO.setTable_code(sys_BaseOperator_LogVO.getTable_code());
					paramSys_BaseOperator_LogVO.setTable_name(sys_BaseOperator_LogVO.getTable_name());
					paramSys_BaseOperator_LogVO.setField_code(key);
					paramSys_BaseOperator_LogVO.setField_name(columnMap.get(key));
					if(prev_data != null){
						paramSys_BaseOperator_LogVO.setPrev_data(prev_data.toString());
					}
					if(next_data != null){
						paramSys_BaseOperator_LogVO.setNext_data(next_data.toString());
					}
					sys_BaseOperator_LogVOList.add(paramSys_BaseOperator_LogVO);
				}					
			}
		}
		if(sys_BaseOperator_LogVOList.size() > 0){
			
			if("Microsoft SQL Server".equals(databaseId)){
				sys_BaseOperator_LogDao.batchInsertForSqlServer(sys_BaseOperator_LogVOList);
			}else{
				sys_BaseOperator_LogDao.batchInsertForOracle(sys_BaseOperator_LogVOList);
			}
			
		}
    }*/
}



