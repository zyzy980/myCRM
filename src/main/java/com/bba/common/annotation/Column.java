package com.bba.common.annotation;


import com.bba.common.enums.BooleanEnum;
import com.bba.common.enums.TypeSerializerEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Column {

    String value() default "";

    BooleanEnum key() default BooleanEnum.NO;

    TypeSerializerEnum type() default TypeSerializerEnum.EMPTY;

    int decimalsize() default -1;		//数值会固定小数点位数

}
