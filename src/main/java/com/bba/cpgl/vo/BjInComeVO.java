package com.bba.cpgl.vo;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("BJ_INCOME")
@Data
public class BjInComeVO {

        @TableId(type = IdType.AUTO)
        private Integer id;

        @TableId
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date amount_date;

        @TableId
        private BigDecimal amount;

        @TableId
        private String remark;

        @TableId
        private String handler;

        @TableId
        private String sheet_no;

        @TableId
        private String state;


}
