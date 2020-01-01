package com.bba.settlement.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/18 10:57
 * 对下保费结算表
 */
@TableName("JS_VIN_DOWN_PREMIUM")
@Data
public class Js_Vin_Down_PremiumVO {

    @TableId(type = IdType.AUTO)
    @Excel(name = "ID")
    private Long id;

    @Excel(name = "发运单号")
    private String vdr_no;

    @TableId
    @Excel(name = "VIN")
    private String vin;

    @TableId
    @Excel(name = "AIC")
    private String aic;

    @TableId
    @Excel(name = "承运商编号")
    private String carrier_no;

    @TableId
    @Excel(name = "承运商名称")
    private String carrier_name;

    @TableId
    @Excel(name = "经销商编号")
    private String dealer;

    @TableId
    @Excel(name = "经销商名称")
    private String dealer_name;

    @TableId
    @Excel(name = "小车车型")
    private String car_type;

    @TableId
    @Excel(name = "运输方式")
    private String trans_mode;

    @TableId
    @Excel(name = "起运地")
    private String begin_city;

    @TableId
    @Excel(name = "目的地")
    private String end_city;

    @TableId
    private String begin_date;

    @TableId
    @Excel(name = "车辆项目")
    private String vehicle_project;

    @TableId
    @Excel(name = "数据状态")
    private String data_state;

    @TableId
    @Excel(name = "结算状态")
    private String js_state;

    @TableId
    @Excel(name = "不含税单价")
    private BigDecimal not_tax_price;

    @TableId
    @Excel(name = "税率")
    private BigDecimal tax_rate;

    @TableId
    @Excel(name = "含税合计")
    private BigDecimal tax_amount;

    @TableId
    @Excel(name = "不含税合计")
    private BigDecimal not_tax_amount;

    @TableId
    @Excel(name = "不含税合计")
    private String js_no;

    @TableId
    @Excel(name = "备注")
    private String remark;

    @TableId
    @Excel(name = "创建人")
    private String create_by;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableId
    @Excel(name = "创建时间")
    private String create_date;

    @TableId
    @Excel(name = "结算人")
    private String js_by;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableId
    @Excel(name = "结算时间")
    private Date js_date;

    @TableField(exist = false)
    private Integer data_count;

    @TableField(exist = false)
    private String begin_date_month;

    @TableField(exist = false)
    private String sheet_no;

}
