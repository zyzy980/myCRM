package com.bba.jcda.vo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bba.common.interceptor.EntityField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("ZD_CARRIER")
@Data
public class Zd_CarrierVO {

    @EntityField(value = "ID")
    @TableId
    private String id;

    @EntityField(value = "类型")
    @TableId
    private String type;

    @EntityField(value = "公司编号")
    @TableId
    private String code;

    @EntityField(value = "公司")
    @TableId
    private String name;

    @EntityField(value = "税号")
    @TableId
    private String tax;

    @EntityField(value = "税号")
    @TableId
    private BigDecimal tax_rate;

    @EntityField(value = "发票地址")
    @TableId
    private String in_address;

    @EntityField(value = "开户银行")
    @TableId
    private String bank;

    @EntityField(value = "开户账号")
    @TableId
    private String bank_no;

    @EntityField(value = "发票电话")
    @TableId
    private String in_tel;

    @EntityField(value = "状态")
    @TableId
    private String status;

    @EntityField(value = "省")
    @TableId
    private String pri;

    @EntityField(value = "市")
    @TableId
    private String city;

    @EntityField(value = "地址")
    @TableId
    private String address;

    @EntityField(value = "备注")
    @TableId
    private String remark;

    @EntityField(value = "创建")
    @TableId
    private String create_by;

    @EntityField(value = "创建时间")
    @TableId
    private Date create_date;


}
