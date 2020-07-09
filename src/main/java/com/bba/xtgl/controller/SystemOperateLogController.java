package com.bba.xtgl.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.SessionUtils;
import com.bba.xtgl.service.api.ISysOperateLogService;
import com.bba.xtgl.vo.SysOperateLogVO;
import com.bba.xtgl.vo.SysUserVO;

@Controller
@RequestMapping("/sysInfo/operateLog")
public class SystemOperateLogController {
	@Autowired
	private ISysOperateLogService iSysOperateLogService;

	// @Log("日志管理清单")
	@RequestMapping(value = "/getListForGrid", method = RequestMethod.POST)
	public @ResponseBody PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters,
			HttpSession session) {
		// customSearchFilters = MyUtils.decode(customSearchFilters); // 防止中文乱码
		SysUserVO sysUserVO = SessionUtils.currentSession();
		jqGridParamModel.put("within_code", "eq", sysUserVO.getWithin_code());
		jqGridParamModel.put("yw_location", "eq", sysUserVO.getWhcenter());
		PageVO pageVO = iSysOperateLogService.getListForGrid(jqGridParamModel, customSearchFilters);
		return pageVO;
	}

	// @Log("查看日志信息详情")
	@RequestMapping(value = "/getDetail", method = RequestMethod.POST)
	public @ResponseBody ResultVO getDetail(String sn) {
		SysUserVO sysUserVO = SessionUtils.currentSession();
		SysOperateLogVO vo = new SysOperateLogVO();
		vo.setSn(sn);
		vo.setWithinCode(sysUserVO.getWithin_code());
		vo.setYwLocation(sysUserVO.getWhcenter());
		vo = iSysOperateLogService.getDetail(vo);
		ResultVO resultVO = new ResultVO();
		resultVO.setResultCode("0");
		resultVO.setResultDataFull(vo);
		return resultVO;
	}

}
