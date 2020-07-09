package com.bba.nosettlement.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.bba.common.annotation.Column;
import com.bba.common.annotation.Table;
import com.bba.common.enums.BooleanEnum;
import com.bba.common.enums.TypeSerializerEnum;
import lombok.Data;

import java.util.Date;

@Data
@Table("JS_NON_VEHICLE")
public class Js_Non_VehicleVO {

        /**
         * 字段名 id
         * */
        @Excel(name = "id")
        @Column(key = BooleanEnum.YES)
        private String id;
        /**
         * 字段名 属性
         * */
        @Excel(name = "属性")
        @Column
        private String attribute;
        /**
         * 字段名 type
         * */
        @Excel(name = "类型")
        @Column
        private String type;
        /**
         * 字段名 handover_no
         * */
        @Excel(name = "交接单号")
        @Column
        private String handover_no;
        /**
         * 字段名 service_type
         * */
        @Excel(name = "服务类型")
        @Column
        private String service_type;
        /**
         * 字段名 询价号
         * */
        @Excel(name = "询价号")
        @Column
        private String xunjia_no;
        /**
         * 字段名 vin
         * */
        @Excel(name = "车架号")
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
        @Excel(name = "客户名称")
        @Column
        private String cus_name;
        /**
         * 字段名 发运日期
         * */
        @Excel(name = "发运日期")
        @Column(type=TypeSerializerEnum.DATE)
        private String begin_date;
        /**
         * 字段名 收车日期
         * */
        @Excel(name = "收车日期")
        @Column(type=TypeSerializerEnum.DATE)
        private String receipt_date;
        /**
         * 字段名 运输方式
         * */
        @Excel(name = "运输方式")
        @Column
        private String trans_mode;
        /**
         * 字段名 小车车型
         * */
        @Excel(name = "运输车型")
        @Column
        private String car_type;
        /**
         * 字段名 承运数量
         * */
        @Excel(name = "承运数量")
        @Column
        private String shipment_qty;
        /**
         * 字段名 计费数量
         * */
        @Excel(name = "计费数量")
        @Column
        private String js_qty;
        /**
         * 字段名 起运地
         * */
        @Excel(name = "起运地")
        @Column
        private String begin_address;
        /**
         * 字段名 目的地
         * */
        @Excel(name = "目的地")
        @Column
        private String end_address;
        /**
         * 字段名 需求部门
         * */
        @Excel(name = "需求部门")
        @Column
        private String demand_sector;
        /**
         * 字段名 计费标准
         * */
        @Excel(name = "计费标准")
        @Column
        private String freight_basis;
        /**
         * 字段名 合同线路距离
         * */
        @Excel(name = "合同线路距离")
        @Column
        private String mil;
        /**
         * 字段名 单价
         * */
        @Excel(name = "单价")
        @Column
        private String price;
        /**
         * 字段名 密闭式运输车单价（不含税）
         * */
        @Excel(name = "密闭式运输车单价（不含税）")
        @Column
        private String not_tax_losed_car_price;
        /**
         * 字段名 closed_num
         * */
        @Excel(name = "密闭式运输车台数")
        @Column
        private String closed_num;
        /**
         * 字段名 不含税计费标准
         * */
        @Excel(name = "不含税计费标准")
        @Column
        private String not_tax_price;
        /**
         * 字段名 不含税运费
         * */
        @Excel(name = "不含税运费")
        @Column
        private String not_tax_freight;
        /**
         * 字段名 保费
         * */
        @Excel(name = "不含税保费")
        @Column
        private String not_tax_premium;
        /**
         * 字段名 不含税其他费用
         * */
        @Excel(name = "不含税其他费用")
        @Column
        private String not_tax_other_amount;
        /**
         * 字段名 含税合计
         * */
        @Excel(name = "含税费用合计")
        @Column
        private String tax_amount;
        /**
         * 字段名 不含税合计
         * */
        @Excel(name = "不含税费用合计")
        @Column
        private String not_tax_amount;

        /**
         * 字段名 not_tax_rescue_truck_price
         * */
        @Excel(name = "救援车元/台/KM(不含税)")
        @Column
        private String not_tax_rescue_truck_price;
        /**
         * 字段名 not_tax_rescue_truck_startorice
         * */
        @Excel(name = "救援车起步价")
        @Column
        private String not_tax_rescue_truck_startoric;
        /**
         * 字段名 税率
         * */
        @Excel(name = "税率")
        @Column
        private String tax_rate;
        /**
         * 字段名 结算号
         * */

        @Column
        private String js_no;
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
         * 字段名 账单编号
         * */
        @Excel(name = "账单编号")
        @Column
        private String bill_number;
        /**
         * 字段名 车辆项目
         * */
        @Excel(name = "车辆项目")
        @Column
        private String vehicle_project;
        /**
         * 字段名 数据来源
         * */

        @Column
        private String data_from;
        /**
         * 字段名 结算状态
         * */

        @Column
        private String js_state;
        /**
         * 字段名 合同号
         * */

        @Column
        private String contract_no;
        /**
         * 字段名 备注
         * */
        @Excel(name = "备注")
        @Column
        private String remark;
        /**
         * 字段名 创建人员
         * */
        @Excel(name = "创建人员")
        @Column
        private String create_by;
        /**
         * 字段名 创建时间
         * */
        @Excel(name = "创建时间")
        @Column(type=TypeSerializerEnum.DATETIME)
        private String create_date;

        /**
         * 字段名 运输方案
         * */
        @Excel(name = "运输方案")
        @Column
        private String trans_fangan;

        @Column
        private String invoice_no;

        @Column
        private Date invoice_date;

        @Column
        private Date delivery_date;

        @Column
        private Date return_date;

        @Column
        private String bus_id;

}
