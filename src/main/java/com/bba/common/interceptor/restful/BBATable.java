/**
	此文件归微科创源有限公司所有
	
	创  建  人:caining
	创建时间:2018年9月10日

	文件描述:
		数据表注解
*/
package com.bba.common.interceptor.restful;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体类 注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface BBATable {
	
	//表名
	String value() default "";
	
}