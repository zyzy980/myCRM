package com.bba.xtgl.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.bba.util.SessionUtils;
import com.bba.xtgl.service.api.ISysTaskFunctionService;
import com.bba.xtgl.vo.SysTaskRoleVO;
import com.bba.xtgl.vo.SysUserVO;

@Controller
@RequestMapping("/sysInfo/taskFunction")
public class SysTaskFunctionController {
	
	@Autowired
	private ISysTaskFunctionService sysTaskFunctionService;

	@Log("查询角色任务清单")
	@RequestMapping(value="/getListForGrid", method ={RequestMethod.POST})
	@ResponseBody
	public PageVO getListForGrid(JqGridParamModel jqGridParamModel,String role_id){
		PageVO pageVO = sysTaskFunctionService.findListForRoleId(jqGridParamModel,role_id);
		return pageVO;
	}
	
	@Log("更新角色任务")
	@RequestMapping(value="/updateTaskRole", method ={RequestMethod.POST})
	@ResponseBody
	public  ResultVO  updateButton( @RequestBody List<HashMap<String, Object>>  taskRoleList,HttpServletRequest request,HttpServletResponse response){
 
		List<SysTaskRoleVO> taskRoleList_1=new ArrayList<SysTaskRoleVO>();
		for (Iterator iterator = taskRoleList.iterator(); iterator.hasNext();){
			SysTaskRoleVO taskRoleVO = new SysTaskRoleVO();
			Map buttonVO1 = (Map)iterator.next();
			taskRoleVO.setRole_id(buttonVO1.get("role_id").toString());
			taskRoleVO.setFunction_id(buttonVO1.get("function_id").toString());
			taskRoleVO.setFunction_name(buttonVO1.get("function_name").toString());
			taskRoleVO.setIsExist(Boolean.parseBoolean(buttonVO1.get("isExist").toString()));
			taskRoleList_1.add(taskRoleVO);
		}
		sysTaskFunctionService.updateTaskRole(taskRoleList_1);
		return ResultVO.successResult("保存成功");
	}
	@Log("查询主页任务清单")
	@RequestMapping(value="/findHomeDataList", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO findHomeDataList(HttpServletRequest request,HttpServletResponse response){
		SysUserVO sysUserVO = (SysUserVO)request.getSession().getAttribute(SessionUtils.SESSION_KEY);
		List<Map<String,Object>> taskRoleList = sysTaskFunctionService.findHomeTaskRoleList(sysUserVO.getUserId());
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("taskRoleList" , taskRoleList);

		ResultVO resultVO = ResultVO.successResult();
		resultVO.setResultDataFull(param);
		return resultVO;
	}
	
}