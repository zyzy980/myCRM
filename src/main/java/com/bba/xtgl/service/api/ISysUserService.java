package com.bba.xtgl.service.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.*;

public interface ISysUserService {
	
	/**
	 * 用户登录
	 * 
	 * @param userId 		用户名
	 * @param password		密码(未加密)
	 * @param whit	公司编号
	 * @return
	 */
	public SysUserVO loginIn(SysUserVO sysUserVO);
	public AppUpdateVo getAppVersion();

	public PageVO getListForGrid(JqGridParamModel jqGridParamModel,
			String customSearchFilters);
	public PageVO getCusListForGrid(JqGridParamModel jqGridParamModel,
			String customSearchFilters);
	public SysUserVO getUserId(String userId);
	
	
	public List<SysUserVO> findSysUserList(SysUserVO sysUserVO);
	
	public void updateSysUserVO(SysUserVO sys);
	
	public void addSysUserVO(SysUserVO sys) ;
	
	public SysUserVO findByUserIdAndwithincode(Map<String, String> map);
	
	
	public void updateSysUserDetail(SysUserVO sysUserVO);
	
	public List<SysUserDetailRolesVO> getRoles(SysUserDetailRolesVO vo);
	
	public List<SysUserDetailRolesVO> getCheckedRoles(SysUserVO vo);
	
	public void addUserRoles(Map<String, Object> map);
	
	public void deleteUserRoles(String userId);
	
	public int updatePassword(String within_code,String userId, String password);

	public List<SysAddressVO> getList(JqGridParamModel jqGridParamModel);
	
	
	public void userstate(String kind,List<Integer> list);
	
	 
	public ResultVO importExcelVO(HttpServletRequest request,HttpServletResponse response, HttpSession session);
	
	//刪除
	public ResultVO delete(List<String> ids);
	
	public ResultVO addUserDetail(String jsonData);
	
	/**
	 * 查找承运商
	 * @return
	 */
	List<Map<String,Object>> queryContractor();
	
	/**
	 * 功能描述:  注册试用用户
	 * @return:
	 * @auther: laoli
	 * @date: 2019/2/13 17:30
	 */
	ResultVO register(RegisterVO registerVO);

	/**
	 * 功能描述:  注册试用用户，写用户表，权限，菜单，业务等表
	 * @return:
	 * @auther: ltj
	 * @date: 2019/3/18 17:30
	 */
	ResultVO registerdata(RegisterVO vo);

	/**
	 * @Description 判断手机号码是否重复
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	int querySysUsersByTel(String telphone, String withinCode);

}
