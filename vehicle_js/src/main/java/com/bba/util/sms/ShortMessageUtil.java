package com.bba.util.sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bba.util.GsonUtil;
import com.bba.util.HttpRequestUtils;
import com.bba.util.JSONUtils;
import com.bba.util.StringUtils;
import com.bba.xtgl.vo.SysUserVO;

/**
 * 发送短信工具类，这里只做短信发送调用接口的方法，具体的短信业务逻辑不在这个类中体现。
 * 
 * @author 樊松
 *
 */
public class ShortMessageUtil {

	/**
	 * 短信数据源
	 */
	private static final String SOURCE_FROM = "A0010";

	/**
	 * 令牌
	 */
	private static final String TOKEN = "E7FB90C82425E1B212225D4751ED66C9";

	/**
	 * 调用发送短信的url
	 */
	private static final String SMS_URL = "http://139.224.44.115:8054/api/Sms/SendSms";

	/**
	 * 发送短信共用工具类
	 * 
	 * @param contents
	 *            短信内容
	 * @param mobile
	 *            短信号码
	 * @return
	 */
	public static String sendSms(String contents, String mobile) {
		String resultData = HttpRequestUtils.sendPost(SMS_URL, StringUtils.join(new String[] {
				"SourceFrom=" + SOURCE_FROM, "Token=" + TOKEN, "Contents=" + contents, "Destcode=" + mobile }, "&"));
		return resultData;
	}

	// {1}司机,您有新的运输任务,请点击链接及时确认时确认www.weekeyan.com/View/TMS/transport_info.html?trainno={2}
	public static void main(String[] args) {
		String content = "测试司机,您有新的运输任务,请点击链接及时确认" + URLShortLinkTool
				.generate("http://www.weekeyan.com/View/TMS/transport_info.html?trainno=" + "ZT10000");
		System.out.println(content);
		System.out.println(sendSms(content, "18681461099"));
	}
}
