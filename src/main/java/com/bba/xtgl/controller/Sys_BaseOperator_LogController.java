package com.bba.xtgl.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bba.common.interceptor.Log;
import com.bba.common.interceptor.Sys_BaseOperator_LogVO;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.MyUtils;
import com.bba.util.SessionUtils;
import com.bba.xtgl.service.api.ISys_BaseOperator_LogService;
import com.bba.xtgl.vo.SysUserVO;

@Controller
@RequestMapping("/system/Sys_BaseOperator_LogController")
public class Sys_BaseOperator_LogController
{
	@Autowired
	private ISys_BaseOperator_LogService sys_BaseOperator_LogService;
	
	@Log("基础数据修改日志-查询")
	@RequestMapping(value = "/getListForGrid", method = RequestMethod.POST)
	public @ResponseBody PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters,HttpSession session) {
		customSearchFilters = MyUtils.decode(customSearchFilters); // 防止中文乱码
		//SysUserVO sysUserVO=SessionUtils.currentSession();
//		jqGridParamModel.put("within_code", "eq", sysUserVO.getWithin_code());
		PageVO pageVO = sys_BaseOperator_LogService.getListForGrid(jqGridParamModel,customSearchFilters);
		return pageVO;
	}
	
	@RequestMapping(value = "/getDetail", method = RequestMethod.POST)
	public @ResponseBody ResultVO getDetail(String id) {
		Sys_BaseOperator_LogVO vo = new Sys_BaseOperator_LogVO();
		vo.setId(id);
		vo = sys_BaseOperator_LogService.getDetail(vo);
		ResultVO resultVO = new ResultVO();
		resultVO.setResultCode("0");
		resultVO.setResultDataFull(vo);
		return resultVO;
	}
	
	
}
