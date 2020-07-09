package com.bba.nosettlement.vo;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.bba.common.annotation.Column;
import com.bba.common.annotation.Table;
import com.bba.common.enums.BooleanEnum;
import com.bba.common.enums.TypeSerializerEnum;
import lombok.Data;




/*
* 非商品车统计 - 导入
* */
@Table("tr_non_statistical_import")
@Data
public class Tr_Non_Statistical_ImportVO {

        /**
         * 字段名 id
         */
        @Excel(name = "id")
        @Column(key = BooleanEnum.YES)
        private String id;

        @Excel(name = "序号")
        @Column
        private String num;
        /**
         * 字段名 业务完成月
         */
        @Excel(name = "业务完成月")
        @Column
        private String bus_completion_month;
        /**
         * 字段名 统计月
         */
        @Excel(name = "统计月")
        @Column
        private String statistical_month;
        /**
         * 字段名 询价号
         */
        @Excel(name = "询价号")
        @Column
        private String xunjia_no;
        /**
         * 字段名 合同号
         */
        @Excel(name = "合同号")
        @Column
        private String contract_no;
        /**
         * 字段名 属性
         */
        @Excel(name = "属性")
        @Column
        private String attribute;
        /**
         * 字段名 项目
         */
        @Excel(name = "项目")
        @Column
        private String project;
        /**
         * 字段名 需求部门
         */
        @Excel(name = "需求部门")
        @Column
        private String demand_sector;
        /**
         * 字段名 客户
         */
        @Excel(name = "客户")
        @Column
        private String cus_no;
        /**
         * 字段名 发运指令人
         */
        @Excel(name = "发运指令人")
        @Column
        private String applican;
        /**
         * 字段名 交接单号
         */
        @Excel(name = "交接单号")
        @Column
        private String handover_no;
        /**
         * 字段名 发运日期
         */
        @Excel(name = "发运日期")
        @Column(type=TypeSerializerEnum.DATETIME)
        private String begin_date;
        /**
         * 字段名 收车日期
         */
        @Excel(name = "收车日期")
        @Column(type=TypeSerializerEnum.DATETIME)
        private String receipt_date;
        /**
         * 字段名 运输方式
         */
        @Excel(name = "运输方式")
        @Column
        private String trans_mode;
        /**
         * 字段名 运输方案
         */
        @Excel(name = "运输方案")
        @Column
        private String trans_fangan;
        /**
         * 字段名 发运地
         */
        @Excel(name = "发运地")
        @Column
        private String begin_city;
        /**
         * 字段名 目的地
         */
        @Excel(name = "目的地")
        @Column
        private String end_city;
        /**
         * 字段名 运输车型
         */
        @Excel(name = "运输车型")
        @Column
        private String car_type;
        /**
         * 字段名 合同线路距离
         */
        @Excel(name = "合同路线距离")
        @Column
        private Integer mil;
        /**
         * 字段名 （合同）运输费用/公里/台
         */
        @Excel(name = "(合同)运输费用/公里/台")
        @Column
        private Double ht_price;
        /**
         * 字段名 承运数量
         */
        @Excel(name = "承运数量")
        @Column
        private Integer chengyun_qty;
        /**
         * 字段名 计费数量_收
         */
        @Excel(name = "计费数量-收")
        @Column
        private Integer jifei_qty;
        /**
         * 字段名 （合同）运费小计-收
         */
        @Excel(name = "（合同）运费小计-收")
        @Column
        private Double not_tax_freight;
        /**
         * 字段名 （合同）保费/台-收
         */
        @Excel(name = "（合同）保费/台-收")
        @Column
        private Double not_tax_premium_price;
        /**
         * 字段名 保费小计-收
         */
        @Excel(name = "保费小计-收")
        @Column
        private Double not_tax_premium;
        /**
         * 字段名 其他费用-收
         */
        @Excel(name = "其他费用-收")
        @Column
        private Double not_tax_other_amount;
        /**
         * 字段名 备注_收
         */
        @Excel(name = "备注-收")
        @Column
        private String up_remark;
        /**
         * 字段名 统计收入
         */
        @Excel(name = "统计收入")
        @Column
        private Double not_tax_amount;
        /**
         * 字段名 分包商
         */
        @Excel(name = "分包商")
        @Column
        private String carrier_no;
        /**
         * 字段名 运费成本
         */
        @Excel(name = "运费成本")
        @Column
        private Double freight_zucheng;
        /**
         * 字段名 （合同）计费标准/公里/台
         */
        @Excel(name = "（合同）计费标准/公里/台")
        @Column
        private Double ht_jifei_biaozhun;
        /**
         * 字段名 计费数量_成本
         */
        @Excel(name = "计费数量-成本")
        @Column
        private Double down_jifei_qty;
        /**
         * 字段名 （合同）运费小计-成本
         */
        @Excel(name = "（合同）运费小计-成本")
        @Column
        private Double not_tax_down_freight;
        /**
         * 字段名 保费/台-成本
         */
        @Excel(name = "保费/台-成本")
        @Column
        private Double down_premium_price;
        /**
         * 字段名 保费小计-成本
         */
        @Excel(name = "保费小计-成本")
        @Column
        private Double down_premium_total;
        /**
         * 字段名 其他费用-成本
         */
        @Excel(name = "其他费用-成本")
        @Column
        private Double not_tax_other_down_amount;
        /**
         * 字段名 备注成本
         */
        @Excel(name = "备注成本")
        @Column
        private String down_remark;
        /**
         * 字段名 统计成本
         */
        @Excel(name = "统计成本")
        @Column
        private Double down_amount;
        /**
         * 字段名 收单日期
         */
        @Excel(name = "收单日期")
        @Column(type=TypeSerializerEnum.DATETIME)
        private String receipt_sheet_date;
        /**
         * 字段名 送单日期
         */
        @Excel(name = "送单日期")
        @Column(type=TypeSerializerEnum.DATETIME)
        private String delivery_date;
        /**
         * 字段名 回单日期
         */
        @Excel(name = "回单日期")
        @Column(type=TypeSerializerEnum.DATETIME)
        private String return_date;
        /**
         * 字段名 结算异常备注
         */
        @Excel(name = "结算异常备注")
        @Column
        private String js_unusual_remark;
        /**
         * 字段名 结算号
         */
        @Excel(name = "结算号")
        @Column
        private String js_no;
        /**
         * 字段名 发票号码
         */
        @Excel(name = "发票号码")
        @Column
        private String invoice_no;
        /**
         * 字段名 开票日期
         */
        @Excel(name = "开票日期")
        @Column(type=TypeSerializerEnum.DATETIME)
        private String invoice_date;
        /**
         * 字段名 实际收入含税
         */
        @Excel(name = "实际收入含税")
        @Column
        private Double real_tax_amount;
        /**
         * 字段名 实际收入不含税
         */
        @Excel(name = "实际收入不含税")
        @Column
        private Double real_ntax_amount;
        /**
         * 字段名 实际成本
         */
        @Excel(name = "实际成本")
        @Column
        private Double real_cost;
        /**
         * 字段名 结算进度
         */
        @Excel(name = "结算进度")
        @Column
        private String up_js_state;
        /**
         * 字段名 商务审核人
         */
        @Excel(name = "商务审核人")
        @Column
        private String business_auditor;

