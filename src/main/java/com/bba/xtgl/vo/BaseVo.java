package com.bba.xtgl.vo;


public class BaseVo {
	
	private String resultCode;
	
	private String message;
	
	public static BaseVo successResult(String message) {
		BaseVo resultVO = new BaseVo();
		resultVO.setResultCode("0");
		resultVO.setMessage(message);
		return resultVO;
	}

	public static BaseVo failResult(String message) {
		BaseVo resultVO = new BaseVo();
		resultVO.setResultCode("3");
		resultVO.setMessage(message);
		return resultVO;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
