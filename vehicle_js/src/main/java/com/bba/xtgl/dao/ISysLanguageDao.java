package com.bba.xtgl.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysLanguageVo;
 

public interface ISysLanguageDao
{
	public List<SysLanguageVo> getListForGrid(JqGridParamModel jqGridParamModel);
	public int getListForGridCount(JqGridParamModel jqGridParamModel);
	
	
	public void dellanguage(@Param(value="list")List<SysLanguageVo> list);
	
	public void dellanguageByModuleid(@Param(value="within_cde")String within_cde,@Param(value="moduleid")String moduleid);
	
	public void updatelanguage(List<SysLanguageVo> list);
	public void savelanguage(List<SysLanguageVo> list);
	
	public List<SysLanguageVo> GetSysLanguage();
	
	/**
	 * moduleurl=表sys_module
	 * */
	public List<SysLanguageVo> GetSysLanguage_PageAll(@Param(value="moduleurl")String moduleurl, @Param("withinCode")String wtihinCode);
	public SysLanguageVo GetSysLanguage_PageOne(@Param(value="moduleurl")String moduleurl,@Param(value="code")String code);
	List<SysLanguageVo> getLanguageByParamas(List<SysLanguageVo> VOS);
	void insertSysLanguage(SysLanguageVo VO);

    List<Map<String, Object>> findAllList(String moduleurl);
}
