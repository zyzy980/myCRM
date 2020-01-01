package com.bba.nosettlement.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


/*
 * 非商品车统计 - 报表
 * */
@Data
public class NonVehicleTotalVO {
    private String num;//	序号	varchar2(40)	2019000001，以此类推
    private String bus_completion_month;//	业务完成月	varchar2(40)	同统计月
    private String statistical_month;//	统计月	varchar2(40)	201901
    private String xunjia_no;//	询价号	varchar2(40)	js_non_vehicle.xunjia_no
    private String contract_no;//	合同号	varchar2(40)	js_non_vehicle.contract_no
    private String attribute;//	属性	varchar2(40)	体系内or体系外
    private String project;//	项目	varchar2(40)	固定‘非商品车’
    private String demand_sector;//	需求部门	varchar2(40)	tr_non_business.demand_sector
    private String cus_no;//	客户	varchar2(40)	tr_non_business.cus_name
    private String cus_name;
    private String applicant;//	发运指令人	varchar2(40)	tr_non_business.applicant
    private String handover_no;//	交接单号	varchar2(40)	tr_non_business.handover_no
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date begin_date;//	发运日期	date	tr_non_business.begin_date
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receipt_date;//	收车日期	date	tr_non_business.receipt_date
    private String trans_mode;//	运输方式	varchar2(40)	tr_non_business.trans_mode
    private String trans_fangan;//	运输方案	varchar2(40)	tr_non_business.trans_fangan
    private String begin_city;//	发运地	varchar2(40)	tr_non_business.begin_address
    private String end_city;//	目的地	varchar2(40)	tr_non_business.end_address
    private String car_type;//	运输车型	varchar2(40)	tr_non_business.car_type
    private Integer mil;//	合同线路距离	number	js_non_vehicle.mil
    private Double ht_price;//	（合同）运输费用/公里/台	number
    private Integer chengyun_qty;//	承运数量	number	js_non_vehicle.shipment_qty
    private Integer jifei_qty;//	计费数量_收	number	js_non_vehicle.js_qty
    private Double not_tax_freight;//	（合同）运费小计-收	number
    private Double not_tax_premium_price;//	（合同）保费/台-收	number
    private Double not_tax_premium;//	保费小计-收	number
    private Double not_tax_other_amount;//	其他费用-收	number
    private String up_remark;//	备注_收	varchar2(200)	js_non_vehicle.remark
    private Double not_tax_amount;//	统计收入	number
    private String carrier_no;
    private String carrier_name;//	分包商	varchar2(40)	js_non_down_vehicle.carrier_name
    private Double freight_zucheng;//	运费成本	varchar2(40)
    private Double ht_jifei_biaozhun;//	（合同）计费标准/公里/台	price
    private Integer down_jifei_qty;//	计费数量_成本	number
    private Double not_tax_down_freight;//	（合同）运费小计-成本	number
    private Double down_premium_price;//	保费/台-成本	number
    private Double down_premium_total;//	保费小计-成本	number
    private Double not_tax_other_down_amount;//	其他费用-成本	number
    private String down_remark;//	备注成本	varchar2(200)
    private Double down_amount;//	统计成本	number
    private Date receipt_sheet_date;//	收单日期	date	tr_non_business.receipt_sheet_date
    private Date delivery_date;//	送单日期	date	js_non_vehicle.delivery_date
    private Date return_date;//	回单日期	date	js_non_vehicle.return_date
    private String js_unusual_remark;//	结算异常备注	varchar2(100)	tr_non_business.remark
    private String js_no;//	结算号	varchar2(40)	js_non_vehicle.js_no
    private Date invoice_date;//	开票日期	date	js_non_vehicle.invoice_date
    private String invoice_no;//	发票号码	varchar2(100)	js_non_vehicle.invoice_no
    private String real_tax_amount;//	实际收入-含税	number	js_non_vehicle.tax_amount
    private String real_ntax_amount;//	实际收入-不含税	number	js_non_vehicle.not_tax_amount
    private String not_tax_real_cost;//	实际成本-不含税	number	js_non_down_vehicle.not_tax_amount
    private String js_schedule;//	结算进度	varchar2(40)	js_non_vehicle.js_state
    private String business_auditor;//	商务审核人	varchar2(40)	‘杨梅宁’or sys_users 对上商务
    private Double tax__real_cost;//	实际成本-含税	number	js_non_down_vehicle.tax_amount
    private Date down_invoice_month;//	对下开票月份	date	js_non_down_vehicle.invoice_date
    private String down_invoice_no;//	下家发票号码	varchar2(40)	js_non_down_vehicle.invoice_no
    private String down_js_state;//	对下结算进度	varchar2(40)	js_non_down_vehicle.js_state
    private Date down_pay_plan_date;//	付款计划提报月	date	js_non_down_vehicle.pay_plan_date
    private String car_no;//	车牌号	varchar2(40)	tr_non_business.car_no
    private String remark;//	备注	varchar2(200)	tr_non_business.remark
    private String from_data;// 业务=由系统结算； 导入=报表通过Excel模板导入数据可删除
    private String id;
}
