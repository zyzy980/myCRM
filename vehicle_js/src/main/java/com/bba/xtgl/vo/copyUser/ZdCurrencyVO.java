package com.bba.xtgl.vo.copyUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName ZdCurrencyVO
 * @Description TODO
 * @Author lao li
 * @Date 2019/2/21 14:48
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ZdCurrencyVO {

    private String curCode;
    private String sn;
    private String withinCode;
    private String curName;
    private String createDate;
    private String createBy;
    private String createByName;

}
