package com.bba.jcda.vo;
import com.bba.common.annotation.Column;
import com.bba.common.annotation.Table;
import com.bba.common.interceptor.EntityField;
import lombok.Data;


@Data
@Table("CR_CUSTOMER_LINK")
public class Cr_Customer_LinkVO {

	@Column
	@EntityField(value = "ID")
	private String id;

	@Column
	@EntityField(value = "客户ID")
	private String cus_id;

	@Column
	@EntityField(value = "客户编号")
	private String cus_code;

	@Column
	@EntityField(value = "联系人")
	private String realname;

	@Column
	@EntityField(value = "手机号")
	private String mobileno;

	@Column
	@EntityField(value = "类型")
	private String type;

	@Column
	@EntityField(value = "职位")
	private String position;

	@Column
	@EntityField(value = "电邮")
	private String mail;

	@Column
	@EntityField(value = "微信号")
	private String wechat;

	@Column
	@EntityField(value = "对账单邮件")
	private String dui_flag;

	@Column
	@EntityField(value = "备注")
	private String remark;

	@Column
	@EntityField(value = "创建")
	private String create_by;

	@Column
	@EntityField(value = "创建时间")
	private String create_date;

	@Column
	@EntityField(value = "状态")
	private String state;


	@EntityField(value = "新增/删除")
	private String operatetype;
}
