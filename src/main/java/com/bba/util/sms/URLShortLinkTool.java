package com.bba.util.sms;

import java.util.ArrayList;
import java.util.List;

import com.bba.util.GsonUtil;
import com.bba.util.HttpRequestUtils;
import com.google.gson.reflect.TypeToken;


/**
 * 新浪短链接生成工具
 * @author fanx
 * @date 2018/11/07
 *
 */
public class URLShortLinkTool {
	private static String access_token = "31641035";
	
	/**短网址生成URL*/
	public static final String SHORT_URL_GENERATION = "http://api.t.sina.com.cn/short_url/shorten.json?source=%s&url_long=%s";

	public static final String SMS_TO_URL = "http://www.weekeyan.com/View/TMS/transport_info.html";
	
	/**
	 * 短链接生成
	 * @param url 	长链接
	 * @return 		短链接
	 */
	public static String generate(String url){
		String request_url = String.format(SHORT_URL_GENERATION, URLShortLinkTool.access_token, url);
		List<URLShortLinkTool.UrlVO> urlVOs = GsonUtil.getGson().fromJson(HttpRequestUtils.sendGet(request_url)
				, new TypeToken<ArrayList<URLShortLinkTool.UrlVO>>(){}.getType());
		return urlVOs.get(0).getUrl_short();
	}
	
	public static void main(String[] args) {
		//	测试
		System.out.println("测试司机,您有新的运输任务,请点击链接及时确认," + URLShortLinkTool.generate("http://www.cqmscl.com/"));
	}
	
	/**
	 * 返回值接收类
	 * @author fanx
	 *
	 */
	static class UrlVO {
		private String url_long;	//	原始长链接
		private String url_short;	//	短链接
		private String type;		//	链接的类型，0：普通网页、1：视频、2：音乐、3：活动、5、投票
		private String result;
		
		public String getUrl_long() {
			return url_long;
		}
		public void setUrl_long(String url_long) {
			this.url_long = url_long;
		}
		public String getUrl_short() {
			return url_short;
		}
		public void setUrl_short(String url_short) {
			this.url_short = url_short;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getResult() {
			return result;
		}
		public void setResult(String result) {
			this.result = result;
		}
		
	}
	
}
