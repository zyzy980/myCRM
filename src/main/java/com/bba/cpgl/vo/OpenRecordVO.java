package com.bba.cpgl.vo;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@TableName("BJ_OPEN_RECORD")
@Data
public class OpenRecordVO {

        @TableId(type = IdType.AUTO)
        private Integer id;

        @TableId
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date open_date;

        @TableId
        private String sheet_no;

        @TableId
        private String goods_name;

        @TableId
        private String sell_no;

        @TableId
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date sell_date;

        @TableId
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date sold_date;

        @TableId
        private Integer out_time;

        @TableId
        private String goods_type;

        @TableId
        private String box_size;

        @TableId
        private String box_value;

        @TableId
        private String kuaidi_company;

        @TableId
        private String damage;

        @TableId
        private BigDecimal goods_cost;

        @TableId
        private BigDecimal sell_price;

        @TableId
        private BigDecimal goods_profit;

        @TableId
        private String sell_mode;

        @TableId
        private String sold;

        @TableId
        private String remark;

        @TableId
        private String state;

        @TableId
        private String free_shipping;

        @TableId
        private BigDecimal express_fee;

}
