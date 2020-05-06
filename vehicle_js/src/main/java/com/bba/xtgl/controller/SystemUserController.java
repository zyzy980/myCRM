package com.bba.xtgl.controller;

import com.bba.common.constant.SystemUserEnum;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.jcda.vo.Zd_User_WhcenterVO;
import com.bba.util.*;
import com.bba.xtgl.service.api.ISysUserService;
import com.bba.xtgl.vo.RegisterVO;
import com.bba.xtgl.vo.SysAddressVO;
import com.bba.xtgl.vo.SysUserDetailRolesVO;
import com.bba.xtgl.vo.SysUserVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;


@Controller
@RequestMapping("/sysInfo/user")
public class SystemUserController {
    @Autowired
    private ISysUserService sysUserService;


    @RequestMapping("/setYwLocationSession")
    @ResponseBody
    public ResultVO setYwLocationSession(String ywLocationCode, String ywLocationName, String ywLocationName_en, String ywLocationTitle, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        SysUserVO sysUserVO = (SysUserVO) request.getSession().getAttribute(SessionUtils.SESSION_KEY);
        if (null == sysUserVO) {
            return ResultVO.failResult("登录超时，请重新登录");
        }

        if (StringUtils.isBlank(ywLocationCode)) {
            return ResultVO.failResult("业务地点不能为空");
        }

        sysUserVO.setWhcenter(ywLocationCode);
        sysUserVO.setWhcenter_name(ywLocationName);
        sysUserVO.setWhcenter_name_en(ywLocationName_en);
        sysUserVO.setYw_title(ywLocationTitle);
        session.setAttribute("whcenter", ywLocationCode);
        request.getSession().setAttribute(SessionUtils.SESSION_KEY, sysUserVO);
        ResultVO resultVO = new ResultVO();
        resultVO.setResultCode("0");
        resultVO.setResultDataFull("设置业务地点session成功");
        return resultVO;
    }

