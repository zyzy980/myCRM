package com.bba.wdgl.vo;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("QUESTIONS")
@Data
public class QusetionsVO {

        @TableId(type = IdType.AUTO)
        private Integer id;

        @TableId
        private String type;

        @TableId
        private String module;

        @TableId
        private String sheet_no;

        @TableId
        private String question;

        @TableId
        private String answer;

        @TableId
        private String remark;

        @TableId
        private String state;

        @TableId
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date used_date;

        @TableId
        private String create_by;

        @TableId
        private String used_by;

        @TableId
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date create_date;
}
