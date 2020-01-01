package com.bba.jcda.vo;

import lombok.Data;

/**
 * @author Px
 * @version 1.0.0
 * @since 
 * 创建时间：2018年11月24日
 * 功能描述： 基础档案 司机档案 页面View  VO
 */


@Data
public class ZdDriverFileVO {
	private String sn;
	private String state;
	private String isgq;
	private String code;
	private String driverName;
	private String contractor;
	private String carrierName;
	private String telphone;
	private String credentials;
	private String endDate;
	private String transportdate;
	private String admissiondtBegin;
	private String admissiondtEnd;
	private String createBy;
	private String createDate;
	private String updateBy;
	private String updateDate;
}
