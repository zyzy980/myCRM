package com.bba.xtgl.vo;

import java.util.List;

import com.bba.common.interceptor.EntityField;
import com.bba.util.MD5Utils;
import com.bba.util.SessionUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SysUserVO extends BaseVo {
	private Integer id;

	@EntityField(value = "用户编号")
	private String userId;// 用户编号
	
	private String code;

	@EntityField(value = "用户姓名")
	private String realName;// 用户名称

	private String within_code;

	private String whcenter;

	@EntityField(value = "登录密码")
	private String password;

	private String status;

	private String dept_id;

	@EntityField(value = "手机号码")
	private String mobileNo;// 手机

	private String position;// 职务

	@EntityField(value = "性别")
	private String sex;

	private String idcard;// 身份证号

	private String tel;// tel

	@EntityField(value = "邮箱地址")
	private String mail;// 电子邮件 - 用户名
	private String mailhost;// 电子邮件 - 主机名
	private String mailport;// 电子邮件 - 端口
	private String mailpassword;// 电子邮件 - 密码


	private String fax;// 传真

	@EntityField(value = "所属类别")
//	private String userLevel;// 所属类别

	private String address;// 地址

	private String birthday;// 生日

	private String headimg;

	private String province;

	private String company_id;// 所属公司

	private String city;

	private String remark;// 备注

	private String create_by;

	private String create_date;

	private String update_by;

	private String update_date;
	private String token;

	@EntityField(value = "类别值")
	private String partner;
	private String partner_name;

	private String wxopenid;

	private String isCus;// 是否是客户
	private String isContractor;// 是否是承运商操作员
	private String isThDriver;// 是否是提货司机
	private String isPsDriver;// 是否是配送司机
	private String isOp;// 是否是内部司机
	
	private String contractorCode;//承运商编号，用户选择为承运商时必填
	private String cusCode;//客户编号，用户选择为客户时必填

	/**
	 * 登录时承运商和供应商编号
	 */
	private String contractor_supplier_ywlocation;

	private String userloginIp;
	private String userloginTime;

	private String wechar;
	/**
	 * 数据库查询出来的格式多个角色用/分开: 管理员/客户
	 */
	private String userRoles;

	private String logoPath;

	private List<SysUserLocationVO> userLocationList;

	private String whcenter_name;
	private String whcenter_name_en;

	private String yw_title; // 登陆的业务地点描述
	
	private String wechat;

	private String ywId; // 运单号，在推送公众号信息处使用这个字段

	public SysUserVO() {
		super();
	}
	private String sensitive_authority;
	private String validDate;

	private String is_q_site;			//起运站点
	private String is_p_site;			//配送站点
	private String site;				//站点编号

	private String userLevel;		  //0：管理员；1：操作员；2：商务人员；3：财务人员；4：其它；

	private Integer superior_userid;//上级用户
	private String superior_user;//上级用户姓名
	private String user_type;//用户分类  0 CRM  1交易者终端
	private String last_login_time;//最后一次登录时间
	private String trading_account;//交易账号
}

