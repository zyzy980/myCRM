package com.bba.common.constant;

public enum SystemUserEnum {

	ACCOUNT_NOT_BLANK("The username cannot be empty", "用户名不能为空"),
	PASSWORD_NOT_BLANK("The password cannot be empty", "密码不能为空"),
	YZM_NOT_BLANK("The verification code cannot be empty", "验证码不能为空"),
	YZM_ERROR("Incorrect verification code", "验证码错误"),
	USER_AND_PASSWORD_ERROR("User name or password error", "用户名或密码或内码错误"),
	WITHIN_CODE_ERROR("Within Code error","客户公司内码错误"),
	SUCCESS_MSG("User validation passed, loading system list and personalization Settings for you...", "用户验证通过,正在为您加载系统列表和个性化设置...");
	
	private String en_desc;
	private String zh_desc;
	
	private SystemUserEnum(String en_desc, String zh_desc) {
		this.en_desc = en_desc;
		this.zh_desc = zh_desc;
	}
	
	public String getValue(String lang) {
		if("en".equals(lang)) {
			return this.en_desc;
		}else {
			return this.zh_desc;
		}
	}
	
}
