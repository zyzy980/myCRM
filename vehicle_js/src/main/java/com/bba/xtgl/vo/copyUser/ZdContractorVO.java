package com.bba.xtgl.vo.copyUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName ZdContractorVO
 * @Description TODO
 * @Author lao li
 * @Date 2019/2/21 10:57
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class  ZdContractorVO {

    private Long sn;
    private String withinCode;
    private String code;
    private String shortName;
    private String name;
    private String linkName;
    private String linkMobile;
    private String address;
    private String remark;
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;
    private String state;
}
