package com.bba.xtgl.controller;

import com.bba.common.vo.ResultVO;
import com.bba.xtgl.service.api.IZdYwLocationService;
import com.bba.xtgl.vo.ZdYwLocationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/baseInfo/zdYwLocation")
@Controller
public class ZdYwLocationController {

	@Autowired
	private IZdYwLocationService zdYwLocationService;

	@RequestMapping("/getList")
	@ResponseBody
	public ResultVO getList(String withinCode, String state) {
		ResultVO resultVO = new ResultVO();
		ZdYwLocationVO paramZdYwLocationVO = new ZdYwLocationVO();
//		paramZdYwLocationVO.setWithin_code(withinCode);
	//	paramZdYwLocationVO.setState(state);
		List<ZdYwLocationVO> zdYwLocationList = zdYwLocationService
				.findZdYwLocation(paramZdYwLocationVO);
		if (zdYwLocationList != null && zdYwLocationList.size() > 0) {
			resultVO.setResultCode("0");
			resultVO.setResultDataFull(zdYwLocationList);
		} else {
			resultVO = ResultVO.failResult("未查询到任何业务地点列表");
		}
		return resultVO;
	}
}