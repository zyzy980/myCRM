/**********************************************全局变量*******************************************/
var Global_Constant = {
		Global_Constant_YES : "0",
		Global_Constant_NO : "1",
    // 用户所属类型:0=司机；1=操作员；2=合作伙伴,3=客户,4=站点
    UserLevel_Driver: "0",
    UserLevel_Operater: "1",
    UserLevel_Carrier: "2",
    UserLevel_Customer: "3",
    UserLevel_Site: "4",
    // cookie key 名称
    Global_Cookie_OftenUsed_DeliveryLocation: "OftenUsedDeliveryLocation",
    Global_Cookie_OftenUsed_DeliverySeachLocation: "OftenUsedDeliverySeachLocation",
    Global_Cookie_OftenUsed_DeliveryFenLocation: "OftenUsedDeliveryFenLocation",
    Global_Cookie_OftenUsed_DeliverySiteLocation: "OftenUsedDeliverySiteLocation",
    Global_Cookie_OftenUsed_ConsigneeLocation: "OftenUserdConsigneeLocation",
    Global_Cookie_OftenUsed_ConsigneeSeachLocation: "OftenUserdConsigneeSeachLocation",
    Global_Cookie_OftenUsed_ConsigneeFenLocation: "OftenUserdConsigneeFenLocation",
    Global_Cookie_OftenUsed_Customer: "OftenUsedCustomer",
    // 计划yw_plan状态
    Global_Constant_YwOrderState_Normal: 0,
    Global_Constant_YwOrderState_Logout: 1,
    Global_Constant_YwOrderState_Check: 2,
    Global_Constant_YwOrderState_Plan: 4,
    Global_Constant_YwOrderState_DriverAccept: 5,
    Global_Constant_YwOrderState_Executing: 6,
    Global_Constant_YwOrderState_TransportComplete: 8,
    Global_Constant_YwOrderState_ValidateComplete: 9,
    //新增弹出窗体
    Global_Constant_CallerType_Tab: "T",
    Global_Constant_CallerType_Dialog: "D",
    // 提货计划审核状态
    Global_Constant_EstimatePlan_ZC: "1",
    Global_Constant_EstimatePlan_Z: "2",
    Global_Constant_EstimatePlan_LD: "3",
    Global_Constant_EstimatePlan_KD: "4",
    //通用状态是否(N/Y)
    Global_Constant_YesOrNo_No: "N",
    Global_Constant_YesOrNo_Yes: "Y",
};
/**********************************************全局变量*******************************************/


/**********************************************提货计划审核状态---弃用*******************************************/
//var Global_Constant_EstimatePlan_State_DJ = {
//    Code: "-1",Name:"冻结",Color:"red"
//};
//var Global_Constant_EstimatePlan_State_ZC = {
//    Code: "0",Name:"正常",Color:"#222222"
//};
//var Global_Constant_EstimatePlan_State_JHTJ = {
//    Code: "1",Name:"计划提交",Color:"#222222"
//};
//var Global_Constant_EstimatePlan_State_ZGYS = {
//    Code: "2",Name:"业务主管运输方式审核",Color:"#222222"
//};
//var Global_Constant_EstimatePlan_State_ZGPZ = {
//    Code: "3",Name:"业务主管配载审核",Color:"#222222"
//};
//var Global_Constant_EstimatePlan_State_SWZY = {
//    Code: "4",Name:"商务专员审核",Color:"#222222"
//};
//var Global_Constant_EstimatePlan_State_BMYS = {
//    Code: "5",Name:"部门主管运输方式审核",Color:"#222222"
//};
//var Global_Constant_EstimatePlan_State_BMPZ = {
//    Code: "6",Name:"部门主管配载审核",Color:"#222222"
//};
//var Global_Constant_EstimatePlan_State_TZCY = {
//    Code: "7",Name:"通知承运商",Color:"#222222"
//};
//var Global_Constant_EstimatePlan_State_TBZC = {
//    Code: "8",Name:"填报正常",Color:"#222222"
//};
//var Global_Constant_EstimatePlan_State_TBYC = {
//    Code: "9",Name:"填报异常",Color:"#222222"
//};
//var Global_Constant_EstimatePlan_State_TBSH = {
//    Code: "10",Name:"填报审核通过",Color:"#222222"
//};
//var Global_Constant_EstimatePlan_State_TBCH = {
//    Code: "11",Name:"填报审核不通过",Color:"#222222"
//};
/**********************************************发运审核状态*******************************************/

