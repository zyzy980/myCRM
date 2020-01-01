package com.bba.basics.vo;

import lombok.Data;

import java.util.List;

@Data
public class ZdRoutessVO {
    private String sn;
    private String within_code;
    private String route_code;
    private String route_name;
    private String begin_local;
    private String begin_local_code;
    private String begin_local_name;
    private String end_local;
    private String end_local_code;
    private String end_local_name;
    private String create_by;
    private String create_date;
    private String update_by;
    private String update_date;
    private String location_code;
    public List<ZdRoutessDetailVO> detailVOList;


}
