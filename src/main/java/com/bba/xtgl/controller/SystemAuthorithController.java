package com.bba.xtgl.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bba.common.vo.ResultVO;
import com.bba.util.SessionUtils;
import com.bba.xtgl.service.api.ISysAuthorithService;
import com.bba.xtgl.vo.SysAuthorithVO;
import com.bba.xtgl.vo.SysUserVO;

@RequestMapping("/sysInfo/authorith")
@Controller
public class SystemAuthorithController {

	@Autowired
	private ISysAuthorithService sysAuthorithService;

	@RequestMapping("/getAuthorityListById")
	@ResponseBody
	public ResultVO getAuthorityListById(int moduleId, HttpSession session) {
		SysUserVO sysUserVO = (SysUserVO)session.getAttribute(SessionUtils.SESSION_KEY);
		
		ResultVO resultVO = new ResultVO();
		try {
			List<SysAuthorithVO> sysAuthorithVOList = sysAuthorithService.getAuthorityListById(sysUserVO.getUserId(), moduleId);
			resultVO.setResultCode("0");
			resultVO.setResultDataFull(sysAuthorithVOList);
			return resultVO;
		} catch (Exception e) {
			resultVO.setResultCode("3");
			resultVO.setResultDataFull(e.getMessage())	;
			return resultVO;
		}
		
	}

	@RequestMapping("/findAuthorityListById")
	@ResponseBody
	public ResultVO findAuthorityListById(String moduleName) {
		
		SysUserVO sysUserVO = SessionUtils.currentSession();
		List<SysAuthorithVO> sysAuthorithVOList = sysAuthorithService.getAuthorityListByName(sysUserVO.getUserId(), moduleName);
		ResultVO resultVO = new ResultVO();
		resultVO.setResultCode("0");
		resultVO.setResultDataFull(sysAuthorithVOList);
		return resultVO;
	}
}

