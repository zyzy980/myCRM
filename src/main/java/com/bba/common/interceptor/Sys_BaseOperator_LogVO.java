package com.bba.common.interceptor;
public class Sys_BaseOperator_LogVO{

	private String create_by;
	
	private String create_date;
	
	private String table_name;

	private String table_code;
	
	private String field_name;
	
	private String field_code;
	
	private String prev_data;
	
	private String next_data;
	
	private String primary_field;
	
	private String primary_field_value;
	
	private String id;
	
	public String getCreate_by() {
		return create_by;
	}
	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	public String getField_name() {
		return field_name;
	}
	public void setField_name(String field_name) {
		this.field_name = field_name;
	}
	public String getPrev_data() {
		return prev_data;
	}
	public void setPrev_data(String prev_data) {
		this.prev_data = prev_data;
	}
	public String getNext_data() {
		return next_data;
	}
	public void setNext_data(String next_data) {
		this.next_data = next_data;
	}
	public String getPrimary_field() {
		return primary_field;
	}
	public void setPrimary_field(String primary_field) {
		this.primary_field = primary_field;
	}
	public String getPrimary_field_value() {
		return primary_field_value;
	}
	public void setPrimary_field_value(String primary_field_value) {
		this.primary_field_value = primary_field_value;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTable_code() {
		return table_code;
	}
	public void setTable_code(String table_code) {
		this.table_code = table_code;
	}
	public String getField_code() {
		return field_code;
	}
	public void setField_code(String field_code) {
		this.field_code = field_code;
	}
	
}
