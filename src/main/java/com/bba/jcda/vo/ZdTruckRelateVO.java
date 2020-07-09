package com.bba.jcda.vo;

import com.bba.jcda.vo.contractor.BaseVO;
import lombok.Data;

@Data
public class ZdTruckRelateVO extends BaseVO {
    private String sn;
    private String within_code;
    private String code;
    private String truck_no;
    private String contractor;
    private String driver_id;
    private String driver_name;
    private String driver_tel;
    private String state;
    private String create_by;
    private String create_date;
    private String update_by;
    private String update_date;
    private String truck_transport_type;
}
