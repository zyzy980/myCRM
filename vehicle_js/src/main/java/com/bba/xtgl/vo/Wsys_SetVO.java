package com.bba.xtgl.vo;
/**
 * 系统设置
 * @author Administrator
 *
 */
public class Wsys_SetVO {

	private String within_code;//内码
	private String whcenter;//仓储中心
	private String loadtruck_flag;//是否有装车运输
	private String outstock;//出库库存扣减环节
	private String block_pda;//冻结任务pda 执行
	private String move_pda;//库位调整任务PDA执行
	private String putaway_loca;//是否上架暂存区管理
	private String remark;//备注
	private String create_by;//创建
	private String create_date;//创建时间
	private String putaway_task;//上架方式设置
	
	
	
	
	public String getPutaway_task() {
		return putaway_task;
	}
	public void setPutaway_task(String putaway_task) {
		this.putaway_task = putaway_task;
	}
	public String getWithin_code() {
		return within_code;
	}
	public void setWithin_code(String within_code) {
		this.within_code = within_code;
	}
	public String getWhcenter() {
		return whcenter;
	}
	public void setWhcenter(String whcenter) {
		this.whcenter = whcenter;
	}
	public String getLoadtruck_flag() {
		return loadtruck_flag;
	}
	public void setLoadtruck_flag(String loadtruck_flag) {
		this.loadtruck_flag = loadtruck_flag;
	}
	public String getOutstock() {
		return outstock;
	}
	public void setOutstock(String outstock) {
		this.outstock = outstock;
	}
	public String getBlock_pda() {
		return block_pda;
	}
	public void setBlock_pda(String block_pda) {
		this.block_pda = block_pda;
	}
	public String getMove_pda() {
		return move_pda;
	}
	public void setMove_pda(String move_pda) {
		this.move_pda = move_pda;
	}
	public String getPutaway_loca() {
		return putaway_loca;
	}
	public void setPutaway_loca(String putaway_loca) {
		this.putaway_loca = putaway_loca;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
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
	
	
}
