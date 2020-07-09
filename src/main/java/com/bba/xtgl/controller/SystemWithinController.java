package com.bba.xtgl.controller;


import com.bba.common.interceptor.Log;
import com.bba.common.vo.ResultVO;
import com.bba.util.DateUtils;
import com.bba.util.SessionUtils;
import com.bba.xtgl.service.api.ISysWithinService;
import com.bba.xtgl.vo.SysUserVO;
import com.bba.xtgl.vo.SysWithinVO;
import com.bba.xtgl.vo.Wsys_SetVO;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/sysInfo/within")
public class SystemWithinController {

	@Autowired
	private ISysWithinService sysWithinService;

 

	@RequestMapping("/getDetail")
	public ResultVO getListForGrid(HttpSession session) {
		SysUserVO sysUserVO=SessionUtils.currentSession();
		String withinCode = sysUserVO.getWithin_code();
		SysWithinVO sysWithinVO = sysWithinService.getDetailBySessionWithinCode(withinCode);
		ResultVO resultVO = new ResultVO();
		if (sysWithinVO != null) {
			resultVO.setResultCode("0");
			resultVO.setResultDataFull(sysWithinVO);
		} else {
			resultVO = ResultVO.failResult("未查询到公司明细");
		}
		return resultVO;
	}
	
	@Log(saveOrUpdate="sn,新增内码,修改内码")
	@RequestMapping(value = "/saveDetail", method = RequestMethod.POST)
	public ResultVO saveDetail(@RequestBody Map<String,Object> Wsys_WithinSet, HttpServletRequest request,HttpSession session) {
		SysUserVO sysUserVO=SessionUtils.currentSession();
		Gson gson=new Gson();
		SysWithinVO sysWithinVO = gson.fromJson(Wsys_WithinSet.get("sysWithinVO").toString(), SysWithinVO.class);
		Wsys_SetVO wsys_SetVO = gson.fromJson(Wsys_WithinSet.get("Wsys_setVO").toString(), Wsys_SetVO.class);
		wsys_SetVO.setWithin_code(sysUserVO.getWithin_code());
		wsys_SetVO.setWhcenter(sysUserVO.getWhcenter());
		if(org.apache.commons.lang.StringUtils.isBlank(sysWithinVO.getName())){
			return ResultVO.failResult("参数值:name不能为空");
		}
		
		//SysUserVO sysUserVO = SessionUtils.currentSession();
		
		sysWithinVO.setCode(sysUserVO.getWithin_code());
		sysWithinVO.setCreate_by(sysUserVO.getRealName());
		sysWithinVO.setUpdate_by(sysUserVO.getRealName());
		sysWithinVO.setUpdate_date(DateUtils.nowDate());
		ResultVO resultVO = sysWithinService.updateWithin(sysWithinVO, wsys_SetVO,request);
		return resultVO;
	}


}