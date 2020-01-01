package com.bba.xtgl.vo.copyUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName ZdCusVO
 * @Description TODO
 * @Author lao li
 * @Date 2019/2/21 11:36
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ZdCusVO {
    private String withinCode;
    private String code;
    private String name;
    private String shortName;
    private String kind;
    private String remark;
    private String state;
    private String createBy;
    private String createDate;
    private Long sn;
    private String updateBy;
    private String updateDate;
}
