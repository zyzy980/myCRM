package com.bba.util.sms;


/**
 * 基础模版库参数
 * @author 樊松
 * @date 2018/3/17
 * 
 */
public class Data {
	private First first;			//标题
	private Keyword1 keyword1;		//内容1
	private Keyword2 keyword2;		//内容2
	private Keyword3 keyword3;		//内容3
	private Keyword4 keyword4;		//内容4
	private Keyword5 keyword5;		//内容5
	private Remark remark;			//备注
	
	public Data(){}
	
	/**没备注三参数*/
	
	public Data(First first, Keyword1 keyword1, Keyword2 keyword2,
				Keyword3 keyword3) {
		this(first, keyword1, keyword2, keyword3, null, null, null);
//		this.first = first;
//		this.keyword1 = keyword1;
//		this.keyword2 = keyword2;
//		this.keyword3 = keyword3;
	}
	
	/**有备注三参数*/
	
	public Data(First first, Keyword1 keyword1, Keyword2 keyword2,
				Keyword3 keyword3, Remark remark) {
		this(first, keyword1, keyword2, keyword3, null, null, remark);
//		this.first = first;
//		this.keyword1 = keyword1;
//		this.keyword2 = keyword2;
//		this.keyword3 = keyword3;
//		this.remark = remark;
	}
	
	/**有备注四参数*/
	
	public Data(First first, Keyword1 keyword1, Keyword2 keyword2,
				Keyword3 keyword3, Keyword4 keyword4, Remark remark) {
		this(first, keyword1, keyword2, keyword3, keyword4, null, remark);
//		this.first = first;
//		this.keyword1 = keyword1;
//		this.keyword2 = keyword2;
//		this.keyword3 = keyword3;
//		this.keyword4 = keyword4;
//		this.remark = remark;
	}
	
	/**有备注五参数*/
	
	public Data(First first, Keyword1 keyword1, Keyword2 keyword2, Keyword3 keyword3, Keyword4 keyword4, Keyword5 keyword5, Remark remark){
		this.first = first;
		this.keyword1 = keyword1;
		this.keyword2 = keyword2;
		this.keyword3 = keyword3;
		this.keyword4 = keyword4;
		this.keyword5 = keyword5;
		this.remark = remark;
	}

	public First getFirst() {
		return first;
	}

	public void setFirst(First first) {
		this.first = first;
	}

	public Keyword1 getKeyword1(){
		return keyword1;
	}

	public void setKeyword1(Keyword1 keyword1) {
		this.keyword1 = keyword1;
	}

	public Keyword2 getKeyword2() {
		return keyword2;
	}

	public void setKeyword2(Keyword2 keyword2) {
		this.keyword2 = keyword2;
	}

	public Keyword3 getKeyword3(){
		return keyword3;
	}

	public void setKeyword3(Keyword3 keyword3){
		this.keyword3 = keyword3;
	}

	public Keyword4 getKeyword4(){
		return keyword4;
	}

	public void setKeyword4(Keyword4 keyword4) {
		this.keyword4 = keyword4;
	}

	public Keyword5 getKeyword5(){
		return keyword5;
	}

	public void setKeyword5(Keyword5 keyword5){
		this.keyword5 = keyword5;
	}

	public Remark getRemark() {
		return remark;
	}

	public void setRemark(Remark remark) {
		this.remark = remark;
	}
}
