package com.bba.jcda.vo;

import lombok.Data;

/**SYS_SERVER_API_SET or SYS_APILOG*/
@Data
public class SysServerApiSetVO {
	private String sn;//SN
	private String item_code;//接口编号，jobName
	private String item_name;//接口名，jobGroupName
	private String server_ip;//服务器IP
	private String status;//状态 0不执行定时任务，1执行定时任务
	private String item_mode;//接口定时模式
	private String frequency_day;//(方式1)接口频率 单位:天（第几天）
	private String begin_dt;//(方式1)接口启动时间  格式：hh:mi
	private String frequency;//(方式2)接口频率 单位:分钟
	private String previous_begin_dt;//上一次开始时间
	private String previous_end_dt;//上一次结束时间
	private String remark;//备注
	private String create_by;//创建人
	private String create_date;//创建日期
	private String update_by;//更新人
	private String update_date;//更新日期
	private String class_name;//类名
	private String lock_status;//锁状态
}
