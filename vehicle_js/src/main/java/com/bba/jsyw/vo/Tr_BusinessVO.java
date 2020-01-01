package com.bba.jsyw.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 业务数据表--主表
 */
@TableName("TR_BUSINESS")
@Data
public class Tr_BusinessVO {

    @TableId(type = IdType.AUTO)
    @Excel(name = "ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableId
    @Excel(name = "实际起运时间")
    private Date begin_datetime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableId
    @Excel(name = "收车时间")
    private Date receipt_datetime;

    @TableId
    @Excel(name = "发运单号")
    private String vdr_no;

    @TableId
    @Excel(name = "VIN")
    private String vin;

    @TableId
    @Excel(name = "运输方式")
    private String trans_mode;

    @TableId
    @Excel(name = "起运地")
    private String begin_city;

    @TableId
    @Excel(name = "目的地")
    private String end_city;

    @TableId
    @Excel(name = "目的省份")
    private String end_province;

    @TableId
    @Excel(name = "经销商编号")
    private String dealer_no;

    @TableId
    @Excel(name = "小车车型")
    private String car_type;

    @TableId
    @Excel(name = "备注")
    private String remark;

    @TableId
    @Excel(name = "备注")
    private String zs_remark;

    @TableId
    @Excel(name = "经销商名称")
    private String dealer_name;

    @TableId
    @Excel(name = "创建人")
    private String create_by;

    @TableId
    @Excel(name = "创建时间")
    private String create_date;

    @TableId
    @Excel(name = "车辆项目")
    private String vehicle_project;

    @TableId
    @Excel(name = "车牌号")
    private String car_no;

    @TableId
    @Excel(name = "数据来源")
    private String data_from;

    @TableId
    @Excel(name = "数据状态")
    private String data_state;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableId
    @Excel(name = "收单日期")
    private Date receipt_date;

    @TableId
    @Excel(name = "质损类型")
    private String mass_loss_type;

    @TableId
    @Excel(name = "VIP")
    private String up_js_flag;

    @TableId
    @Excel(name = "对下不结")
    private String down_js_flag;

    @TableId
    @Excel(name = "客户编号")
    private String cus_no;

    @TableId
    @Excel(name = "客户名称")
    private String cus_name;

    @TableId
    @Excel(name = "预付")
    private String yf_flag;

    @TableId
    @Excel(name = "AIC")
    private String aic;

    @TableId
    @Excel(name = "上传文件")
    private String upload_files;

    @TableId
    @Excel(name = "审核人")
    private String check_by;

    @TableId
    @Excel(name = "操作人")
    private String operator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableId
    @Excel(name = "审核时间")
    private Date check_date;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableId
    @Excel(name = "操作时间")
    private Date operator_date;

    @TableField(exist = false)
    @Excel(name = "承运商编号")
    private String carrier_no;

    @TableField(exist = false)
    @Excel(name = "承运商名称")
    private String carrier_name;

    @TableField(exist = false)
    private String edi_flag;

    @TableField(exist = false)
    private String batch_no;

}
