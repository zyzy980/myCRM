package com.bba.common.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体类，字段注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface EntityField {
	
	/**
	 * 字段名称
	 */
	String value() default "";
	
	/**
	 * 字段备注描述
	 */
	String desc() default "";
	
	/**
	 * 字典目录值,数据库查询出来的值，从数据字典中转换 
	 */
	String dicType() default "";
	
	/**
	 * 是否主键
	 */
	boolean key() default false;
	
	/**
	 * 为空时默认值
	 */
	String defaultValue() default "";
	
	/**
	 * 英文字段名称
	 */
	String value_en() default "";
}