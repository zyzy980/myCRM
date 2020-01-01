package com.bba.dz.vo;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.bba.common.annotation.Column;
import com.bba.common.annotation.Table;
import com.bba.common.enums.BooleanEnum;
import com.bba.common.enums.TypeSerializerEnum;
import lombok.Data;

@Table("tr_payment_plan")
@Data
public class Tr_Payment_PlanVO {

        /**
         * 字段名
         * */
        @Column(key= BooleanEnum.YES)
        private String id;
        /**
         * 字段名 类型
         * */
        @Excel(name = "类型")
        @Column
        private String type;
        /**
         * 字段名 状态
         * */
        @Excel(name = "状态")
        @Column
        private String state;
        /**
         * 字段名 承运商编号
         * */
        @Excel(name = "承运商编号")
        @Column
        private String carrier_no;
        /**
         * 字段名 承运商名称
         * */
        @Excel(name = "承运商名称")
        @Column
        private String carrier_name;
        /**
         * 字段名 发票号
         * */
        @Excel(name = "发票号")
        @Column
        private String invoice_no;
        /**
         * 字段名 交接单号
         * */
        @Excel(name = "交接单号")
        @Column
        private String handover_no;
        /**
         * 字段名 vin码
         * */
        @Excel(name = "VIN码")
        @Column
        private String vin;
        /**
         * 字段名 发运单号
         * */
        @Excel(name = "发运单号")
        @Column
        private String vdr_no;
        /**
         * 字段名 经销商名称
         * */
        @Excel(name = "经销商名称")
        @Column
        private String dealer_name;
        /**
         * 字段名 发运时间
         * */
        @Excel(name = "发运时间")
        @Column(type= TypeSerializerEnum.DATETIME)
        private String begin_date;
        /**
         * 字段名 收车时间
         * */
        @Excel(name = "收车时间")
        @Column(type= TypeSerializerEnum.DATETIME)
        private String receipt_date;
        /**
         * 字段名 收单日期
         * */
        @Excel(name = "收单日期")
        @Column(type= TypeSerializerEnum.DATETIME)
        private String receipt_sheet_date;
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
        private String begin_address;
        /**
         * 字段名 终点
         * */
        @Excel(name = "终点")
        @Column
        private String end_address;
        /**
         * 字段名 运输车型
         * */
        @Excel(name = "运输车型")
        @Column
        private String car_type;
        /**
         * 字段名 承运数量
         * */
        @Excel(name = "承运数量")
        @Column
        private Integer chengyun_qty;
        /**
         * 字段名 对下单价
         * */
        @Excel(name = "对下单价")
        @Column
        private Double down_price;
        /**
         * 字段名 开票金额含税
         * */
        @Excel(name = "开票金额含税")
        @Column
        private Double tax_amount;
        /**
         * 字段名 开票金额不含税
         * */
        @Excel(name = "开票金额不含税")
        @Column
        private Double not_tax_amount;
        /**
         * 字段名 发票税率
         * */
        @Excel(name = "发票税率")
        @Column
        private Double invoice_tax;
        /**
         * 字段名 合同价含税
         * */
        @Excel(name = "合同价含税")
        @Column
        private Double tax_ht_amount;
        /**
         * 字段名 合同价不含税
         * */
        @Excel(name = "合同价不含税")
        @Column
        private Double not_tax_ht_amount;
        /**
         * 字段名 差额含税
         * */
        @Excel(name = "差额含税")
        @Column
        private Double tax_difference;
        /**
         * 字段名 差额不含税
         * */
        @Excel(name = "差额不含税")
        @Column
        private Double not_tax_difference;
        /**
         * 字段名 操作人
         * */
        @Excel(name = "操作人")
        @Column
        private String operator_by;
        /**
         * 字段名 操作时间
         * */
        @Excel(name = "操作时间")
        @Column(type= TypeSerializerEnum.DATETIME)
        private String operator_date;
        /**
         * 字段名 备注
         * */
        @Excel(name = "备注")
        @Column
        private String remark;

        @Column
        private String data_type;

        @Column
        private String vin_id;

        @Column
        private String pay_apply;

        @Column
        private String contract_no;

        @Column
        private String pay_expect;//预付号
        @Column
        private String js_no;
}
