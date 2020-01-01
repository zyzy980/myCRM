package com.bba.xtgl.service.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bba.common.vo.PageVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysLanguageVo;

public interface ISysLanguageService
{
 	public PageVO getListForGrid(JqGridParamModel jqGridParamModel,String customSearchFilters);

 	public void dellanguage(List<SysLanguageVo> list);

 	public void savelanguage(List<SysLanguageVo> list);
 	public List<SysLanguageVo> GetSysLanguage();
 	public List<SysLanguageVo> GetSysLanguage(HttpServletRequest request,HttpServletResponse response);
 	public List<SysLanguageVo> GetSysLanguage(HttpServletRequest request, HttpServletResponse response,String url);
 	public SysLanguageVo GetSysLanguage(HttpServletRequest request,HttpServletResponse response,String url,String code);
	
}
