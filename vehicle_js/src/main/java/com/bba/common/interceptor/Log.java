package com.bba.common.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Log {
	
	// 模块名字
	String value() default "";
	
	//一个方法多个模块使用  , "sn,新增,修改"  如果sn=0||null 记录新增,否则修改
	String saveOrUpdate() default "";

	// 是否进行日期
	boolean log() default true;
	
	String[] params() default {};
}