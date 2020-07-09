package com.bba.tzgl.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("JS_DZ_SHEET")
@Data
public class Ledger_Dz_SheetVO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableId
    private String remark;

    @TableId
    private String dz_sheet;

    @TableId
    private String dz_op_by;

    @TableId
    private Date dz_op_datetime;

    @TableId
    private BigDecimal not_tax_amount;

    @TableId
    private BigDecimal tax_amount;

    @TableId
    private String contract_no;

    @TableId
    private String state;

    @TableId
    private String js_batch;

    @TableId
    private String js_no;

    @TableId
    private String type;

    @TableId
    private String bill_number;

    @TableField(exist = false)
    private String project;

}
