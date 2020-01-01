package com.bba.jcda.vo;

import com.bba.common.annotation.Column;
import com.bba.common.annotation.Table;
import com.bba.common.enums.BooleanEnum;
import com.bba.common.enums.TypeSerializerEnum;
import lombok.Data;


@Table("JS_TAX_RATE")
@Data
public class Js_Tax_RateVO {

    @Column(key = BooleanEnum.YES)
    private String id;

    @Column(type= TypeSerializerEnum.DATE)
    private String tax_month;

    @Column
    private Double tax_rate;

}
