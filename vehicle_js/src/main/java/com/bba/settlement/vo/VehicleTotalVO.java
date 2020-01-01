package com.bba.settlement.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

/*
* 商品车统计 - 报表
* */
@Data
public class VehicleTotalVO {
    private String num;//	序号	varchar2(40)	2019000001，以此类推
    private String bus_completion_month;//	业务完成月	varchar2(40)	暂为空
    private String statistical_month;//	统计月	varchar2(40)	201901
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date begin_date;//	发运日期	date	tr_business.begin_date
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receipt_date;//	收车日期	date	tr_business.receipt_date
    private String attribute;//	属性	varchar2(40)	固定‘体系内’
    private String project;//	项目	varchar2(40)	固定‘商品车’
    private String type;//	类别	varchar2(40)	暂为空
    private String xunjia_no;//	询价号	varchar2(40)	js_vin_amount.xunjia_no
    private String contract_no;//	合同号	varchar2(40)	js_vin_amount.contract_no
    private String cus_contract_no;//	客户合同号	varchar2(40)	js_vin_amount.cus_contract_no
    private String carrier_name;//	分包商	varchar2(40)	js_vin_down_amount.carrier_name
    private String aic;//	aic	varchar2(40)	tr_business.aic
    private String vdr_no;//	运单号	varchar2(40)	tr_business.vdr_no
    private String vin;//	vin码	varchar2(40)	tr_business.vin
    private String trans_mode;//	运输方式	varchar2(40)	tr_business.trans_mode
    private String begin_city;//	起点	varchar2(40)	tr_business.becin_city
    private String end_city;//	终点	varchar2(40)	tr_business.end_city
    private String end_province;//	省份	varchar2(40)	tr_business.end_province
    private String dealer_name;//	经销商名称	varchar2(100)	tr_business.dealer_name
    private Integer mil;//	合同里程	number	js_vin_amount.mil
    private String car_type;//	车型	varchar2(40)	tr_business.car_type
    private Double price;//	对上单价	number	js_vin_amount.price
    private Double not_tax_freight;//	运费收入小计	number	js_vin_amount.not_tax_freight
    private Double not_tax_premium;//	保费收入	number	js_vin_amount.not_tax_premium
    private Double not_tax_other_amount;//	其他收入	number	js_vin_amount.not_tax_other_amount
    private Double not_tax_amount;//	统计收入	number	js_vin_amount.not_tax_amount
    private Double not_tax_price;//	对下价格	number	js_vin_down_amount.not_tax_price
    private Double not_tax_other_amount_down;//	其他费用	number	js_vin_down_amount.not_tax_other_amount
    private Double down_ntax_premium;//	保费支出	number	js_vin_down_premium.not_tax_amount
    private Double down_ntax_amount;//	统计成本	number	not_tax_price+not_tax_other_amount+down_ntax_premium
    private String zs_remark;//	重损备注	varchar2(100)	tr_business.remark
    private Date receipt_sheet_date;//	收单日期	date	空
    private Date return_date;//	回单日期	date	空
    private String js_unusual_remark;//	结算异常备注	varchar2(100)	js_vin_down_amount.remark
    private String js_no;//	结算号	varchar2(40)	js_vin_amount.js_no
    private Date invoice_date;//	开票日期	date	js_vin_amount.invoice_date
    private String invoice_no;//	发票号码	varchar2(100)	js_vin_amount.invoice_no
    private Double real_tax_amount;//	实际收入含税	number	js_vin_amount.tax_amount
    private Double real_ntax_amount;//	实际收入不含税	number	js_vin_amount.not_tax_amount
    private Double real_cost;//	实际成本	number	js_vin_amount.not_tax_amount
    private String js_schedule;//	结算进度	varchar2(40)	js_vin_amount.js_state
    private String business_auditor;//	商务审核人	varchar2(40)	‘杨梅宁’or sys_users 对上商务




    private Double tax_real_cost;//	实际成本-含税	number	js_vin_down_amount.tax_amount
    private String down_invoice_month; //	对下开票月份	date	js_vin_down_amount.invoice_date
    private String down_invoice_no; //	下家发票号码	varchar2(40)	js_vin_down_amount.invoice_no
    private String down_js_state;//	对下结算进度	varchar2(1)	js_vin_down_amount.js_state
    private String down_pay_plan_date;//	付款计划提报月	date	js_vin_down_amount.pay_plan_date
    private String car_no;//	车牌号	varchar2(40)	tr_business.car_no
    private String remark;//	备注	varchar2(200)	tr_business.remark
    private String from_data;// 业务=由系统结算； 导入=报表通过Excel模板导入数据可删除
    private String id;
}
