package com.bba.dz.vo;


import com.bba.common.annotation.Column;
import com.bba.common.annotation.Table;
import com.bba.common.enums.BooleanEnum;
import com.bba.common.enums.TypeSerializerEnum;
import lombok.Data;



@Data
@Table("JS_DZ_SHEET_DETAIL")
public class Js_Dz_Sheet_DetailAccountVO {

        /**
         * 字段名 id
         * */
        @Column(key = BooleanEnum.YES)
        private String id;
        /**
         * 字段名 询价号
         * */
        @Column
        private String xunjia_no;
        /**
         * 字段名 运单
         * */
        @Column
        private String vdr_no;
        /**
         * 字段名 vin
         * */
        @Column
        private String vin;
        /**
         * 字段名 客户编号
         * */
        @Column
        private String cus_no;
        /**
         * 字段名 客户名称
         * */
        @Column
        private String cus_name;
        /**
         * 字段名 经销商编号
         * */
        @Column
        private String dealer;
        /**
         * 字段名 经销商名称
         * */
        @Column
        private String dealer_name;
        /**
         * 字段名 质损状态
         * */
        @Column
        private String qu_state;
        /**
         * 字段名 起运地
         * */
        @Column
        private String begin_city;
        /**
         * 字段名 目的地
         * */
        @Column
        private String end_city;
        /**
         * 字段名 运输方式
         * */
        @Column
        private String trans_mode;
        /**
         * 字段名 小车车型
         * */
        @Column
        private String car_type;
        /**
         * 字段名 公路里程
         * */
        @Column
        private String mil;
        /**
         * 字段名 发运日期
         * */
        @Column(type = TypeSerializerEnum.DATETIME)
        private String begin_date;
        /**
         * 字段名 收车日期
         * */
        @Column(type = TypeSerializerEnum.DATETIME)
        private String receipt_date;
        /**
         * 字段名 数据类型
         * */
        @Column
        private String js_data_type;
        /**
         * 字段名 车辆项目
         * */
        @Column
        private String vehicle_project;
        /**
         * 字段名 数据来源
         * */
        @Column
        private String data_from;
        /**
         * 字段名 状态
         * */
        @Column
        private String data_state;
        /**
         * 字段名 合同号
         * */
        @Column
        private String contract_no;
        /**
         * 字段名 不含税运费
         * */
        @Column
        private String not_tax_freight;
        /**
         * 字段名 不含税其他费用
         * */
        @Column
        private String not_tax_other_amount;
        /**
         * 字段名 承运数量
         * */
        @Column
        private String shipment_qty;
        /**
         * 字段名 结算数量
         * */
        @Column
        private String js_qty;
        /**
         * 字段名 不含税计费标准
         * */
        @Column
        private String not_tax_price;
        /**
         * 字段名 结算号
         * */
        @Column
        private String js_no;
        /**
         * 字段名 保费
         * */
        @Column
        private String not_tax_premium;
        /**
         * 字段名 含税合计
         * */
        @Column
        private String tax_amount;
        /**
         * 字段名 不含税合计
         * */
        @Column
        private String not_tax_amount;
        /**
         * 字段名 备注
         * */
        @Column
        private String remark;
        /**
         * 字段名 创建人员
         * */
        @Column
        private String create_by;
        /**
         * 字段名 创建时间
         * */
        @Column(type = TypeSerializerEnum.DATETIME)
        private String create_date;
        /**
         * 字段名 结算批次
         * */
        @Column
        private String js_batch;
        /**
         * 字段名 对账单号
         * */
        @Column
        private String dz_sheet;
        /**
         * 字段名 单价
         * */
        @Column
        private String price;
        /**
         * 字段名 结算数据id
         * */
        @Column
        private String vin_id;

        @Column
        private String tax_rate;
        @Column
        private String bill_number;
        @Column
        private String type;
        @Column
        private String his_flag;
        @Column
        private String contract_type;
        @Column
        private String contract_sheet_no;

        private String first_mileage;
        private String first_price;
        private String two_mileage;
        private String two_price;
        private String three_mileage;
        private String three_price;
        private String first_route;
        private String two_route;
        private String three_route;




}
