package com.bba.jcda.vo;


import com.bba.common.interceptor.EntityField;
import com.bba.common.vo.PageVO;

/**
 * 零件档案信息
 */
public class ZdPartsVo extends PageVO{
	@EntityField(value = "内码")
	private String within_code;   
	@EntityField(value = "仓储中心")
	private String whcenter;  
	@EntityField(value = "货主编号")
	private String owner_code;  
	@EntityField(value = "货物类型")
	private String goods_typeid; 
	@EntityField(value = "货物编号")
	private String goods_no;          
	@EntityField(value = "货主物料编号")
	private String owner_goods;        
	@EntityField(value = "货物名称")
	private String goods_name;         
	@EntityField(value = "出库SNP")
	private String snp_out;            
    @EntityField(value = "库存SNP")
	private String snp;                
	@EntityField(value = "规格描述")
	private String spec;               
	@EntityField(value = "计量UNIT")
	private String unit;               
	@EntityField(value = "净重单位")
	private String net_unit;           
	@EntityField(value = "NET")
	private String net;    
	@EntityField(value = "长度单位")
	private String dim_unit;           
	@EntityField(value = "SNP长")
	private String snp_l;              
	@EntityField(value = "SNP宽")
	private String snp_w;              
	@EntityField(value = "SNP高")
	private String snp_h;              
	@EntityField(value = "包装方案")
	private String package_type;       
	@EntityField(value = "价值等级")
	private String value_level;        
	@EntityField(value = "是否易损")
	private String yisun;              
	@EntityField(value = "货物单价")
	private String price;              
	@EntityField(value = "质保期")
	private String lifecycle;          
	@EntityField(value = "批次管理")
	private String batch;              
	@EntityField(value = "货主批次管理")
	private String owner_batch;        
	@EntityField(value = "入库是否换装")
	private String repackage;           
	@EntityField(value = "准许ASN超量收")
	private String asn_flag;           
	@EntityField(value = "入库SNP")
	private String snp_in;             
	@EntityField(value = "入库需要捆绑")
	private String in_binding;         
	@EntityField(value = "入库分配库位方案")
	private String putawayloca_fa;     
	@EntityField(value = "生成交付订单方式")
	private String createdelivery_type;
	@EntityField(value = "备货交付方式")
	private String createdelivery_qty; 
	@EntityField(value = "是否质检")
	private String quality_flag;       
	@EntityField(value = "质检类型")
	private String quality_type;       
	@EntityField(value = "抽检比例")
	private String quality_rate;       
	@EntityField(value = "出库批次顺序")
	private String out_batch;          
	@EntityField(value = "拣货方式")
	private String pick_type;          
	@EntityField(value = "出库交付方案")
	private String delivery_fa;        
	@EntityField(value = "最高库存")
	private String max_stock;          
	@EntityField(value = "最低库存")
	private String min_stock;          
	@EntityField(value = "货物属性")
	private String attribute;          
	@EntityField(value = "备注")
	private String remark;             
	//@EntityField(value = "创建时间")
	private String create_date;        
	@EntityField(value = "创建人")
	private String create_by;          
	@EntityField(value = "状态")
	private String state;              
	@EntityField(value = "ID")
	private String id;                 
	@EntityField(value = "货物条码")
	private String goods_scanbar;      
	@EntityField(value = "箱子序列管理")
	private String box_sn;             
	@EntityField(value = "已设置上架库位")
	private String putaway_flag;       
	@EntityField(value = "已设置拣货库位")
	private String picking_flag;

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
	public String getOwner_code() {
		return owner_code;
	}
	public void setOwner_code(String owner_code) {
		this.owner_code = owner_code;
	}
	public String getGoods_typeid() {
		return goods_typeid;
	}
	public void setGoods_typeid(String goods_typeid) {
		this.goods_typeid = goods_typeid;
	}
	public String getGoods_no() {
		return goods_no;
	}
	public void setGoods_no(String goods_no) {
		this.goods_no = goods_no;
	}
	public String getOwner_goods() {
		return owner_goods;
	}
	public void setOwner_goods(String owner_goods) {
		this.owner_goods = owner_goods;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getSnp_out() {
		return snp_out;
	}
	public void setSnp_out(String snp_out) {
		this.snp_out = snp_out;
	}
	public String getSnp() {
		return snp;
	}
	public void setSnp(String snp) {
		this.snp = snp;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getNet_unit() {
		return net_unit;
	}
	public void setNet_unit(String net_unit) {
		this.net_unit = net_unit;
	}
	public String getNet() {
		return net;
	}
	public void setNet(String net) {
		this.net = net;
	}
	public String getDim_unit() {
		return dim_unit;
	}
	public void setDim_unit(String dim_unit) {
		this.dim_unit = dim_unit;
	}
	public String getSnp_l() {
		return snp_l;
	}
	public void setSnp_l(String snp_l) {
		this.snp_l = snp_l;
	}
	public String getSnp_w() {
		return snp_w;
	}
	public void setSnp_w(String snp_w) {
		this.snp_w = snp_w;
	}
	public String getSnp_h() {
		return snp_h;
	}
	public void setSnp_h(String snp_h) {
		this.snp_h = snp_h;
	}
	public String getPackage_type() {
		return package_type;
	}
	public void setPackage_type(String package_type) {
		this.package_type = package_type;
	}
	public String getValue_level() {
		return value_level;
	}
	public void setValue_level(String value_level) {
		this.value_level = value_level;
	}
	public String getYisun() {
		return yisun;
	}
	public void setYisun(String yisun) {
		this.yisun = yisun;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getLifecycle() {
		return lifecycle;
	}
	public void setLifecycle(String lifecycle) {
		this.lifecycle = lifecycle;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getOwner_batch() {
		return owner_batch;
	}
	public void setOwner_batch(String owner_batch) {
		this.owner_batch = owner_batch;
	}
	public String getRepackage() {
		return repackage;
	}
	public void setRepackage(String repackage) {
		this.repackage = repackage;
	}
	public String getAsn_flag() {
		return asn_flag;
	}
	public void setAsn_flag(String asn_flag) {
		this.asn_flag = asn_flag;
	}
	public String getSnp_in() {
		return snp_in;
	}
	public void setSnp_in(String snp_in) {
		this.snp_in = snp_in;
	}
	public String getIn_binding() {
		return in_binding;
	}
	public void setIn_binding(String in_binding) {
		this.in_binding = in_binding;
	}
	public String getPutawayloca_fa() {
		return putawayloca_fa;
	}
	public void setPutawayloca_fa(String putawayloca_fa) {
		this.putawayloca_fa = putawayloca_fa;
	}
	public String getCreatedelivery_type() {
		return createdelivery_type;
	}
	public void setCreatedelivery_type(String createdelivery_type) {
		this.createdelivery_type = createdelivery_type;
	}
	public String getCreatedelivery_qty() {
		return createdelivery_qty;
	}
	public void setCreatedelivery_qty(String createdelivery_qty) {
		this.createdelivery_qty = createdelivery_qty;
	}
	public String getQuality_flag() {
		return quality_flag;
	}
	public void setQuality_flag(String quality_flag) {
		this.quality_flag = quality_flag;
	}
	public String getQuality_type() {
		return quality_type;
	}
	public void setQuality_type(String quality_type) {
		this.quality_type = quality_type;
	}
	public String getQuality_rate() {
		return quality_rate;
	}
	public void setQuality_rate(String quality_rate) {
		this.quality_rate = quality_rate;
	}
	public String getOut_batch() {
		return out_batch;
	}
	public void setOut_batch(String out_batch) {
		this.out_batch = out_batch;
	}
	public String getPick_type() {
		return pick_type;
	}
	public void setPick_type(String pick_type) {
		this.pick_type = pick_type;
	}
	public String getDelivery_fa() {
		return delivery_fa;
	}
	public void setDelivery_fa(String delivery_fa) {
		this.delivery_fa = delivery_fa;
	}
	public String getMax_stock() {
		return max_stock;
	}
	public void setMax_stock(String max_stock) {
		this.max_stock = max_stock;
	}
	public String getMin_stock() {
		return min_stock;
	}
	public void setMin_stock(String min_stock) {
		this.min_stock = min_stock;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getCreate_by() {
		return create_by;
	}
	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGoods_scanbar() {
		return goods_scanbar;
	}
	public void setGoods_scanbar(String goods_scanbar) {
		this.goods_scanbar = goods_scanbar;
	}
	public String getBox_sn() {
		return box_sn;
	}
	public void setBox_sn(String box_sn) {
		this.box_sn = box_sn;
	}
	public String getPutaway_flag() {
		return putaway_flag;
	}
	public void setPutaway_flag(String putaway_flag) {
		this.putaway_flag = putaway_flag;
	}
	public String getPicking_flag() {
		return picking_flag;
	}
	public void setPicking_flag(String picking_flag) {
		this.picking_flag = picking_flag;
	}

}
