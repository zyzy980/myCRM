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
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date question_date;

        @TableId
        private String create_by;

        @TableId
        private String used_by;

        @TableId
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date create_date;

        @TableId
        private String huawei_no;

        @TableId
        private String account_manager;

        @TableId
        private String contact_info;

        @TableId
        private String contact_name;

        @TableId
        private String handler;

        @TableId
        private String priority;

        @TableId
        private String feedback_huawei;

        @TableId
        private String huawei_answer;

        @TableId
        private String work_order_no;
}
