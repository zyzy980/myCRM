package com.bba.nosettlement.vo;

import com.bba.common.annotation.Column;
import com.bba.common.annotation.Table;
import com.bba.common.enums.BooleanEnum;
import com.bba.common.enums.TypeSerializerEnum;
import lombok.Data;

@Data
@Table("JS_DZ_NON_SHEET")
public class Js_Dz_Non_SheetVO {


        /**
         * 字段名 id
         */
        @Column(key = BooleanEnum.YES)
        private String id;
        /**
         * 字段名 remark
         */
        @Column
        private String remark;
        /**
         * 字段名 dz_sheet
         */
        @Column
        private String dz_sheet;
        /**
         * 字段名 dz_op_by
         */
        @Column
        private String dz_op_by;
        /**
         * 字段名 dz_op_datetime
         */
        @Column(type = TypeSerializerEnum.DATETIME)
        private String dz_op_datetime;
        /**
         * 字段名 not_tax_amount
         */
        @Column
        private String not_tax_amount;
        /**
         * 字段名 tax_amount
         */
        @Column
        private String tax_amount;
        /**
         * 字段名 state
         */
        @Column
        private String state;
        /**
         * 字段名 js_batch
         */
        @Column
        private String js_batch;
        /**
         * 字段名 js_no
         */
        @Column
        private String js_no;
        /**
         * 字段名 send_mail_date
         */
        @Column(type = TypeSerializerEnum.DATETIME)
        private String send_mail_date;

        @Column
        private String cus_no;
        @Column
        private String cus_name;
}