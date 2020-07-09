package com.bba.fpgl.vo;

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
 * @Date: 2019/8/1 11:08
 * 付款发票
 */
@TableName("JS_PAYMENT_INVOICE")
@Data
public class Payment_invoiceVO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableId
    private String carrier_no;

    @TableId
    private String carrier_name;

    @TableId
    private String invoice_no;

    @TableId
    private BigDecimal tax_rate;

    @TableId
    private BigDecimal tax_total;

    @TableId
    private BigDecimal ntax_total;

    @TableId
    private BigDecimal tax_amount;

    @TableId
    private String js_project;

    @TableId
    private String create_by;

    @TableId
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_date;

    @TableId
    private String state;

    @TableId
    private String js_no;

    @TableId
    private String sheet_no;

    @TableId
    private String type;

    @TableId
    private String vehicle_project;

    @TableId
    private Date check_date;

    @TableId
    private String check_by;

    private String receipt_date_month;
}
