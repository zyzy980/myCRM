package com.bba.util.sms;

/**
 * 第五个内容 
 * @author 樊松
 * @date 2018/3/17
 * 
 */
public class Keyword1 {
	private String value;		//信息内容
	private String color;		//内容颜色(rbg值),不填的话默认黑色
	
	public Keyword1(){}
	public Keyword1(String value){
		this.value = value;
		this.color = "#FF0000";
	}
	public Keyword1(String value, String color){
		this.value = value;
		this.color = color;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}
