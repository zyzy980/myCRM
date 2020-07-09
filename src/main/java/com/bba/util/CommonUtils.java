package com.bba.util;

import com.bba.common.vo.ResultVO;
import com.bba.util.HttpRequestUtils;
import com.bba.util.JSONUtils;
import com.bba.util.OperatorException;
import com.bba.util.ResultDataFullMore;

public class CommonUtils {

	private static final String SHEET_ID_URL = "";
	
	private static final String State_URL = "";
	
	private static final String getDate_URL = "";
	
	/**
	 * 获取单号
	 * @param within_code 公司内码
	 * @param whcenter    仓储中心	
	 * @param table_name  表名称	
	 * 返回的值，前2个字母是OK的说明取单号成功，否则取单号失败。
	 */
	public static String getSheet_id(String within_code,String whcenter, String table_name){
		System.out.println(within_code+whcenter+table_name);
		String jsonData = HttpRequestUtils.sendPost(SHEET_ID_URL, "table_name="+table_name+"&within_code="+within_code+"&whcenter="+whcenter);
		
		ResultVO resultVO = JSONUtils.toJSONObject(ResultVO.class, jsonData);
		
		if (org.apache.commons.lang.StringUtils.equals(resultVO.getResultCode(), "0")) {
			return resultVO.getResultDataFull() + "";
		}
		ResultDataFullMore vo = null;
		
		
		vo = JSONUtils.toJSONObject(ResultDataFullMore.class, JSONUtils.toJSONString(resultVO.getResultDataFull()));
		if(vo!=null){
			
			if(vo.getMoreMessage()!=null){
				throw new OperatorException(vo.getSimpleMessage()+","+vo.getMoreMessage());
			}else{
				throw new OperatorException(vo.getSimpleMessage());
			}
			

		}
		throw new OperatorException(resultVO.getResultDataFull() + "");
	}
	
	/**
	 * 获取状态
	 * @param within_code 公司内码
	 * @param whcenter    仓储中心	
	 * @param table_name  表名称	
	 * @param currid  	     单号
	 * 返回的值，前2个字母是OK的说明取单号成功，否则取单号失败。
	 */
	public static String getState(String within_code,String whcenter, String table_name,String currid){
		String jsonData = HttpRequestUtils.sendPost(State_URL, "table_name="+table_name+"&within_code="+within_code+"&whcenter="+whcenter+"&currid="+currid);
		
		ResultVO resultVO = JSONUtils.toJSONObject(ResultVO.class, jsonData);
		
		if (org.apache.commons.lang.StringUtils.equals(resultVO.getResultCode(), "0")) {
			return resultVO.getResultDataFull() + "";
		}
		ResultDataFullMore vo = null;
		
		
		vo = JSONUtils.toJSONObject(ResultDataFullMore.class, JSONUtils.toJSONString(resultVO.getResultDataFull()));
		if(vo!=null){
			throw new OperatorException(vo.getMoreMessage());

		}
		throw new OperatorException(resultVO.getResultDataFull() + "");
	}
	
	/**
	 * 获取数据库当前时间
	 */
	public static String getDate(){
		String jsonData = HttpRequestUtils.sendPost(getDate_URL,"");
		
		ResultVO resultVO = JSONUtils.toJSONObject(ResultVO.class, jsonData);
		
		if (org.apache.commons.lang.StringUtils.equals(resultVO.getResultCode(), "0")) {
			return resultVO.getResultDataFull() + "";
		}
		ResultDataFullMore vo = null;
		
		
		vo = JSONUtils.toJSONObject(ResultDataFullMore.class, JSONUtils.toJSONString(resultVO.getResultDataFull()));
		if(vo!=null){
			throw new OperatorException(vo.getMoreMessage());

		}
		throw new OperatorException(resultVO.getResultDataFull() + "");
	}
	
	/**
	 * 生成全球唯一码
	 * */
	public static String getToken()
	{
		return MyUUid.getUUid();
	}
}
