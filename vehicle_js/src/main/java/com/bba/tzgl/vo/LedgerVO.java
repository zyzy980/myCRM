package com.bba.tzgl.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/29 15:57
 */
@TableName("JS_LEDGER")
@Data
public class LedgerVO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableId
    private String sheet_no;

    @TableId
    private String ledger_no;

    @TableId
    private String dz_sheet;

    @TableId
    private String bill_number;

    @TableId
    private String ledger_type;

    @TableId
    private String state;

    @TableId
    private String contract_no;

    @TableId
    private String js_project;

    @TableId
    private String js_no;

    @TableId
    private BigDecimal tax_up_total;

    @TableId
    private BigDecimal ntax_up_total;

    @TableId
    private BigDecimal tax_amount;

    @TableId
    private String invoice_no;

    @TableId
    private BigDecimal tax_down_total;

    @TableId
    private BigDecimal ntax_down_total;

    @TableId
    private BigDecimal tax_rate;

    @TableId
    private BigDecimal not_tax_profit;

    @TableId
    private String remark;

    @TableId
    private String create_by;

    @TableId
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_date;

    @TableId
    private String cus_no;

    @TableId
    private String cus_name;

    @TableId
    private String check_by;

    @TableId
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date check_date;

    @TableId
    private String cus_invoice;

    @TableId
    private String project;

    @TableId
    private String invoice_company;

    /**数量*/
    @TableField(exist = false)
    private Integer counts;
}
