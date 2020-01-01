package com.bba.util.sms;

/**
 * @Auther: Administrator
 * @Date: 2018/12/20 08:44
 * @Description: 发送短信和公众号的url常量
 */
public class URLConstant {

    /**
     * 功能描述: 短信的链接跳转地址
     * @auther: laoli
     * @date: 2018/12/20 8:45
     */
    public static final String SMS_TO_URL = "http://www.weekeyan.com/View/TMS/transport_info.html";

    /**
     * 功能描述: 派遣时候推送司机消息
     * @auther: laoli
     * @date: 2018/12/20 8:57
     */
    public static final String URL_BY_DISPATCH_TO_DRIVER = "http://www.weekeyan.com/oxplatformwechat/tms/transportationTaskReminder";

    /**
     * 功能描述: 派遣时推送客户消息
     * @auther: laoli
     * @date: 2018/12/20 8:58
     */
    public static final String URL_BY_DISPATCH_TO_CUS = "http://www.weekeyan.com/oxplatformwechat/tms/noticeofTransportByWaybill";

    /**
     * 功能描述: 车次状态改变的时候推送司机消息
     * @auther: laoli
     * @date: 2018/12/20 8:59
     */
    public static final String URL_BY_TRAINNO_STATE_CHANGE = "http://www.weekeyan.com/oxplatformwechat/tms/transportationStatusNotification";
}
