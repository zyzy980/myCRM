package com.bba.xtgl.vo.copyUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName ZdDriverRelateVO
 * @Description TODO
 * @Author lao li
 * @Date 2019/2/23 15:35
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ZdDriverRelateVO {
    private Long sn;
    private String withinCode;
    private String code;
    private String contractor;
    private String name;
    private String mobile;
    private String state;
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;
}