    @RequestMapping("/getSession")
    @ResponseBody
    public ResultVO getSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(sysUserVO);
        return resultVO;
    }

    @RequestMapping("/getUserLevel")
    @ResponseBody
    public ResultVO getUserLevel(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(sysUserVO.getUserLevel());
        return resultVO;
    }


    // 用户注销
    @RequestMapping(value = "/logout", method = {RequestMethod.POST})
    @ResponseBody
    public ResultVO logout(HttpSession session) {
        session.removeAttribute(SessionUtils.SESSION_KEY);
        session.removeAttribute("within_code");
        session.removeAttribute("whcenter");
        return ResultVO.successResult();
    }

    @Log("用户管理-清单")
    @RequestMapping(value = "/getListForGrid", method = RequestMethod.POST)
    public @ResponseBody
    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters, HttpSession session) {
        customSearchFilters = MyUtils.decode(customSearchFilters); // 防止中文乱码
        SysUserVO sysUserVO = SessionUtils.currentSession();
        /*jqGridParamModel.put("within_code", "eq", sysUserVO.getWithin_code());
        jqGridParamModel.put("is_ps_driver", "eq", "N");*/
        PageVO pageVO = sysUserService.getListForGrid(jqGridParamModel, customSearchFilters);
        return pageVO;
    }

    @Log("用户管理-客户下员工清单")
    @RequestMapping(value = "/getCusListForGrid", method = RequestMethod.POST)
    public @ResponseBody
    PageVO getCusListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters, HttpSession session, String cusCode) {
        customSearchFilters = MyUtils.decode(customSearchFilters); // 防止中文乱码
        cusCode = MyUtils.decode(cusCode); // 防止中文乱码
        SysUserVO sysUserVO = SessionUtils.currentSession();
        jqGridParamModel.put("within_code", "eq", sysUserVO.getWithin_code());
        jqGridParamModel.setFilters(cusCode);
        PageVO pageVO = sysUserService.getCusListForGrid(jqGridParamModel, customSearchFilters);
        return pageVO;
    }

    @Log("用户管理-查看用户信息")
    @RequestMapping(value = "/updateDetail", method = {RequestMethod.POST})
    @ResponseBody
    public ResultVO updateDetail(String code, HttpSession session) {
        ResultVO resultVO = new ResultVO();
        Map<String, String> map = new HashMap<String, String>();
        Map<Object, Object> map2 = new HashMap<Object, Object>();
        map.put("userid", code);
        SysUserVO sysUserVO = SessionUtils.currentSession();
        map.put("within_code", sysUserVO.getWithin_code());
        SysUserVO userVO = this.sysUserService.findByUserIdAndwithincode(map);
        map2.put("userVO", userVO);
        resultVO.setResultDataFull(map2);
        resultVO.setResultCode("0");
        return resultVO;
    }

    @Log(value = "用户登陆", params = {"userCode", "within_code"})
    @RequestMapping(value = "/loginIn", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ResultVO loginIn(String userId, String password, String within_code, String yzm, String lang, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        userId = MyUtils.urlDecode(userId);
        password = MyUtils.urlDecode(password);
        yzm = MyUtils.urlDecode(yzm);

        String yzm_code = StringUtils.upperCase((String) session.getAttribute("yzm_code"));
        yzm = StringUtils.upperCase(yzm);
//		if(!StringUtils.equals(yzm_code, yzm)) {
//			session.removeAttribute("yzm_code");
//			return ResultVO.failResult(SystemUserEnum.YZM_ERROR.getValue(lang));
//		}
//		else
//		{
//			session.removeAttribute("yzm_code");
//		}

        SysUserVO sysUserVO = new SysUserVO();
        sysUserVO.setUserId(userId);
        sysUserVO.setPassword(password);
        sysUserVO.setWithin_code(within_code);

        if (StringUtils.isEmpty(sysUserVO.getUserId())) {
            return ResultVO.failResult(SystemUserEnum.ACCOUNT_NOT_BLANK.getValue(lang));
        }
        if (StringUtils.isEmpty(sysUserVO.getPassword())) {
            return ResultVO.failResult(SystemUserEnum.PASSWORD_NOT_BLANK.getValue(lang));
        }
        //sysUserVO.setWithin_code("BBA");


        session.setAttribute("within_code", sysUserVO.getWithin_code());
        sysUserVO = sysUserService.loginIn(sysUserVO);
//		if(!within_code.equals(sysUserVO.getWithin_code())) {
//			return ResultVO.failResult(SystemUserEnum.WITHIN_CODE_ERROR.getValue(lang));
//		}
        if (sysUserVO == null) {
            return ResultVO.failResult(SystemUserEnum.USER_AND_PASSWORD_ERROR.getValue(lang));
        }
        if (null == sysUserVO.getStatus()) {
            return ResultVO.failResult("用户状态为NULL，不能登录");
        }
        if (!sysUserVO.getStatus().equals("1")) {
            if (sysUserVO.getStatus().equals("0"))
                return ResultVO.failResult("此用户已注销，不能登录");
            else
                return ResultVO.failResult("用户不是正常状态，不能登录");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotEmpty(sysUserVO.getValidDate())) {
            Date validDate = sdf.parse(sysUserVO.getValidDate());
            Date curDate = new Date();
            if (curDate.compareTo(validDate) == 1) {
                return ResultVO.failResult("当前用户已经过期，不能登陆");
            }
        }
       /* if (StringUtils.isBlank(sysUserVO.getPartner()) &&
                !EnumeratedConstants.RESULT_Y.equals(sysUserVO.getIsContractor()) &&
                !EnumeratedConstants.RESULT_Y.equals(sysUserVO.getIsCus())) {
            return ResultVO.failResult("该用户未设置任何业务地点");
        }*/

        Zd_User_WhcenterVO wz_User_WhcenterVO = new Zd_User_WhcenterVO();
        wz_User_WhcenterVO.setWithin_code(sysUserVO.getWithin_code());
        wz_User_WhcenterVO.setuserid(sysUserVO.getUserId());
        wz_User_WhcenterVO.setWhcenter(sysUserVO.getPartner());

        List<Zd_User_WhcenterVO> ywlocationList = null;
        /*
        if (StringUtils.equals(EnumeratedConstants.RESULT_Y, sysUserVO.getIsOp())) {
            //BBA
            ywlocationList = iZd_YWLocationService.findWhcenterListByUser(wz_User_WhcenterVO);
        } else if (StringUtils.equals("Y", sysUserVO.getIsContractor())) {
            //承运商
            ywlocationList = new ArrayList<Zd_User_WhcenterVO>();

        } else if (StringUtils.equals("Y", sysUserVO.getIsCus())) {
            //客户
            ywlocationList = new ArrayList<Zd_User_WhcenterVO>();
        }
        for (int i = 0; i < ywlocationList.size(); i++) {
            if (ywlocationList.get(i).getYw_title() == null) {
                ywlocationList.get(i).setYw_title("");
            }
        }
        */

        HttpSession sessionLogin = request.getSession(true);
        String sessionId = request.getRequestedSessionId();
        String loginUserId = sysUserVO.getUserId();
        if (!SessionSaveMap.getSessionIdMap().containsKey(loginUserId)) {
            SessionSaveMap.getSessionIdMap().put("userName", loginUserId);
        } else {
            SessionSaveMap.getSessionIdMap().remove("userName");
            SessionSaveMap.getSessionIdMap().put("userName", loginUserId);
        }

        sysUserVO.setPassword(null);
        request.getSession().setAttribute(SessionUtils.SESSION_KEY, sysUserVO);
        session.setAttribute("userId", sysUserVO.getUserId());
        session.setAttribute("realName", sysUserVO.getRealName());
        session.setAttribute("password", sysUserVO.getPassword());
        System.out.println("******************************" + sysUserVO.toString() + "******************************");
        session.setAttribute("sysUserVO", sysUserVO);
        System.out.println("++++++++++++++++++++++++++++++" + SessionUtils.currentSession().toString() + "++++++++++++++++++++++++++++++");

        ResultVO resultVO = ResultVO.successResult();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("msg", SystemUserEnum.SUCCESS_MSG.getValue(lang));
        returnMap.put("Token", sysUserVO.getToken());
        returnMap.put("within_code", sysUserVO.getWithin_code());
		returnMap.put("userlevel",sysUserVO.getUserLevel());
        returnMap.put("partner", sysUserVO.getPartner());
        returnMap.put("partner_name", sysUserVO.getPartner_name());
        returnMap.put("ywlocationList", ywlocationList);
        returnMap.put("role", sysUserVO.getIsContractor());
        resultVO.setResultDataFull(returnMap);
        return resultVO;
    }

    @Log("用户管理-添加用户/修改")
    @RequestMapping(value = "/SaveDetail", method = {RequestMethod.POST})
    @ResponseBody
    public ResultVO SaveDetail(@RequestBody SysUserVO sysUserVO, HttpSession session) {
        SysUserVO userSession = SessionUtils.currentSession();


        if (StringUtils.isBlank(sysUserVO.getMobileNo())) {
            sysUserVO.setMobileNo(MyUUid.getUUid());
        }

        if (sysUserVO.getId() == 0) {
            sysUserVO.setPassword(MD5Utils.encode(sysUserVO.getPassword()));
            sysUserVO.setCreate_by(userSession.getCreate_by());
            sysUserVO.setWithin_code(userSession.getWithin_code());
            //判断编号是否重复
            Map<String, String> map = new HashMap<String, String>();
            map.put("userid", sysUserVO.getUserId());
            map.put("within_code", userSession.getWithin_code());
            SysUserVO vo2 = sysUserService.findByUserIdAndwithincode(map);
            if (vo2 != null) {
                return ResultVO.failResult("用户编号请勿重复");
            }
            this.sysUserService.addSysUserVO(sysUserVO);

            return ResultVO.successResult("添加成功!");
        } else {
            // update
            if ("******".equals(sysUserVO.getPassword())
                    || StringUtils.isBlank(sysUserVO.getPassword())) {
                sysUserVO.setPassword(null);
            } else {
                // 修改密码
                sysUserVO.setPassword(MD5Utils.encode(sysUserVO.getPassword()));
            }
            sysUserVO.setUpdate_by(userSession.getRealName());
            sysUserVO.setUpdate_date(DateUtils.nowDate());
            this.sysUserService.updateSysUserVO(sysUserVO);

            return ResultVO.successResult("更新成功!");
        }

    }


    @Log("用户管理-修改用户密码")
    @RequestMapping(value = "/updatePassword", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ResultVO updatePassword(String password1, String password2, HttpServletRequest request) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        password1 = MyUtils.decode(password1);
        password2 = MyUtils.decode(password2);
        if (!password1.equals(password2)) {
            return ResultVO.failResult("两次输入的密码不相同");
        }
        password1 = MD5Utils.encode(password1);
        int i = sysUserService.updatePassword(sysUserVO.getWithin_code(), sysUserVO.getUserId(), password1);
        if (i > 0) {
            return ResultVO.successResult("OK");
        } else {
            return ResultVO.failResult("FAIL");
        }
    }

    @Log("用户管理-角色初始化")
    @RequestMapping(value = "/getRoles", method = {RequestMethod.POST})
    @ResponseBody
    public ResultVO getRoles(String id, String[] roleIds) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = new ResultVO();
        resultVO.setResultCode("0");
        id = MyUtils.decode(id);
        SysUserDetailRolesVO sysUserDetailRolesVO = new SysUserDetailRolesVO();
        sysUserDetailRolesVO.setWithin_code(sysUserVO.getWithin_code());
        List<SysUserDetailRolesVO> list = this.sysUserService.getRoles(sysUserDetailRolesVO);
        List<SysUserDetailRolesVO> listChecked = this.sysUserService.getCheckedRoles(sysUserVO);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setChecked(false);
            for (int j = 0; j < listChecked.size(); j++) {
                if (listChecked.get(j).getId().equals(list.get(i).getId())) {
                    list.get(i).setChecked(true);
                }
            }
        }
        resultVO.setResultDataFull(list);
        return resultVO;
    }


    @RequestMapping(value = "/getUserManagerRoles", method = {RequestMethod.POST})
    @ResponseBody
    public ResultVO getUserManagerRoles(String id) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = new ResultVO();
        resultVO.setResultCode("0");
        id = MyUtils.decode(id);
        SysUserDetailRolesVO sysUserDetailRolesVO = new SysUserDetailRolesVO();
        sysUserDetailRolesVO.setWithin_code(sysUserVO.getWithin_code());
        List<SysUserDetailRolesVO> list = this.sysUserService.getRoles(sysUserDetailRolesVO);

        SysUserVO tempUserVo = new SysUserVO();
        tempUserVo.setWithin_code(sysUserVO.getWithin_code());
        tempUserVo.setUserId(id);
        List<SysUserDetailRolesVO> listChecked = this.sysUserService.getCheckedRoles(tempUserVo);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setChecked(false);
            for (int j = 0; j < listChecked.size(); j++) {
                if (listChecked.get(j).getId().equals(list.get(i).getId())) {
                    list.get(i).setChecked(true);
                }
            }
        }
        resultVO.setResultDataFull(list);
        return resultVO;
    }


    @Log("用户管理-更新用户角色")
    @RequestMapping(value = "/saveAndDelete", method = {RequestMethod.POST})
    @ResponseBody
    public ResultVO saveAndDelete(String id, String[] roleIdArray) {
        id = MyUtils.decode(id);
        SysUserVO sysUserVO = (SysUserVO) SessionUtils.currentSession();
        String within_code = sysUserVO.getWithin_code();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", id);
        map.put("roleIds", roleIdArray);
        map.put("within_code", within_code);
        this.sysUserService.deleteUserRoles(id);
        this.sysUserService.addUserRoles(map);
        return ResultVO.successResult("操作成功!");
    }

    @Log("用户管理-获取省市")
    @RequestMapping(value = "/getList", method = {RequestMethod.POST})
    @ResponseBody
    public ResultVO getList(JqGridParamModel jqGridParamModel,
                            String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(
                customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<SysAddressVO> addressList = this.sysUserService
                .getList(jqGridParamModel);
        ResultVO resultVO = new ResultVO();
        if (addressList.size() == 0 || addressList == null) {
            return ResultVO.failResult("未查询到任何省市区街道列表");
        } else {
            resultVO.setResultCode("0");
            resultVO.setResultDataFull(addressList);
            return resultVO;
        }
    }


    @Log("用户管理-获取省市")
    @RequestMapping(value = "/userstate", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ResultVO userstate(String kind, String idJson, HttpServletRequest request, HttpServletResponse response) {
        try {
            idJson = java.net.URLDecoder.decode(idJson, "UTF-8");
        } catch (Exception e) {
        }
        List<Integer> list = JSONUtils.toJSONObjectList(Integer.class, idJson);
        sysUserService.userstate(kind, list);
        return ResultVO.successResult("处理成功");
    }


    @Log("用户管理-获取用户仓储中心信息")
    @RequestMapping(value = "/findZd_WhcenterListByUser", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ResultVO findZd_WhcenterListByUser(String kind, String idJson, HttpServletRequest request, HttpServletResponse response) {
        ResultVO resultVO = new ResultVO();
        resultVO.setResultCode("0");
        //resultVO.setResultDataFull(null);
        return resultVO;
    }
	
	/*
	@Log("用户管理-下载导入模板")
	@ResponseBody
	@RequestMapping(value = "/exportExcel")
	public void exportTemplateExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		POIUtils.exportTemplate(request, response, "用户管理-导入模板", "Resource/excel/users.xlsx");
	}
	*/

    @Log("用户管理-下载导入模板")
    @ResponseBody
    @RequestMapping(value = "/exportUserExcel")
    public void exportUserExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        POIUtils.exportTemplate(request, response, "用户信息-导入模板", "Resource/excel/用户信息-导入模板.xlsx");
    }


    @Log("用户管理-导入")
    @RequestMapping(value = "/importExcel")
    public @ResponseBody
    ResultVO importExcel(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        return sysUserService.importExcelVO(request, response, session);
    }


    @RequestMapping("/code.do")
    public void getCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int width = 94;// 定义图片的width
        int height = 40;// 定义图片的height
        int codeCount = 4;// 定义图片上显示验证码的个数
        int xx = 15;
        int fontHeight = 18;
        int codeY = 25;
        char[] codeSequence = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics gd = buffImg.getGraphics();
        // 创建一个随机数生成器类
        Random random = new Random();
        // 将图像填充为白色
        gd.setColor(Color.WHITE);
        gd.fillRect(0, 0, width, height);
        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
        // 设置字体。
        gd.setFont(font);
        // 画边框。
        // gd.setColor(Color.BLACK);
        gd.drawRect(0, 0, width - 1, height - 1);
        // 随机产生40条干扰线，使图象中的认证码不易被其它程序探测到。
        gd.setColor(Color.BLACK);
        for (int i = 0; i < 0; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            gd.drawLine(x, y, x + xl, y + yl);
        }
        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();
        int red = 0, green = 0, blue = 0;
        // 随机产生codeCount数字的验证码。
        for (int i = 0; i < codeCount; i++) {
            // 得到随机产生的验证码数字。
            String code = String.valueOf(codeSequence[random.nextInt(codeSequence.length - 1)]);
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            // 用随机产生的颜色将验证码绘制到图像中。
            gd.setColor(new Color(red, green, blue));
            gd.drawString(code, (i + 1) * xx, codeY);
            // 将产生的四个随机数组合在一起。
            randomCode.append(code);
        }
        // 将四位数字的验证码保存到Session中。
        HttpSession session = req.getSession();
        //System.out.print(randomCode);
        session.setAttribute("yzm_code", randomCode.toString());
        // 禁止图像缓存。
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setContentType("image/jpeg");
        // 将图像输出到Servlet输出流中。
        ServletOutputStream sos = resp.getOutputStream();
        ImageIO.write(buffImg, "jpeg", sos);
        sos.close();
    }


    @Log(value = "车型档案-删除")
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResultVO deleteDetail(@RequestBody List<String> ids, HttpSession session) {
        return this.sysUserService.delete(ids);
    }

    @RequestMapping(value = "/addUserDetail", method = RequestMethod.POST)
    public @ResponseBody
    ResultVO addUserDetail(String jsonData) {
        ResultVO resultVO = sysUserService.addUserDetail(jsonData);
        return resultVO;
    }

    @RequestMapping(value = "/querycontractor", method = RequestMethod.POST)
    public @ResponseBody
    ResultVO queryContractor() {
        ResultVO resultVO = ResultVO.successResult();
        List<Map<String, Object>> list = sysUserService.queryContractor();
        resultVO.setResultDataFull(list);
        return list != null ? resultVO : ResultVO.failResult("暂无数据");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public @ResponseBody
    ResultVO register(String jsonData) {
        RegisterVO registerVO = JSONUtils.toJSONObject(RegisterVO.class, jsonData);
        ResultVO resultVO = sysUserService.register(registerVO);
        return resultVO;
    }

    @RequestMapping(value = "/registerdata", method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    ResultVO registerdata(String jsondata) {
        RegisterVO registerVO = JSONUtils.toJSONObject(RegisterVO.class, jsondata);
        ;
        ResultVO resultVO = sysUserService.registerdata(registerVO);
        return resultVO;
    }

}
