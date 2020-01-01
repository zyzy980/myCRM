package com.bba.xtgl.controller;

import com.bba.common.vo.ResultVO;
import com.bba.xtgl.service.api.IPngService;
import com.bba.xtgl.vo.ImageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/imageInfo/image")
public class ImageController {
	@Autowired
	IPngService pngService;
	
	/**
	 * 获取session
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getImage")
	public @ResponseBody ResultVO getImage(@ModelAttribute(value = "imageVO")ImageVO imageVO ,HttpServletRequest request,HttpServletResponse response){
		ResultVO resultVO = ResultVO.successResult();
		resultVO.setResultDataFull(pngService.findImage(imageVO));
		return resultVO;
	}
}
