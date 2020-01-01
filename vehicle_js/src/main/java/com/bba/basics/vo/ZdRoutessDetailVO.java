package com.bba.basics.vo;

import lombok.Data;

@Data
public class ZdRoutessDetailVO {
    private String sn;
    private String within_code;
    private String route_code;
    private String route_type;
    private String is_generate_order;
    private String begin_local_code;
    private String begin_local;
    private String begin_local_name;
    private String begin_fence;
    private String begin_arrive_trigger_event;
    private String begin_arrive_trigger_state;
    private String end_local_code;
    private String end_local;
    private String end_local_name;
    private String end_fence;
    private String end_arrive_trigger_event;
    private String end_arrive_trigger_state;
    private String remark;
    private String create_by;
    private String create_date;
    private String update_by;
    private String update_date;
}
