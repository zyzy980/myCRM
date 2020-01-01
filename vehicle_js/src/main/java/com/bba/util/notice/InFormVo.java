package com.bba.util.notice;

import lombok.Data;

/**
 * @Author bcmaidou
 * @Date 2019/6/13 15:19
 */
@Data
public class InFormVo {

    //通知人编号
    private String Destcode;
    //通知内容
    private String Contents;
    //通知数据源
    private String SourceFrom;
    //通知点击链接网页url
    private String LinkUrl;
    //验证令牌
    private String Token;
    //自定义内容
    private String Other1;
    //自定义内容
    private String Other2;
}
