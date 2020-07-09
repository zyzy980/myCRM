package com.bba.bbgl.vo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import java.math.BigDecimal;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/29 15:57
 */
@TableName("JS_ANALYSIS_SUMMARY")
@Data
public class Js_Analysis_SummaryVO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableId
    private String department;

    @TableId
    private String statistics_year;

    @TableId
    private String sheet_no;

    @TableId
    private String statistics_month;

    @TableId
    private BigDecimal month_ntax_up_total;

    @TableId
    private BigDecimal month_tax_up_total;

    @TableId
    private BigDecimal month_usd_up_total;

    @TableId
    private BigDecimal month_ntax_down_total;

    @TableId
    private BigDecimal month_tax_down_total;

    @TableId
    private BigDecimal month_usd_down_total;

    @TableId
    private BigDecimal no_contract_up_total;

    @TableId
    private BigDecimal no_contract_down_total;

    @TableId
    private BigDecimal contract_down_total;

    @TableId
    private BigDecimal order_no_reback_total;

    @TableId
    private BigDecimal not_sellement_total;

    @TableId
    private BigDecimal dz_sheet_total;

    @TableId
    private BigDecimal wait_invoice_total;

    @TableId
    private BigDecimal cus_checking_total;

    @TableId
    private BigDecimal lessthan_30_total;

    @TableId
    private BigDecimal total_30_60;

    @TableId
    private BigDecimal total_60_90;

    @TableId
    private BigDecimal morethan_90_total;


}
