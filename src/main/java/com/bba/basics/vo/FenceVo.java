package com.bba.basics.vo;

/**
 * @Author bcmaidou
 * @Date 2019/4/18 11:46
 */

import lombok.Data;

/**
 * 定时电子围栏的处理
 * @Author bcmaidou
 * @Date 11:46 2019/4/18
 */
@Data
public class FenceVo {

    private String guid;  //订单id
    private String begin_fence; //起运地电子围栏
    private String begin_arrive_trigger_event;//触发 无
    private String begin_arrive_trigger_state; //触发的事件
    private String end_fence; //目的地电子围栏
    private String end_arrive_trigger_event; //触发 无
    private String end_arrive_trigger_state;  //触发的事件
    private String longitude; //司机经度
    private String latitude; //司机纬度

    private String state; //订单状态
    private String within_code;
    private String trainno;
}
