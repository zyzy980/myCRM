package com.bba.dz.vo;

import com.bba.common.annotation.Column;
import com.bba.common.annotation.Table;
import com.bba.common.enums.BooleanEnum;
import com.bba.common.enums.TypeSerializerEnum;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Table("JS_DZ_SHEET")
public class Js_Dz_SheetAccountVO {

    @Column
    private Long id;

    @Column
    private String remark;

    @Column(key = BooleanEnum.YES)
    private String dz_sheet;
    @Column
    private String dz_op_by;

    @Column(type = TypeSerializerEnum.DATETIME)
    private String dz_op_datetime;
    @Column(type = TypeSerializerEnum.DATETIME)
    private String send_mail_date;
    @Column
    private BigDecimal not_tax_amount;
    @Column
    private BigDecimal tax_amount;
    @Column
    private String contract_no;
    @Column
    private String state;


    @Column
    private String js_batch;
    @Column
    private String js_no;

    @Column
    private String cus_no;
    @Column
    private String cus_name;




    private String return_date;
}