var Global_Constant_EstimateDespatch_State_ZC = {
    Code: "0",Name:"正常",Color:"#222222"
};
var Global_Constant_EstimateDespatch_State_TJ = {
    Code: "1",Name:"任务派发",Color:"#222222"
};
var Global_Constant_EstimateDespatch_State_CLDD = {//确认到达
    Code: "2",Name:"车辆到达",Color:"#222222"
};
var Global_Constant_EstimateDespatch_State_ZCWB = {//
    Code: "3",Name:"装车完毕",Color:"#222222"
};
var Global_Constant_EstimateDespatch_State_GXZT = {
    Code: "4",Name:"干线在途",Color:"#222222"
};
var Global_Constant_EstimateDespatch_State_PSZ = {
    Code: "5",Name:"配送中",Color:"#222222"
};
var Global_Constant_EstimateDespatch_State_SJJF = {
    Code: "6",Name:"司机交付",Color:"#222222"
};
var Global_Constant_EstimateDespatch_State_YSWC = {
    Code: "7",Name:"运输完成",Color:"#222222"
};
//var Global_Constant_EstimateDespatch_State_ZC = {
//    Code: "0",Name:"正常",Color:"#222222"
//};
//var Global_Constant_EstimateDespatch_State_TJ = {
//    Code: "1",Name:"物流专员提交",Color:"#222222"
//};
//var Global_Constant_EstimateDespatch_State_ZGYS = {
//    Code: "2",Name:"业务主管审核",Color:"#222222"
//};
//var Global_Constant_EstimateDespatch_State_ZGPZ = {
//    Code: "3",Name:"商务专员审核",Color:"#222222"
//};
//var Global_Constant_EstimateDespatch_State_SWZY = {
//    Code: "4",Name:"部门主管运输方式审核",Color:"#222222"
//};
//var Global_Constant_TakeDeliverDespatchState_BMPZ = {
//    Code: "5",Name:"部门主管指定承运商审核",Color:"#222222"
//};
//var Global_Constant_EstimateDespatch_State_QRZC = {
//    Code: "7",Name:"确认装车",Color:"#222222"
//};


/*零件类别*/
var Global_Constant_PartType_WX = {
    Code: "0",Name:"危险品",Color:"#222222"
};
var Global_Constant_PartType_YS = {
    Code: "1",Name:"易碎品",Color:"#222222"
};
var Global_Constant_PartType_GZ = {
    Code: "2",Name:"贵重品",Color:"#222222"
};

/**********************************************发运管理业务类型*******************************************/
var Global_Constant_Shipment_YwType_PS = {
    Code: "PS",Name:"PDC-4S配送"
};
var Global_Constant_Shipment_YwType_ZC = {
    Code: "ZC",Name:"PDC-PDC整车"
};
var Global_Constant_Shipment_YwType_FH = {
    Code: "FH",Name:"索赔件运输"
};
var Global_Constant_Shipment_YwType_DZ = {
    Code: "DZ",Name:"POPP运输"
};
var Global_Constant_Shipment_YwType_LJ = {
    Code: "LJ",Name:"周转料架运输"
};
/**********************************************提货计划审核状态--新*******************************************/

var Global_Constant_EstimatePlan_State_ZC = {
    Code: "0",Name:"正常",Color:"#222222"
};
var Global_Constant_EstimatePlan_State_JHTJ = {
    Code: "1",Name:"计划提交",Color:"#222222"
};
var Global_Constant_EstimatePlan_State_SWZY = {
    Code: "2",Name:"商务专员审核",Color:"#222222"
};
var Global_Constant_EstimatePlan_State_BMZG = {
    Code: "3",Name:"部门主管审核",Color:"#222222"
};
var Global_Constant_EstimatePlan_State_TZCY = {
    Code: "4",Name:"车辆安排中",Color:"#222222"
};
var Global_Constant_EstimatePlan_State_TBZC = {
    Code: "5",Name:"车辆已安排",Color:"#222222"
};

