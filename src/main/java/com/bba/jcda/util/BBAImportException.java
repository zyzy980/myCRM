/**
	此文件归微科创源有限公司所有
	
	创  建  人:caining
	创建时间:2018年6月29日

	文件描述:
		BBA导入异常,导入失败时记录错误日志,并事物回滚
*/
package com.bba.jcda.util;

public class BBAImportException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BBAImportException(String message){
		super(message);
	}
}
