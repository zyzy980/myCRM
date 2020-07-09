package com.bba.xtgl.vo.copyUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.annotation.Resource;

/**
 * @ClassName ZdYwLocationBaseVO
 * @Description TODO
 * @Author lao li
 * @Date 2019/2/23 11:42
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ZdYwLocationBaseVO {

    private Long sn;
    private String withinCode;
    private String locationCode;
    private String kind;
    private Long baseSn;
}
