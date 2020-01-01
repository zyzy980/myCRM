package com.bba.tzgl.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/29 15:57
 */
@TableName("JS_LEDGER_DETAIL")
@Data
public class Ledger_DetailVO {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableId
    private String sheet_no;

    @TableId
    private String js_no;

    @TableId
    private String carrier_no;

    @TableId
    private String carrier_name;

    @TableId
    private BigDecimal tax_down_total;

    @TableId
    private BigDecimal ntax_down_total;

    @TableId
    private BigDecimal tax_rate;

    @TableId
    private String carrier_invoice;

    @TableId
    private String js_project;

    /*@TableField(value = "invoice_no",strategy = FieldStrategy.IGNORED)*/
    @TableId
    private String invoice_no;

    @TableId
    private String premium_flag;

    @TableId
    private String premium_month;

    /**数量*/
    @TableField(exist = false)
    private Integer counts;
}
