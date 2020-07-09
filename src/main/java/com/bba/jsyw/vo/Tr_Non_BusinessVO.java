package com.bba.jsyw.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 非商品车业务数据表--主表
 */
@TableName("TR_NON_BUSINESS")
@Data
public class Tr_Non_BusinessVO {

    @TableId(type = IdType.AUTO)
    @Excel(name = "ID")
    private Long id;

    @TableId
    @Excel(name = "属性")
    private String attribute;

    @TableId
    @Excel(name = "类型")
    private String type;

    @TableId
    @Excel(name = "交接单号（合同号）")
    private String handover_no;

    @TableId
    @Excel(name = "服务类型")
    private String service_type;

    @TableId
    @Excel(name = "VIN")
    private String vin;

    @TableId
    @Excel(name = "付款单位")
    private String cus_no;

    @TableId
    @Excel(name = "付款单位名称")
    private String cus_name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableId
    @Excel(name = "发运时间")
    private Date begin_date;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableId
    @Excel(name = "收车时间")
    private Date receipt_date;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableId
    @Excel(name = "收单日期")
    private Date receipt_sheet_date;

    @TableId
    @Excel(name = "运输方式")
    private String trans_mode;

    @TableId
    @Excel(name = "运输方案")
    private String trans_fangan;

    @TableId
    @Excel(name = "小车车型")
    private String car_type;

    @TableId
    @Excel(name = "承运数量")
    private BigDecimal shipment_qty;

    @TableId
    @Excel(name = "对上结费数量")
    private BigDecimal up_js_qty;

    @TableId
    @Excel(name = "启运城市")
    private String begin_city;

    @TableId
    @Excel(name = "目的城市")
    private String end_city;

    @TableId
    @Excel(name = "启运地址")
    private String begin_address;

    @TableId
    @Excel(name = "目的地地址")
    private String end_address;

    @TableId
    @Excel(name = "需求部门")
    private String demand_sector;

    @TableId
    @Excel(name = "申请人（发运指令人）")
    private String applicant;

    @TableId
    @Excel(name = "是否有牌")
    private String license_plate_flag;

    @TableId
    @Excel(name = "配载说明")
    private String peizai_info;

    @TableId
    @Excel(name = "是否保密")
    private String secrecy_flag;

    @TableId
    @Excel(name = "启运联系人")
    private String begin_link;

    @TableId
    @Excel(name = "目的地联系人")
    private String end_link;

    @TableId
    @Excel(name = "目的地联系人号码")
    private String end_link_tel;

    @TableId
    @Excel(name = "启运联系人号码")
    private String begin_link_tel;

    @TableId
    @Excel(name = "备注")
    private String remark;

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
    @Excel(name = "数据来源")
    private String data_from;

    @TableId
    @Excel(name = "数据状态")
    private String data_state;

    @TableId
    @Excel(name = "车牌号")
    private String car_no;

    @TableId
    @Excel(name = "结算价格")
    private String js_jiage;

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
    @Excel(name = "承运商")
    private String carrier_no;

    @TableField(exist = false)
    @Excel(name = "承运商名称")
    private String carrier_name;

    @TableField(exist = false)
    @Excel(name = "对下结费数量")
    private BigDecimal down_js_qty;

}
