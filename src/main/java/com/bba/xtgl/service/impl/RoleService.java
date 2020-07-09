package com.bba.xtgl.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bba.common.vo.PageVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.util.SessionUtils;
import com.bba.xtgl.dao.IRoleDao;
import com.bba.xtgl.service.api.IRoleService;
import com.bba.xtgl.vo.RoleVO;
import com.bba.xtgl.vo.SysUserVO;
import com.bba.common.vo.ResultVO;

@Service
@Transactional
public class RoleService implements IRoleService {
	@Resource
	private IRoleDao roleDao;

	@Override
	public List<RoleVO> findRoleButtonList(RoleVO roleVO) {
		return roleDao.findRoleButtonList(roleVO);
	}

	@Override
	public PageVO getListForGrid(JqGridParamModel jqGridParamModel,
			String customSearchFilters) {
		String filters = JqGridSearchParamHandler.processSql(
				customSearchFilters, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		List<RoleVO> list = roleDao.getListForGrid(jqGridParamModel);
		int records = roleDao.getListForGridCount(jqGridParamModel);
		return new PageVO(jqGridParamModel.getPage(),
				jqGridParamModel.getRows(), list, records);
	}

	@Override
	public void insertRole(List<RoleVO> rolelist, HttpServletRequest request,HttpServletResponse response) {
		SysUserVO sysUserVO = (SysUserVO) request.getSession().getAttribute("sysUserVO");
		List<RoleVO> roleAdd = new ArrayList<RoleVO>();
		List<RoleVO> roleUpdate = new ArrayList<RoleVO>();
		for (RoleVO roleVO : rolelist) {
			if ("add".equals(roleVO.getOperateType())) {
				roleVO.setCreateby(sysUserVO.getRealName());
				roleAdd.add(roleVO);
			}else{
				roleVO.setUpdateby(sysUserVO.getRealName());
				roleUpdate.add(roleVO);
			}
			roleVO.setWithin_code(sysUserVO.getWithin_code());
			roleVO.setCreateby(sysUserVO.getUserId());
			roleVO.setUpdateby(sysUserVO.getUserId());
		}
		if (roleAdd.size() > 0) {
			roleDao.insertRole(roleAdd);
		}
		if (roleUpdate.size() > 0) {
			roleDao.updateRole(roleUpdate);
		}
	}

	@Override
	public void deleteRole(List<RoleVO> list) {
		SysUserVO sysUserVO = SessionUtils.currentSession();
		roleDao.deleteRole(list, sysUserVO.getWithin_code());
		roleDao.deleteAuthorith(list, sysUserVO.getWithin_code());
		roleDao.deleteSysRole(list, sysUserVO.getWithin_code());
	}

	
	
	//系统权限 - 开始
	/*
	 * 获取权限
	 * */
	@Override
	public List<RoleVO> GetRole(HttpServletRequest request,HttpServletResponse response)
	{
		//select * from sys_module where moduleurl='View/SysInfo/SysModuleManage.html'
	    //String ContextPath=request.getContextPath(); //项目名称     /BBA
		String ServletPath=request.getServletPath(); //请求页面或其他地址    /View/SysInfo/SysModuleManage.html
		SysUserVO sysUserVO=(SysUserVO)request.getSession().getAttribute(SessionUtils.SESSION_KEY);
		if(null==sysUserVO)
		{
			return null;
		}
		if(null!=ServletPath && ServletPath.substring(0, 1).equals("/"))
		{
			ServletPath=ServletPath.substring(1);
		}
		List<RoleVO> list=null;
		//admin不受权限控制  && sysUserVO.getWithin_code().equals("TMS")
		/*if(sysUserVO.getUserId().equals("admin"))
		{*/
			list=roleDao.GetAdminRole(sysUserVO.getWithin_code(),ServletPath);
		/*}
		else
		{
			list=roleDao.GetUserRole(sysUserVO.getWithin_code(),ServletPath,sysUserVO.getUserId());
		}*/
		return list;
	}
	
	//系统权限 - 结束
	
	
	
	
	@Override
	public ResultVO copyRole(String roleid_src,String roleid_to,SysUserVO sysUserVO)
	{
		if(null==roleid_src || roleid_src.equals(""))
		{
			return ResultVO.failResult("没有选择复制权限来源");
		}
		if(null==roleid_to || roleid_to.equals(""))
		{
			return ResultVO.failResult("没有选择复制权限拥有");
		}
		String within_code=sysUserVO.getWithin_code();
		String[] roleid_src_Array=roleid_src.split(",");
		String[] roleid_to_Array=roleid_to.split(",");
		
		String sql="";
		sql="delete sys_authorith where within_code='"+within_code+"' and roleId in('"+roleid_src.replace(",", "','")+"')";
		roleDao.save(sql);
		for(int i=0,ilen=roleid_src_Array.length;i<ilen;i++)
		{
			for(int r=0,rlen=roleid_to_Array.length;r<rlen;r++)
			{
				sql="insert into sys_authorith(roleId,moduleid,buttonUse,within_code) select '"+roleid_src_Array[i]+"',moduleid,buttonUse,within_code from sys_authorith where within_code='"+within_code+"' and roleId='"+roleid_to_Array[r]+"'";
				roleDao.save(sql);
			}
		}
		
		return ResultVO.successResult("复制权限成功");
	}
}
