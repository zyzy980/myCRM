package com.bba.basics.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName EchartsVO
 * @Description  excharts 渲染图表插件实体类，用于前端的接收
 * @Author laoli
 * @Date
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EchartsVO {

    private String name;
    private String value;
}
