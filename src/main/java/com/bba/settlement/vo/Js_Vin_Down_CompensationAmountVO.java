package com.bba.settlement.vo;


import com.baomidou.mybatisplus.annotations.TableField;
import com.bba.common.annotation.Column;
import com.bba.common.enums.BooleanEnum;
import com.bba.common.enums.TypeSerializerEnum;
import lombok.Data;

import java.util.Date;

@Data
public class Js_Vin_Down_CompensationAmountVO {

    /**
     * 字段名 id
     */
    @Column(key = BooleanEnum.YES)
    private String id;
    /**
     * 字段名 询价号
     */
    @Column
    private String xunjia_no;
    /**
     * 字段名 运单
     */
    @Column
    private String vdr_no;
    /**
     * 字段名 vin
     */
    @Column
    private String vin;
    /**
     * 字段名 经销商编号
     */
    @Column
    private String dealer;
    /**
     * 字段名 经销商名称
     */
    @Column
    private String dealer_name;
    /**
     * 字段名 质损状态
     */
    @Column
    private String qu_state;
    /**
     * 字段名 起运地
     */
    @Column
    private String begin_city;
    /**
     * 字段名 目的地
     */
    @Column
    private String end_city;
    /**
     * 字段名 运输方式
     */
    @Column
    private String trans_mode;
    /**
     * 字段名 小车车型
     */
    @Column
    private String car_type;
    /**
     * 字段名 发运日期
     */
    @Column(type=TypeSerializerEnum.DATE)
    private String begin_date;
    /**
     * 字段名 收车日期
     */
    @Column(type=TypeSerializerEnum.DATE)
    private String receipt_date;
    /**
     * 字段名 数据类型
     */
    @Column
    private String js_data_type;
    /**
     * 字段名 车辆项目
     */
    @Column
    private String vehicle_project;
    /**
     * 字段名 数据来源
     */
    @Column
    private String data_from;
    /**
     * 字段名 正式合同号
     */
    @Column
    private String contract_no;
    @Column
    private String contract_type;
    /**
     * 字段名 结算状态
     */
    @Column
    private String js_state;
    /**
     * 字段名 不含税运费
     */
    @Column
    private String not_tax_freight;
    /**
     * 字段名 不含税其他费用
     */
    @Column
    private String not_tax_other_amount;
    /**
     * 字段名 承运数量
     */
    @Column
    private String shipment_qty;
    /**
     * 字段名 结算数量
     */
    @Column
    private String js_qty;
    /**
     * 字段名 不含税计费标准
     */
    @Column
    private String not_tax_price;
    /**
     * 字段名 结算号
     */
    @Column
    private String js_no;
    /**
     * 字段名 含税合计
     */
    @Column
    private String tax_amount;
    /**
     * 字段名 不含税合计
     */
    @Column
    private String not_tax_amount;
    /**
     * 字段名 备注
     */
    @Column
    private String remark;
    /**
     * 字段名 创建人员
     */
    @Column
    private String create_by;
    /**
     * 字段名 创建时间
     */
    @Column(type=TypeSerializerEnum.DATETIME)
    private String create_date;
    /**
     * 字段名 结算批次
     */
    @Column
    private String js_batch;
    /**
     * 字段名 承运商编号
     */
    @Column
    private String carrier_no;
    /**
     * 字段名 承运商名称
     */
    @Column
    private String carrier_name;
    /**
     * 字段名 运输段顺序
     */
    @Column
    private String business_order;
    /**
     * 字段名 历史结算
     */
    @Column
    private String his_flag;

    @Column
    private String yugu_flag;

    @Column
    private String dz_sheet;

    @Column
    private String tax_rate;

    @TableField(exist = false)
    private String col_contract_type;

    @Column
    private String bill_number;

    @Column
    private String invoice_no;

    @Column
    private Date invoice_date;

    @Column
    private Date pay_plan_date;

    @Column
    private String old_tax_amount;
    @Column
    private String old_ntax_amount;
    @Column
    private String tax_down_bucha_total;
    @Column
    private String ntax_down_bucha_total;

}
