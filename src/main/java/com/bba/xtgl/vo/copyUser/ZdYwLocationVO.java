package com.bba.xtgl.vo.copyUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName ZdYwLocationVO
 * @Description TODO
 * @Author lao li
 * @Date 2019/2/20 16:15
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ZdYwLocationVO {
    private String sn;
    private String withinCode;
    private String code;
    private String name;
    private String logo;
    private String address;
    private String linkMan;
    private String linkTel;
    private String remark;
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;
    private String title;
    private String nameEn;
}