        /**
         * 字段名 账单编号，业务自编
         */
        @Excel(name = "账单编号")
        @Column
        private String bill_number;
        /**
         * 字段名 实际成本含税
         */
        @Excel(name = "实际成本含税")
        @Column
        private Double tax__real_cost;
        /**
         * 字段名 对下开票月份
         */
        @Excel(name = "对下开票月份")
        @Column(type=TypeSerializerEnum.DATETIME)
        private String down_invoice_month;
        /**
         * 字段名 下家发票号码
         */
        @Excel(name = "下家发票号码")
        @Column
        private String down_invoice_no;
        /**
         * 字段名 对下结算进度
         */
        @Excel(name = "对下结算进度")
        @Column
        private String down_js_state;
        /**
         * 字段名 付款计划提报月
         */
        @Excel(name = "付款计划提报月")
        @Column(type=TypeSerializerEnum.DATETIME)
        private String down_pay_plan_date;
        /**
         * 字段名 车牌号
         */
        @Excel(name = "车牌号")
        @Column
        private String car_no;
        /**
         * 字段名 备注
         */
        @Excel(name = "备注")
        @Column
        private String remark;
        /**
         * 字段名 导入时间
         */
        @Column(type=TypeSerializerEnum.DATETIME)
        private String import_date;
        /**
         * 字段名 导入人
         */
        @Column
        private String import_by;
}
