package com.bba.xtgl.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.Session;

import com.bba.util.SessionUtils;
import com.sun.mail.imap.protocol.SearchSequence;
import org.springframework.stereotype.Service;

import com.bba.common.vo.PageVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.xtgl.dao.IButtonDao;
import com.bba.xtgl.service.api.IButtonService;
import com.bba.xtgl.vo.ButtonVO;
import com.bba.xtgl.vo.SysUserVO;


@Service
public class ButtonService implements IButtonService {
	@Resource
	private IButtonDao buttonDao;
	
	@Override
	public PageVO getListForGrid(JqGridParamModel jqGridParamModel,String customSearchFilters) {
		String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		List<ButtonVO> list = buttonDao.getListForGrid(jqGridParamModel);
		int records = buttonDao.getListForGridCount(jqGridParamModel);
		return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,records);
	}
	
	@Override
	public List<ButtonVO> findButtonByPropertyList(List<ButtonVO> buttonVOList) {
		return buttonDao.findListByPropertyList(buttonVOList);
	}



	@Override
	public List<ButtonVO> findButtonByModuleId(ButtonVO buttonVO) {
		ButtonVO param = new ButtonVO();
		param.setModuleid(buttonVO.getModuleid());
		List<ButtonVO> dataVOList = this.findButtonList(buttonVO);
		param.setRoleId(buttonVO.getRoleId());
		List<ButtonVO> buttonRoleList=buttonDao.findButtonByRole(buttonVO);
		for (int i=0;i<dataVOList.size();i++) {
			for (int j=0;j<buttonRoleList.size();j++) {
				if(dataVOList.get(i).getButtonId().equals(buttonRoleList.get(j).getButtonId())){
					dataVOList.get(i).setIsOwnAuthorith(true);
				}
			}
		}
		return dataVOList;
	}


	@Override
	public void updateButton(List<ButtonVO> buttonlist) {
		List<ButtonVO> insertList=new ArrayList<ButtonVO>();
		List<ButtonVO> deleteList=new ArrayList<ButtonVO>();
		SysUserVO sysUserVO=(SysUserVO)com.bba.util.SessionUtils.currentSession();
		String within_code=sysUserVO.getWithin_code();
		ButtonVO param = new ButtonVO();
		param.setRoleId(buttonlist.get(0).getRoleId());
		param.setModuleid(buttonlist.get(0).getModuleid());
		// 获取存在的权限
		List<ButtonVO> buttonRoleList=buttonDao.findButtonByRole(param);
		for(ButtonVO buttonVO : buttonlist){
			boolean isExist = false;
			buttonVO.setWithin_code(within_code);
			for (int j = 0; j < buttonRoleList.size(); j++) {
				if(buttonVO.getButtonUse().equals(buttonRoleList.get(j).getButtonUse())){
					isExist = true;
					if(buttonVO.getIsOwnAuthorith() == false){
						deleteList.add(buttonRoleList.get(j));
					}
					break;
				}
			}
			if(isExist == false && buttonVO.getIsOwnAuthorith()){
				insertList.add(buttonVO);
			}
		}
		if(insertList.size()>0){
			buttonDao.insertAuthorith(insertList);
		}
		
		if(deleteList.size()>0){
			buttonDao.deleteAuthorith(deleteList);
		}
		
	}
	@Override
	public void updateButtonMsg(List<ButtonVO> buttonlist) {
		List<ButtonVO> buttonAdd=new ArrayList<ButtonVO>();
		List<ButtonVO> buttonUpdate=new ArrayList<ButtonVO>();
		SysUserVO sysUserVO=(SysUserVO)com.bba.util.SessionUtils.currentSession();
		String within_code=sysUserVO.getWithin_code();
		for (ButtonVO buttonVO : buttonlist) {
			buttonVO.setWithin_code(within_code);
			if("add".equals(buttonVO.getOperateType())){
				buttonAdd.add(buttonVO);
			}
			if("edit".equals(buttonVO.getOperateType())){
				buttonUpdate.add(buttonVO);
			}
		}
		if(buttonAdd.size()>0){
			buttonDao.insertButton(buttonAdd);
		}
		if(buttonUpdate.size()>0)
		{			
			List<ButtonVO> paramList = new ArrayList<ButtonVO>();
			for (ButtonVO vo : buttonUpdate) {
				ButtonVO param = new ButtonVO();
				param.setButtonId(vo.getButtonId());
				paramList.add(param);
			}
			List<ButtonVO> oldButtonList = buttonDao.findListByPropertyList(paramList);
			
			
			buttonDao.updateButton(buttonUpdate);
			
			List<Map<String,Object>> updateAuthorithList = new ArrayList<Map<String, Object>>();
			for (ButtonVO oldButton : oldButtonList) {
				for (int i = 0; i < buttonUpdate.size(); i++) {
					if(oldButton.getButtonId().equals(buttonUpdate.get(i).getButtonId())){
						
						if(!oldButton.getButtonUse().equals(buttonUpdate.get(i).getButtonUse())){
							Map<String, Object> param = new HashMap<String , Object>();
							param.put("moduleid", oldButton.getModuleid());
							param.put("oldButtonUse", oldButton.getButtonUse());
							param.put("newButtonUse", buttonUpdate.get(i).getButtonUse());
							updateAuthorithList.add(param);
							break;
						}
					}
				}
			}
			if(updateAuthorithList.size() > 0){
				buttonDao.updateAuthorith(updateAuthorithList);
			}
			
		}
		
	}
	
	
	@Override
	public List<ButtonVO> findButtonList(ButtonVO buttonVO) {
		List<ButtonVO> list = new ArrayList<ButtonVO>(3);
		list.add(buttonVO);
		return buttonDao.findListByPropertyList(list);
	}



	@Override
	public void deleteButton(List<ButtonVO> list) {

		SysUserVO sysUserVO = SessionUtils.currentSession();
		for(ButtonVO VO : list){
			buttonDao.deleteSysAuthorith(sysUserVO.getWithin_code(), VO.getButtonId(),VO.getModuleid());
		}

		buttonDao.deleteSysRoleButtons(list,sysUserVO.getWithin_code());
		// 删除角色拥有权限的按钮关联表

		/*List<ButtonVO> buttonVOList = buttonDao.findListByPropertyList(list);
		buttonDao.deleteButton(list);
		if(buttonVOList.size() > 0){
			// 删除用户该权限
			buttonDao.deleteAuthorith(buttonVOList);
		}*/
	}

}
