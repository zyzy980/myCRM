package com.bba.jsyw.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 多承运商导入实体类
 */
@Data
public class Tr_ImportMoreCarrierVO{

    @Excel(name = "VIN")
    private String vin;

    @Excel(name = "承运商编号一")
    private String carrier_no_one;

    private String carrier_name_one;

    @Excel(name = "运输方式一")
    private String trans_mode_one;

    @Excel(name = "起运地一")
    private String begon_city_one;

    @Excel(name = "目的地一")
    private String end_city_one;

    @Excel(name = "承运商编号二")
    private String carrier_no_two;

    private String carrier_name_two;

    @Excel(name = "运输方式二")
    private String trans_mode_two;

    @Excel(name = "起运地二")
    private String begon_city_two;

    @Excel(name = "目的地二")
    private String end_city_two;

    @Excel(name = "承运商编号三")
    private String carrier_no_three;

    private String carrier_name_three;

    @Excel(name = "运输方式三")
    private String trans_mode_three;

    @Excel(name = "起运地三")
    private String begon_city_three;

    @Excel(name = "目的地三")
    private String end_city_three;

    private String create_by;

}
