package com.bba.xtgl.vo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

@TableName("SYS_DICTIONARY_DATA")
@Data
public class Sys_Dictionary_DataVO {

    @TableId
    private Integer sn;

    @TableId
    private String dictext;

    @TableId
    private String dicvalue;

    @TableId
    private String typecode;

    @TableId
    private String dicorder;

    @TableId
    private String remark;

    @TableId
    private String isdefault;

    @TableId
    private String within_code;

    @TableId
    private String dictext_en;

}
