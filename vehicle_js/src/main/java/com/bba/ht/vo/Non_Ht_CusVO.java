package com.bba.ht.vo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.bba.common.annotation.Column;
import com.bba.common.annotation.Table;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Table("NON_HT_CUS")
@TableName("NON_HT_CUS")
@Data
public class Non_Ht_CusVO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableId
    private String sheet_no;

    @TableId
    private String contract_no;
    @Column
    @TableId
    private String cus_no;
    @Column
    @TableId
    private String cus_name;
    @Column
    @TableId
    private String contract_type;

    @TableId
    private String xunjia_no;
    @Column
    @TableId
    private String contract_name;

    @TableId
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date begin_date;

    @TableId
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date end_date;

    @TableId
    private String remark;

    @TableId
    private String state;

    @TableId
    private String create_by;

    @TableId
    private Date create_date;

    @TableId
    private String check_by;

    @TableId
    private Date check_date;

    @TableId
    private String cus_contract_no;

    @TableId
    private BigDecimal tax_rate;

}
