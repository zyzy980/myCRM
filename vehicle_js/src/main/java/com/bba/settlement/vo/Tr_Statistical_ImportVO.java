package com.bba.settlement.vo;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.bba.common.annotation.Column;
import com.bba.common.annotation.Table;
import com.bba.common.enums.BooleanEnum;
import com.bba.common.enums.TypeSerializerEnum;
import lombok.Data;




/*
* 商品车统计 - 导入
* */
@Table("tr_statistical_import")
@Data
public class Tr_Statistical_ImportVO {

        /**
         * 字段名
         * */
        @Column(key = BooleanEnum.YES)
        private String id;

        @Excel(name = "序号")
        @Column
        private Integer num;
        /**
         * 字段名 业务完成月
         * */
        @Excel(name = "业务完成月")
        @Column
        private String bus_completion_month;
        /**
         * 字段名 统计月
         * */
        @Excel(name = "统计月")
        @Column
        private String statistical_month;
        /**
         * 字段名 发运日期
         * */
        @Excel(name = "发运日期")
        @Column(type = TypeSerializerEnum.DATETIME)
        private String begin_date;
        /**
         * 字段名 收车日期
         * */
        @Excel(name = "收车日期")
        @Column(type = TypeSerializerEnum.DATETIME)
        private String receipt_date;
        /**
         * 字段名 属性
         * */
        @Excel(name = "属性")
        @Column
        private String attribute;
        /**
         * 字段名 项目
         * */
        @Excel(name = "项目")
        @Column
        private String project;
        /**
         * 字段名 类型
         * */
        @Excel(name = "类型")
        @Column
        private String type;
        /**
         * 字段名 询价号
         * */
        @Excel(name = "询价号")
        @Column
        private String xunjia_no;
        /**
         * 字段名 客户编号
         * */
        @Excel(name = "客户")
        @Column
        private String cus_no;
        /**
         * 字段名 合同号
         * */
        @Excel(name = "合同号")
        @Column
        private String contract_no;
        /**
         * 字段名 客户合同号
         * */
        @Excel(name = "客户合同号")
        @Column
        private String cus_contract_no;
        /**
         * 字段名 分包商
         * */
        @Excel(name = "分包商")
        @Column
        private String carrier_no;
        /**
         * 字段名 aic
         * */
        @Excel(name = "AIC")
        @Column
        private String aic;
        /**
         * 字段名 运单号
         * */
        @Excel(name = "运单号")
        @Column
        private String vdr_no;
        /**
         * 字段名 vin
         * */
        @Excel(name = "VIN码")
        @Column
        private String vin;
        /**
         * 字段名 运输方式
         * */
        @Excel(name = "运输方式")
        @Column
        private String trans_mode;
        /**
         * 字段名 起点
         * */
        @Excel(name = "起点")
        @Column
        private String begin_city;
        /**
         * 字段名 终点
         * */
        @Excel(name = "终点")
        @Column
        private String end_city;
        /**
         * 字段名 目的省份
         * */
        @Excel(name = "省份")
        @Column
        private String end_province;
        /**
         * 字段名 经销商名称
         * */
        @Excel(name = "经销店名称")
        @Column
        private String dealer_name;
        /**
         * 字段名 合同里程
         * */
        @Excel(name = "合同里程")
        @Column
        private String mil;
        /**
         * 字段名 车型
         * */
        @Excel(name = "车型")
        @Column
        private String car_type;
        /**
         * 字段名 对上单价
         * */
        @Excel(name = "对上单价")
        @Column
        private Double price;
        /**
         * 字段名 运费收入小计
         * */
        @Excel(name = "运费收入小计")
        @Column
        private Double not_tax_freight;
        /**
         * 字段名 保费收入
         * */
        @Excel(name = "保费收入")
        @Column
        private Double not_tax_premium;
        /**
         * 字段名 其他收入
         * */
        @Excel(name = "其他收入")
        @Column
        private Double not_tax_other_amount;
        /**
         * 字段名 统计收入
         * */
        @Excel(name = "统计收入")
        @Column
        private Double not_tax_amount;
        /**
         * 字段名 对下价格
         * */
        @Excel(name = "对下价格")
        @Column
        private Double not_tax_down_price;
        /**
         * 字段名 对下其他费用
         * */
        @Excel(name = "其他费用")
        @Column
        private Double not_tax_other_down_amount;
        /**
         * 字段名 对下保费
         * */
        @Excel(name = "保费支出")
        @Column
        private Double not_tax_down_premium;
        /**
         * 字段名 对下统计成本
         * */
        @Excel(name = "统计成本")
        @Column
        private Double not_tax_down_amount;
        /**
         * 字段名 重损备注
         * */
        @Excel(name = "重损备注")
        @Column
        private String zs_remark;
        /**
         * 字段名 收单日期
         * */
        @Excel(name = "收单日期")
        @Column(type = TypeSerializerEnum.DATETIME)
        private String receipt_sheet_date;
        /**
         * 字段名 送单日期
         * */
        @Excel(name = "送单日期")
        @Column(type = TypeSerializerEnum.DATETIME)
        private String delivery_date;
        /**
         * 字段名 回单日期
         * */
        @Excel(name = "回单日期")
        @Column(type = TypeSerializerEnum.DATETIME)
        private String return_date;
        /**
         * 字段名 结算异常备注
         * */
        @Excel(name = "结算异常备注")
        @Column
        private String js_unusual_remark;
        /**
         * 字段名 结算号
         * */
        @Excel(name = "结算号")
        @Column
        private String js_no;
        /**
         * 字段名 开票日期
         * */
        @Excel(name = "开票日期")
        @Column(type = TypeSerializerEnum.DATETIME)
        private String invoice_date;
        /**
         * 字段名 发票号码
         * */
        @Excel(name = "发票号码")
        @Column
        private String invoice_no;
        /**
         * 字段名 实际收入含税
         * */
        @Excel(name = "实际收入-含税")
        @Column
        private Double real_tax_amount;
        /**
         * 字段名 实际收入不含税
         * */
        @Excel(name = "实际收入-不含税")
        @Column
        private Double real_ntax_amount;
        /**
         * 字段名 实际成本不含税
         * */
        @Excel(name = "实际成本-不含税")
        @Column
        private Double real_cost;
        /**
         * 字段名 结算进度
         * */
        @Excel(name = "结算进度")
        @Column
        private String up_js_state;
        /**
         * 字段名 商务审核人
         * */
        @Excel(name = "商务审核人")
        @Column
        private String business_auditor;
        /**
         * 字段名 导入时间
         * */
        @Excel(name = "导入时间")
        @Column(type = TypeSerializerEnum.DATETIME)
        private String import_date;
        /**
         * 字段名 导入人
         * */
        @Excel(name = "导入人")
        @Column
        private String import_by;
        /**
         * 字段名 账单编号，业务员自编
         * */
        @Excel(name = "账单编号")
        @Column
        private String bill_number;
        /**
         * 字段名 实际成本含税
         * */
        @Excel(name = "实际成本-含税")
        @Column
        private Double tax__real_cost;
        /**
         * 字段名 对下开票月份
         * */
        @Excel(name = "对下开票月份")
        @Column(type = TypeSerializerEnum.DATETIME)
        private String down_invoice_month;
        /**
         * 字段名 下家开票号码
         * */
        @Excel(name = "下家发票号码")
        @Column
        private String down_invoice_no;
        /**
         * 字段名 对下结算进度
         * */
        @Excel(name = "对下结算进度")
        @Column
        private String down_js_state;
        /**
         * 字段名 付款计划提报月
         * */
        @Excel(name = "付款计划提报月")
        @Column(type = TypeSerializerEnum.DATETIME)
        private String down_pay_plan_date;
        /**
         * 字段名 车牌号
         * */
        @Excel(name = "车牌号")
        @Column
        private String car_no;
        /**
         * 字段名 备注
         * */
        @Excel(name = "备注")
        @Column
        private String remark;

}
