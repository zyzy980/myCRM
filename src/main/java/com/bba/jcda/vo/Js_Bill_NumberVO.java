package com.bba.jcda.vo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.bba.common.annotation.Table;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


@Table("JS_BILL_NUMBER")
@Data
public class Js_Bill_NumberVO {

    @TableId(type = IdType.AUTO)
    private String id;

    @TableId
    private String bill_number;

    @TableId
    private String create_by;

    @TableId
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_date;

}
