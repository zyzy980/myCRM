package com.bba.xtgl.vo.copyUser;

import com.bba.jcda.vo.contractor.BaseVO;
import lombok.Data;

/**
 * @ClassName ZdBusinessStateVO
 * @Discription TODO
 * @Author lao li
 * @Date 2019-04-02 19:05
 * @Version 1.0
 */
@Data
public class ZdBusinessStateVO extends BaseVO {

    private String sn;
    private String withinCode;
    private String state;
    private String stateName;
    private String stateNameEn;
    private String stateType;
    private String mappingOther;
    private String stateShortName;
}

