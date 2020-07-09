package com.bba.fpgl.vo;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("TR_PAYMENT_PLAN")
@Data
public class Tr_Payment_PlanVO {

        @TableId(type = IdType.AUTO)
        private String id;

        @TableId
        private String type;

        @TableId
        private String state;

        @TableId
        private String carrier_no;

        @TableId
        private String carrier_name;

        @TableId
        private String invoice_no;

        @TableId
        private String handover_no;

        @TableId
        private String vin;

        @TableId
        private String vdr_no;

        @TableId
        private String dealer_name;

        @TableId
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date begin_date;

        @TableId
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date receipt_date;

        @TableId
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date receipt_sheet_date;

        @TableId
        private String trans_mode;

        @TableId
        private String begin_address;

        @TableId
        private String end_address;

        @TableId
        private String car_type;

        @TableId
        private Integer chengyun_qty;

        @TableId
        private BigDecimal down_price;

        @TableId
        private BigDecimal tax_amount;

        @TableId
        private BigDecimal not_tax_amount;

        @TableId
        private BigDecimal invoice_tax;

        @TableId
        private BigDecimal tax_ht_amount;

        @TableId
        private BigDecimal not_tax_ht_amount;

        @TableId
        private BigDecimal tax_difference;

        @TableId
        private BigDecimal not_tax_difference;

        @TableId
        private String operator_by;

        @TableId
        private String operator_date;

        @TableId
        private String remark;

        @TableId
        private String contract_no;

        @TableId
        private String vin_id;

        @TableId
        private String pay_apply;

        @TableId
        private String data_type;

        @TableId
        private String js_no;


}
