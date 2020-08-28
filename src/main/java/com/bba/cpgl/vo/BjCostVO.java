package com.bba.cpgl.vo;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@TableName("BJ_COST")
@Data
public class BjCostVO {

        @TableId(type = IdType.AUTO)
        private Integer id;

        @TableId
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date used_date;

        @TableId
        private BigDecimal cost;

        @TableId
        private String cost_remark;

        @TableId
        private String used_by;

        @TableId
        private String sheet_no;


}
