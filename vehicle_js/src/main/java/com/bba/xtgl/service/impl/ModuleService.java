package com.bba.xtgl.service.impl;

import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.util.SessionUtils;
import com.bba.util.StringUtils;
import com.bba.xtgl.dao.IModuleDao;
import com.bba.xtgl.dao.ISys_RolebuttonsDao;
import com.bba.xtgl.service.api.IModuleService;
import com.bba.xtgl.vo.ModuleVO;
import com.bba.xtgl.vo.SysUserVO;
import com.bba.xtgl.vo.Sys_RolebuttonsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ModuleService implements IModuleService {
	@Resource
	private IModuleDao moduleDao;

	@Autowired
	private ISys_RolebuttonsDao sys_rolebuttonsDao;
	
	@Override
	public PageVO getListForGrid(JqGridParamModel jqGridParamModel,String customSearchFilters) {
		String filters = JqGridSearchParamHandler.processSql(
				customSearchFilters, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		List<ModuleVO> list = moduleDao.getListForGrid(jqGridParamModel);
		int records = moduleDao.getListForGridCount(jqGridParamModel);
		return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,records);
	}
	
	
	@Override
	public List<ModuleVO> findModuleList(ModuleVO moduleVO,HttpServletRequest request,HttpServletResponse response) {
		List<ModuleVO> moduleFatherList=moduleDao.findModuleList(moduleVO);
		List<ModuleVO> moduleAllList=findModuleAllList(moduleFatherList,request,response);
		return moduleAllList;
	}
	/**
	 * 获取子菜单
	 * @param moduleFatherList
	 * @return
	 */
	private List<ModuleVO> findModuleAllList(List<ModuleVO> moduleFatherList,HttpServletRequest request,HttpServletResponse response){
		List<ModuleVO> moduleChildList=new ArrayList<ModuleVO>();
		SysUserVO sysUserVO=SessionUtils.currentSession();
		for (ModuleVO moduleVO2 : moduleFatherList) {
			moduleVO2.setWithin_code(sysUserVO.getWithin_code());
			List<ModuleVO> moduleChild=moduleDao.findNextModuleList(moduleVO2);
			boolean check=false;
			for (ModuleVO moduleVO : moduleChild) {
				if(moduleVO.getModuleurl()!=null&&!"".equals(moduleVO.getModuleurl())){
					HttpSession session=request.getSession();
					moduleVO.setUserCode((String) session.getAttribute("userId"));
					List<ModuleVO> moduleRoleList=moduleDao.findRoleModuleList(moduleVO);
					if(moduleRoleList!=null&&moduleRoleList.size()>0){
						moduleChildList.addAll(moduleRoleList);
						if(!check){
							moduleChildList.add(moduleVO2);
							check=true;
						}
					}
				}else{
					List<ModuleVO> mouleList=new ArrayList<ModuleVO>();
					mouleList.add(moduleVO);
					moduleChildList.addAll(findModuleAllList(mouleList,request,response));
				}
			}
		}
		return moduleChildList;
	}
	@Override
	public void insertModule(ModuleVO moduleVO) {
		SysUserVO sysUserVO =(SysUserVO)com.bba.util.SessionUtils.currentSession();
		moduleVO.setWithin_code(sysUserVO.getWithin_code());
		if(moduleVO.getModulefathername()==null||"".equals(moduleVO.getModulefathername())){
			moduleVO.setModulefathername("根模块");
		}
		if(moduleVO.getOrderid()==null||"".equals(moduleVO.getOrderid())){
			moduleVO.setOrderid(0+"");
		}
		if(moduleVO.getIco()==null||"".equals(moduleVO.getIco())){
			moduleVO.setIco("");
		}
		moduleDao.insertModule(moduleVO);
		
	}
	@Override
	public ModuleVO findModuleVO(ModuleVO moduleVO) {
		return moduleDao.findModuleVO(moduleVO).get(0);
	}
	@Override
	public void updateModule(ModuleVO moduleVO) {
		if(moduleVO.getModulefathername()==null||"".equals(moduleVO.getModulefathername())){
			moduleVO.setModulefathername("根模块");
		}
		if(moduleVO.getOrderid()==null||"".equals(moduleVO.getOrderid())){
			moduleVO.setOrderid(0+"");
		}
		if(moduleVO.getIco()==null||"".equals(moduleVO.getIco())){
			moduleVO.setIco("");
		}
		moduleDao.updateModule(moduleVO);
		
	}
	
	@Override
	public void orderModule(List<ModuleVO> list) {
		for(int i = 0; i < list.size(); i++){
			list.get(i).setOrderid(String.valueOf(i));
		}
		moduleDao.batchUpdate(list);
	}
	@Override
	public void deleteModule(List<ModuleVO> list) {
		SysUserVO sysUserVO  = SessionUtils.currentSession();
		moduleDao.deleteModule(list);
		moduleDao.deleteSysAuthorith(list, sysUserVO.getWithin_code());
		moduleDao.deleteSysLanguage(list, sysUserVO.getWithin_code());
		moduleDao.deleteSysRoleButtons(list, sysUserVO.getWithin_code());
	}
	
	@Override
	public List<ModuleVO> findModuleAll(ModuleVO moduleVO) {
		SysUserVO sysUserVO = SessionUtils.currentSession();
		moduleVO.setWithin_code(sysUserVO.getWithin_code());
		return moduleDao.findModuleAll(moduleVO);
	}
	@Override
	public List<ModuleVO> findModuleLeftList(ModuleVO moduleVO) {
		return moduleDao.findNextModuleList(moduleVO);
	}
	
	
	@Override
	public ResultVO findModuleByUserId(SysUserVO sysUserVO) {
		String userId=sysUserVO.getUserId();
		String within_code=sysUserVO.getWithin_code();
		List<ModuleVO> moduleVOList = null;
		if(sysUserVO.getUserId().equals("admin")){
			moduleVOList = moduleDao.findModuleByAdminId(within_code,userId);
		}else{
			moduleVOList = moduleDao.findModuleByUserId(within_code,userId);
		}
		List<Sys_RolebuttonsVO> sys_rolebuttonsVOList =
				sys_rolebuttonsDao.findListByUser(sysUserVO.getUserId());


		Map<String, List<Sys_RolebuttonsVO>> sys_rolebuttonsVOMap = new HashMap<String, List<Sys_RolebuttonsVO>>();
		for(Sys_RolebuttonsVO vo: sys_rolebuttonsVOList){
			if(StringUtils.isNotBlank(vo.getModuleurl())){
				String newurl = vo.getModuleurl().replaceAll("/","");
				if(newurl.indexOf(".")!=-1) {
					newurl = newurl.substring(0, newurl.lastIndexOf("."));
				}
				vo.setModuleurl(newurl);
			}else{
				continue;
			}
			List<Sys_RolebuttonsVO> list = sys_rolebuttonsVOMap.get(vo.getModuleurl());
			if(list == null){
				list = new ArrayList<Sys_RolebuttonsVO>();
				sys_rolebuttonsVOMap.put(vo.getModuleurl(), list);
			}
			list.add(vo);
		}

		//默认都加入一个关闭按钮
		for(String key: sys_rolebuttonsVOMap.keySet()){
			List<Sys_RolebuttonsVO> dataList = sys_rolebuttonsVOMap.get(key);

			Sys_RolebuttonsVO firstSys_RolebuttonsVO = dataList.get(dataList.size() - 1);
			//不包含关闭按钮就添加一个默认的
			if(!"icon-cancel".equals(firstSys_RolebuttonsVO.getButtonicon())){

				Sys_RolebuttonsVO vo = new Sys_RolebuttonsVO();
				vo.setButtonname("关闭");
				vo.setButtonname_en("close");
				vo.setButtonuse("close");
				vo.setButtonicon("icon-cancel");
				dataList.add(vo);
			}
		}

		ResultVO resultVO = ResultVO.successResult();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("moduleVOList", moduleVOList);
		resultMap.put("sys_rolebuttonsVOMap", sys_rolebuttonsVOMap);
		resultVO.setResultDataFull(resultMap);
		return resultVO;
	}
	
	@Override
	public Integer findModuleByUserId_Count(SysUserVO sysUserVO,String moduleurl) {
		String userId=sysUserVO.getUserId();
		String within_code=sysUserVO.getWithin_code();
		if(userId.equals("admin"))
			return 1;
		else
			return moduleDao.findModuleByUserId_Count(within_code,userId,moduleurl.replace("'", ""));
	}
	
}
