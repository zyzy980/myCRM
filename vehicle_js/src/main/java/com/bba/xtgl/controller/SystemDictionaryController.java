package com.bba.xtgl.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.util.MyUtils;
import com.bba.util.SessionUtils;
import com.bba.xtgl.service.api.ISysDictionaryDataService;
import com.bba.xtgl.service.api.ISysDictionaryService;
import com.bba.xtgl.vo.DictionaryVO;
import com.bba.xtgl.vo.SysDictionaryDataVO;
import com.bba.xtgl.vo.SysDictionaryVO;
import com.bba.xtgl.vo.SysUserVO;

@RequestMapping("/sysInfo/dictionary")
@Controller
public class SystemDictionaryController {
	
	@Autowired
	private ISysDictionaryService sysDictionaryService;
	@Autowired
	private ISysDictionaryDataService sysDictionaryDataService;
	
	@Log("数据字典管理-获取字典菜单")
	@RequestMapping(value = "/getListForGrid", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO getListForGrid(JqGridParamModel jqGridParamModel,String customSearchFilters){ 
		//SysUserVO sysUserVO=SessionUtils.currentSession();
		//jqGridParamModel.put("within_code", "eq", sysUserVO.getWithin_code());
		String filters = JqGridSearchParamHandler.processSql( customSearchFilters, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		List<DictionaryVO> list = sysDictionaryService.getListForGrid(jqGridParamModel);
		ResultVO resultVO = ResultVO.successResult();
		resultVO.setResultDataFull(list);
		return resultVO;
	}
	@Log("数据字典管理-获取字典明细")
	@RequestMapping(value = "/getListForGridSubordiById", method = RequestMethod.POST)
	@ResponseBody
	public PageVO getListForGridSubordiById(JqGridParamModel jqGridParamModel,String customSearchFilters) {
		//SysUserVO sysUserVO=SessionUtils.currentSession();
		//jqGridParamModel.put("within_code", "eq", sysUserVO.getWithin_code());
		customSearchFilters = MyUtils.decode(customSearchFilters);
		String filters = JqGridSearchParamHandler.processSql( customSearchFilters, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		PageVO pageVO = sysDictionaryService.getListForGridSubordinate(jqGridParamModel);
	
		return pageVO;
	}

	
	
	@Log("数据字典管理-新增字典菜单")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO add(Integer sn,Integer parentsn,String typename,String typecode,String remark,HttpServletRequest request,HttpServletResponse response){
		SysUserVO sysUserVO= (SysUserVO)request.getSession().getAttribute(SessionUtils.SESSION_KEY);
		SysDictionaryVO sysVo=new SysDictionaryVO();
		 sysVo.setSn(sn);
		 sysVo.setParentSn(parentsn);
		 sysVo.setWithin_code(sysUserVO.getWithin_code());
		 sysVo.setWhcenter(sysUserVO.getWhcenter());
		 sysVo.setTypeName(MyUtils.urlDecode(typename));
		 sysVo.setTypeCode(MyUtils.urlDecode(typecode));
		 sysVo.setRemark(MyUtils.urlDecode(remark));
		 if(null==sysVo.getTypeName())
			 sysVo.setTypeName("");
		 if(null==sysVo.getTypeCode())
			 sysVo.setTypeCode("");
		 if(null==sysVo.getRemark())
			 sysVo.setRemark("");
		 
		sysDictionaryService.add(sysVo);
		return ResultVO.successResult("新增成功");
	}
	
	@Log("数据字典管理-删除字典菜单")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO delete(Integer[] array, String typeCode){
		if(StringUtils.isBlank(typeCode)) {
			return ResultVO.failResult("目录名称不能为空");
		}
		SysUserVO sysUserVO = SessionUtils.currentSession();
		sysDictionaryService.delete(array);
		List<SysDictionaryDataVO> sysDictionaryDataVOList = new ArrayList<SysDictionaryDataVO>();
		SysDictionaryDataVO paramVO = new SysDictionaryDataVO();
		paramVO.setWithin_code(sysUserVO.getWithin_code());
		paramVO.setTypeCode(typeCode);
		sysDictionaryDataVOList.add(paramVO);
		sysDictionaryDataService.batchDelete(sysDictionaryDataVOList);
		ResultVO resultVO = ResultVO.successResult();
		return resultVO;
	}
	
	@Log("数据字典管理-修改字典菜单")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO update(Integer sn,Integer parentsn,String typename,String typecode,String remark)
	{
		
		 SysDictionaryVO sysVo=new SysDictionaryVO();
		 sysVo.setSn(sn);
		 sysVo.setParentSn(parentsn);
		 sysVo.setTypeName(MyUtils.urlDecode(typename));
		 sysVo.setTypeCode(MyUtils.urlDecode(typecode));
		 sysVo.setRemark(MyUtils.urlDecode(remark));
		 if(null==sysVo.getTypeName())
			 sysVo.setTypeName("");
		 if(null==sysVo.getTypeCode())
			 sysVo.setTypeCode("");
		 if(null==sysVo.getRemark())
			 sysVo.setRemark("");
 
		sysDictionaryService.update(sysVo);
		return ResultVO.successResult("修改成功!");
	}
	
	@Log("数据字典管理-保存或修改字典明细")
	@RequestMapping(value = "/SaveDictionaryDataDetail", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO SaveDictionaryDataDetail(@RequestBody List<DictionaryVO> gridAllData,HttpServletRequest request,HttpServletResponse response){
		sysDictionaryService.insertDictionaryVO(gridAllData, request, response);
		return ResultVO.successResult("保存成功!");
	
	}
	
	@Log("数据字典管理-获取字典菜单明细")
	@RequestMapping(value = "/getDictionary", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO getDictionary(String sn){
		ResultVO resultVO = ResultVO.successResult();
		SysDictionaryVO vo = sysDictionaryService.getDictionary(Integer.valueOf(sn));
		resultVO.setResultDataFull(vo);
		return resultVO;
	}
	
	@Log("数据字典管理-删除字典明细")
	@RequestMapping(value = "/deleteData", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO deleteData(Integer[] dicDataServerArray){
		ResultVO resultVO = ResultVO.successResult();
		sysDictionaryService.deleteData(dicDataServerArray);
		
		return resultVO;
	}
	
	

	/*@ResponseBody
	@RequestMapping(value = "/findAll", method = RequestMethod.POST)
	public Object findAll(HttpServletRequest request,HttpServletResponse response){
		List<Map<String, Object>> dictionaryList = sysDictionaryDao.findAllDictionaryList();
		List<Map<String, Object>> dictionaryDataList = sysDictionaryDao.findAllDictionaryDataList();
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("dictionaryList", dictionaryList);
		returnMap.put("dictionaryDataList", dictionaryDataList);
		return returnMap;
	}*/
	
	@ResponseBody
	@RequestMapping(value = "/getstate", method = RequestMethod.POST)
	public ResultVO getState(String tableName) {
		ResultVO resultVO = ResultVO.successResult();
		List<SysDictionaryDataVO> dictionaryData = sysDictionaryService.getState(tableName);
		resultVO.setResultDataFull(dictionaryData);
		return dictionaryData != null ? resultVO : ResultVO.failResult("暂无数据");
	}

	@ResponseBody
	@RequestMapping(value = "/validPassword", method = RequestMethod.POST)
	public ResultVO validPassword(String password,String userName) {
		ResultVO resultVO = sysDictionaryService.validPassword(password,userName);
		return resultVO;
	}

	@Log("数据字典管理-获取字典菜单")
	@RequestMapping(value = "/getDic", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO getDic(JqGridParamModel jqGridParamModel,String customSearchFilters){
		SysUserVO sysUserVO=SessionUtils.currentSession();
		jqGridParamModel.put("within_code", "eq", sysUserVO.getWithin_code());
		String filters = JqGridSearchParamHandler.processSql( customSearchFilters, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		List<SysDictionaryVO> list = sysDictionaryService.getZdDic(jqGridParamModel);
		ResultVO resultVO = ResultVO.successResult();
		resultVO.setResultDataFull(list);
		return resultVO;
	}

	@Log("数据字典管理-获取字典明细")
	@RequestMapping(value = "/getZdDicData", method = RequestMethod.POST)
	@ResponseBody
	public PageVO getZdDicData(JqGridParamModel jqGridParamModel,String customSearchFilters) {
		SysUserVO sysUserVO=SessionUtils.currentSession();
		jqGridParamModel.put("within_code", "eq", sysUserVO.getWithin_code());
		customSearchFilters = MyUtils.decode(customSearchFilters);
		String filters = JqGridSearchParamHandler.processSql( customSearchFilters, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		PageVO pageVO = sysDictionaryService.getDicData(jqGridParamModel);

		return pageVO;
	}

	@Log("数据字典管理-保存或修改字典明细")
	@RequestMapping(value = "/saveDicDetail", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO saveDicDetail(@RequestBody List<DictionaryVO> gridAllData,HttpServletRequest request,HttpServletResponse response){
		sysDictionaryService.insertDicData(gridAllData, request, response);
		return ResultVO.successResult("保存成功!");

	}

	@Log("数据字典管理-删除字典明细")
	@RequestMapping(value = "/deleteDicData", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO deleteDicData(Integer[] dicDataServerArray){
		ResultVO resultVO = ResultVO.successResult();
		sysDictionaryService.deleteDicData(dicDataServerArray);

		return resultVO;
	}

	@Log("数据字典管理-删除字典菜单")
	@RequestMapping(value = "/deleteZdDic", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO deleteZdDic(Integer[] array, String typeCode){
		if(StringUtils.isBlank(typeCode)) {
			return ResultVO.failResult("目录名称不能为空");
		}
		SysUserVO sysUserVO = SessionUtils.currentSession();
		sysDictionaryService.deleteDic(array);
		List<SysDictionaryDataVO> sysDictionaryDataVOList = new ArrayList<SysDictionaryDataVO>();
		SysDictionaryDataVO paramVO = new SysDictionaryDataVO();
		paramVO.setWithin_code(sysUserVO.getWithin_code());
		paramVO.setTypeCode(typeCode);
		sysDictionaryDataVOList.add(paramVO);
		sysDictionaryDataService.batchDelete(sysDictionaryDataVOList);
		ResultVO resultVO = ResultVO.successResult();
		return resultVO;
	}

	@Log("数据字典管理-获取字典菜单明细")
	@RequestMapping(value = "/getCusDictionary", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO getCusDictionary(String sn){
		ResultVO resultVO = ResultVO.successResult();
		SysDictionaryVO vo = sysDictionaryService.getCusDictionary(Integer.valueOf(sn));
		resultVO.setResultDataFull(vo);
		return resultVO;
	}

	@Log("数据字典管理-修改字典菜单")
	@RequestMapping(value = "/updateDic", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO updateDic(Integer sn,Integer parentsn,String typename,String typecode,String remark)
	{

		SysDictionaryVO sysVo=new SysDictionaryVO();
		sysVo.setSn(sn);
		sysVo.setParentSn(parentsn);
		sysVo.setTypeName(MyUtils.urlDecode(typename));
		sysVo.setTypeCode(MyUtils.urlDecode(typecode));
		sysVo.setRemark(MyUtils.urlDecode(remark));
		if(null==sysVo.getTypeName())
			sysVo.setTypeName("");
		if(null==sysVo.getTypeCode())
			sysVo.setTypeCode("");
		if(null==sysVo.getRemark())
			sysVo.setRemark("");

		sysDictionaryService.updateDic(sysVo);
		return ResultVO.successResult("修改成功!");
	}

	@Log("数据字典管理-新增字典菜单")
	@RequestMapping(value = "/addDic", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO addDic(Integer sn,Integer parentsn,String typename,String typecode,String remark,HttpServletRequest request,HttpServletResponse response){
		SysUserVO sysUserVO= (SysUserVO)request.getSession().getAttribute(SessionUtils.SESSION_KEY);
		SysDictionaryVO sysVo=new SysDictionaryVO();
		sysVo.setSn(sn);
		sysVo.setParentSn(parentsn);
		sysVo.setWithin_code(sysUserVO.getWithin_code());
		sysVo.setWhcenter(sysUserVO.getWhcenter());
		sysVo.setTypeName(MyUtils.urlDecode(typename));
		sysVo.setTypeCode(MyUtils.urlDecode(typecode));
		sysVo.setRemark(MyUtils.urlDecode(remark));
		if(null==sysVo.getTypeName())
			sysVo.setTypeName("");
		if(null==sysVo.getTypeCode())
			sysVo.setTypeCode("");
		if(null==sysVo.getRemark())
			sysVo.setRemark("");

		sysDictionaryService.addDic(sysVo);
		return ResultVO.successResult("新增成功");
	}

}
