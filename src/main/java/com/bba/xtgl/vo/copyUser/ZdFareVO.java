package com.bba.xtgl.vo.copyUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName ZdFareVO
 * @Description TODO
 * @Author lao li
 * @Date 2019/2/21 13:51
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ZdFareVO {

    private String withinCode;
    private String code;
    private String taxRate;
    private String remark;
    private String state;
    private String createBy;
    private String createDate;
    private String sn;
}

