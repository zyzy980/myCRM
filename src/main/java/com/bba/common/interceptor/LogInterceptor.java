package com.bba.common.interceptor;

import com.bba.common.vo.ResultVO;
import com.bba.common.vo.SysOperateLogResultVO;
import com.bba.util.JSONUtils;
import com.bba.util.SessionUtils;
import com.bba.xtgl.service.api.ISysOperateLogService;
import com.bba.xtgl.vo.SysOperateLogVO;
import com.bba.xtgl.vo.SysUserVO;
import org.apache.commons.lang.StringUtils;
//import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class LogInterceptor {
/*
	private final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

	@Resource
	private ISysOperateLogService sysOperateLogService;

	*//**
	 * 判断字符串是否是一个IP地址
	 * 
	 * @param addr
	 * @return
	 *//*
	private Method getRequestMethod(ProceedingJoinPoint pj) {
		// 获取调用的方法
		String methodName = pj.getSignature().getName();
		Object args[] = pj.getArgs();
		Method[] methodArray = pj.getTarget().getClass().getDeclaredMethods();
		for (Method vo : methodArray) {
			if (vo.getName().equals(methodName)) {
				// 避免Controller层方法名相同，参数不同
				if (vo.getParameterTypes().length != args.length) {
					continue;
				}
				return vo;
			}
		}
		return null;
	}

	private String getParamWithin_code(Object[] args) {
		try {
			for (Object obj : args) {
				if (obj != null) {
					Method[] ms = obj.getClass().getMethods();
					for (Method m : ms) {
						if (StringUtils.equals(m.getName(), "getWithin_code")) {
							String withinCode = "" + m.invoke(obj, null);
							return withinCode;
						}
					}
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return "";
	}

	public Object around(ProceedingJoinPoint pj) throws Throwable {
		SysOperateLogVO logVo = new SysOperateLogVO();
		String withinCode = "";// 内码
		String operator = "";// 操作人(编号-名称)
		String operatemodule = "";// 操作模块
		String operateitem = "";// 操作项
		String operateresult = "";// 操作结果（Fail,Success）
		String params = "";// 相关参数或者语句
		String platform = "网页";// 系统来源(网页;小程序;公众号;)
		String ywLocation = "";// 业务地点
		String remark = "";// 备注

		Method requestMethod = this.getRequestMethod(pj);
		// 是否进行日志
		if (requestMethod == null || requestMethod.getAnnotation(Log.class) == null
				|| !requestMethod.getAnnotation(Log.class).log()) {
			return pj.proceed();
		}
		HttpServletRequest request = SessionUtils.currencyHttpServletRequest();
		// 日志对象
		Log log = requestMethod.getAnnotation(Log.class);

		Object args[] = pj.getArgs();

		*//*-- 设置“相关参数或者语句” --*//*
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		String paramsNameArray[] = discoverer.getParameterNames(requestMethod);// 获取方法形参名
		String[] zhidingParam = log.params();
		if (zhidingParam != null && zhidingParam.length > 0) {// 获取指定的参数值,例：“@Log(value="用户登陆",params={"userId","within_code"})”
			for (int i = 0; i < paramsNameArray.length; i++) {
				boolean exist = false;
				for (int j = 0; j < zhidingParam.length; j++) {
					if (StringUtils.equals(paramsNameArray[i], zhidingParam[j])) {
						exist = true;
						break;
					}
				}
				if (exist) {
					map.put(paramsNameArray[i], args[i]);
				}
			}
		} else {// 获取全部参数值
			for (int i = 0; i < paramsNameArray.length; i++) {
				if (args[i] instanceof HttpServletRequest || args[i] instanceof HttpServletResponse
						|| args[i] instanceof HttpSession || args[i] instanceof CommonsMultipartFile
						|| args[i] instanceof CommonsMultipartFile[]) {
					continue;
				}
				if(args[i] instanceof Object[]) {
					Object[] tempArray = (Object[])args[i];
					if(tempArray != null && tempArray.length > 0) {
						if(tempArray[0] instanceof CommonsMultipartFile) {
							continue;
						}
					}
				}
				
				if(paramsNameArray[i] == null) {
					map.put("@RequestBody", args[i]);
				}else {
					
					map.put(paramsNameArray[i], args[i]);

				}
			}
		}
		if (map.size() > 0) {
			params = JSONUtils.toJSONString(map);// 请求参数转为JSON格式
		}

		*//*-- 设置“操作模块”、“操作项” --*//*
		if (StringUtils.isNotEmpty(log.value()) && log.value().length() > 0) {
			String[] logVals = log.value().split("-");
			if (logVals != null && logVals.length > 0) {
				for (int i = 0; i < logVals.length; i++) {
					if (i == logVals.length-1) {
						operateitem = logVals[i];
					} else {
						operatemodule += ("-" + logVals[i]);
					}
				}
				if (StringUtils.isNotEmpty(operateitem)) {
					if(null!=operatemodule && operatemodule.length()>1)
					{
						operatemodule = operatemodule.substring(1);// 去除第一个“-”
					}
				}
			}
		}

		*//*-- 设置“操作人”、“内码”、“业务地点” --*//*
		SysUserVO sysUserVO = SessionUtils.currentSession();
		if (sysUserVO == null) {// 操作人为ip地址
			sysUserVO = new SysUserVO();
			String ip = SessionUtils.getRequestIp(request);
			operator = ip;

			withinCode = request.getParameter("within_code");
			if (StringUtils.isBlank(withinCode)) {
				withinCode = getParamWithin_code(args);
			}
		} else {
			operator = sysUserVO.getUserId() + "-" + sysUserVO.getRealName();
			withinCode = sysUserVO.getWithin_code();
			ywLocation = sysUserVO.getWhcenter();
		}

		*//*-- 设置“操作结果”、“相关参数或者语句” --*//*
		Object obj = null;
		try {
			obj = pj.proceed();
			if (obj instanceof ResultVO) {
				ResultVO resultVO = (ResultVO) obj;
				if (!StringUtils.equals(resultVO.getResultCode(), "0")) {
					operateresult = SysOperateLogResultVO.FAIL;// 失败日志
					params = JSONUtils.toJSONString(obj);
					return obj;
				}
			}
			operateresult = SysOperateLogResultVO.SUCCESS;// 成功日志
		} catch (Exception e) {
			operateresult = SysOperateLogResultVO.FAIL;
			params = logVo.getParams() + "<br/>异常原因:" + e.getMessage();
			throw e;
		} finally {
			logVo.setWithinCode(withinCode);// 内码
			logVo.setOperator(operator);// 操作人(编号-名称)
			logVo.setOperatemodule(operatemodule);// 操作模块
			logVo.setOperateitem(operateitem);// 操作项
			logVo.setParams(params);// 相关参数或者语句
			logVo.setOperateresult(operateresult);// 操作结果（Fail,Success）
			logVo.setPlatform(platform);// 系统来源(网页;小程序;公众号;)
			logVo.setYwLocation(ywLocation);// 业务地点
			logVo.setRemark(remark);// 备注
			sysOperateLogService.save(logVo);
		}
		return obj;
	}*/
}
