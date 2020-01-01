package com.bba.xtgl.vo.copyUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName SysRoleVO
 * @Description TODO
 * @Author lao li
 * @Date 2019/2/23 17:04
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRoleVO {
    private Long roleId;
    private String roleName;
    private String roleDescription;
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;
    private String withinCode;
}
