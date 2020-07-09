package com.bba.xtgl.vo;


import lombok.Data;

@Data
public class Sys_within_setVO {

	private String sn;

	private String code;//公司编码

	private String sel_tihuo;// 是否提货显示 Y/N,Y=准许,N=不准许  def N

	private String vision;//0=标准版，1=企业版,2=豪华版 def 0

	private String end_date;//截止有效期

	private String amount;// 系统余额
	private String is_driver_check; //司机需要审核？ def n
	private String is_truck_check; //车牌需要审核？ def n
	private String gps_offline_time;// GPS离线时效 /min
	private String is_km_address;// 公众号在途跟踪是否显示地址名称 def Y
	private String gps_overspeed;// 超速边界
	private String gps_avgspeed;// 平均速度

	private String api_error_send_sms;  //api接口是否发送短信
	private String api_error_send_email; //api接口是否发送邮件
	//is_auto_generate_yd
	private String isAutoGenerateYd;

	private String is_must_fence_check_arrival;  //到达目的地是否强制围栏校验
	private String is_must_phone_jiaofu;			//交付时是否强制拍运单
	private String tihuojiaofu_mode;
	private String show_bill_mode;
	private String is_auto_generate_yd;  //订单审核时是否自动生成运单
	private String is_error_notice;  //小程序上报异常时是否推送
	private String is_jiaofu_notice;  //小程序交付时是否推送
	private String is_must_phone_q_site_fh;      //起运站点是否拍货物装车照片
	private String is_must_phone_p_site_fh;      //配送站点是否拍货物装车照片
}

