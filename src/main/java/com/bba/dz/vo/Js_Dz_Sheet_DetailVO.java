package com.bba.dz.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@TableName("JS_DZ_SHEET_DETAIL")
@Data
public class Js_Dz_Sheet_DetailVO {


    @TableId(type = IdType.AUTO)
    private BigDecimal id;

    @TableField
    private String xunjia_no;

    @TableField
    private String vdr_no;

    @TableField
    private String vin;

    @TableField
    private String cus_no;

    @TableField
    private String cus_name;

    @TableField
    private String dealer;

    @TableField
    private String dealer_name;

    @TableField
    private String qu_state;

    @TableField
    private String begin_city;

    @TableField
    private String end_city;

    @TableField
    private String trans_mode;

    @TableField
    private String car_type;

    @TableField
    private String mil;

    @TableField
    private BigDecimal price;

    @TableField
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date begin_date;

    @TableField
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receipt_date;

    @TableField
    private String js_data_type;

    @TableField
    private String vehicle_project;

    @TableField
    private String data_from;

    @TableField
    private String contract_no;

    @TableField
    private String contract_type;

    /*@TableField
    private String js_state;*/
    @TableField
    private String data_state;
    @TableField
    private BigDecimal not_tax_freight;

    @TableField
    private BigDecimal not_tax_other_amount;

    @TableField
    private BigInteger shipment_qty;

    @TableField
    private BigInteger js_qty;

    @TableField
    private BigDecimal not_tax_price;

    @TableField
    private String js_no;

    @TableField
    private BigDecimal not_tax_premium;

    @TableField
    private BigDecimal tax_amount;

    @TableField
    private BigDecimal tax_rate;

    @TableField
    private BigDecimal not_tax_amount;

    @TableField
    private String remark;

    @TableField
    private String create_by;

    @TableField
    private Date create_date;

    @TableField
    private String js_batch;

    @TableField
    private String dz_sheet;

    @TableField
    private Long vin_id;

    @TableField
    private String bill_number;

    @TableField
    private String type;

    @TableField
    private String his_flag;

    //合同类型
//    private String contract_type;

    @TableField
    private String contract_sheet_no;

}
