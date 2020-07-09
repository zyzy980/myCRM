package com.bba.jsyw.vo;

import lombok.Data;

/**
 * VL_业务数据表
 */
@Data
public class Vl_Tr_BusinessVO {

    private Long id;

    private String begin_datetime;

    private String receipt_datetime;

    private String vdr_no;

    private String vin;

    private String trans_mode;

    private String begin_city;

    private String end_city;

    private String end_province;

    private String dealer_no;

    private String car_type;

    private String remark;

    private String dealer_name;

    private String create_by;

    private String create_date;

    private String vehicle_project;

    private String data_from;

    private String data_state;

    private String receipt_date;

    private String mass_loss_type;

    private String up_js_flag;

    private String down_js_flag;

    private String carrier_no;

    private String carrier_name;

    private String yf_flag;

    private String edi_flag;

    private String batch_no;

    private String aic;

    private String cus_no;

    private String cus_name;


    //List 去重，重写equals
    public boolean equals(Object obj) {
        Vl_Tr_BusinessVO vo = (Vl_Tr_BusinessVO)obj;
        return this.getBatch_no().equals(vo.getBatch_no()) ;
    }
    @Override
    public int hashCode() {
        String result = batch_no;
        return result.hashCode();
    }
}
