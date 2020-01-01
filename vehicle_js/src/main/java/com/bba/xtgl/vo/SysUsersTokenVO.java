package com.bba.xtgl.vo;

public class SysUsersTokenVO {

	/*
	 -- Create table
create table SYS_USERS_TOKEN
(
  ID          NUMBER(10) not null,
  USERID      VARCHAR2(50) not null,
  TOKEN       VARCHAR2(50) not null,
  IPADDR      VARCHAR2(16) not null,
  BROWSER     VARCHAR2(60) not null,
  CREATE_DATE DATE default sysdate
)
	 * */
 
		private String within_code;
		private String whcenter;
	    private Integer id;
	    private String userid;
	    private String token;
	    private String ipaddr;
	    private String browser;
	    private String create_date;
	    
	    
	    
		public Integer getId()
		{
			return id;
		}
		public void setId(Integer id)
		{
			this.id = id;
		}
		public String getUserid()
		{
			return userid;
		}
		public void setUserid(String userid)
		{
			this.userid = userid;
		}
		public String getToken()
		{
			return token;
		}
		public void setToken(String token)
		{
			this.token = token;
		}
		public String getIpaddr()
		{
			return ipaddr;
		}
		public void setIpaddr(String ipaddr)
		{
			this.ipaddr = ipaddr;
		}
		public String getBrowser()
		{
			return browser;
		}
		public void setBrowser(String browser)
		{
			this.browser = browser;
		}
		public String getCreate_date()
		{
			return create_date;
		}
		public void setCreate_date(String create_date)
		{
			this.create_date = create_date;
		}
		public String getWithin_code()
		{
			return within_code;
		}
		public void setWithin_code(String within_code)
		{
			this.within_code = within_code;
		}
		public String getWhcenter()
		{
			return whcenter;
		}
		public void setWhcenter(String whcenter)
		{
			this.whcenter = whcenter;
		}
	     

}
