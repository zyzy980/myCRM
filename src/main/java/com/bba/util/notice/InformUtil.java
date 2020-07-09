package com.bba.util.notice;

import com.bba.util.JSONUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 通知pc客户端弹框信息
 *
 * @Author bcmaidou
 * @Date 2019/6/13 14:41
 */
@Component
public class InformUtil {

    @Value("${sourceFrom}")
    private String sourceFrom;
    @Value("${token}")
    private String token;
    @Value("${inFromUrl}")
    private String inFromUrl;
    /**
     * 通知发送
     *
     * @Author bcmaidou
     * @Date 15:09 2019/6/13
     */
    public void sendNotice(InFormVo inFormVo) {

        try {
            inFormVo.setToken(token);
            inFormVo.setSourceFrom(sourceFrom);

            String s = JSONUtils.toJSONString(inFormVo);
//            String responseMsg = HttpUtil.doPost(inFromUrl, s);
//            System.out.println("通知内容：" + JSONUtils.toJSONString(inFormVo) + "-------通知发送结果:" + responseMsg);
        } catch (Exception e) {
            System.out.println("通知内容发送失败，失败原因:"+e.getMessage());
        }
    }

    /**
     * 批量通知发送
     *
     * @Author bcmaidou
     * @Date 15:09 2019/6/13
     */
    public void sendNoticeList(List<InFormVo> inFormVo) {

        try {
            inFormVo.forEach(item -> {
                item.setToken(token);
                item.setSourceFrom(sourceFrom);
            });

            String s = JSONUtils.toJSONString(inFormVo);
//            String responseMsg = HttpUtil.doPost(inFromUrl+"List", s);
//            System.out.println("通知内容：" + JSONUtils.toJSONString(inFormVo) + "-------通知发送结果:" + responseMsg);
        } catch (Exception e) {
            System.out.println("通知内容发送失败，失败原因:"+e.getMessage());
        }
    }
}
