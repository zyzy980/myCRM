package com.bba.settlement.vo;

import com.bba.common.annotation.Column;
import com.bba.common.annotation.Table;
import com.bba.common.enums.BooleanEnum;
import com.bba.common.enums.TypeSerializerEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

//补差表
@Table("js_compensation")
@Data
public class Js_CompensationVO {


    /**
     * 字段名 id
     */
    @Column(key = BooleanEnum.YES)
    private String id;
    /**
     * 字段名 车辆项目
     */
    @Column
    private String vehicle_project;
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
     * 字段名 起运日期
     */
    @Column(type = TypeSerializerEnum.DATE)
    private String begin_date;
    /**
     * 字段名 收车日期
     */
    @Column(type = TypeSerializerEnum.DATE)
    private String receipt_date;
    /**
     * 字段名 小车车型
     */
    @Column
    private String car_type;
    /**
     * 字段名 运输方式
     */
    @Column
    private String trans_mode;
    /**
     * 字段名 客户编号
     */
    @Column
    private String cus_no;
    /**
     * 字段名 客户名称
     */
    @Column
    private String cus_name;
    /**
     * 字段名 类型
     */
    @Column
    private String type;
    /**
     * 字段名 对账单号
     */
    @Column
    private String dz_sheet;
    /**
     * 字段名 账单编号
     */
    @Column
    private String bill_number;
    /**
     * 字段名 前合同号
     */
    @Column
    private String befor_contract_no;
    /**
     * 字段名 前合同类型
     */
    @Column
    private String befor_contract_type;
    /**
     * 字段名 后合同号
     */
    @Column
    private String after_contract_no;
    /**
     * 字段名 后合同类型
     */
    @Column
    private String after_contract_type;
    /**
     * 字段名 结算号
     */
    @Column
    private String js_no;
    /**
     * 字段名 js_project
     */
    @Column
    private String js_batch;
    /**
     * 字段名 结算数据id
     */
    @Column
    private String vin_id;
    /**
     * 字段名 vin
     */
    @Column
    private String vin;
    /**
     * 字段名 承运商
     */
    @Column
    private String carrier_no;
    /**
     * 字段名 承运商名称
     */
    @Column
    private String carrier_name;
    /**
     * 字段名 税率
     */
    @Column
    private BigDecimal tax_rate;
    /**
     * 字段名 应收运费rmb（含税）
     */
    @Column
    private BigDecimal tax_up_total;
    /**
     * 字段名 应收运费rmb（不含税）
     */
    @Column
    private BigDecimal ntax_up_total;
    /**
     * 字段名 税额
     */
    @Column
    private BigDecimal tax_amount;
    /**
     * 字段名 tax_down_total
     */
    @Column
    private BigDecimal tax_down_total;
    /**
     * 字段名 ntax_down_total
     */
    @Column
    private BigDecimal ntax_down_total;
    /**
     * 字段名 备注
     */
    @Column
    private String remark;
    /**
     * 字段名 状态
     */
    @Column
    private String state;
    /**
     * 字段名 invoice_no
     */
    @Column
    private String invoice_no;
    /**
     * 字段名 invoice_date
     */
    @Column(type = TypeSerializerEnum.DATETIME)
    private String invoice_date;
    /**
     * 字段名 invoice_flag
     */
    @Column
    private String invoice_flag;


    @Column
    private String dealer_name;
    @Column
    private String vdr_no;
    @Column
    private BigDecimal old_tax_amount;
    @Column
    private BigDecimal old_ntax_amount;
    @Column
    private BigDecimal now_tax_amount;
    @Column
    private BigDecimal now_ntax_amount;
    @Column
    private String mil;//现合同里程
    @Column
    private BigDecimal price;//现合同单价
    @Column
    private BigDecimal not_tax_premium;//保费补差
    @Column
    private BigDecimal not_tax_other_amount;//其他费用补差
    @Column
    private BigDecimal not_tax_freight;//运费补差
    @Column
    private String after_contract_sheet_no;//现合同单号
    @Column
    private Date create_date;
    @Column
    private String create_by;
}
