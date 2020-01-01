package com.bba.settlement.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.bba.common.annotation.Column;
import com.bba.common.annotation.Table;
import com.bba.common.enums.BooleanEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import oracle.sql.NUMBER;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@TableName("JS_VIN_AMOUNT")
@Table("JS_VIN_AMOUNT")
@Data
public class Js_Vin_AmountVO {


    @Excel(name = "ID")
    @TableId(type = IdType.AUTO)
    @Column(key = BooleanEnum.YES)
    private Long id;

    @TableField
    @Column
    private String xunjia_no;

    /*@TableField
    private String sheet_no;*/

    @Excel(name = "运单号")
    @TableField
    @Column
    private String vdr_no;

    @Excel(name = "VIN")
    @TableField
    @Column
    private String vin;

    @TableField
    @Column
    private String cus_no;

    @TableField
    @Column
    private String cus_name;

    @TableField
    @Column
    private String dealer;

    @TableField
    @Column
    private String dealer_name;

    @TableField
    @Column
    private String qu_state;

    @TableField
    @Column
    private String begin_city;

    @TableField
    @Column
    private String end_city;

    @TableField
    @Column
    private String trans_mode;

    @TableField
    @Column
    private String car_type;

    @TableField
    @Column
    private String mil;

    @TableField
    @Column
    private BigDecimal price;

    @TableField
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column
    private Date begin_date;

    @TableField
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column
    private Date receipt_date;

    @TableField
    @Column
    private String js_data_type;

    @TableField
    @Column
    private String vehicle_project;

    @TableField
    @Column
    private String data_from;

    @TableField
    @Column
    private String contract_no;

    @Excel(name = "状态")
    @TableField
    @Column
    private String js_state;

    @TableField
    @Column
    private BigDecimal not_tax_freight;

    @Excel(name = "其他费用")
    @TableField
    @Column
    private BigDecimal not_tax_other_amount;

    @TableField
    @Column
    private BigInteger shipment_qty;

    @TableField
    @Column
    private BigInteger js_qty;

    @TableField
    @Column
    private BigDecimal not_tax_price;

    @TableField
    @Column
    private String js_no;

    @TableField
    @Column
    private BigDecimal not_tax_premium;

    @TableField
    @Column
    private BigDecimal tax_amount;

    @TableField
    @Column
    private BigDecimal not_tax_amount;

    @TableField
    @Column
    private BigDecimal tax_rate;

    @Excel(name = "备注")
    @TableField
    @Column
    private String remark;

    @TableField
    @Column
    private String create_by;

    @TableField
    @Column
    private Date create_date;

    @TableField
    @Column
    private String js_batch;

    @TableField
    @Column
    private String dz_sheet;

    @TableField
    @Column
    private String his_flag;

    @TableField
    @Column
    private String contract_type;

    @Excel(name = "账单编号")
    @TableField
    @Column
    private String bill_number;

    @TableField
    @Column
    private String invoice_no;

    @TableField
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date invoice_date;

    @TableField
    @Column
    private Date delivery_date;

    @TableField
    @Column
    private Date return_date;

    @TableField
    @Column
    private String cus_contract_no;

    @TableField
    @Column
    private String contract_sheet_no;

}
