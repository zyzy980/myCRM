/**
	此文件归微科创源有限公司所有
	
	创  建  人:caining
	创建时间:2018年9月10日

	文件描述:
		数据列注解
*/
package com.bba.common.interceptor.restful;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体类 注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface BBAId {
	
	// 列名
	String name() default "";
	
	// 是否排序
	boolean isSort() default false;
	
	// 排序类型
	BBASortType sortType() default BBASortType.ASC;
}