package com.bba.xtgl.vo.copyUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName ZdTruckRelateVO
 * @Description TODO
 * @Author lao li
 * @Date 2019/2/21 16:55
 * @Version 1.0
 */
@Data
@EqualsAndHashCode
public class ZdTruckRelateVO {
    private Long sn;
    private String withinCode;
    private String code;
    private String truckNo;
    private String contractor;
    private String driverId;
    private String driverName;
    private String driverTel;
    private String state;
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;

}
