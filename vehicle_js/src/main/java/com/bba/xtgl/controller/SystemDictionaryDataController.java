package com.bba.xtgl.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bba.common.vo.ResultVO;
import com.bba.xtgl.service.api.ISysDictionaryService;
import com.bba.xtgl.service.api.ISys_Dictionary_DataService;
import com.bba.xtgl.vo.SysDictionaryDataVO;
import com.bba.xtgl.vo.Sys_Dictionary_DataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RequestMapping("/sysInfo/dictionaryData")
@RestController
public class SystemDictionaryDataController {

	 
	@Autowired
	private ISysDictionaryService sysDictionaryService;

	@Autowired
	private ISys_Dictionary_DataService sys_dictionary_dataService;
	
	@RequestMapping("getDictionaryDataListCache")
	public ResultVO getDictionaryDataListCache(String dicTypeCode) {
		List<SysDictionaryDataVO> dictionaryDataVOList = sysDictionaryService.findDictionaryDataList(dicTypeCode);
		ResultVO resultVO = new ResultVO();
		resultVO.setResultCode("0");
		resultVO.setResultDataFull(dictionaryDataVOList);
		return resultVO;
	}

	@RequestMapping("getDictionaryDataListAll")
	public ResultVO getDictionaryDataListAll() {
		EntityWrapper<Sys_Dictionary_DataVO> wrapper = new EntityWrapper<Sys_Dictionary_DataVO>();
		wrapper.orderAsc(Arrays.asList("DICORDER"));
		List<Sys_Dictionary_DataVO> list = sys_dictionary_dataService.selectList(wrapper);
		Map<String, List<Sys_Dictionary_DataVO>> baseDataMap =
				new HashMap<String, List<Sys_Dictionary_DataVO>>();
		for(Sys_Dictionary_DataVO dataVO: list){
			List<Sys_Dictionary_DataVO> newList = baseDataMap.get(dataVO.getTypecode());
			if(newList == null){
				newList = new ArrayList<Sys_Dictionary_DataVO>();
				baseDataMap.put(dataVO.getTypecode(), newList);
			}
			newList.add(dataVO);
		}
		ResultVO resultVO = ResultVO.successResult();
		resultVO.setResultDataFull(baseDataMap);
		return resultVO;
	}

	@RequestMapping("getDictionaryDataList")
	public List<SysDictionaryDataVO> getDictionaryDataList(String dicTypeCode) {
		return sysDictionaryService.findDictionaryDataList(dicTypeCode);
	}
}
