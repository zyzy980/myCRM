package com.bba.util;


public class ResultDataFullMore {

	private String simpleMessage;
	private String moreMessage;
	private Boolean showMoreMessage;
	private Boolean autoHide;


	public ResultDataFullMore() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getSimpleMessage() {
		return simpleMessage;
	}

	public void setSimpleMessage(String simpleMessage) {
		this.simpleMessage = simpleMessage;
	}

	public String getMoreMessage() {
		return moreMessage;
	}

	public void setMoreMessage(String moreMessage) {
		this.moreMessage = moreMessage;
	}

	public Boolean getShowMoreMessage() {
		return showMoreMessage;
	}

	public void setShowMoreMessage(Boolean showMoreMessage) {
		this.showMoreMessage = showMoreMessage;
	}

	public Boolean getAutoHide() {
		return autoHide;
	}

	public void setAutoHide(Boolean autoHide) {
		this.autoHide = autoHide;
	}

	public ResultDataFullMore(String simpleMessage) {
		super();
		this.simpleMessage = simpleMessage;
	}

	public ResultDataFullMore(String simpleMessage, String moreMessage,
			Boolean showMoreMessage, Boolean autoHide) {
		super();
		this.simpleMessage = simpleMessage;
		this.moreMessage = moreMessage;
		this.showMoreMessage = showMoreMessage;
		this.autoHide = autoHide;
	}

}