/**********************************************全局字典*******************************************/
var Global_DicType = {
    //客户类别
    Global_DicType_ZdCusType:"ZdCusType",
    // 相关缓存名称（cookie）
    Global_cookie_othenUseArea : "othenUseArea",
    Global_cookie_locationUseArea : "locationUseArea",
    //下包商或者客户
    Global_DicType_ContractorOrCus : "ContractorOrCus",
    Global_DicType_YunShuFangXiang : "YunShuFangXiang",
    Global_DicType_SiteorLocationKind : "SiteorLocationKind",
    //地点编号类型
    Global_DicType_LocationKind : "LocationKind",
    //承运商类别
    Global_DicType_ZdContractorType: "ZdContractorType",
    //通用状态 无效/有效
    Global_DicType_CommonState: "CommonStateType",
    //下包商或者客户
    Global_DicType_PassOrFail: "PassOrFail",
    //异常类型
    Global_DicType_ErrorKind: "ErrorKind",
    // 运输方式
    Global_DicType_TransportType: "TransportType",
    //车辆业务范围
    Global_DicType_TruckService: "TruckService",
    //订单类型
    Global_DicType_OrderType: "OrderType",
     //订单状态
    Global_DicType_OrderStatus: "OrderStatus",
    //通用是否
    Global_DicType_CommonYesNo: "CommonYesNoType",
    //快递发运属性
    Global_DicType_ExpressType: "PartsExpressProperty",
    //快递状态
    Global_DicType_ExpreState: "ExpreState",
    //状态编码
    Global_DicType_StateCode: "StateCode",
    //驾证类型
    Global_DicType_DriverKind: "DriverKind",
    //审核状态
    Global_DicType_CheckState: "CheckState",
    //审核结果
    Global_DicType_CheckResult: "CheckResult",
    //经销商类别
    Global_DicType_DealerKind: "DealerKind",
    //经销商等级
    Global_DicType_DealerLevel: "DealerLevel",
    //经销商区域
    Global_DicType_DealerArea: "DealerArea",
    //经销商状态
    Global_DicType_DealerState: "DealerState",
    //车辆类型
    Global_DicType_TruckType: "TruckType",
    //延期类型
    Global_DicType_DelayState: "delayState",
    //过期状态
    Global_DicType_GQState: "GQState",
    //承运商类型
    //车辆类型
    Global_DicType_ContractorType: "ContractorType",
    //使用性质
    Global_DicType_UsingType: "UsingType",
    //站点类别
    Global_DicType_SiteType : "SiteType",
    //提货计划审核状态
//    Global_DicType_TakeDeliverPlanCheckState: "DeliverPlanCheckState",
Global_DicType_TakeDeliverPlanCheckState: "PlanCheckState",
    //发运审核状态
//    Global_DicType_TakeDeliverDespatchState: "despatchState",
Global_DicType_TakeDeliverDespatchState: "despatchCheckState",
    //业务类型
    Global_DicType_ShipmentYwType: "ShipmentYwType",

    Global_DicType_TruckType_Qian:"0",
    Global_DicType_TruckType_Gua: "1",

    // 用户所属类型:0=司机；1=操作员；2=合作伙伴,3=客户
    Global_DicType_UserLevel_Driver: "0",
    Global_DicType_UserLevel_Operater: "1",
    Global_DicType_UserLevel_Carrier: "2",
    Global_DicType_UserLevel_Customer: "3",
    // 常用的有效、无效、
    Global_DicType_BaseStateInValid: "0",
    Global_DicType_BaseStateValid: "1",
    Global_DicType_BaseState_Pause: "2",
    // 系统相关默认图片
     Global_defaultYwLocationPic:"Resource/images/index/defaultYwLocationPic.png"

    
}
var Global_defaultYwLocationPic = "Resource/images/index/defaultYwLocationPic.png";
/**********************************************全局字典*******************************************/

