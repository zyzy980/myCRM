package com.bba.xtgl.vo;

 
public class SysUploadfileConfigVO {
	/*
	CODE	VARCHAR2(60)	N			编号
	SAVE_PATH	VARCHAR2(260)	N			保存路径+（程序自动加/年月日/文件名）
	ACCESS_IP	VARCHAR2(20)	N			访问IP地址+端口
	REMARK	VARCHAR2(100)	Y			备注
	CREATE_DATE	DATE	Y	sysdate		创建时间
	CREATE_BY	VARCHAR2(60)	Y			创建人
	*/
	private String code;
	private String save_path;
	private String access_ip;
	private String remark;
	
	/**
	 * 共享文件夹帐号
	 * */
	private String account;
	/**
	 * 共享文件夹密码
	 * */
	private String password;
	/**
	 * LOC本机；NET局域网互联网
	 * */
	private String kind;
	
	
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code = code;
	}
	public String getSave_path()
	{
		return save_path;
	}
	public void setSave_path(String save_path)
	{
		this.save_path = save_path;
	}
	public String getAccess_ip()
	{
		return access_ip;
	}
	public void setAccess_ip(String access_ip)
	{
		this.access_ip = access_ip;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public String getAccount()
	{
		return account;
	}
	public void setAccount(String account)
	{
		this.account = account;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getKind()
	{
		return kind;
	}
	public void setKind(String kind)
	{
		this.kind = kind;
	}
 
	
}
