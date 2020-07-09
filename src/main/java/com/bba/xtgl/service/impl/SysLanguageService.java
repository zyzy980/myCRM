package com.bba.xtgl.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bba.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bba.common.vo.PageVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.util.SessionUtils;
import com.bba.xtgl.dao.ISysLanguageDao;
import com.bba.xtgl.service.api.ISysLanguageService;
import com.bba.xtgl.vo.SysLanguageVo;
import com.bba.xtgl.vo.SysUserVO;

@Service
@Transactional
public class SysLanguageService implements ISysLanguageService
{

	@Autowired
	private ISysLanguageDao iSysLanguageDao;
	
	
	@Override
	public PageVO getListForGrid(JqGridParamModel jqGridParamModel,String customSearchFilters) 
	{
		SysUserVO sysUserVO=(SysUserVO)SessionUtils.currentSession();
		String within_code=sysUserVO.getWithin_code();
		//String whcenter=sysUserVO.getWhcenter();
		jqGridParamModel.put("within_code", within_code);
		//jqGridParamModel.put("whcenter", whcenter);
		String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		List<SysLanguageVo> list = iSysLanguageDao.getListForGrid(jqGridParamModel);
		int records = iSysLanguageDao.getListForGridCount(jqGridParamModel);
		return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,records);
	}


	@Override
	public void dellanguage(List<SysLanguageVo> list)
	{
		iSysLanguageDao.dellanguage(list);
	}


	@Override
	public void savelanguage(List<SysLanguageVo> list)
	{
		List<SysLanguageVo> insertList=new ArrayList<SysLanguageVo>();
		List<SysLanguageVo> updateList=new ArrayList<SysLanguageVo>();
		for(SysLanguageVo vo:list)
		{
			if(vo.getSn()!=0)
			{
				updateList.add(vo);
			}
			else
			{
				insertList.add(vo);
			}
		}
		if(updateList.size()>0)
			iSysLanguageDao.updatelanguage(updateList);
		if(insertList.size()>0)
			iSysLanguageDao.savelanguage(insertList);
	}

	/**
	 * 获取中英文标签标签
	 * */
	@Override
	public List<SysLanguageVo> GetSysLanguage()
	{
		return iSysLanguageDao.GetSysLanguage();
	}
	

	/**
	 * 获取中英文标签整个页面标签
	 * */
	@Override
	public List<SysLanguageVo> GetSysLanguage(HttpServletRequest request, HttpServletResponse response)
	{
		SysUserVO sysUserVO = SessionUtils.currentSession();
		//String ContextPath=request.getContextPath(); //项目名称 
		String ServletPath=request.getServletPath(); //请求页面或其他地址
		if(null!=ServletPath && ServletPath.substring(0, 1).equals("/"))
		{
			ServletPath=ServletPath.substring(1);
			if("".equals(ServletPath)){
				ServletPath = "Index/Login.html";
			}
		}
		return iSysLanguageDao.GetSysLanguage_PageAll(ServletPath, sysUserVO == null ? "" : sysUserVO.getWithin_code());
	}
	
	/**
	 * 获取中英文标签整个页面标签
	 * */
	@Override
	public List<SysLanguageVo> GetSysLanguage(HttpServletRequest request, HttpServletResponse response,String url)
	{
		SysUserVO sysUserVO = SessionUtils.currentSession();
		return iSysLanguageDao.GetSysLanguage_PageAll(url, sysUserVO == null ? "" : sysUserVO.getWithin_code());
	}

	/**
	 * 获取中英文标签 某个页面的某个标签
	 * */
	@Override
	public SysLanguageVo GetSysLanguage(HttpServletRequest request, HttpServletResponse response, String url,String code)
	{
		return iSysLanguageDao.GetSysLanguage_PageOne(url,code);
	}

}
