package com.bba.xtgl.controller;

import com.bba.common.vo.ResultVO;
import com.bba.xtgl.service.api.IWsys_SetService;
import com.bba.xtgl.vo.Wsys_SetVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/sysInfo/wsys_set")
public class Wsys_SetController {
	@Autowired
	private IWsys_SetService wsys_SetService;
	
	@RequestMapping("/getDetail")
	@ResponseBody
	public ResultVO getListForGrid(Wsys_SetVO vo,HttpSession session) {
		vo.setWithin_code((String) session.getAttribute("within_code"));
		vo.setWhcenter((String) session.getAttribute("whcenter"));
		Wsys_SetVO wsys_SetVO = wsys_SetService.getByWithinWhcenter(vo);
		ResultVO resultVO = new ResultVO();
		if (wsys_SetVO != null) {
			resultVO.setResultCode("0");
			resultVO.setResultDataFull(wsys_SetVO);
		} else {
			resultVO = ResultVO.failResult("未查询到公司系统设置明细");
		}
		return resultVO;
	}
}
