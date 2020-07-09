package com.bba.xtgl.controller;

import com.bba.common.common.SysOperateLog;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.MyUtils;
import com.bba.util.SessionUtils;
import com.bba.xtgl.service.api.IModuleService;
import com.bba.xtgl.vo.ModuleVO;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/module")
public class ModuleController extends SysOperateLog {
	
	@Autowired
	private IModuleService moduleService;


	@Log(value = "模块管理-查询模块管理")
	@RequestMapping(value="/getListForGrid", method ={RequestMethod.POST})
	public @ResponseBody PageVO getListForGrid(JqGridParamModel jqGridParamModel,String customSearchFilters,HttpServletRequest request){
		SysUserVO sysUserVO=SessionUtils.currentSession();
		jqGridParamModel.put("within_code", "eq", sysUserVO.getWithin_code());
		customSearchFilters = MyUtils.paramDecode(customSearchFilters);
		PageVO pageVO = moduleService.getListForGrid(jqGridParamModel,customSearchFilters);
		return pageVO;
	}
	
	@Log(value = "模块管理-查询模块管理明细")
	@RequestMapping(value="/getModuleVO")
	public @ResponseBody ResultVO getModuleVO(@ModelAttribute(value = "moduleVO")ModuleVO moduleVO ,HttpServletRequest request,HttpServletResponse response){
		SysUserVO sysUserVO=SessionUtils.currentSession();
		moduleVO.setWithin_code(sysUserVO.getWithin_code());
		ResultVO resultVO=new ResultVO();
		resultVO.setResultCode("0");
		resultVO.setResultDataFull(moduleService.findModuleVO(moduleVO));
		return resultVO;
	}
	
	@Log(value = "模块管理-删除模块管理")
	@RequestMapping(value="/deleteModule", method ={RequestMethod.POST})
	public @ResponseBody  ResultVO  deleteModule(String moduleArray,HttpServletRequest request,HttpServletResponse response){
		SysUserVO sysUserVO=SessionUtils.currentSession();
		List<ModuleVO> list=new ArrayList<ModuleVO>();
		String[] moduleArray_arr=moduleArray.split(",");
		for(int i=0,ilen=moduleArray_arr.length;i<ilen;i++)
		{
			ModuleVO vo=new ModuleVO();
			vo.setWithin_code(sysUserVO.getWithin_code());
			vo.setModuleid(moduleArray_arr[i].trim());
			list.add(vo);
		}
		moduleService.deleteModule(list);
		return ResultVO.successResult("删除成功");
	}
	
	@Log(value = "模块管理-排序模块管理")
	@RequestMapping(value="/orderModule", method ={RequestMethod.POST})
	public @ResponseBody ResultVO orderModule(@RequestBody List<Map<String,Object>> moduleMapArray,HttpServletRequest request,HttpServletResponse response){
		List<ModuleVO> moduleList = new ArrayList<ModuleVO>();
		for(Iterator<Map<String,Object>> iterable = moduleMapArray.iterator();iterable.hasNext();){
			Map<String,Object> map = iterable.next();
			ModuleVO moduleVO = new ModuleVO();
			moduleVO.setModuleid(map.get("moduleid").toString());
			moduleList.add(moduleVO);
		}
		moduleService.orderModule(moduleList);
		return ResultVO.successResult("排序成功");
	}
	
