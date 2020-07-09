package com.bba.jsyw.vo;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 非商品车业务数据表--承运商
 */
@TableName("TR_NON_BUSINESS_CARRIER")
@Data
public class Tr_Non_Business_CarrierVO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableId
    private Long m_id;

    @TableId
    private String remark;

    @TableId
    private String create_by;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableId
    private Date create_date;

    @TableId
    private String data_from;

    @TableId
    private String carrier_no;

    @TableId
    private String carrier_name;

    @TableId
    private String business_order;

    @TableId
    private String trans_mode;

    @TableId
    private String begin_city;

    @TableId
    private String end_city;

    @TableId
    private BigDecimal down_js_qty;

    @TableId
    private String trans_fangan;
}
