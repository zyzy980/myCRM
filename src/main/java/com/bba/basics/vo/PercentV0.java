package com.bba.basics.vo;

import lombok.Data;

/**
 * 查询需要计算进程百分比的数据
 * @Author bcmaidou
 * @Date 2019/4/19 10:00
 */
@Data
public class PercentV0 {

    private String guid;  //yw_order_detail 的 guid
    private String sale_detail_guid;
    private String end_longitude; //目的地的经度
    private String end_latitude; //目的地的纬度
    private String current_longitude; //司机当前位置的经度
    private String current_latitude; //司机当前位置的纬度
}
