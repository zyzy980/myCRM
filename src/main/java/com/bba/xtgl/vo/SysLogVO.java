package com.bba.xtgl.vo;

public class SysLogVO {
	private String log_id;			//id
	
	private String tableName;		//表名
	
	private String fieldName;		//字段名称
	
	private String operation;		//操作步骤
	
	private String operatingtime;	//操作时间
	
	private String operator;		//操作人
	
	private String operationfield;	//操作字段
	
	private String fielddescription;//字段描述
	
	private String bfoperation;		//操作前数据
	
	private String afoperation;		//操作后数据
	
	private String table_name;
	
	private String column_name;
	
	private String comments;

	public String getLog_id() {
		return log_id;
	}

	public void setLog_id(String log_id) {
		this.log_id = log_id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getOperatingtime() {
		return operatingtime;
	}

	public void setOperatingtime(String operatingtime) {
		this.operatingtime = operatingtime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperationfield() {
		return operationfield;
	}

	public void setOperationfield(String operationfield) {
		this.operationfield = operationfield;
	}

	public String getFielddescription() {
		return fielddescription;
	}

	public void setFielddescription(String fielddescription) {
		this.fielddescription = fielddescription;
	}

	public String getBfoperation() {
		return bfoperation;
	}

	public void setBfoperation(String bfoperation) {
		this.bfoperation = bfoperation;
	}

	public String getAfoperation() {
		return afoperation;
	}

	public void setAfoperation(String afoperation) {
		this.afoperation = afoperation;
	}

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
	
}
