package com.bba.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bba.xtgl.vo.SysUserVO;



/**
 * session帮助类
 */
public class SessionUtils {
	public static final String SESSION_KEY = "sysUserVO";
	
	/**
	 * 获取当前用户session对象
	 */
	public static SysUserVO currentSession(){
		ServletRequestAttributes servletContainer = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletContainer.getRequest();
        return (SysUserVO)request.getSession().getAttribute(SESSION_KEY);
	}
	public static HttpSession getSession(){
		ServletRequestAttributes servletContainer = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletContainer.getRequest();
        return request.getSession();
	}
	public static boolean isMobile(HttpServletRequest request){
		//是否手机端
		Enumeration<String> headerNames = request.getHeaderNames();
		String[] deviceArray = new String[]{"android", "mac os","windows phone"};
	    //获取获取的消息头名称，获取对应的值，并输出
		while(headerNames.hasMoreElements()){
		    String nextElement = headerNames.nextElement();
		    String value = request.getHeader(nextElement);
		    if("user-agent".equals(nextElement)){
		    	value = value.toLowerCase();
		    	for(String device : deviceArray){
		    		if(value.indexOf(device) > -1){
		    			return true;
			    	}
		    	}
		    	//break;
		    }
		}
		return false;
	}
	
	private static boolean isIPAddr(String addr){
        if(StringUtils.isEmpty(addr))
            return false;
        String[] ips = StringUtils.split(addr, '.');
        if(ips.length != 4)
            return false;
        try{
            int ipa = Integer.parseInt(ips[0]);
            int ipb = Integer.parseInt(ips[1]);
            int ipc = Integer.parseInt(ips[2]);
            int ipd = Integer.parseInt(ips[3]);
            return ipa >= 0 && ipa <= 255 && ipb >= 0 && ipb <= 255 && ipc >= 0
                    && ipc <= 255 && ipd >= 0 && ipd <= 255;
        }catch(Exception e){}
        return false;
    }
    
    /**
     * 获取客户端IP地址，此方法用在proxy环境中
     * @param req
     * @return
     */
    private static String getRemoteAddr(HttpServletRequest req) {
        String ip = req.getHeader("X-Forwarded-For");
        if(StringUtils.isNotBlank(ip)){
            String[] ips = StringUtils.split(ip,',');
            if(ips!=null){
                for(String tmpip : ips){
                    if(StringUtils.isBlank(tmpip))
                        continue;
                    tmpip = tmpip.trim();
                    if(isIPAddr(tmpip) && !tmpip.startsWith("10.") && !tmpip.startsWith("192.168.") && !"127.0.0.1".equals(tmpip)){
                        return tmpip.trim();
                    }
                }
            }
        }
        ip = req.getHeader("x-real-ip");
        if(isIPAddr(ip))
            return ip;
        ip = req.getRemoteAddr();
        if(ip.indexOf('.')==-1)
            ip = "127.0.0.1";
        return ip;
    }
    
	/**
	 * 获取请求者IP地址
	 */
	public static String getRequestIp(HttpServletRequest request){
		String ip = getRemoteAddr(request);
		return ip;
	}
	
	/**
	 * 获取浏览器全称
	 * */
	public static String getBrowser(HttpServletRequest request){
		return request.getHeader("User-Agent");
	}
	/**
	 * 获取浏览器 名字
	 * */
	public static String getBrowserName(HttpServletRequest request){
		String Agent = request.getHeader("User-Agent");
		if(null!=Agent && Agent.length()>20)
			Agent=Agent.substring(0,20);
		return Agent;
	}
	
	public static HttpServletRequest currencyHttpServletRequest(){
		ServletRequestAttributes servletContainer = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if(servletContainer == null) {
        	return null;
        }
		HttpServletRequest request = servletContainer.getRequest();
        return request;
	}
	
	public static String getLang(){
		String lang = "zh";
		ServletRequestAttributes servletContainer = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if(servletContainer == null) {
        	return lang;
        }
		HttpServletRequest request = servletContainer.getRequest();
		return "" + request.getSession().getAttribute("lang");
	}
 
	public static String getServerAbsoluteUrl(HttpServletRequest request){
		String rootPath = request.getSession().getServletContext().getRealPath("");
		if(rootPath.endsWith("\\") || rootPath.endsWith("//")){
			rootPath = rootPath.substring(0, rootPath.length() - 1);
		}
		int lastIndex = rootPath.lastIndexOf("\\");
		if(lastIndex == -1){
			lastIndex = rootPath.substring(0,  rootPath.length() - 1).lastIndexOf("/");
		}
		String str =  rootPath.substring(0, lastIndex);
		return str;
	}
}
