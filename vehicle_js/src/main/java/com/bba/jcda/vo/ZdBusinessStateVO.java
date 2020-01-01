package com.bba.jcda.vo;

import lombok.Data;

@Data
public class ZdBusinessStateVO {
    private String sn;//SN
    private String within_code;//内码
    private String state;//状态编号
    private String state_shortname;//状态简写
    private String state_name;//状态名称
    private String state_name_en;//英文名称
    private String mapping_other;//映射状态
    private String state_type;//类别（运单/订单）

    private String create_by;//创建
    private String create_date;//创建时间
    private String update_by;//更新
    private String update_date;//更新时间
    private String location_code;

    private String mapping_other_name;//映射状态名称
}
