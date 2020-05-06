﻿/** ********************************************全局变量****************************************** */
var Global_Constant = {
	// 新增弹出窗体
	Global_Constant_CallerType_Tab : "T",
	Global_Constant_CallerType_Dialog : "D",
	// 是与否
	Global_Constant_YES : "0",
	Global_Constant_NO : "1",
	Global_Constant_UserLevel_Driver : "0",
	
	Global_Constant_Yee:'1',
	Global_Constant_Noo:'0',
	
	Global_Constant_yes:"Y",
	Global_Constant_no: "N",
};

/*********全局基础档案key***********/

var Global_BaseDataKey = {
    CR_CUSTOMER: "CR_CUSTOMER",
    ZD_CARRIER: "ZD_CARRIER",
    EXPORT_TO_EXCEL_CURRENT: "0"
}

/** ********************************************全局字典****************************************** */
var Global_DicType = {
	//城市档案
    CITY_ARCHIVE: "CITY_ARCHIVE",
	//运输方式
    TRANS_MODE: "TRANS_MODE",
    //运输方案
    TRANS_FANGAN: "TRANS_FANGAN",
	//合同状态
    CONTRACT_STATE: "CONTRACT_STATE",
	//暂定合同类型
    Temp_ht_type:"Temp_ht_type",
	//结算状态
    JS_STATE: "JS_STATE",
    //对上对账状态
    DS_DZ_STATE: "DS_DZ_STATE",

	//对下结算状态
	JS_DOWN_STATE:"JS_DOWN_STATE",

    Global_DicType_isgd:"isgd",
	Global_DicType_Status : "Status",
	// Add(仓储中心类别)
	Global_DicType_Zd_Whcenter : "Storage",
	// Add(库区档案类别)
	Global_DicType_Zd_Area : "Area",
	//库区属性
	Global_DicType_Area_Att :"AreaAttribute",
	//货主资料
	Global_DicType_Wz_Owner : "WzOwner",
	//价值等级
	Global_DicType_Level:"value_Level",
	//批次管理方式
	Global_DicType_Batch:"Batch",
	//准许Asn超量
	Global_DicType_Asn_flag:"Asn_flag",
	//生成交付订单方式
	Global_DicType_CreateDelivery_type:"CreateDelivery_type",
	//备货交付订单数量方式
	Global_DicType_CreateDelivery_qty:"CreateDelivery_qty",
	//质检类型
	Global_DicType_Quality_type:"Quality_type",
	//出库批次顺序
	Global_DicType_Out_Batch:"Out_Batch",
	//货物属性
	Global_DicType_Goods_Att:"Goods_Attribute",
	//拣货方式
	Global_DicType_Pick_type:"Pick_type",
	//用户任务类型
	Global_DicType_UserTask_Kind:"UserTask_Kind",
	// 审核状态
	Global_DicType_CheckState : "CheckState",
	// 公共状态(有效/无效)
	Global_DicType_BaseState : "BaseState",
	// 公共状态(是/否)
	Global_DicType_CommonStateYesNo : "CommonStateYesNo",
	// (单号类别)
	Global_DicType_Biil_Sheetid_Sheet_Type : "Sheet_Type",
	// (单号规则)
	Global_DicType_Biil_Sheetid_Rule_Type : "Rule_Type",
	//包装箱类型
	Global_DicType_Statue :"statue",
	//用户所属类别
	Global_DicType_User_Userlevel :"User_Userlevel",
	//数据来源 0=业务系统；1=APP
	Global_DicType_DataFrom : "0",
	
	Global_DicType_Type:"type",
	// 进出库
	Global_DicType_YwManager_IN_OUT : "IN_OUT",
	//与零件对应的关系
	Global_DicType_ZD_PACKAGE_Ref_Parts : "Ref_Parts",
	// 常用的有效、无效、
	Global_DicType_BaseStateValid : "0",
	Global_DicType_BaseStateInValid : "1",

	// CheckState， 常用的0.有效、1.审核 2.无效、
	Global_DicType_CheckState_Valid : "0",
	Global_DicType_CheckState_Pause : "1",
	Global_DicType_CheckState_InValid : "2",
 
	Global_DicType_Bill_Transfer_Tast_TaskPriority:"TaskPriority",
	 
	Global_DicType_CommonState:"CommonStateType",
	Global_DicType_CommonYesNo:"CommonYesNoType",
	Global_DicType_LocationKind:"LocationKind",
	
	Global_DicType_SUPPLY_METHOD:"SUPPLY_METHOD",
	Global_DicType_waybilltype:"ORDERKIND",
	Global_DicType_INTLTRANSTYPE:"INTLTRANSTYPE",


	JS_DOWN_BUSINESS_ORDER:"JS_DOWN_BUSINESS_ORDER",
	JS_DOWN_CARRIER_STATE:"JS_DOWN_CARRIER_STATE",
	JS_DOWN_DATA_TYPE:"JS_DOWN_DATA_TYPE",

	NO_VEHICLE_UP_JS_STATE:"NO_VEHICLE_UP_JS_STATE",

	NO_VEHICLE_DOWN_JS_STATE:"NO_VEHICLE_DOWN_JS_STATE",

	JS_NON_DOWN_DATA_TYPE:"JS_NON_DOWN_DATA_TYPE",

	DS_DZ_NON_STATE:"DS_DZ_NON_STATE",

	baobiao_payplan_state:"baobiao_payplan_state",
	baobiao_payplan_type:"baobiao_payplan_type",
	baobiao_payplan_data_type:"baobiao_payplan_data_type",
	//业务地点档案默认图片
	Global_defaultYwLocationPic:"../../Resource/images/index/defaultYwLocationPic.png",
	 //审核状态
    Global_DicType_CheckState: "CheckState",
     //过期状态
    Global_DicType_GQState: "GQState",
    //车辆类型
    Global_DicType_TruckType:"TruckType",
    //业务系统类型
    Global_DicType_YwKind:"YwKind",
    //通用状态
    Global_DicType_State:"States",
    //地点分类
    Global_DicType_kind:"kind",
    //承运商地点分类
    Global_DicType_CarrierKind:"CarrierKind",
    //驾驶证类别
    Global_DicType_ZdDriverKind:"ZdDriverKind",
    //装货方式
    Global_DicType_LOAD_TYPE:"LOAD_TYPE",
    //卸货方式
    Global_DicType_UNLOAD_TYPE:"UNLOAD_TYPE",
    //客户分类
    Global_DicType_divkind:"divkind",
    //客户状态
    Global_DicType_CusState:"CusState", 
    //订单状态
    Global_DicType_STATE:"STATE",

	/**z整车结算系统状态归类***********************************************/
	/**基础档案*/
	//客户分类
	Global_DicType_divtype:"CustomerSupplier",
	//客户状态
	Global_DicType_CusStatus:"Cus_Status",
    Global_DicType_Cus_Link_Type:"Cus_Link_Type",
    CAR_TYPE:"CAR_TYPE",
    /**业务管理*/
    //业务数据状态
    Business_State:"Business_State",
	//质损状态
    Mass_loss_State:"Mass_loss_State",
	//结算标识
    Business_JsState:"Business_JsState",
	//数据来源
    Data_From:"Data_From",
	//车辆项目
    Vehicle_Project:"Vehicle_Project",
	//非商品车类型
    Non_Type:"Non_Type",
	//非商品车属性
    Non_Attribute:"Non_Attribute",
	/**保费*/
    Premium_State:"Premium_State",
    Premium_Js_State: "Premium_Js_State",
	/**台账管理*/
	//可生成台账的对账单状态
    Dz_State:"Dz_State",
	//对账单类型
    Dz_Type:"Dz_Type",
	//台账状态
	Ledger_State:"Ledger_State",
    //台账类型
    Ledger_Type:"Ledger_Type",
	//补差类型
    COMPENSATION_TYPE:"COMPENSATION_TYPE",
    //补差状态
    COMPENSATION_STATE:"COMPENSATION_STATE",
    /**文档管理*/
    Questions_State:"Questions_State",
    Questions_Type:"Questions_Type",

	/**发票管理*/
	Invoice_State:"Invoice_State",
	/******************************************************************/

	Global_DicType_orderState:"orderState",
    //在途状态
    Global_DicType_forglobal_state:"forglobal_state",
     //运输方式
    Global_DicType_TransportType : "transportType",
    //托盘摆放方向
    Global_DicType_DIRECTION:"DIRECTION",	
    //供应商、承运商确认状态
   Global_DicType_CONFIRM:"CONFIRM",
   //默认发货地
   Global_DicType_CommonStateYesNos : "CommonStateYesNos",
   //状态是否启用
   Global_DicType_STATES:"STATES",
	  
   Global_DicType_LoadingSettingItem:"LoadingSettingItem",

	//分段类别
	Global_DicType_routeType:"routeType",
	//运单明细状态
	Global_DicType_sheet_yw_order_detail_state:"sheet_yw_order_detail_state",
   
   //0.jqGrid 1.时间轴 2.地图 3.异常
   TRAINDETAIL_JQGRID : "0",
   TRAINDETAIL_TIMELINE : "1",
   TRAINDETAIL_MAP : "2",
   TRAINDETAIL_EXCEPTION : "3",

	// 订单详情页面的标签下标 0 轨迹节点 1 地图 2 异常 3 图片
	SALE_NODES : "0",
	SALE_MPAS : "1",
	SALE_ABNORMAL : "2",
	SALE_PICTURE : "3"

}
var Global_Constant_CallerType_Tab = "T";
var BaseStateInValid = "0";
var BaseStateValid = "1";
/** ********************************************字典****************************************** */
var zidian = {
		BaseStateInValid : "0",
		BaseStateValid : "1",
		BaseStatePause : "2",
		BaseTypeQuYu : "区域",
		BaseTypeZhanDian : "站点",	
}
