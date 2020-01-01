package com.bba.jcda.vo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

@TableName("CR_CUSTOMER")
@Data
public class Cr_CustomerVO {

    @TableId
    private Integer id;

    @TableId
    private String type;

    @TableId
    private String code;

    @TableId
    private String name;

    @TableId
    private String tax;

    @TableId
    private String in_address;

    @TableId
    private String bank;

    @TableId
    private String bank_no;

    @TableId
    private String in_tel;

    @TableId
    private String status;

    @TableId
    private String pri;

    @TableId
    private String city;

    @TableId
    private String address;

    @TableId
    private String remark;

    @TableId
    private String create_by;

    @TableId
    private Date create_date;

}
