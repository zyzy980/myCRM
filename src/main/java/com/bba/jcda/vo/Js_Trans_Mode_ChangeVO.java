package com.bba.jcda.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


@TableName("JS_TRANS_MODE_CHANGE")
@Data
public class Js_Trans_Mode_ChangeVO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableId
    @Excel(name = "第一段路线")
    private String first_route;

    @TableId
    @Excel(name = "第二段路线")
    private String two_route;

    @TableId
    @Excel(name = "第三段路线")
    private String three_route;

    @TableId
    @Excel(name = "运输方式")
    private String trans_mode;

    @TableId
    private String create_by;

    @TableId
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_date;

}
