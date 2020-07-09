package com.bba.jcda.vo;

public class Zd_YWLocationVO {
 
		/**
		* 字段名 内码
		* */
		private String within_code;
		/**
		* 字段名 业务地点编号
		* */
		private String code;
		/**
		* 字段名 业务地点名称
		* */
		private String name;
		/**
		* 字段名 业务地点名称-英文
		* */
		private String name_en;
		/**
		* 字段名 备注说明
		* */
		private String remark;
 
		/**
		* 字段名 创建
		* */
		private String create_by;
		private String create_name;
		/**
		* 字段名 创建时间
		* */
		private String create_date;
		/**
		* 字段名 sn
		* */
		private Integer sn;
		/**
		* 字段名 更新
		* */
		private String update_by;
		private String update_name;
		/**
		* 字段名 更新时间
		* */
		private String update_date;
		/**
		* 字段名 ico
		* */
		private String logo;


		private String address;
		private String linkman;
		private String linktel;
		
		private String title;
		private String state;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLinkman() {
			return linkman;
		}

		public void setLinkman(String linkman) {
			this.linkman = linkman;
		}

		public String getLinktel() {
			return linktel;
		}

		public void setLinktel(String linktel) {
			this.linktel = linktel;
		}

		/**
		* 设置 内码
		* */
		public void setwithin_code(String within_code) 
		{
			this.within_code = within_code;
		}

		/**
		* 获取 内码
		* */
		public String getwithin_code()
		{
			return within_code;
		}

		/**
		* 设置 业务地点编号
		* */
		public void setcode(String code) 
		{
			this.code = code;
		}

		/**
		* 获取 业务地点编号
		* */
		public String getcode()
		{
			return code;
		}

		/**
		* 设置 业务地点名称
		* */
		public void setname(String name) 
		{
			this.name = name;
		}
		

		/**
		* 获取 业务地点名称
		* */
		public String getname()
		{
			return name;
		}
		
		/**
		* 设置 业务地点名称 - 英文
		* */
		public void setname_en(String name_en) 
		{
			this.name_en = name_en;
		}
		

		/**
		* 获取 业务地点名称 - 英文
		* */
		public String getname_en()
		{
			return name_en;
		}

		/**
		* 设置 备注说明
		* */
		public void setremark(String remark) 
		{
			this.remark = remark;
		}

		/**
		* 获取 备注说明
		* */
		public String getremark()
		{
			return remark;
		}

		 

		/**
		* 设置 创建
		* */
		public void setcreate_by(String create_by) 
		{
			this.create_by = create_by;
		}

		/**
		* 获取 创建
		* */
		public String getcreate_by()
		{
			return create_by;
		}

		
	public void setcreate_name(String create_name) 
		{
			this.create_name = create_name;
		}
		public String getcreate_name()
		{
			return create_name;
		}

		/**
		* 设置 创建时间
		* */
		public void setcreate_date(String create_date) 
		{
			this.create_date = create_date;
		}

		/**
		* 获取 创建时间
		* */
		public String getcreate_date()
		{
			return create_date;
		}

		

		public Integer getSn() {
			return sn;
		}

		public void setSn(Integer sn) {
			this.sn = sn;
		}

		/**
		* 设置 更新
		* */
		public void setupdate_by(String update_by) 
		{
			this.update_by = update_by;
		}

		/**
		* 获取 更新
		* */
		public String getupdate_by()
		{
			return update_by;
		}

		
	public void setupdate_name(String update_name) 
		{
			this.update_name = update_name;
		}
		public String getupdate_name()
		{
			return update_name;
		}

		/**
		* 设置 更新时间
		* */
		public void setupdate_date(String update_date) 
		{
			this.update_date = update_date;
		}

		/**
		* 获取 更新时间
		* */
		public String getupdate_date()
		{
			return update_date;
		}

		/**
		* 设置 ico
		* */
		public void setlogo(String logo) 
		{
			this.logo = logo;
		}

		/**
		* 获取 ico
		* */
		public String getlogo()
		{
			return logo;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
		
		
 
}
