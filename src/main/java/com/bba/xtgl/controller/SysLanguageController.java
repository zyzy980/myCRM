package com.bba.xtgl.controller;

 
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bba.xtgl.dao.ISysLanguageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JSONUtils;
import com.bba.util.JqGridParamModel;
import com.bba.util.MyUtils;
import com.bba.util.SessionUtils;
import com.bba.xtgl.service.api.ISysLanguageService;
import com.bba.xtgl.vo.SysLanguageVo;
import com.bba.xtgl.vo.SysUserVO;
 
@Controller
@RequestMapping("/admin/syslanguage")
public class SysLanguageController {
	
	@Autowired
	private ISysLanguageService iSysLanguageService;

	@Autowired
	private ISysLanguageDao sysLanguageDao;

	@RequestMapping(value = "/test")
	@ResponseBody
	public List<Map<String, Object>> test(String moduleurl){
		List<Map<String, Object>> list = sysLanguageDao.findAllList(moduleurl);
		return list;
	}

	@RequestMapping(value = "/saveLocalLang", method = {RequestMethod.POST,RequestMethod.GET})
	public void saveLocalLang(String lang) 
	{
		lang = MyUtils.decode(lang);
		HttpSession session =  SessionUtils.currencyHttpServletRequest().getSession();
		session.setAttribute("lang", lang);
	}
	
	
	@Log("中英文管理-语言切换管理")
	@RequestMapping(value = "/getListForGrid", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters,HttpServletRequest request,HttpServletResponse response) 
	{
		SysUserVO sysUserVO=SessionUtils.currentSession();
		jqGridParamModel.put("within_code", "eq", sysUserVO.getWithin_code());
		customSearchFilters = MyUtils.decode(customSearchFilters); // 防止中文乱码
		PageVO pageVO = iSysLanguageService.getListForGrid(jqGridParamModel,customSearchFilters);
		return pageVO;
	}
	
	@Log("中英文管理-删除语言切换数据")
	@RequestMapping(value = "/dellanguage", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResultVO dellanguage(@RequestBody List<SysLanguageVo> list,HttpServletRequest request,HttpServletResponse response) 
	{
		iSysLanguageService.dellanguage(list);
		return ResultVO.successResult("删除成功");
	}
	
	@Log("中英文管理-保存语言切换数据")
	@RequestMapping(value = "/savelanguage", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResultVO savelanguage(String lang,HttpServletRequest request,HttpServletResponse response) 
	{
		try
		{
			lang= java.net.URLDecoder.decode(lang,"UTF-8");
		}
		catch(Exception e)
		{}
		List<SysLanguageVo> list=JSONUtils.toJSONObjectList(SysLanguageVo.class,lang);
		
		SysUserVO sysUserVO=(SysUserVO)SessionUtils.currentSession();
		String within_code=sysUserVO.getWithin_code();
		String whcenter=sysUserVO.getWhcenter();
		String userid=sysUserVO.getUserId();
		for(int i=0,ilen=list.size();i<ilen;i++)
		{
			list.get(i).setWithin_code(within_code);
			list.get(i).setWhcenter(whcenter);
			list.get(i).setCreateby(userid);
			list.get(i).setUpdateby(userid);
			if(null==list.get(i).getEn())
			{
				list.get(i).setEn("");
			}
		}
		iSysLanguageService.savelanguage(list);
		return ResultVO.successResult("保存成功");
	}
	
	 
}