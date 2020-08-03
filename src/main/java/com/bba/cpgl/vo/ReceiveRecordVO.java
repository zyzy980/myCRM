package com.bba.cpgl.vo;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@TableName("BJ_RECEIVE_RECORD")
@Data
public class ReceiveRecordVO {

        @TableId(type = IdType.AUTO)
        private Integer id;

        @TableId
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date receive_date;

        @TableId
        private BigInteger box_num;

        @TableId
        private BigDecimal box_price;

        @TableId
        private BigDecimal box_total_price;

        @TableId
        private BigDecimal freight;

        @TableId
        private BigDecimal total_freight;

        @TableId
        private String docking_man;

        @TableId
        private String sheet_no;

        @TableId
        private String remark;

}
