package com.bba.util;

/**
 * 调用存储过程时,返回字符属于错误标识,抛出该异常使得事务回滚 
 */
public class OperatorException extends RuntimeException  {

	private static final long serialVersionUID = 1L;

	public static final String FIND_MULTIPLE_DATA = "获取到了多条数据";
	
	public static final String NOT_FIND_PARAMS = "未发现参数";

	public static final String RESULT_SUCCESS = "OK";
	
	
	public OperatorException(String message) {
		super(message);
	}

	public OperatorException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
