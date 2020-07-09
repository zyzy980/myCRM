package com.bba.tzgl.vo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/29 15:57
 */
@TableName("JS_COMPENSATION")
@Data
public class Js_CompensationVO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableId
    private String vehicle_project;

    @TableId
    private String begin_city;

    @TableId
    private String end_city;

    @TableId
    private String trans_mode;

    @TableId
    private String cus_no;

    @TableId
    private String cus_name;

    @TableId
    private String type;

    @TableId
    private String dz_sheet;

    @TableId
    private String bill_number;

    @TableId
    private String befor_contract_no;

    @TableId
    private String befor_contract_type;

    @TableId
    private String after_contract_no;

    @TableId
    private String after_contract_type;

    @TableId
    private String js_no;

    @TableId
    private String js_batch;

    @TableId
    private Long vin_id;

    @TableId
    private String vin;

    @TableId
    private String carrier_no;

    @TableId
    private String carrier_name;

    @TableId
    private BigDecimal tax_rate;

    @TableId
    private BigDecimal tax_up_total;

    @TableId
    private BigDecimal ntax_up_total;

    @TableId
    private BigDecimal tax_amount;

    @TableId
    private BigDecimal tax_down_total;

    @TableId
    private BigDecimal ntax_down_total;

    @TableId
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date begin_date;

    @TableId
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receipt_date;

    @TableId
    private String car_type;

    @TableId
    private String remark;

    @TableId
    private String state;

    @TableId
    private String invoice_no;

    @TableId
    private String invoice_flag;

    @TableId
    private Date invoice_date;

    @TableId
    private String dealer_name;
    @TableId
    private String vdr_no;
    @TableId
    private BigDecimal old_tax_amount;
    @TableId
    private BigDecimal old_ntax_amount;
    @TableId
    private BigDecimal now_tax_amount;
    @TableId
    private BigDecimal now_ntax_amount;
    @TableId
    private String mil;//现合同里程
    @TableId
    private BigDecimal price;//现合同单价
    @TableId
    private BigDecimal not_tax_premium;//保费补差
    @TableId
    private BigDecimal not_tax_other_amount;//其他费用补差
    @TableId
    private BigDecimal not_tax_freight;//运费补差
    @TableId
    private String after_contract_sheet_no;//现合同单号
    @TableId
    private Date create_date;
    @TableId
    private String create_by;
}
