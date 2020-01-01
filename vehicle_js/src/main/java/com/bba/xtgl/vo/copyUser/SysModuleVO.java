package com.bba.xtgl.vo.copyUser;

import com.bba.jcda.vo.contractor.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName SysModuleVO
 * @Description TODO
 * @Author lao li
 * @Date 2019/2/20 10:26
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysModuleVO extends BaseVO {

    private Long moduleId;
    private String moduleName;
    private String moduleDescription;
    private String moduleUrl;
    private Long moduleFatherId;
    private Long orderId;
    private Long isShow;
    private Long isNav;
    private String ico;
    private Long isHomePage;
    private String moduleFatherName;
    private String withinCode;
    private String moduleNameEn;

    private Long oldModuleId;
}
