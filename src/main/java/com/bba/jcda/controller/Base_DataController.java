package com.bba.jcda.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bba.common.constant.BaseDataKeyEnum;
import com.bba.common.vo.ResultVO;
import com.bba.jcda.service.api.ICr_CustomerService;
import com.bba.jcda.service.api.IZd_CarrierService;
import com.bba.jcda.vo.Cr_CustomerVO;
import com.bba.jcda.vo.Zd_CarrierVO;
import com.bba.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bba.common.constant.OperateConstant;
import com.bba.xtgl.service.api.ISysDictionaryDataService;
import com.bba.xtgl.vo.SysDictionaryDataVO;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/base/Base_DataController")
public class Base_DataController {

	@Autowired
	private ISysDictionaryDataService sysDictionaryDataService;
	@Autowired
	private IZd_CarrierService zd_carrierService;
	@Autowired
	private ICr_CustomerService cr_customerService;
	

	@RequestMapping("getBaseData")
	@ResponseBody
	public ResultVO getBaseData(@RequestBody List<String> dicTypeCodeList){

		Map<String, Object> baseDataMap = new HashMap<String, Object>();
		for (String key: dicTypeCodeList) {
			if(BaseDataKeyEnum.CR_CUSTOMER.getCode().equals(key)){
				Cr_CustomerVO customerVO = new Cr_CustomerVO();
				customerVO.setStatus("0");
				EntityWrapper wapper = new EntityWrapper();
				wapper.setEntity(customerVO);
				List<Cr_CustomerVO> list = cr_customerService.selectList(wapper);
				baseDataMap.put(key, list);
			}else if(BaseDataKeyEnum.ZD_CARRIER.getCode().equals(key)){
				Zd_CarrierVO zd_CarrierVO = new Zd_CarrierVO();
				zd_CarrierVO.setStatus("0");
				EntityWrapper wapper = new EntityWrapper();
				wapper.setEntity(zd_CarrierVO);
				List<Zd_CarrierVO> list = zd_carrierService.selectList(wapper);
				baseDataMap.put(key, list);
			}
		}
		ResultVO resultVO = ResultVO.successResult();
		resultVO.setResultDataFull(baseDataMap);
		return resultVO;
	}
		
	@RequestMapping(value = "/exportExcelData", method = RequestMethod.POST)
	public void exportExcelData(CommonExportVO<?> commonExportVO, HttpServletResponse response) {
		
		Map<String, String> requestPropertyMap = new HashMap<String, String>();
		
        String sessionid = SessionUtils.currencyHttpServletRequest().getSession().getId();
		requestPropertyMap.put("Cookie", "JSESSIONID="+sessionid);
		SysUserVO sysUserVO = SessionUtils.currentSession();
		commonExportVO.setWhcenter(sysUserVO.getWhcenter());
		if(StringUtils.isBlank(commonExportVO.getExport_title())){
			commonExportVO.setExport_title("Excel导出数据");
		}
		List<Map> dataList = null;
		if(StringUtils.equals(commonExportVO.getExport_KIND(), OperateConstant.Global_DicType_Export_Kind_Where)){
			String jsonData = HttpRequestUtils.sendPost(commonExportVO.getUrl(), ""+commonExportVO.getExport_customSearchFilters(),requestPropertyMap);
			Map<String, Object> jsonMap = JSONUtils.toJSONObject(Map.class, jsonData);
			dataList = (List<Map> )jsonMap.get("rows");
			if(dataList == null){
				throw new RuntimeException("导出失败,请联系管理员");
			}
			
			//如果有数据字典需要转化
			if(StringUtils.isNotBlank(commonExportVO.getFiledData())){
				List<Map> filedData = JSONUtils.toJSONObjectList(Map.class, commonExportVO.getFiledData());
				int i = 0;
				for (Map<String, Object> map : filedData) {
					Object key = map.get("data_dictionary");
					Object filed = map.get("filed");
					Object defaultValue = map.get("defaultValue");
					if(key == null || filed == null){
						throw new RuntimeException("第"+(i+1)+"行,数据字典参数格式有误");
					}
					Map<String, SysDictionaryDataVO> sysDictionaryDataVOMap = sysDictionaryDataService.findDictionaryDataMap(key.toString());
					for (Map mapVO : dataList) {
						Object value = mapVO.get(filed);
						if(value == null){
							if(defaultValue != null){
								mapVO.put(filed, defaultValue.toString());
							}
						}else{
							
							SysDictionaryDataVO sysDictionaryDataVO = sysDictionaryDataVOMap.get(value);
							if(sysDictionaryDataVO != null){
								mapVO.put(filed, sysDictionaryDataVO.getDicText());
							}
						}
					}
					i++;
				}
			}
		}else{
			dataList = JSONUtils.toJSONObjectList(Map.class, commonExportVO.getExport_CurrentPageData());
		}
		try {
			POIUtils.commonClassExport(commonExportVO, dataList, response);
		} catch (Exception e) {
			throw new RuntimeException("导出失败,请联系管理员");
		}
	}	
	
	
}
