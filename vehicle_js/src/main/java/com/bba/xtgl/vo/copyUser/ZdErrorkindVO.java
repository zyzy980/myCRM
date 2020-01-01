package com.bba.xtgl.vo.copyUser;

import com.bba.jcda.vo.contractor.BaseLocationVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName ZdErrorkindVO
 * @Discription TODO
 * @Author lao li
 * @Date 2019/3/14 0014 21:30
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ZdErrorkindVO extends BaseLocationVO {

    private String sn;
    private String withinCode;
    private String name;
    private String remark;
    private String kind;
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;
    private String extendField1;

    @Override
    public String getKind_code() {
        return null;
    }
}
