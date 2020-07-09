package com.bba.xtgl.vo.copyUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName ZdTruckTypeVO
 * @Description TODO
 * @Author lao li
 * @Date 2019/2/23 10:10
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ZdTruckTypeVO {
    private Long sn;
    private String withinCode;
    private String code;
    private String name;
    private Float gwt;
    private Float vol;
    private Float h;
    private Float l;
    private Float w;
    private String remark;
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;
    private Float tBufferH;
    private Float tBufferL;
    private Float tBufferW;
    private Float cBufferH;
    private Float cBufferL;
    private Float cBufferW;
    private Float doorH;
    private Float doorW;
    private String loadType;
    private String unloadType;
    private String state;
}
