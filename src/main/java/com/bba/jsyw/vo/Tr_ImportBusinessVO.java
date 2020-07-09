package com.bba.jsyw.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 业务数据--导入VO
 */
@TableName("TR_BUSINESS")
@Data
public class Tr_ImportBusinessVO extends Tr_BusinessVO{

    @TableId
    @Excel(name = "承运商编号")
    private String carrier_no;
}
