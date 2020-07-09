package com.bba.jcda.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.bba.common.annotation.Column;
import com.bba.common.annotation.Table;
import com.bba.common.enums.BooleanEnum;
import com.bba.common.enums.TypeSerializerEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

//TR_STATISTICAL_RULES 统计规则表
@Table("tr_statistical_rules")
@Data
public class Tr_statistical_rulesVO {

        /**
         * 字段名 sn
         * */
        @Column(key = BooleanEnum.YES)
        private String sn;
        /**
         * 字段名 状态
         * */
        @Column
        private String state;
        /**
         * 字段名 运输方式
         * */
        @Column
        @Excel(name = "运输方式")
        private String trans_mode;
        /**
         * 字段名 起运地
         * */
        @Column
        @Excel(name = "起运地")
        private String begin_city;
        /**
         * 字段名 目的地
         * */
        @Column
        @Excel(name = "目的地")
        private String end_city;
        /**
         * 字段名 对上第一段里程
         * */
        @Column
        @Excel(name = "对上第一段里程")
        private Integer up_first_mileage;
        /**
         * 字段名 对上第一段单价
         * */
        @Column
        @Excel(name = "对上第一段单价")
        private Double up_first_price;
        /**
         * 字段名 对上第二段里程
         * */
        @Column
        @Excel(name = "对上第二段里程")
        private Integer up_two_mileage;
        /**
         * 字段名 对上第二段单价
         * */
        @Column
        @Excel(name = "对上第二段单价")
        private Double up_two_price;
        /**
         * 字段名 对上第三段里程
         * */
        @Column
        @Excel(name = "对上第三段里程")
        private Integer up_three_mileage;
        /**
         * 字段名 对上第三段单价
         * */
        @Column
        @Excel(name = "对上第三段单价")
        private Double up_three_price;
        /**
         * 字段名 对上保费
         * */
        @Column
        @Excel(name = "对上保费")
        private Double up_premium;
        /**
         * 字段名 对上总价
         * */
        @Column
        @Excel(name = "对上总价")
        private Double up_total;
        /**
         * 字段名 对下第一段里程
         * */
        @Column
        @Excel(name = "对下第一段里程")
        private  Integer down_first_mileage;
        /**
         * 字段名 对下第一段单价
         * */
        @Column
        @Excel(name = "对下第一段单价")
        private Double down_first_price;
        /**
         * 字段名 对下第二段里程
         * */
        @Column
        @Excel(name = "对下第二段里程")
        private  Integer down_two_mileage;
        /**
         * 字段名 对下第二段单价
         * */
        @Column
        @Excel(name = "对下第二段单价")
        private Double down_two_price;
        /**
         * 字段名 对下第三段里程
         * */
        @Column
        @Excel(name = "对下第三段里程")
        private Integer down_three_mileage;
        /**
         * 字段名 对下第三段单价
         * */
        @Column
        @Excel(name = "对下第三段单价")
        private Double down_three_price;
        /**
         * 字段名 对下过海费
         * */
        @Column
        @Excel(name = "对下过海费")
        private Double down_cross_sea_amount;
        /**
         * 字段名 对下保费
         * */
        @Column
        @Excel(name = "对下保费")
        private Double down_premium;
        /**
         * 字段名 对下总价
         * */
        @Column
        @Excel(name = "对下总价")
        private Double down_total;
        /**
         * 字段名 对上过海费
         * */
        @Column
        @Excel(name = "对上过海费")
        private Double up_cross_sea_amount;
        /**
         * 字段名 创建人员
         * */
        @Column
        private String create_by;
        /**
         * 字段名 创建时间
         * */
        @Column(type = TypeSerializerEnum.DATETIME)
        private String create_date;

        @Column
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Excel(name = "有效期起")
        private Date begin_date;

        @Column
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Excel(name = "有效期止")
        private Date end_date;

    }