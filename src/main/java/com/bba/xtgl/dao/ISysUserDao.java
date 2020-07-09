package com.bba.xtgl.dao;

import java.util.List;
import java.util.Map;

import com.bba.xtgl.vo.*;
import org.apache.ibatis.annotations.Param;

import com.bba.util.JqGridParamModel;
import org.springframework.stereotype.Repository;

@Repository
public interface ISysUserDao {

	
	public List<SysUserVO> getListForGrid(JqGridParamModel jqGridParamModel);
	public List<SysUserVO> getCusListForGrid(JqGridParamModel jqGridParamModel);
	public int getListForGridCount(JqGridParamModel jqGridParamModel);
	public SysUserVO getUserId(String userId);
	public AppUpdateVo getAppVersion();
	public void addSysUserVO(SysUserVO sys);
	
	public SysUserVO findByUserId(Map<String, String> map);
	public List<SysUserVO> findListByPropertys(List<SysUserVO> list);
	
	
	public int updateSysUserVO(SysUserVO sys);
	
	public List<SysUserDetailRolesVO> getRoles(SysUserDetailRolesVO vo);
	
	public List<SysUserDetailRolesVO> getCheckedRoles(SysUserVO vo);
	
	public void addUserRoles(Map<String, Object> map);
	public void deleteUserRoles(String userId);
	public List<SysAddressVO> getList(JqGridParamModel jqGridParamMode);

	public void updateUserstate(@Param(value="kind")String kind,@Param(value="list")List<Integer> list);


	public void save(@Param(value="sql")String sql);
	
	public Integer CheckUser(@Param(value="wheresql")String wheresql);
	
	public int deleteSysUser(List<String> id);
	
	public String getWxOpenId(@Param(value = "userId")String userId, @Param(value = "userLevel")String userLevel);
	
	void addUserDetail(SysUserVO sysUserVO);
	
	/**
	 * 查找承运商
	 * @return
	 */
	List<Map<String,Object>> queryContractor(@Param("code")String code);
	
	void updateUserDetail(SysUserVO sysUserVO);

	/**
	 * 功能描述: 通过客户编号去查询客户的手机号码  当前查询的数据只查询 is_cus为Y的数据
	 * @param:
	 * @return:
	 * @auther: laoli
	 * @date: 2018/12/14 15:08
	 */
	SysUserVO getTelPhoneByCusNo(@Param(value = "cusNo")String cusNo);

	/**
	 * 功能描述: 通过承运商编号去查询承运商下面用户的手机号码，暂时只做一条
	 * @param:
	 * @return:
	 * @auther: laoli
	 * @date: 2018/12/14 16:51
	 */
	SysUserVO getTelPhoneByContractorCode(@Param(value = "contractorCode")String contractorCode);

	void register(Map<String,Object> params);

	/**
	 * @Description 判断手机号码是否重复
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	int querySysUsersByTel(@Param("telphone")String telphone, @Param("withinCode")String withinCode);

	/**
	 *查询客户信息
	 * @Author bcmaidou
	 * @Date 15:49 2019/4/11
	 */

	/**
	 * 查询司机信息
	 * @Author bcmaidou
	 * @Date 16:05 2019/4/11
	 */

    SysUserVO getDriverInfo(@Param("driverTel")String driverTel, @Param("withinCode")String withinCode);

    List<SysUserVO> getCusInfo(@Param("withinCode")String withinCode, @Param("contId")String contId);
}
