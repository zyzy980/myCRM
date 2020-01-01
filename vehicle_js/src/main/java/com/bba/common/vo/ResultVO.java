package com.bba.common.vo;

import com.bba.util.ResultDataFullMore;

public class ResultVO {

	private String resultCode;

	private Object resultDataFull;


	public static ResultVO successResult() {
		ResultVO resultVO = new ResultVO();
		resultVO.setResultCode("0");
		resultVO.setResultDataFull(new ResultDataFullMore("操作成功", "", false, false));
		return resultVO;
	}

	public static ResultVO successResult(String message) {
		ResultVO resultVO = new ResultVO();
		resultVO.setResultCode("0");
		resultVO.setResultDataFull(new ResultDataFullMore(message, "", false, true));
		return resultVO;
	}

	public static ResultVO successResult(String simpleMessage,String moreMessage) {
		ResultVO resultVO = new ResultVO();
		resultVO.setResultCode("0");
		resultVO.setResultDataFull(new ResultDataFullMore(simpleMessage, moreMessage, true,false));
		return resultVO;
	}
	
	
	public static ResultVO failResult(String simpleMessage) {
		ResultVO resultVO = new ResultVO();
		resultVO.setResultCode("3");
		resultVO.setResultDataFull(new ResultDataFullMore(simpleMessage, "", false,false));
		return resultVO;
	}
	
	public static ResultVO failResult(String simpleMessage,String moreMessage) {
		ResultVO resultVO = new ResultVO();
		resultVO.setResultCode("3");
		resultVO.setResultDataFull(new ResultDataFullMore(simpleMessage, moreMessage, true,false));
		return resultVO;
	}


	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public Object getResultDataFull() {
		return resultDataFull;
	}

	public void setResultDataFull(Object resultDataFull) {
		this.resultDataFull = resultDataFull;
	}
	
}
