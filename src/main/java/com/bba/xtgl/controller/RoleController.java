package com.bba.xtgl.controller;

import java.util.ArrayList;
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
import com.bba.xtgl.service.api.IRoleService;
import com.bba.xtgl.vo.RoleVO;
import com.bba.xtgl.vo.SysUserVO;

@Controller
@RequestMapping("/admin/role")
public class RoleController {
	@Autowired
	private IRoleService roleService;

	@Log(value = "角色管理-查询角色管理")
	@RequestMapping(value = "/getListForGridById", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody PageVO getListForGridById(JqGridParamModel jqGridParamModel, String customSearchFilters) {
		SysUserVO sysUserVO=SessionUtils.currentSession();
		jqGridParamModel.put("within_code", "eq", sysUserVO.getWithin_code());
		customSearchFilters = MyUtils.decode(customSearchFilters);
		PageVO pageVO = roleService.getListForGrid(jqGridParamModel,customSearchFilters);
		return pageVO;
	}

	@Log(value = "角色管理-保存数据")
	@RequestMapping(value = "/insertRole", method = { RequestMethod.POST })
	public @ResponseBody ResultVO insertRole(@RequestBody List<Map<String, Object>> roleMaplist,HttpServletRequest request, HttpServletResponse response)
	{
		SysUserVO sysUserVO = (SysUserVO) request.getSession().getAttribute(SessionUtils.SESSION_KEY);
		List<RoleVO> roleList = new ArrayList<RoleVO>();
		for (Iterator<Map<String, Object>> iterator = roleMaplist.iterator(); iterator.hasNext();) {
			RoleVO role = new RoleVO();
			Map<String, Object> map = iterator.next();
			role.setRoleid(map.get("roleid").toString());
			role.setRolename(map.get("rolename").toString());
			role.setRoledescription(map.get("roledescription").toString());
			role.setOperateType(map.get("operateType").toString());
			role.setWithin_code(sysUserVO.getWithin_code());
			roleList.add(role);
		}
		roleService.insertRole(roleList, request, response);
		return ResultVO.successResult("保存成功");
	}

	@Log(value = "角色管理-删除")
	@RequestMapping(value = "/deleteRole", method = { RequestMethod.POST })
	public @ResponseBody ResultVO deleteRole(
			@RequestBody List<RoleVO> rolelist, HttpServletRequest request,
			HttpServletResponse response) {
		
		
		roleService.deleteRole(rolelist);
		return ResultVO.successResult("删除成功");
	}
	
	
	
	//@Log(value = "角色管理-复制权限")
	@RequestMapping(value = "/copyRole", method = { RequestMethod.POST ,RequestMethod.GET })
	public @ResponseBody ResultVO copyRole(String roleid_src,String roleid_to, HttpServletRequest request,HttpServletResponse response) {
		
		SysUserVO sysUserVO =  SessionUtils.currentSession();
		return roleService.copyRole(roleid_src,roleid_to,sysUserVO);
	}
	
	
}
