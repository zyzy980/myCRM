package com.bba.xtgl.vo.copyUser;

import com.bba.jcda.vo.contractor.BaseVO;
import lombok.Data;

/**
 * @ClassName ZdDictionaryDataVO
 * @Discription TODO
 * @Author lao li
 * @Date 2019-04-02 19:52
 * @Version 1.0
 */
@Data
public class ZdDictionaryDataVO extends BaseVO {
    private String sn;
    private String withinCode;
    private String dicTextEn;
    private String dicText;
    private String dicValue;
    private String typeCode;
    private String dicorder;
    private String remark;
    private String isDefault;
}
