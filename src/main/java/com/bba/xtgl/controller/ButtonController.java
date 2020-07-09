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
import com.bba.util.MyUtils;
import com.bba.util.SessionUtils;
import com.bba.xtgl.service.api.IButtonService;
import com.bba.xtgl.vo.ButtonVO;
import com.bba.xtgl.vo.SysUserVO;

@Controller
@RequestMapping("/admin/button")
public class ButtonController {
	@Autowired
	private IButtonService buttonService;

	@Log("按钮管理-查询角色按钮")
	@RequestMapping(value = "/getButtonList", method = { RequestMethod.POST })
	@ResponseBody
	public ResultVO getButtonList(ButtonVO buttonVO,HttpServletRequest request, HttpServletResponse response) {
		SysUserVO sysUserVO=SessionUtils.currentSession();
		buttonVO.setWithin_code(sysUserVO.getWithin_code());
		List<ButtonVO> list = buttonService.findButtonByModuleId(buttonVO);
		ResultVO resultVO = ResultVO.successResult();
		resultVO.setResultDataFull(list);
		return resultVO;
	}

	@Log("按钮管理-查询按钮管理")
	@RequestMapping(value = "/findButtonListById", method = RequestMethod.POST)
	public @ResponseBody PageVO findButtonListById(JqGridParamModel jqGridParamModel, String customSearchFilters) {
		SysUserVO sysUserVO=SessionUtils.currentSession();
		jqGridParamModel.put("within_code", "eq", sysUserVO.getWithin_code());
		customSearchFilters = MyUtils.decode(customSearchFilters); // 防止中文乱码
		PageVO pageVO = buttonService.getListForGrid(jqGridParamModel,customSearchFilters);
		return pageVO;
	}

	@Log("按钮管理-更新角色按钮")
	@RequestMapping(value = "/updateButton", method = { RequestMethod.POST })
	@ResponseBody
	public ResultVO updateButton(
			@RequestBody List<HashMap<String, Object>> buttonList,
			HttpServletRequest request, HttpServletResponse response) {
		if (buttonList.isEmpty()) {
			return ResultVO.failResult("没有数据保存");
		}
		ResultVO resultVO = new ResultVO();
		String msg = "";
		List<ButtonVO> buttonList_1 = new ArrayList<ButtonVO>();
		for (Iterator iterator = buttonList.iterator(); iterator.hasNext();) {
			ButtonVO buttonVO = new ButtonVO();
			Map buttonVO1 = (Map) iterator.next();
			buttonVO.setRoleId(buttonVO1.get("roleId").toString());
			buttonVO.setModuleid(buttonVO1.get("moduleid").toString());
			buttonVO.setButtonUse(buttonVO1.get("buttonUse").toString());
			buttonVO.setIsOwnAuthorith(Boolean.parseBoolean(buttonVO1.get(
					"isOwnAuthorith").toString()));
			buttonList_1.add(buttonVO);
		}
		buttonService.updateButton(buttonList_1);
		return ResultVO.successResult("保存成功");
	}

	@Log("按钮管理-更新按钮管理")
	@RequestMapping(value = "/updatButton", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public ResultVO updatButton(@RequestBody List<HashMap<String, Object>> buttonList,HttpServletRequest request, HttpServletResponse response) {
		List<ButtonVO> buttonList_1 = new ArrayList<ButtonVO>();
		for (Iterator iterator = buttonList.iterator(); iterator.hasNext();) {
			ButtonVO buttonVO = new ButtonVO();
			Map buttonVO1 = (Map) iterator.next();
			buttonVO.setButtonName(buttonVO1.get("buttonName").toString());
			buttonVO.setButtonName_en(buttonVO1.get("buttonName_en").toString());
			buttonVO.setModuleid(buttonVO1.get("moduleid").toString());
			buttonVO.setButtonId(buttonVO1.get("buttonId").toString());
			buttonVO.setButtonSdescription(buttonVO1.get("buttonSdescription").toString());
			buttonVO.setButtonUse(buttonVO1.get("buttonUse").toString());
			buttonVO.setButtonIcon(buttonVO1.get("buttonIcon").toString());
			buttonVO.setButtonOrder(buttonVO1.get("buttonOrder").toString());
			buttonVO.setOperateType(buttonVO1.get("operateType").toString());
			buttonList_1.add(buttonVO);
		}
		buttonService.updateButtonMsg(buttonList_1);
		buttonList=null;
		return ResultVO.successResult("保存成功");
	}

	@Log("按钮管理-删除按钮管理")
	@RequestMapping(value = "/deleteButton", method = { RequestMethod.POST })
	@ResponseBody
	public ResultVO deleteButton(@RequestBody List<ButtonVO> buttonList,
			HttpServletRequest request, HttpServletResponse response) {
		buttonService.deleteButton(buttonList);
		return ResultVO.successResult("删除成功");
	}
}
