package com.bba.settlement.vo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@TableName("JS_VIN_TEMP_AMOUNT")
@Data
public class Js_Vin_Temp_AmountVO {


    @TableId(type = IdType.AUTO)
    private Long id;

    @TableId
    private String xunjia_no;

   /* @TableId
    private String sheet_no;*/

    @TableId
    private String vdr_no;

    @TableId
    private String vin;

    @TableId
    private String cus_no;

    @TableId
    private String cus_name;

    @TableId
    private String dealer;

    @TableId
    private String dealer_name;

    @TableId
    private String qu_state;

    @TableId
    private String begin_city;

    @TableId
    private String end_city;

    @TableId
    private String trans_mode;

    @TableId
    private String car_type;

    @TableId
    private String mil;

    @TableId
    private BigDecimal price;

    @TableId
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date begin_date;

    @TableId
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receipt_date;

    @TableId
    private String js_data_type;

    @TableId
    private String vehicle_project;

    @TableId
    private String data_from;

    @TableId
    private String contract_no;

    @TableId
    private String contract_type;

    @TableId
    private String js_state;

    @TableId
    private BigDecimal not_tax_freight;

    @TableId
    private BigDecimal not_tax_other_amount;

    @TableId
    private BigInteger shipment_qty;

    @TableId
    private BigInteger js_qty;

    @TableId
    private BigDecimal not_tax_price;

    @TableId
    private String js_no;

    @TableId
    private BigDecimal not_tax_premium;

    @TableId
    private BigDecimal tax_amount;

    @TableId
    private BigDecimal not_tax_amount;

    @TableId
    private BigDecimal tax_rate;

    @TableId
    private String remark;

    @TableId
    private String create_by;

    @TableId
    private Date create_date;

    @TableId
    private String js_batch;

    @TableId
    private String dz_sheet;

    @TableId
    private String bill_number;

    @TableId
    private String invoice_no;

    @TableId
    private Date invoice_date;

    @TableId
    private Date delivery_date;

    @TableId
    private Date return_date;

    @TableId
    private String cus_contract_no;

    @TableId
    private Long vin_id;

    @TableId
    private String contract_sheet_no;
}
