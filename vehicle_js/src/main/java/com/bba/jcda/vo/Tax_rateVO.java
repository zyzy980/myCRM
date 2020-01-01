package com.bba.jcda.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.bba.common.annotation.Column;
import com.bba.common.enums.TypeSerializerEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author:Suwendaidi
 * @Date: 2019/8/1 11:08
 * 收款发票
 */
@TableName("JS_TAX_RATE")
@Data
public class Tax_rateVO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableId
    private BigDecimal tax_rate;

    @TableId
    @JsonFormat(pattern = "yyyy-MM")
    private Date tax_month;

    @TableId
    private String create_by;

    @TableId
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_date;

}
