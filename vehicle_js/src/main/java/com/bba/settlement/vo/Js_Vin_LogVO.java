package com.bba.settlement.vo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@TableName("JS_VIN_LOG")
@Data
public class Js_Vin_LogVO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableId
    private String check_by;

    @TableId
    private Date check_date;

    @TableId
    private String check_state;

    @TableId
    private String remark;

    @TableId
    private String vdr_no;
}
