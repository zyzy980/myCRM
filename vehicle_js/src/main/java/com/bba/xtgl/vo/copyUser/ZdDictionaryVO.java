package com.bba.xtgl.vo.copyUser;

import com.bba.jcda.vo.contractor.BaseVO;
import lombok.Data;

/**
 * @ClassName ZdDictionaryVO
 * @Discription TODO
 * @Author lao li
 * @Date 2019-04-02 19:38
 * @Version 1.0
 */
@Data
public class ZdDictionaryVO extends BaseVO {
    private String sn;
    private String withinCode;
    private String typeName;
    private String typeCode;
    private String parentSn;
    private String remark;
}