	@Log(saveOrUpdate = "模块管理-moduleid,新增模块管理明细,修改模块管理明细")
	@RequestMapping(value="/insertModule", method ={RequestMethod.POST})
	public @ResponseBody ResultVO insertModule(
			String modulefatherid,String moduleid,String modulename,String modulename_en,String moduleurl,
			String moduledescription,String orderid,String isshow,String isnav,String ishomepage,String ico, String blue_ico
			, HttpServletRequest request, HttpServletResponse response){
		
		SysUserVO sysUserVO=SessionUtils.currentSession();
		ModuleVO moduleVO=new ModuleVO();
		moduleVO.setWithin_code(sysUserVO.getWithin_code());
		moduleVO.setModulefatherid(modulefatherid);
		moduleVO.setModulefathername("");
		moduleVO.setModuleid((null==moduleid)?"0":moduleid);
		moduleVO.setModulename(MyUtils.urlDecode(modulename));
		moduleVO.setModulename_en(MyUtils.urlDecode(modulename_en));
		moduleVO.setModuleurl(MyUtils.urlDecode(moduleurl));
		moduleVO.setModuledescription(MyUtils.urlDecode(moduledescription));
		moduleVO.setOrderid((null==orderid)?"0":orderid);
		moduleVO.setIsshow((null==isshow)?"0":isshow);
		moduleVO.setIsnav((null==isnav)?"0":isnav);
		moduleVO.setIshomepage((null==ishomepage)?"0":ishomepage);
		moduleVO.setIco((null==ico)?"0":ico);
		moduleVO.setBlue_ico((null==blue_ico)?"0":blue_ico);
		
		if(null==moduleVO.getModulename())
			moduleVO.setModulename("");
		if(null==moduleVO.getModulename_en())
			moduleVO.setModulename_en("");
		if(null==moduleVO.getModuleurl())
			moduleVO.setModuleurl("");
		if(null==moduleVO.getModuledescription())
			moduleVO.setModuledescription("");
		
		if(!StringUtils.isEmpty(moduleVO.getModuleid())&&!"0".equals(moduleVO.getModuleid())){
			moduleService.updateModule(moduleVO);
			return ResultVO.successResult("修改成功");
		}else{
			moduleService.insertModule(moduleVO);
			return ResultVO.successResult("新增成功");
		}
	}

	public String getBluePng(String blue_ico) {
		String[] strList = blue_ico.split("\\\\");
		String str = strList[strList.length - 1];
		str = "white_" + str;
		strList[strList.length - 1] = str;
		blue_ico = com.bba.util.StringUtils.join(strList, "\\");
		return blue_ico;
	}

	@Log(value = "模块管理-用户左菜单查询",log = false)
	@ResponseBody
	@RequestMapping(value="/getSessionModulesForMenu", method ={RequestMethod.GET})
	public ResultVO getSessionModulesForMenu(HttpServletRequest request){
		SysUserVO sysUserVO = (SysUserVO)request.getSession().getAttribute(SessionUtils.SESSION_KEY);
		ResultVO resultVO = moduleService.findModuleByUserId(sysUserVO);
		return resultVO;
	}

	@Log(value = "模块管理-查询所有")
	@RequestMapping(value="/findModuleAll")
	public @ResponseBody ResultVO findModuleAll(@ModelAttribute(value = "moduleVO")ModuleVO moduleVO ,HttpServletRequest request,HttpServletResponse response){
		ResultVO resultVO=new ResultVO();
		resultVO.setResultCode("0");
		resultVO.setResultDataFull(moduleService.findModuleAll(moduleVO));
		return resultVO;
	}
	
	@Log(value = "模块管理-查询左菜单")
	@RequestMapping(value="/findModuleLeftList")
	public @ResponseBody ResultVO findModuleLeftList(@ModelAttribute(value = "moduleVO")ModuleVO moduleVO ,HttpServletRequest request,HttpServletResponse response){
		SysUserVO sysUserVO=SessionUtils.currentSession();
		moduleVO.setWithin_code(sysUserVO.getWithin_code());
		ResultVO resultVO=new ResultVO();
		resultVO.setResultCode("0");
		resultVO.setResultDataFull(moduleService.findModuleLeftList(moduleVO));
		return resultVO;
	}
	
	@RequestMapping(value="/getModule")
	public @ResponseBody ResultVO getModule(@ModelAttribute(value = "module")ModuleVO module ,HttpServletRequest request,HttpServletResponse response){
		ResultVO resultVO=new ResultVO();
		resultVO.setResultCode("0");
		resultVO.setResultDataFull(moduleService.findModuleList(module,request,response));
		return resultVO;
	}
}