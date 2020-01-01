package com.bba.settlement.vo;


import com.bba.common.annotation.Column;
import com.bba.common.annotation.Table;
import com.bba.common.enums.BooleanEnum;
import com.bba.common.enums.TypeSerializerEnum;
import lombok.Data;



@Data
@Table("js_non_downtemp_vehicle")
public class Js_Non_DownTemp_VehicleVO {

        /**
         * 字段名 id
         * */
        @Column(key = BooleanEnum.YES)
        private String id;
        /**
         * 字段名 bus_id
         * */
        @Column
        private String bus_id;
        /**
         * 字段名 type
         * */
        @Column
        private String type;
        /**
         * 字段名 handover_no
         * */
        @Column
        private String handover_no;
        /**
         * 字段名 service_type
         * */
        @Column
        private String service_type;
        /**
         * 字段名 xunjia_no
         * */
        @Column
        private String xunjia_no;
        /**
         * 字段名 vin
         * */
        @Column
        private String vin;
        /**
         * 字段名 carrier_no
         * */
        @Column
        private String carrier_no;
        /**
         * 字段名 carrier_name
         * */
        @Column
        private String carrier_name;
        /**
         * 字段名 begin_date
         * */
        @Column(type=TypeSerializerEnum.DATETIME)
        private String begin_date;
        /**
         * 字段名 receipt_date
         * */
        @Column(type=TypeSerializerEnum.DATETIME)
        private String receipt_date;
        /**
         * 字段名 trans_mode
         * */
        @Column
        private String trans_mode;
        /**
         * 字段名 car_type
         * */
        @Column
        private String car_type;
        /**
         * 字段名 shipment_qty
         * */
        @Column
        private String shipment_qty;
        /**
         * 字段名 js_qty
         * */
        @Column
        private String js_qty;
        /**
         * 字段名 begin_address
         * */
        @Column
        private String begin_address;
        /**
         * 字段名 end_address
         * */
        @Column
        private String end_address;
        /**
         * 字段名 freight_basis
         * */
        @Column
        private String freight_basis;
        /**
         * 字段名 mil
         * */
        @Column
        private String mil;
        /**
         * 字段名 price
         * */
        @Column
        private String price;
        /**
         * 字段名 not_tax_losed_car_price
         * */
        @Column
        private String not_tax_losed_car_price;
        /**
         * 字段名 closed_num
         * */
        @Column
        private String closed_num;
        /**
         * 字段名 not_tax_price
         * */
        @Column
        private String not_tax_price;
        /**
         * 字段名 not_tax_freight
         * */
        @Column
        private String not_tax_freight;
        /**
         * 字段名 not_tax_other_amount
         * */
        @Column
        private String not_tax_other_amount;
        /**
         * 字段名 tax_amount
         * */
        @Column
        private String tax_amount;
        /**
         * 字段名 not_tax_amount
         * */
        @Column
        private String not_tax_amount;
        /**
         * 字段名 create_date
         * */
        @Column(type = TypeSerializerEnum.DATETIME)
        private String create_date;
        /**
         * 字段名 not_tax_rescue_truck_price
         * */
        @Column
        private String not_tax_rescue_truck_price;
        /**
         * 字段名 not_tax_rescue_truck_startoric
         * */
        @Column
        private String not_tax_rescue_truck_startoric;
        /**
         * 字段名 tax_rate
         * */
        @Column
        private String tax_rate;
        /**
         * 字段名 js_no
         * */
        @Column
        private String js_no;
        /**
         * 字段名 js_batch
         * */
        @Column
        private String js_batch;
        /**
         * 字段名 vehicle_project
         * */
        @Column
        private String vehicle_project;
        /**
         * 字段名 data_from
         * */
        @Column
        private String data_from;
        /**
         * 字段名 js_state
         * */
        @Column
        private String js_state;
        /**
         * 字段名 contract_no
         * */
        @Column
        private String contract_no;
        /**
         * 字段名 remark
         * */
        @Column
        private String remark;
        /**
         * 字段名 create_by
         * */
        @Column
        private String create_by;
        /**
         * 字段名 attribute
         * */
        @Column
        private String attribute;
        /**
         * 字段名 bill_number
         * */
        @Column
        private String bill_number;
        /**
         * 字段名 trans_fangan
         * */
        @Column
        private String trans_fangan;
        /**
         * 字段名 invoice_date
         * */
        @Column
        private String invoice_date;
        /**
         * 字段名 invoice_no
         * */
        @Column
        private String invoice_no;
        /**
         * 字段名 pay_plan_date
         * */
        @Column(type=TypeSerializerEnum.DATETIME)
        private String pay_plan_date;
        /**
         * 字段名 yugu_flag
         * */
        @Column
        private String yugu_flag;
        /**
         * 字段名 contract_type
         * */
        @Column
        private String contract_type;
        @Column
        private String vin_id;

}
