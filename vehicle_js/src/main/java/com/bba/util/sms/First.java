package com.bba.util.sms;

/**
 * @Auther: Administrator
 * @Date: 2018/12/19 09:58
 * @Description:
 */
public class First {
    private String value;		//信息内容
    private String color;		//内容颜色(rbg值),不填的话默认黑色

    public First(){}
    public First(String value){
        this.value = value;
        this.color = "#FF0000";
    }
    public First(String value, String color){
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
