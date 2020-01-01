package com.bba.xtgl.service.impl;


import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.*;
import com.bba.xtgl.dao.*;
import com.bba.xtgl.service.api.ISysUserService;
import com.bba.xtgl.vo.*;
import com.bba.xtgl.vo.copyUser.*;
import com.bba.xtgl.vo.copyUser.SysRoleVO;
import com.bba.xtgl.vo.copyUser.SysSheetIdVO;
import com.bba.xtgl.vo.copyUser.ZdYwLocationVO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class SysUserService implements ISysUserService {

    @Resource
    private ISysWithinDao sysWithinDao;
    @Resource
    private IZdYwLocationDao zdYwLocationDao;
    @Resource
    private ISysUserDao sysUserDao;
    @Resource
    private ISysUsersTokenDao iSysUsersTokenDao;
    @Resource
    private ICopySysUserDao copyDao;
    @Resource
    private ISysAuthorithDao sysAuthorithDao;
    @Resource
    private ISys_userdetailrolesDao sys_userdetailrolesDao;
    @Resource
    private ISys_within_setDao sys_within_setDao;


    //LTJ:加载 dao
    @Resource
    private IModuleDao moduleDao;
    @Resource
    private IButtonDao buttonDao;
    @Resource
    private IRoleDao roleDao;
    @Resource
    private ISysLanguageDao languageDao;

    //业务数据
    @Resource
    private IZd_ErrorKindDao zd_errorKindDao;
    @Resource
    private ISysSheetIdDao sysSheetIdDao;

    @Autowired
    private IZd_dictionaryDao iZd_dictionaryDao;

    @Override
    public SysUserVO loginIn(SysUserVO sysUserVO) {
        sysUserVO.setPassword(MD5Utils.encode(sysUserVO.getPassword()));
        List<SysUserVO> sysUserList = this.findSysUserList(sysUserVO);
        if (sysUserList.size() == 0) {
            return null;
        } else {
            ServletRequestAttributes servletContainer = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletContainer.getRequest();
            request.getSession().setAttribute(SessionUtils.SESSION_KEY, sysUserList.get(0));
            SysUserVO userVo = sysUserList.get(0);
            //写Token表 开始
            try {
                String Token = CommonUtils.getToken();
                userVo.setToken(Token);
                SysUsersTokenVO tokenVo = new SysUsersTokenVO();
                tokenVo.setWithin_code(userVo.getWithin_code());
                tokenVo.setWhcenter("");
                tokenVo.setUserid(userVo.getUserId());
                tokenVo.setToken(Token);
                tokenVo.setIpaddr(SessionUtils.getRequestIp(request));
                tokenVo.setBrowser(SessionUtils.getBrowserName(request));
                /*if (!configManager.getMultiuser()) {
                    iSysUsersTokenDao.delete(tokenVo);
                }*/
                iSysUsersTokenDao.insert(tokenVo);
            } catch (Exception etoken) {

            }
            //写Token表 结束
            return userVo;
        }
    }

    /**
     * 获取APP版本信息
     */
    @Override
    public AppUpdateVo getAppVersion() {
        AppUpdateVo appUpdateVo = sysUserDao.getAppVersion();
        if (appUpdateVo == null) {
            appUpdateVo.setResultCode("3");
            appUpdateVo.setMessage("获取版本信息失败");
        } else {
            appUpdateVo.setResultCode("0");
        }
        return appUpdateVo;
    }


    @Override
    public List<SysUserVO> findSysUserList(SysUserVO sysUserVO) {

        List<SysUserVO> list = new ArrayList<SysUserVO>();
        list.add(sysUserVO);
        return sysUserDao.findListByPropertys(list);
    }


    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(
                customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<SysUserVO> list = sysUserDao.getListForGrid(jqGridParamModel);
        int records = sysUserDao.getListForGridCount(jqGridParamModel);
        // 获取用户所有角色
        return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
    }

    @Override
    public PageVO getCusListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        List<SysUserVO> list = sysUserDao.getCusListForGrid(jqGridParamModel);
        jqGridParamModel.setFilters(null);
        String filters = JqGridSearchParamHandler.processSql(
                customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        int records = sysUserDao.getListForGridCount(jqGridParamModel);
        // 获取用户所有角色
        return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
    }


    @Override
    public SysUserVO findByUserIdAndwithincode(Map<String, String> map) {

        return this.sysUserDao.findByUserId(map);
    }

    @Override
    public void updateSysUserVO(SysUserVO sys) {
        this.sysUserDao.updateSysUserVO(sys);
    }

    @Override
    public void addSysUserVO(SysUserVO sys) {

        this.sysUserDao.addSysUserVO(sys);

    }


    @Override
    public void updateSysUserDetail(SysUserVO sysUserVO) {
        this.sysUserDao.updateSysUserVO(sysUserVO);
    }


    @Override
    public List<SysUserDetailRolesVO> getRoles(SysUserDetailRolesVO vo) {
        return this.sysUserDao.getRoles(vo);
    }

    @Override
    public List<SysUserDetailRolesVO> getCheckedRoles(SysUserVO vo) {
        return this.sysUserDao.getCheckedRoles(vo);
    }

    @Override
    public void addUserRoles(Map<String, Object> map) {
        this.sysUserDao.addUserRoles(map);
    }

    @Override
    public void deleteUserRoles(String userId) {
        this.sysUserDao.deleteUserRoles(userId);
    }

    @Override
    public int updatePassword(String within_code, String userId, String password) {
        SysUserVO sysUserVO = new SysUserVO();
        sysUserVO.setWithin_code(within_code);
        sysUserVO.setUserId(userId);
        sysUserVO.setPassword(password);
        return sysUserDao.updateSysUserVO(sysUserVO);
    }

    @Override
    public List<SysAddressVO> getList(JqGridParamModel jqGridParamModel) {
        return this.sysUserDao.getList(jqGridParamModel);
    }

    @Override
    public SysUserVO getUserId(String userId) {
        //
        return this.sysUserDao.getUserId(userId);
    }

    @Override
    public void userstate(String kind, List<Integer> list) {
        sysUserDao.updateUserstate(kind, list);
    }


    /**
     * 手工导入数据
     */
    @Override
    public ResultVO importExcelVO(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        String userid = sysUserVO.getUserId();
        String within_code = sysUserVO.getWithin_code();
        List<SysUserVO> list = new ArrayList<>();
        try {
            list = POIUtils.importEntityExcel(request, SysUserVO.class);
        } catch (InvalidFormatException | InstantiationException | IllegalAccessException | IOException e) {
            e.printStackTrace();
        }

        //判断用户编号或用户手机号码是否存在
        Integer iCount = 0;
        for (int i = 0; i < list.size(); i++) {
            if (null == list.get(i).getUserId() || list.get(i).getUserId().equals("")) {
                iCount = i + 2;
                return ResultVO.failResult("第" + iCount + "行用户编号不能为空");
            }
            if (null == list.get(i).getRealName() || list.get(i).getRealName().equals("")) {
                iCount = i + 2;
                return ResultVO.failResult("第" + iCount + "行用户名称不能为空");
            }
            if (null == list.get(i).getPassword() || list.get(i).getPassword().equals("")) {
                iCount = i + 2;
                return ResultVO.failResult("第" + iCount + "行密码不能为空");
            }
//			if(null==list.get(i).getUserLevel() || list.get(i).getUserLevel().equals(""))
//			{
//				iCount=i+2;
//				return ResultVO.failResult("第"+iCount+"行所属类别不能为空");
//			}

            if (null == list.get(i).getPartner() || list.get(i).getPartner().equals("")) {
                iCount = i + 2;
                return ResultVO.failResult("第" + iCount + "行所属类值不能为空");
            }

            iCount = sysUserDao.CheckUser(" within_code='" + within_code + "' and userid='" + list.get(i).getUserId() + "'");
            if (iCount > 0) {
                iCount = i + 2;
                return ResultVO.failResult("第" + iCount + "行用户编号已存在系统中");
            }

            //用户类别处理
//			if(list.get(i).getUserLevel().equals("操作员"))
//				list.get(i).setUserLevel("1");
//			else if(list.get(i).getUserLevel().equals("承运商"))
//				list.get(i).setUserLevel("2");
//			else if(list.get(i).getUserLevel().equals("供应商"))
//				list.get(i).setUserLevel("5");

        }

        StringBuilder sql_sb = new StringBuilder();
        sql_sb.append("insert into sys_users(userid,realname,password,status,mobileno,sex,mail,create_by,create_date,userlevel,within_code,remark,partner,partner_name)");
        for (int i = 0; i < list.size(); i++) {
            String sql = " select ";
            sql += "'" + list.get(i).getUserId() + "',";
            sql += "'" + list.get(i).getRealName().replace("'", "''") + "',";
            sql += "'" + MD5Utils.encode(list.get(i).getPassword()) + "',";
            sql += "'1',";
            if (null == list.get(i).getMobileNo() || list.get(i).getMobileNo().equals(""))
                sql += "'" + MyUUid.getUUid() + "',";
            else
                sql += "'" + list.get(i).getMobileNo() + "',";
            sql += "'" + list.get(i).getSex() + "',";
            sql += "'" + list.get(i).getMail() + "',";
            sql += "'" + userid + "',";
            sql += "sysdate,";
//			sql+="'"+list.get(i).getUserLevel()+"',";
            sql += "'" + within_code + "',";
            sql += "'手工导入用户信息',";
            sql += "'" + list.get(i).getPartner() + "',";

//			if(list.get(i).getUserLevel().equals("1"))
//				sql+="(select wm_concat(name) from ZD_YWLOCATION where within_code='"+within_code+"' and upper(code) in('"+list.get(i).getPartner().toUpperCase().replace(",", "','")+"')) ";
//			else if(list.get(i).getUserLevel().equals("2"))
//				sql+="(select wm_concat(name) from ZD_CONTRACTOR where within_code='"+within_code+"' and upper(code) in('"+list.get(i).getPartner().toUpperCase().replace(",", "','")+"')) ";
//			else if(list.get(i).getUserLevel().equals("5"))
//				sql+="(select wm_concat(name1) from SCTS_SUPPLIER where within_code='"+within_code+"' and upper(werks) in('"+list.get(i).getPartner().toUpperCase().replace(",", "','")+"')) ";
//			else 
//				sql+="'' ";


            sql += " from dual ";
            if (i < list.size() - 1)
                sql += " union all ";
            sql_sb.append(sql);
        }
        sysUserDao.save(sql_sb.toString());
        return ResultVO.successResult("导入成功");

    }

    @Override
    public ResultVO delete(List<String> ids) {
        sysUserDao.deleteSysUser(ids);
        return ResultVO.successResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultVO addUserDetail(String jsonData) {
        SysUserVO sessionUser = SessionUtils.currentSession();
        Map<String, String> maps = JSONUtils.toJSONObject(Map.class, jsonData);
        SysUserVO sysUserVO = new SysUserVO();
        sysUserVO.setRealName(maps.get("realName"));
        sysUserVO.setUserLevel(maps.get("userLevel"));
        sysUserVO.setUserId(maps.get("userId")); // TODO 保留，等存储过程更新之后自动生成该字段
        sysUserVO.setCode(maps.get("userId"));
        sysUserVO.setMobileNo(maps.get("mobileNo"));
        sysUserVO.setSex(maps.get("sex"));
        sysUserVO.setBirthday(maps.get("birthday"));
//		sysUserVO.setWechar(maps.get("wechar")); TODO 改为wechat
        sysUserVO.setWechar(maps.get("wechar"));
        sysUserVO.setWechat(maps.get("wechat"));
        sysUserVO.setTel(maps.get("tel"));
        sysUserVO.setMail(maps.get("mail"));
        sysUserVO.setMailpassword(maps.get("mailpassword"));
        sysUserVO.setMailport(maps.get("mailport"));
        sysUserVO.setMailhost(maps.get("mailhost"));
        sysUserVO.setFax(maps.get("fax"));
        sysUserVO.setWithin_code(sessionUser.getWithin_code());
        sysUserVO.setIsOp(maps.get("isOp") == null ? "N" : maps.get("isOp"));
        sysUserVO.setIsCus(maps.get("isCus") == null ? "N" : maps.get("isCus"));
        sysUserVO.setIsContractor(maps.get("isContractor") == null ? "N" : maps.get("isContractor"));
        sysUserVO.setIsPsDriver(maps.get("isPsDriver"));
        sysUserVO.setSensitive_authority(maps.get("sensitive_authority"));
        sysUserVO.setPartner(maps.get("ywLocation") == null ? "" : maps.get("ywLocation"));
        sysUserVO.setContractorCode(maps.get("contractor") == null ? "" : maps.get("contractor"));
        sysUserVO.setCusCode(maps.get("cusNo") == null ? "" : maps.get("cusNo"));
        sysUserVO.setAddress(maps.get("address"));
        sysUserVO.setRemark(maps.get("remark"));
        if (Integer.parseInt(maps.get("id")) == 0) {
            sysUserVO.setPassword(MD5Utils.encode(maps.get("password")));
            sysUserVO.setCreate_by(sessionUser.getRealName());
            sysUserVO.setCreate_date(sessionUser.getCreate_date());
            sysUserDao.addUserDetail(sysUserVO);
        } else {
            if ("******".equals(maps.get("password"))
                    || StringUtils.isBlank(maps.get("password"))) {
                sysUserVO.setPassword(null);
            } else {
                // 修改密码
                sysUserVO.setPassword(MD5Utils.encode(maps.get("password")));
            }
            if ("******".equals(maps.get("mailpassword"))) {
                sysUserVO.setMailpassword(null);
            }
            sysUserVO.setUpdate_by(sessionUser.getRealName());
            sysUserVO.setUpdate_date(sessionUser.getUpdate_date());
            sysUserDao.updateUserDetail(sysUserVO);
        }
        ResultVO resultVO = ResultVO.successResult("操作成功");
        return resultVO;
    }

    @Override
    public List<Map<String, Object>> queryContractor() {
        SysUserVO sysUserVO = (SysUserVO) SessionUtils.currentSession();
        return sysUserDao.queryContractor(sysUserVO.getWithin_code());
    }

    @Override
    public ResultVO register(RegisterVO registerVO) {
        ResultVO resultVO = ResultVO.successResult();
        // 将内码设置成TMS，即复制只复制TMS的数据
        String withinCode = "TMS";
        // 给新增用户复制一些基础资料
        // 首先增加模块表


        //只复制超级管理员所有权限,获取老的超级管理ROLEID
        SysRoleVO sysRoleVO = new SysRoleVO();
        sysRoleVO.setWithinCode(withinCode);
        sysRoleVO.setRoleName("超级系统管理员");
        List<SysRoleVO> roleList = copyDao.querySysRole(sysRoleVO);


        SysRoleVO newSysRoleVO = new SysRoleVO();
        newSysRoleVO.setWithinCode(registerVO.getWithinCode());
        newSysRoleVO.setRoleName("超级系统管理员");
        List<SysRoleVO> newRoleList = new ArrayList<SysRoleVO>();
        newRoleList.add(newSysRoleVO);
        copyDao.insertSysRole(newRoleList, registerVO.getWithinCode());

        SysRoleVO newParamSysRoleVO = new SysRoleVO();
        newParamSysRoleVO.setWithinCode(registerVO.getWithinCode());
        newParamSysRoleVO.setRoleName("超级系统管理员");
        List<SysRoleVO> newDataRoleList = copyDao.querySysRole(newParamSysRoleVO);


        List<SysModuleVO> moduleList = copyDao.querySysModule(withinCode);
        List<SysRoleButtonsVO> buttonList = copyDao.querySysButtons(withinCode);


        Long oldRoleId = roleList.get(0).getRoleId();
        Long newRoleId = newDataRoleList.get(0).getRoleId();


        List<SysModuleVO> newSysModuleVOList = getNewModuleData(moduleList, registerVO.getWithinCode(), buttonList);


        SysAuthorithVO sysAuthorithVO = new SysAuthorithVO();
        sysAuthorithVO.setWithin_code(withinCode);
        sysAuthorithVO.setRoleId(Integer.valueOf(oldRoleId + ""));
        List<SysAuthorithVO> sysAuthorithVOList = sysAuthorithDao.findListSys_authorith(sysAuthorithVO);

        //复制新模块按钮数据
        for (SysAuthorithVO vo : sysAuthorithVOList) {
            vo.setWithin_code(registerVO.getWithinCode());
            vo.setRoleId(Integer.valueOf("" + newRoleId));
            for (SysModuleVO newVO : newSysModuleVOList) {
                if (StringUtils.equals(String.valueOf(vo.getModuleId()), String.valueOf(newVO.getOldModuleId()))) {
                    vo.setModuleId(Integer.valueOf(newVO.getModuleId() + ""));
                    break;
                }
            }
        }

        sysAuthorithDao.insertSys_authorith(sysAuthorithVOList);


        List<SysUserDetailRolesVO> sysUserDetailRolesVOList = new ArrayList<SysUserDetailRolesVO>();
        SysUserDetailRolesVO paramVO = new SysUserDetailRolesVO();
        paramVO.setWithin_code(registerVO.getWithinCode());
        paramVO.setRoleid("" + newRoleId);
        paramVO.setUserId("admin");
        sysUserDetailRolesVOList.add(paramVO);
        sys_userdetailrolesDao.batchInsert(sysUserDetailRolesVOList);


        List<Sys_within_setVO> sys_within_setVOList = new ArrayList<Sys_within_setVO>();
        Sys_within_setVO paramSys_within_setVO = new Sys_within_setVO();
        paramSys_within_setVO.setCode(registerVO.getWithinCode());
        paramSys_within_setVO.setSel_tihuo("Y");
        paramSys_within_setVO.setVision("0");
        paramSys_within_setVO.setAmount("0");
        sys_within_setVOList.add(paramSys_within_setVO);
        sys_within_setDao.batchInsert(sys_within_setVOList);

        // 新增业务地点表
        List<ZdYwLocationVO> locationList = getInfoForYwLocation(registerVO);
        copyDao.insertZdLocation(locationList);

        // 复制异常表
        List<ZdErrorkindVO> errorList = copyDao.queryZdErrorKind(withinCode);
        copyDao.insertZdErrorKind(errorList, registerVO.getWithinCode());

        //新增用户表
        SysUsersVO userVO = getInfo(registerVO, withinCode);
        copyDao.insertSysUser(userVO);

        return resultVO;
    }

    /**
     * 功能描述:  往sysUserVO塞数据，为了代码整洁，不写在业务逻辑里面
     *
     * @return:
     * @auther: laoli
     * @date: 2019/2/20 15:05
     */
    private SysUsersVO getInfo(RegisterVO registerVO, String withinCode) {
        SysUsersVO userVO = new SysUsersVO();
        userVO.setWithinCode(registerVO.getWithinCode());
        userVO.setCode(String.valueOf(System.currentTimeMillis()));
        userVO.setUserId(registerVO.getUserName());
        userVO.setRealName(registerVO.getUserName());
        userVO.setPassword(MD5Utils.encode(registerVO.getPassword()));
        userVO.setStatus("1");
        userVO.setMobileNo(registerVO.getMobile());
        userVO.setIsCus("N");
        userVO.setIsContractor("N");
        userVO.setIsPsDriver("N");
        userVO.setIsThDriver("N");
        userVO.setIsOP("Y");

        String ywLocation1 = registerVO.getYwLocationOne();
        String ywLocation2 = registerVO.getYwLocationTwo() == "" ? "" : registerVO.getYwLocationTwo();
        String ywLocation3 = registerVO.getYwLocationThree() == "" ? "" : registerVO.getYwLocationThree();
        StringBuilder partner = new StringBuilder();
        partner.append(ChineseToPinyin.getPinYinHeadChar(ywLocation1));

        // 复制生成车次和运单表
        List<SysSheetIdVO> sheetList = copyDao.querySysSheetId(withinCode);
        copyDao.insertSysSheetId(sheetList, registerVO.getWithinCode(), ChineseToPinyin.getPinYinHeadChar(ywLocation1), "YW_PLAN_EXEC", "车次计划表");
        if (!StringUtils.isEmpty(ywLocation2)) {
            partner.append(",");
            partner.append(ChineseToPinyin.getPinYinHeadChar(ywLocation2));
            copyDao.insertSysSheetId(sheetList, registerVO.getWithinCode(), ChineseToPinyin.getPinYinHeadChar(ywLocation2), "YW_PLAN_EXEC", "车次计划表");
        }
        if (!StringUtils.isEmpty(ywLocation3)) {
            partner.append(",");
            partner.append(ChineseToPinyin.getPinYinHeadChar(ywLocation3));
            copyDao.insertSysSheetId(sheetList, registerVO.getWithinCode(), ChineseToPinyin.getPinYinHeadChar(ywLocation3), "YW_PLAN_EXEC", "车次计划表");
        }
        userVO.setPartner(partner.toString());

        // 设置新增用户的有效期 为7天，即在当前时间加上七天，超过7天不能登陆
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        // 得到当前时间7天后的时间
        Date sevenAfterDate = DateUtils.getDateAfter(currentDate, 7);
        String curDate = sdf.format(sevenAfterDate);
        userVO.setValidDate(curDate);
        return userVO;
    }

    /**
     * 功能描述:  往sysUserVO塞数据，为了代码整洁，不写在业务逻辑里面
     *
     * @return:
     * @auther: laoli
     * @date: 2019/2/20 15:05
     */
    private List<ZdYwLocationVO> getInfoForYwLocation(RegisterVO registerVO) {
        List<ZdYwLocationVO> list = new ArrayList<ZdYwLocationVO>();
        ZdYwLocationVO ywLocationVO = new ZdYwLocationVO();
        // 首先将第一个业务地点加入到list中去
        ywLocationVO.setWithinCode(registerVO.getWithinCode());
        ywLocationVO.setCode(ChineseToPinyin.getPinYinHeadChar(registerVO.getYwLocationOne()));
        ywLocationVO.setName(registerVO.getYwLocationOne());
        ywLocationVO.setTitle(registerVO.getYwLocationOne());
        list.add(ywLocationVO);

        if (StringUtils.isNotEmpty(registerVO.getYwLocationTwo())) {
            ywLocationVO = new ZdYwLocationVO();
            ywLocationVO.setWithinCode(registerVO.getWithinCode());
            ywLocationVO.setCode(ChineseToPinyin.getPinYinHeadChar(registerVO.getYwLocationTwo()));
            ywLocationVO.setName(registerVO.getYwLocationTwo());
            ywLocationVO.setTitle(registerVO.getYwLocationTwo());
            list.add(ywLocationVO);
        }

        if (StringUtils.isNotEmpty(registerVO.getYwLocationThree())) {
            ywLocationVO = new ZdYwLocationVO();
            ywLocationVO.setWithinCode(registerVO.getWithinCode());
            ywLocationVO.setCode(ChineseToPinyin.getPinYinHeadChar(registerVO.getYwLocationThree()));
            ywLocationVO.setName(registerVO.getYwLocationThree());
            ywLocationVO.setTitle(registerVO.getYwLocationThree());
            list.add(ywLocationVO);
        }
        return list;
    }

    /**
     * 功能描述: 获取新的模块信息
     *
     * @param moduleVO   旧的数据
     * @param withinCode 内码
     * @return:
     * @auther: laoli
     * @date: 2019/2/22 10:12
     */
    private List<SysModuleVO> getNewModuleData(List<SysModuleVO> moduleVO, String withinCode, List<SysRoleButtonsVO> buttonList) {

        List<SysModuleVO> newModuleList = new ArrayList<SysModuleVO>();
        // 循环传进来的moduleVO

        for (int i = 0; i < moduleVO.size(); i++) {
            // 如果 fatherId 为空就是父菜单，先增加
            SysModuleVO sysModuleVO = moduleVO.get(i);
            SysModuleVO newSysModuleVO = JSONUtils.toJSONObject(SysModuleVO.class, JSONUtils.toJSONString(sysModuleVO));
            newSysModuleVO.setWithinCode(withinCode);
            newSysModuleVO.setOldModuleId(sysModuleVO.getModuleId());
            Long newModuleId = new Long(0);
            if (sysModuleVO.getModuleFatherId() != 0) {
                continue;
            }
            copyDao.insertSysModuleTwo(newSysModuleVO);
            newModuleList.add(newSysModuleVO);
        }

        for (int i = 0; i < moduleVO.size(); i++) {
            // 如果 fatherId 为空就是父菜单，先增加
            SysModuleVO sysModuleVO = moduleVO.get(i);
            SysModuleVO newSysModuleVO = JSONUtils.toJSONObject(SysModuleVO.class, JSONUtils.toJSONString(sysModuleVO));
            newSysModuleVO.setWithinCode(withinCode);
            newSysModuleVO.setOldModuleId(sysModuleVO.getModuleId());
            Long newModuleId = new Long(0);
            if (sysModuleVO.getModuleFatherId() != 0) {
                //非主要模块，查询之前插入的数据ModuleFatherId
                for (SysModuleVO newVO : newModuleList) {
                    if (String.valueOf(newVO.getOldModuleId()).equals("" + sysModuleVO.getModuleFatherId())) {
                        newSysModuleVO.setModuleFatherId(newVO.getModuleId());
                        break;
                    }

                }
                copyDao.insertSysModuleTwo(newSysModuleVO);
                newModuleList.add(newSysModuleVO);
            }

        }
        //复制新模块按钮数据
        for (SysRoleButtonsVO vo : buttonList) {
            //Long newModuleId = new Long(0);
            for (SysModuleVO newVO : newModuleList) {
                if (String.valueOf(vo.getModuleId()).equals(String.valueOf(newVO.getOldModuleId()))) {
                    vo.setModuleId(newVO.getModuleId());
                    break;
                }
            }
            vo.setWithinCode(withinCode);
			/*if(newModuleId == 0){
				//无用模块数据不需要
				continue;
			}*/
            copyDao.insertSysButtons(vo);
        }
        return newModuleList;
    }

    /**
     * 功能描述: 复制承运商档案表
     *
     * @param withinCode 查询的内码
     * @param registerVO 用户输入的信息
     * @return:
     * @auther: laoli
     * @date: 2019/2/23 11:24
     */
    private void copyContractorInfo(String withinCode, RegisterVO registerVO) {
        ZdYwLocationBaseVO baseVO = new ZdYwLocationBaseVO();
        List<ZdContractorVO> contractorList = copyDao.queryZdContractor(withinCode);
        for (ZdContractorVO contractorVO : contractorList) {
            contractorVO.setWithinCode(registerVO.getWithinCode());
            copyDao.insertZdContractor(contractorVO);
            Long baseSn = contractorVO.getSn();
            baseVO = new ZdYwLocationBaseVO();
            baseVO.setWithinCode(registerVO.getWithinCode());
            baseVO.setBaseSn(baseSn);
            baseVO.setKind("ZD_CONTRACTOR");
            baseVO.setLocationCode(ChineseToPinyin.getPinYinHeadChar(registerVO.getYwLocationOne()));
            copyDao.insertZdYwLocationBase(baseVO);
            if (StringUtils.isNotEmpty(registerVO.getYwLocationTwo())) {
                baseVO = new ZdYwLocationBaseVO();
                baseVO.setWithinCode(registerVO.getWithinCode());
                baseVO.setBaseSn(baseSn);
                baseVO.setKind("ZD_CONTRACTOR");
                baseVO.setLocationCode(ChineseToPinyin.getPinYinHeadChar(registerVO.getYwLocationTwo()));
                copyDao.insertZdYwLocationBase(baseVO);
            }
            if (StringUtils.isNotEmpty(registerVO.getYwLocationThree())) {
                baseVO = new ZdYwLocationBaseVO();
                baseVO.setWithinCode(registerVO.getWithinCode());
                baseVO.setBaseSn(baseSn);
                baseVO.setKind("ZD_CONTRACTOR");
                baseVO.setLocationCode(ChineseToPinyin.getPinYinHeadChar(registerVO.getYwLocationThree()));
                copyDao.insertZdYwLocationBase(baseVO);
            }
        }
    }

    /**
     * 功能描述: 复制客户 zd_cus表
     *
     * @param:
     * @return:
     * @auther: laoli
     * @date: 2019/2/23 14:06
     */
    private void copyCus(String withinCode, RegisterVO registerVO) {
        ZdYwLocationBaseVO baseVO = new ZdYwLocationBaseVO();
        List<ZdCusVO> cusList = copyDao.queryZdCus(withinCode);
        for (ZdCusVO cusVo : cusList) {
            cusVo.setWithinCode(registerVO.getWithinCode());
            copyDao.insertZdCus(cusVo);
            Long cusId = cusVo.getSn();

            baseVO = new ZdYwLocationBaseVO();
            baseVO.setKind("ZD_CUS");
            baseVO.setWithinCode(registerVO.getWithinCode());
            baseVO.setBaseSn(cusId);
            baseVO.setLocationCode(ChineseToPinyin.getPinYinHeadChar(registerVO.getYwLocationOne()));
            copyDao.insertZdYwLocationBase(baseVO);

            if (StringUtils.isNotEmpty(registerVO.getYwLocationTwo())) {
                baseVO = new ZdYwLocationBaseVO();
                baseVO.setWithinCode(registerVO.getWithinCode());
                baseVO.setKind("ZD_CUS");
                baseVO.setBaseSn(cusId);
                baseVO.setLocationCode(ChineseToPinyin.getPinYinHeadChar(registerVO.getYwLocationTwo()));
                copyDao.insertZdYwLocationBase(baseVO);
            }

            if (StringUtils.isNotEmpty(registerVO.getYwLocationThree())) {
                baseVO = new ZdYwLocationBaseVO();
                baseVO.setWithinCode(registerVO.getWithinCode());
                baseVO.setKind("ZD_CUS");
                baseVO.setBaseSn(cusId);
                baseVO.setLocationCode(ChineseToPinyin.getPinYinHeadChar(registerVO.getYwLocationThree()));
                copyDao.insertZdYwLocationBase(baseVO);
            }
        }
    }

    /**
     * 功能描述:  复制 zd_truck_type 表
     *
     * @return:
     * @auther: laoli
     * @date: 2019/2/23 14:08
     */
    private void copyZdTruckType(String withinCode, RegisterVO registerVO) {
        ZdYwLocationBaseVO baseVO = new ZdYwLocationBaseVO();
        List<ZdTruckTypeVO> truckList = copyDao.queryZdTruckType(withinCode);
        for (ZdTruckTypeVO truck : truckList) {
            truck.setWithinCode(registerVO.getWithinCode());
            copyDao.insertZdTruckType(truck);
            Long truckSn = truck.getSn();

            baseVO.setWithinCode(registerVO.getWithinCode());
            baseVO.setKind("ZD_TRUCKTYPE");
            baseVO.setBaseSn(truckSn);
            baseVO.setLocationCode(ChineseToPinyin.getPinYinHeadChar(registerVO.getYwLocationOne()));
            copyDao.insertZdYwLocationBase(baseVO);

            if (StringUtils.isNotEmpty(registerVO.getYwLocationTwo())) {
                baseVO = new ZdYwLocationBaseVO();
                baseVO.setWithinCode(registerVO.getWithinCode());
                baseVO.setKind("ZD_TRUCKTYPE");
                baseVO.setBaseSn(truckSn);
                baseVO.setLocationCode(ChineseToPinyin.getPinYinHeadChar(registerVO.getYwLocationTwo()));
                copyDao.insertZdYwLocationBase(baseVO);
            }

            if (StringUtils.isNotEmpty(registerVO.getYwLocationThree())) {
                baseVO = new ZdYwLocationBaseVO();
                baseVO.setWithinCode(registerVO.getWithinCode());
                baseVO.setKind("ZD_TRUCKTYPE");
                baseVO.setBaseSn(truckSn);
                baseVO.setLocationCode(ChineseToPinyin.getPinYinHeadChar(registerVO.getYwLocationThree()));
                copyDao.insertZdYwLocationBase(baseVO);
            }
        }
    }

    /**
     * 功能描述:  复制 zd_truck_relate 表
     *
     * @return:
     * @auther: laoli
     * @date: 2019/2/23 15:16
     */
    private void copyZdTruckRelate(String withinCode, RegisterVO registerVO) {
        ZdYwLocationBaseVO baseVO = new ZdYwLocationBaseVO();
        List<ZdTruckRelateVO> relateList = copyDao.queryZdTruckRelate(withinCode);
        for (ZdTruckRelateVO relateVO : relateList) {
            relateVO.setWithinCode(registerVO.getWithinCode());
            copyDao.insertZdTruckRelate(relateVO);
            Long relateSn = relateVO.getSn();

            baseVO = new ZdYwLocationBaseVO();
            baseVO.setBaseSn(relateSn);
            baseVO.setLocationCode(ChineseToPinyin.getPinYinHeadChar(registerVO.getYwLocationOne()));
            baseVO.setWithinCode(registerVO.getWithinCode());
            baseVO.setKind("ZD_TRUCK_RELATE");
            copyDao.insertZdYwLocationBase(baseVO);

            if (StringUtils.isNotEmpty(registerVO.getYwLocationTwo())) {
                baseVO = new ZdYwLocationBaseVO();
                baseVO.setBaseSn(relateSn);
                baseVO.setWithinCode(registerVO.getWithinCode());
                baseVO.setLocationCode(ChineseToPinyin.getPinYinHeadChar(registerVO.getYwLocationTwo()));
                baseVO.setKind("ZD_TRUCK_RELATE");
                copyDao.insertZdYwLocationBase(baseVO);
            }

            if (StringUtils.isNotEmpty(registerVO.getYwLocationThree())) {
                baseVO = new ZdYwLocationBaseVO();
                baseVO.setBaseSn(relateSn);
                baseVO.setWithinCode(registerVO.getWithinCode());
                baseVO.setLocationCode(ChineseToPinyin.getPinYinHeadChar(registerVO.getYwLocationThree()));
                baseVO.setKind("ZD_TRUCK_RELATE");
                copyDao.insertZdYwLocationBase(baseVO);
            }
        }
    }

    /**
     * 功能描述:  复制 zd_driver_relate 表
     *
     * @param:
     * @return:
     * @auther: laoli
     * @date: 2019/2/23 16:05
     */
    private void copyZdDriverRelate(String withinCode, RegisterVO registerVO) {
        ZdYwLocationBaseVO baseVO = new ZdYwLocationBaseVO();
        List<ZdDriverRelateVO> relateList = copyDao.queryZdDriverRelate(withinCode);
        for (ZdDriverRelateVO relateVO : relateList) {
            relateVO.setWithinCode(registerVO.getWithinCode());
            copyDao.insertDriverRelate(relateVO);
            Long relateSn = relateVO.getSn();

            baseVO.setBaseSn(relateSn);
            baseVO.setWithinCode(registerVO.getWithinCode());
            baseVO.setKind("ZD_DRIVER_RELATE");
            baseVO.setLocationCode(ChineseToPinyin.getPinYinHeadChar(registerVO.getYwLocationOne()));
            copyDao.insertZdYwLocationBase(baseVO);

            if (StringUtils.isNotEmpty(registerVO.getYwLocationTwo())) {
                baseVO = new ZdYwLocationBaseVO();
                baseVO.setBaseSn(relateSn);
                baseVO.setWithinCode(registerVO.getWithinCode());
                baseVO.setKind("ZD_DRIVER_RELATE");
                baseVO.setLocationCode(ChineseToPinyin.getPinYinHeadChar(registerVO.getYwLocationTwo()));
                copyDao.insertZdYwLocationBase(baseVO);
            }

            if (StringUtils.isNotEmpty(registerVO.getYwLocationThree())) {
                baseVO = new ZdYwLocationBaseVO();
                baseVO.setBaseSn(relateSn);
                baseVO.setWithinCode(registerVO.getWithinCode());
                baseVO.setKind("ZD_DRIVER_RELATE");
                baseVO.setLocationCode(ChineseToPinyin.getPinYinHeadChar(registerVO.getYwLocationThree()));
                copyDao.insertZdYwLocationBase(baseVO);
            }

        }

    }


    /**
     * TMS_WithinCode=TMS 内码
     * NEW_WithinCodeList=用户输入内码
     * moduleList=列表数据
     */
    private List<ModuleVO> insertModule(String TMS_WithinCode, String NEW_WithinCode, String Modulefatherid, List<ModuleVO> moduleList) {
        List<ModuleVO> newList = new ArrayList<ModuleVO>();
        for (int i = 0, ilen = moduleList.size(); i < ilen; i++) {
            moduleList.get(i).setRowNum(currentModuleID.toString());//新ID
            currentModuleID += 1;
            moduleList.get(i).setWithin_code(NEW_WithinCode);
            moduleList.get(i).setModulefatherid(Modulefatherid);
            moduleDao.insertModule(moduleList.get(i));//写入数据

            ModuleVO QmoduleVO = new ModuleVO();
            QmoduleVO.setWithin_code(TMS_WithinCode);
            QmoduleVO.setModulefatherid(moduleList.get(i).getModuleid());
            List<ModuleVO> moduleList2 = moduleDao.findModuleAll(QmoduleVO);
            if (null != moduleList2 && moduleList2.size() > 0) {
                List<ModuleVO> moduleList3 = insertModule(TMS_WithinCode, NEW_WithinCode, moduleList.get(i).getRowNum(), moduleList2);
                for (ModuleVO vo2 : moduleList2) {
                    newList.add(vo2);
                }

            }
        }
        return newList;
    }

    //@Override
    public ResultVO registerdata1(RegisterVO vo) {
        String TMS_WithinCode = vo.getWithin_code_src(); //"TMS";
        Integer validate = 30; //设置从系统当前时间算起3天帐号自动过期
        SysUserVO sessionUser = SessionUtils.currentSession();

        vo.setUserId(vo.getUserName());

        return ResultVO.failResult("");
    }


    private Long currentModuleID = 0L;

    @Override
    public ResultVO registerdata(RegisterVO vo) {
        String TMS_WithinCode = vo.getWithin_code_src(); //"TMS";
        Integer validate = 30; //设置从系统当前时间算起3天帐号自动过期
        SysUserVO sessionUser = SessionUtils.currentSession();

        vo.setUserId(vo.getUserName());
        //用户名是否存在
        SysUserVO sysUserVO = new SysUserVO();
        sysUserVO.setWithin_code(vo.getWithinCode());
        sysUserVO.setUserId(vo.getUserId());
        List<SysUserVO> sysUserList = this.findSysUserList(sysUserVO);
        if (null != sysUserList && sysUserList.size() > 0) {
            return ResultVO.failResult("用户名已存在，不能重复添加");
        }

        //内码是否存在
        SysWithinVO sysWithinVO = sysWithinDao.getByWithinCode(vo.getWithinCode());
        if (null != sysWithinVO && StringUtils.isNotBlank(sysWithinVO.getCode())) {
            return ResultVO.failResult("内码已存在，不能重复添加");
        }
        //写内码表
        sysWithinVO = new SysWithinVO();
        sysWithinVO.setCode(vo.getWithinCode());
        sysWithinVO.setName(vo.getWithinCode());
        sysWithinVO.setState("1");
        sysWithinVO.setLink_moblie(vo.getMobile());
        sysWithinVO.setCreate_by(sessionUser.getUserId());
        sysWithinDao.insert(sysWithinVO);

        //sys_within_set 表
        Sys_within_setVO sys_within_setVO = new Sys_within_setVO();
        sys_within_setVO.setCode(vo.getWithinCode());
        List<Sys_within_setVO> withinsetList = sys_within_setDao.findDataList(sys_within_setVO);
        //不存在就写数据，否则就忽略不写
        if (null == withinsetList || withinsetList.size() <= 0) {
            //新增数据  , 复制TMS的数据并写入
            sys_within_setVO.setCode(TMS_WithinCode);
            withinsetList = sys_within_setDao.findDataList(sys_within_setVO);
            if (null == withinsetList || withinsetList.size() <= 0) {
                //不复制数据 使用默认
                withinsetList = new ArrayList<Sys_within_setVO>();

                Sys_within_setVO wvo = new Sys_within_setVO();
                wvo.setCode(vo.getWithinCode());
                wvo.setSel_tihuo("Y");
                wvo.setVision("0");
                wvo.setEnd_date(DateUtils.dateFormat(DateUtils.getDateAfter(new Date(), validate))); //validate天过期
                wvo.setAmount("0");

                withinsetList.add(wvo);
            } else {
                for (Sys_within_setVO wvo : withinsetList) {
                    wvo.setCode(vo.getWithinCode());
                    wvo.setEnd_date(DateUtils.dateFormat(DateUtils.getDateAfter(new Date(), validate))); //validate天过期
                }
            }
            sys_within_setDao.batchInsert(withinsetList);
        }


        //写用户表
        sysUserVO.setCode(vo.getUserId());
        sysUserVO.setRealName(vo.getUserId());
        sysUserVO.setPassword(MD5Utils.encode(vo.getPassword()));
        sysUserVO.setMobileNo(vo.getMobile());
        sysUserVO.setStatus("1");
        sysUserVO.setIsCus("N");
        sysUserVO.setIsOp("Y");
        sysUserVO.setCusCode(vo.getUserId());
        sysUserVO.setWhcenter(vo.getYwLocationOne());
        sysUserVO.setHeadimg("http://139.224.44.115:8056/Resource/images/headimg.png");//头像
        sysUserVO.setPartner(ChineseToPinyin.getPinYinHeadChar(vo.getYwLocationOne()) + (StringUtils.isNotBlank(ChineseToPinyin.getPinYinHeadChar(vo.getYwLocationTwo())) ? "," + ChineseToPinyin.getPinYinHeadChar(vo.getYwLocationTwo()) : "") + (StringUtils.isNotBlank(ChineseToPinyin.getPinYinHeadChar(vo.getYwLocationThree())) ? "," + ChineseToPinyin.getPinYinHeadChar(vo.getYwLocationThree()) : ""));
        sysUserVO.setValidDate(DateUtils.dateFormat(DateUtils.getDateAfter(new Date(), validate))); //帐号3天后过期
        sysUserVO.setCreate_by(sessionUser.getUserId());
        sysUserDao.addUserDetail(sysUserVO);

        //写 ZD_YWLOCATION 业务地点表
        com.bba.xtgl.vo.ZdYwLocationVO zdywlocvo = new com.bba.xtgl.vo.ZdYwLocationVO();
        zdywlocvo.setWithin_code(vo.getWithinCode());
        zdywlocvo.setCode(ChineseToPinyin.getPinYinHeadChar(vo.getYwLocationOne()));
        zdywlocvo.setName(vo.getYwLocationOne());
        zdywlocvo.setTitle(vo.getYwLocationOne());
        zdywlocvo.setName_en(vo.getYwLocationOne());
        zdywlocvo.setState("0");
        zdywlocvo.setIco("../../vehicle_js/Resource/upload/1529654149494.png");
        zdywlocvo.setCreate_by(sessionUser.getUserId());
        zdYwLocationDao.insert(zdywlocvo);

        if (StringUtils.isNotBlank(vo.getYwLocationTwo())) {
            zdywlocvo.setCode(ChineseToPinyin.getPinYinHeadChar(vo.getYwLocationTwo()));
            zdywlocvo.setName(vo.getYwLocationTwo());
            zdywlocvo.setTitle(vo.getYwLocationTwo());
            zdywlocvo.setName_en(vo.getYwLocationTwo());
            zdywlocvo.setIco("../../vehicle_js/Resource/upload/1529654138588.png");
            zdYwLocationDao.insert(zdywlocvo);
        }

        if (StringUtils.isNotBlank(vo.getYwLocationThree())) {
            zdywlocvo.setCode(ChineseToPinyin.getPinYinHeadChar(vo.getYwLocationThree()));
            zdywlocvo.setName(vo.getYwLocationThree());
            zdywlocvo.setTitle(vo.getYwLocationThree());
            zdywlocvo.setName_en(vo.getYwLocationThree());
            zdywlocvo.setIco("../../vehicle_js/Resource/upload/1529654149494.png");
            zdYwLocationDao.insert(zdywlocvo);
        }


        //SYS_MODULE模块表 - 开始
        currentModuleID = moduleDao.GetModuleSeq();
        ModuleVO moduleVO = new ModuleVO();
        moduleVO.setWithin_code(TMS_WithinCode);
        moduleVO.setModulefatherid("0");
        List<ModuleVO> moduleList = moduleDao.findModuleAll(moduleVO);
        List<ModuleVO> newList = insertModule(TMS_WithinCode, vo.getWithinCode(), "0", moduleList);
        if (null != newList && newList.size() > 0) {
            for (ModuleVO mvo : newList) {
                moduleList.add(mvo);
            }
        }
        //SYS_MODULE模块表 - 结束

        // sys_language 语言表 - 开始
        List<SysLanguageVo> newLanguageVoS = new ArrayList<SysLanguageVo>();
        for (int i = 0, ilen = moduleList.size(); i < ilen; i++) {
            SysLanguageVo languageVO = new SysLanguageVo();
            languageVO.setWithin_code(TMS_WithinCode);
            languageVO.setModuleid(moduleList.get(i).getModuleid());
            List<SysLanguageVo> languageVos = new ArrayList<SysLanguageVo>();
            languageVos.add(languageVO);
            languageVos = languageDao.getLanguageByParamas(languageVos);
            for (SysLanguageVo sVo : languageVos) {
                sVo.setWithin_code(vo.getWithinCode());
                sVo.setModuleid(moduleList.get(i).getRowNum());
                languageDao.insertSysLanguage(sVo);
            }
        }
        // sys_language 语言表 - 结束

        //SYS_ROLEBUTTONS表 - 开始
        List<ButtonVO> newbuttonList = new ArrayList<ButtonVO>();
        for (int i = 0, ilen = moduleList.size(); i < ilen; i++) {
            ButtonVO buttonVO = new ButtonVO();
            buttonVO.setWithin_code(TMS_WithinCode);
            buttonVO.setModuleid(moduleList.get(i).getModuleid());
            List<ButtonVO> buttonList = new ArrayList<ButtonVO>();
            buttonList.add(buttonVO);
            buttonList = buttonDao.findListByPropertyList(buttonList);//获取到旧TMS按钮数据
            for (ButtonVO bvo : buttonList) {
                String old_buttonid = bvo.getButtonId();
                bvo.setWithin_code(vo.getWithinCode());
                bvo.setModuleid(moduleList.get(i).getRowNum()); //新moduleid
                buttonDao.insert(bvo);
                String new_buttonid = bvo.getButtonId();

                bvo.setRowNum(new_buttonid); //新buttonID
                bvo.setButtonId(old_buttonid);

                newbuttonList.add(bvo);
            }

        }
        //SYS_ROLEBUTTONS表 - 结束

        //SYS_ROLE表 - 开始
        Long currentRoleID = roleDao.GetRoleButtonSeq();
        RoleVO roleVO = new RoleVO();
        roleVO.setWithin_code(TMS_WithinCode);
        List<RoleVO> roleList = roleDao.GetRoleButtonAllList(roleVO);
        for (RoleVO rvo : roleList) {
            rvo.setWithin_code(vo.getWithinCode());
            rvo.setRowNum(currentRoleID.toString());
            rvo.setCreateby(sessionUser.getUserId());
            roleDao.insert(rvo);
            currentRoleID += 1;
        }
        //SYS_ROLE表 - 结束

        //SYS_AUTHORITH表 - 开始
        SysAuthorithVO authorithVO = new SysAuthorithVO();
        authorithVO.setWithin_code(TMS_WithinCode);
        List<SysAuthorithVO> sysAuthorithList = sysAuthorithDao.findListSys_authorith(authorithVO);
        if (null != sysAuthorithList && sysAuthorithList.size() > 0) {
            for (SysAuthorithVO item : sysAuthorithList) {
                item.setWithin_code(vo.getWithinCode());
                //Moduleid
                for (ModuleVO mvo : moduleList) {
                    if (item.getModuleId().toString().equals(mvo.getModuleid())) {
                        item.setModuleId(Integer.parseInt(mvo.getRowNum()));
                        break;
                    }
                }
                //roleid
                for (RoleVO rvo : roleList) {
                    if (item.getRoleId().toString().equals(rvo.getRoleid())) {
                        item.setRoleId(Integer.parseInt(rvo.getRowNum()));
                    }
                }
            }
            sysAuthorithDao.insertSys_authorith(sysAuthorithList);
        }

        //SYS_AUTHORITH表 - 结束

        //sys_userdetailroles 表 - 开始
        SysUserDetailRolesVO userDetailRolesVO = new SysUserDetailRolesVO();
        userDetailRolesVO.setWithin_code(TMS_WithinCode);
        List<SysUserDetailRolesVO> userDetailRolesList = sys_userdetailrolesDao.findAllList(userDetailRolesVO);
        if (null != userDetailRolesList && userDetailRolesList.size() > 0) {
            for (SysUserDetailRolesVO item : userDetailRolesList) {
                item.setWithin_code(vo.getWithinCode());
                item.setUserId(vo.getUserId());
                for (RoleVO t : roleList) {
                    if (item.getRoleid().equals(t.getRoleid())) {
                        item.setRoleid(t.getRowNum());
                        break;
                    }
                }
                if (StringUtils.isBlank(item.getRoleid())) {
                    item.setRoleid("0");
                }
            }
            sys_userdetailrolesDao.batchInsert(userDetailRolesList);
        }
        //sys_userdetailroles 表 - 结束

        //sys_sheetid 表 - 开始
		/*
		Sys_SheetIdVO sheetvo=new Sys_SheetIdVO();
        sheetvo.setWithin_code(vo.getWithinCode());
        sheetvo.setCreate_by(vo.getUserId());
        sheetvo.setKind(TMS_WithinCode);
		sysSheetIdDao.insert(sheetvo);
		*/
        //sys_sheetid 表 - 开始
        Sys_SheetIdVO sheetvo = new Sys_SheetIdVO();
        sheetvo.setWithin_code(TMS_WithinCode);
        List<Sys_SheetIdVO> sheetAllList = sysSheetIdDao.findAllList(sheetvo);
        //过滤重复表名
        List<Sys_SheetIdVO> sheetList = new ArrayList<Sys_SheetIdVO>();
        List<String> keyList = new ArrayList<String>();
        for (Sys_SheetIdVO item : sheetAllList) {
            if (!keyList.contains(item.getTable_name())) {
                sheetList.add(item);
                keyList.add(item.getTable_name());
            }
        }

        if (null != sheetList && sheetList.size() > 0) {
            List<Sys_SheetIdVO> SysSheetList = new ArrayList<Sys_SheetIdVO>();
            for (Sys_SheetIdVO item : sheetList) {
                Sys_SheetIdVO svo = new Sys_SheetIdVO();
                svo.setWithin_code(vo.getWithinCode());
                svo.setYw_location(ChineseToPinyin.getPinYinHeadChar(vo.getYwLocationOne()));
                svo.setTable_name(item.getTable_name());
                svo.setFunction_name(item.getFunction_name());
                svo.setFront(item.getFront());
                svo.setKind(item.getKind());
                svo.setL(item.getL());
                svo.setCreate_by(vo.getUserId());
                SysSheetList.add(svo);
            }

            if (StringUtils.isNotBlank(vo.getYwLocationTwo())) {
                for (Sys_SheetIdVO item : sheetList) {
                    Sys_SheetIdVO svo = new Sys_SheetIdVO();
                    svo.setWithin_code(vo.getWithinCode());
                    svo.setYw_location(ChineseToPinyin.getPinYinHeadChar(vo.getYwLocationTwo()));
                    svo.setTable_name(item.getTable_name());
                    svo.setFunction_name(item.getFunction_name());
                    svo.setFront(item.getFront());
                    svo.setKind(item.getKind());
                    svo.setL(item.getL());
                    svo.setCreate_by(vo.getUserId());
                    SysSheetList.add(svo);
                }
            }

            if (StringUtils.isNotBlank(vo.getYwLocationThree())) {
                for (Sys_SheetIdVO item : sheetList) {
                    Sys_SheetIdVO svo = new Sys_SheetIdVO();
                    svo.setWithin_code(vo.getWithinCode());
                    svo.setYw_location(ChineseToPinyin.getPinYinHeadChar(vo.getYwLocationThree()));
                    svo.setTable_name(item.getTable_name());
                    svo.setFunction_name(item.getFunction_name());
                    svo.setFront(item.getFront());
                    svo.setKind(item.getKind());
                    svo.setL(item.getL());
                    svo.setCreate_by(vo.getUserId());
                    SysSheetList.add(svo);
                }
            }
//            sysSheetIdDao.insert(SysSheetList);

            for (Sys_SheetIdVO sys_sheetIdVO : SysSheetList) {

                sysSheetIdDao.insertDetail2(sys_sheetIdVO);

                String sn = sysSheetIdDao.findCurId();

            }
        }
        //sys_sheetid 表 - 结束

        //ZD_ERRORKIND
        ZdErrorkindVO zdErrorkindVO = new ZdErrorkindVO();
        zdErrorkindVO.setWithinCode(vo.getWithinCode());
        zdErrorkindVO.setCreateBy(vo.getUserId());
        zdErrorkindVO.setKind(TMS_WithinCode);
        zd_errorKindDao.insert(zdErrorkindVO);


        //复制 zd_order_sale_state
        List<ZdOrderSaleStateVO> saleStateVOS = copyDao.queryZdOrderSaleState(TMS_WithinCode);
        // 批量新增 zd_order_sale_state
        copyDao.insertZdOrderSaleState(saleStateVOS, vo.getWithinCode());

        // 复制 zd_business_state
        List<ZdBusinessStateVO> stateVOList = copyDao.queryZdBusinessState(TMS_WithinCode);
        copyDao.insertZdBusinessState(stateVOList, vo.getWithinCode());
        // 赋值字典表
//        List<ZdDictionaryVO> dictionaryList = copyDao.queryZdDictionary(TMS_WithinCode);
//        for (int i = 0; i < dictionaryList.size(); i++) {
//            // 取出主键sn，通过主键sn去匹配 parentsn ，如果相同， 就去增加
//            String sn = dictionaryList.get(i).getSn();
//            // 首先新增 父级菜单，然后去匹配sn，如果sn相同，就将父菜单的sn塞入parentsn
//            dictionaryList.get(i).setWithinCode(vo.getWithinCode());
//            copyDao.insertZdDictionary(dictionaryList.get(i));
//            for (int j = 0; j < dictionaryList.size(); j++) {
//                String parentSn = dictionaryList.get(i).getParentSn();
//                if (StringUtils.equals(sn, parentSn)) {
//                    // 如果相同，就将 之前添加的 sn 赋值到子菜单的parentsn
//                    dictionaryList.get(j).setParentSn(dictionaryList.get(i).getSn());
//                    dictionaryList.get(j).setWithinCode(vo.getWithinCode());
//                    copyDao.insertZdDictionary(dictionaryList.get(j));
//                }
//            }
//        }

        //复制ZD_DICTIONARY 和 ZD_DICTIONARY_DATA
        iZd_dictionaryDao.copyParent(vo);
        String s = iZd_dictionaryDao.parentSn(vo);
        iZd_dictionaryDao.copyDic(vo,s);
//        iZd_dictionaryDao.copyDicData(vo);

        //复制 within_set
//        sysWithinDao.copySet(vo);


        List<ZdDictionaryDataVO> dataVOS = copyDao.queryZdDictionaryData(TMS_WithinCode);
        copyDao.insertZdDictionaryData(dataVOS, vo.getWithinCode());

        return ResultVO.successResult("保存成功");
    }

    @Override
    public int querySysUsersByTel(String telphone, String withinCode) {
        return sysUserDao.querySysUsersByTel(telphone, withinCode);
    }




}
