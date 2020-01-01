package com.bba.fpgl.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
 * 收款发票
 */
@TableName("JS_RECEIVABLE_INVOICE")
@Data
public class Receivable_invoiceVO {

    @TableId(type = IdType.AUTO)
    @Excel(name = "ID")
    private Long id;

    @TableId
    @Excel(name = "单号")
    private String sheet_no;

    @TableId
    @Excel(name = "购方名称")
    private String cus_name;

    @TableId
    @Excel(name = "购方编号")
    private String cus_no;

    @TableId
    @Excel(name = "购方地址电话")
    private String cus_address_tel;

    @TableId
    @Excel(name = "购方开户账号")
    private String cus_bank;

    @TableId
    @Excel(name = "发票备注")
    private String invoice_remark;

    @TableId
    @Excel(name = "商品名称")
    private String goods_name;

    @TableId
    @Excel(name = "计量单位")
    private String unit;

    @TableId
    @Excel(name = "规格型号")
    private String spec;

    @TableId
    @Excel(name = "单价")
    private BigDecimal price;

    @TableId
    @Excel(name = "数量")
    private BigDecimal qty;

    @TableId
    @Excel(name = "金额")
    private BigDecimal amount;

    @TableId
    @Excel(name = "税率")
    private BigDecimal tax_rate;

    @TableId
    @Excel(name = "税额")
    private BigDecimal tax_amount;

    @TableId
    @Excel(name = "开票人")
    private String invoice_by;

    @TableId
    @Excel(name = "复核人")
    private String recheck_by;

    @TableId
    @Excel(name = "收款人")
    private String payee;

    @TableId
    @Excel(name = "税收分类编码")
    private String tax_type_code;

    @TableId
    private Date create_date;

    @TableId
    private String create_by;

    @TableId
    private String state;

    @TableId
    private String js_project;

    @TableId
    private String cus_tax_no;

    @TableId
    @Excel(name = "发票号")
    private String invoice_no;

    @TableId
    @Excel(name = "开票日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date invoice_date;

    @TableId
    private String ledger_no;

    @TableId
    private String project;

    @TableId
    @Excel(name = "单据号")
    private String doc_number;

    @TableId
    @Excel(name = "结算号")
    private String js_no;

    @TableId
    private String type;
}
