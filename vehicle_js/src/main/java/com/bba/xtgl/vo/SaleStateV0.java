package com.bba.xtgl.vo;

import lombok.Data;

/**
 * 订单状态
 * @Author bcmaidou
 * @Date 2019/4/8 10:39
 */
@Data
public class SaleStateV0 {

    private String sn;
    private String withinCode; //内码
    private String orderState; //订单状态
    private String saleState; // 对应sale状态
    private String routeType; //分段运输类别
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;
}
