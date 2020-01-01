package com.bba.ht.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

import static com.baomidou.mybatisplus.enums.IdType.ID_WORKER;

@TableName("HT_CARRIER_FREIGHT")
@Data
public class Ht_Carrier_FreightVO {

    @TableId(type = ID_WORKER)
    private Long sn;

    @TableId
    private String sheet_no;

    @TableId
    @Excel(name = "运输方式")
    private String trans_mode;

    @TableId
    @Excel(name = "第一段里程")
    private BigDecimal first_mileage;

    @TableId
    @Excel(name = "第一段单价")
    private BigDecimal first_price;

    @TableId
    @Excel(name = "第二段里程")
    private BigDecimal two_mileage;

    @TableId
    @Excel(name = "第二段单价")
    private BigDecimal two_price;

    @TableId
    @Excel(name = "第三段里程")
    private BigDecimal three_mileage;

    @TableId
    @Excel(name = "第三段单价")
    private BigDecimal three_price;

    @TableField
    @Excel(name = "第一段路线")
    private String first_route;

    @TableField
    @Excel(name = "第二段路线")
    private String two_route;

    @TableField
    @Excel(name = "第三段路线")
    private String three_route;

    @TableId
    @Excel(name = "备注")
    private String remark;

    @TableId
    @Excel(name = "过海费")
    private BigDecimal cross_sea_amount;

    @TableId
    private String create_by;

    @TableId
    private Date create_date;

    @TableId
    @Excel(name = "保费")
    private BigDecimal premium;

    @TableId
    @Excel(name = "起运地")
    private String begin_city;

    @TableId
    @Excel(name = "目的地")
    private String end_city;

    @TableField(exist = false)
    private String contract_no;

    @TableField(exist = false)
    private String contract_type;
}
