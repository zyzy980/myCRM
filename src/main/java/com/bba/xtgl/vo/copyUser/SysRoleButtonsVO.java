package com.bba.xtgl.vo.copyUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName SysRoleButtonsVO
 * @Description TODO
 * @Author lao li
 * @Date 2019/2/22 16:33
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRoleButtonsVO {
    private Long moduleId;
    private String buttonName;
    private String buttonUse;
    private String buttonDescription;
    private Long buttonId;
    private String buttonIcon;
    private Long buttonOrder;
    private String withinCode;
    private String buttonNameEn;
}
