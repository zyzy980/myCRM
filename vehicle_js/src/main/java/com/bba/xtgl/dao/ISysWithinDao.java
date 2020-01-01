package com.bba.xtgl.dao;

import java.util.List;

import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.RegisterVO;
import com.bba.xtgl.vo.SysWithinCodeVO;

import com.bba.xtgl.vo.SysWithinVO;
import org.apache.ibatis.annotations.Param;

public interface ISysWithinDao{

	public List<SysWithinVO> findWithin(String swthinCode);

	public SysWithinVO getByWithinCode(String withinCode);
	public int batchUpdate(List<SysWithinVO> list);

	public void insert(@Param("vo")SysWithinVO vo);

	List<SysWithinCodeVO> findList(JqGridParamModel jqGridParamModel);

	/**
	 * @Description 删除 sys_authority
	 * @param withinCode 内码 下同
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteSysAuthority(String withinCode);

	/**
	 * @Description 删除 sys_module
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteSysModule(String withinCode);

	/**
	 * @Description 删除 sys_role
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteSysRole(String withinCode);

	/**
	 * @Description 删除 sys_rolebuttons
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteSysRoleButtons(String withinCode);

	/**
	 * @Description 删除 sys_sheetid
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteSysSheetId(String withinCode);

	/**
	 * @Description 删除 sys_users
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteSysUsers(String withinCode);

	/**
	 * @Description 删除 sys_users_token
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteSysUsersToken(String withinCode);

	/**
	 * @Description 删除 sys_within
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteSysWithin(String withinCode);

	/**
	 * @Description 删除 sys_within_set
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteSysWithinSet(String withinCode);

	/**
	 * @Description 删除 yw_fare_get
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwFareGet(String withinCode);

	/**
	 * @Description 删除 yw_fare_get_qtylist
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwFareGetQtyList(String withinCode);

	/**
	 * @Description 删除 yw_fare_get_sign
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwFareGetSign(String withinCode);

	/**
	 * @Description 删除 yw_fare_pay
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwFarePay(String withinCode);

	/**
	 * @Description 删除 yw_fare_pay_qtylist
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwFarePayQtyList(String withinCode);

	/**
	 * @Description 删除 yw_get
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwGet(String withinCode);

	/**
	 * @Description 删除 yw_invoice
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwInvoice(String withinCode);
	
	/**
	 * @Description 删除 yw_invoice_billitem
	 * @param 
	 * @Author lao li
	 * @Date  
	 * @return 
	*/
	void deleteYwInvoiceBillItem(String withinCode);

	/**
	 * @Description 删除 yw_invoice_pay
	 * @param 
	 * @Author lao li
	 * @Date  
	 * @return 
	*/
	void deleteInvoicePay(String withinCode);

	/**
	 * @Description 删除 yw_message_app
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwMessageApp(String withinCode);

	/**
	 * @Description 删除 yw_message_app_his
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwMessageAppHis(String withinCode);

	/**
	 * @Description 删除 yw_message_sms
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwMessageSms(String withinCode);

	/**
	 * @Description 删除 yw_message_sms_his
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwMessageSmsHis(String withinCode);

	/**
	 * @Description 删除 yw_message_wechat
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwMessageWechat(String withinCode);

	/**
	 * @Description 删除 yw_message_wechat_his
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwMessageWechatHis(String withinCode);

	/**
	 * @Description 删除 yw_oil_card
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwOilCard(String withinCode);

	/**
	 * @Description 删除 yw_order_detail
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwOrderDetail(String withinCode);

	/**
	 * @Description 删除 yw_order_detail_exec
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwOrderDetailExec(String withinCode);

	/**
	 * @Description 删除 yw_order_mostly
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwOrderMostly(String withinCode);

	/**
	 * @Description 删除 yw_order_mostly_local
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwOrderMostlyLocal(String withinCode);

	/**
	 * @Description 删除 yw_pay_billitem
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwPayBillItem(String withinCode);

	/**
	 * @Description 删除 yw_pay_fee
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwPayFee(String withinCode);

	/**
	 * @Description 删除 yw_pay_feeitem
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwPayFeeItem(String withinCode);

	/**
	 * @Description 删除 yw_plan_exec
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwPlanExec(String withinCode);

	/**
	 * @Description 删除 yw_plan_trans_recorder
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwPlanTransRecorder(String withinCode);

	/**
	 * @Description 删除 yw_position
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwPosition(String withinCode);

	/**
	 * @Description 删除 yw_position_backup
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwPositionBackUp(String withinCode);

	/**
	 * @Description 删除 yw_position_last
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwPositionLast(String withinCode);

	/**
	 * @Description 删除 yw_position_space
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwPostionSpace(String withinCode);

	/**
	 * @Description 删除 yw_position_space_backup
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwPositionSpaceBackup(String withinCode);

	/**
	 * @Description 删除 yw_rcv_billitem
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwRcvBillItem(String withinCode);

	/**
	 * @Description 删除 yw_rcv_fee
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwRcvFee(String withinCode);

	/**
	 * @Description 删除 yw_rcv_feeitem
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteYwFeeItem(String withinCode);

	/**
	 * @Description 删除 zd_business_state
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdBusinessState(String withinCode);

	/**
	 * @Description 删除 zd_contractor
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdContractor(String withinCode);

	/**
	 * @Description 删除 zd_currency
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdCurrency(String withinCode);

	/**
	 * @Description 删除 zd_cus
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdCus(String withinCode);

	/**
	 * @Description 删除 zd_cus_address
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdCusAddress(String withinCode);

	/**
	 * @Description 删除 zd_dictionary
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdDictionary(String withinCode);

	/**
	 * @Description 删除 zd_dictionary_data
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdDictionaryData(String withinCode);

	/**
	 * @Description 查询 zd_driver_relate 的code，用于去删除 zd_driver
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	List<String> queryZdDriverRelateCode(String withinCode);

	/**
	 * @Description 批量删除 zd_driver
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdDriver(List<String> list);

	/**
	 * @Description 删除 zd_driver_relate
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdDriverRelate(String withinCode);

	/**
	 * @Description 删除 zd_errorkind
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdErrorKind(String withinCode);

	/**
	 * @Description 删除 zd_fare
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdFare(String withinCode);

	/**
	 * @Description 删除 zd_fare_fangan
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdFareFangAn(String withinCode);

	/**
	 * @Description 删除 zd_fare_fangan_rule
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdFangAnRule(String withinCode);

	/**
	 * @Description 删除 zd_fare_fangan_rule_site
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdFareFangAnRuleSite(String withinCode);

	/**
	 * @Description 删除 zd_fare_qtyitem
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdFareQtyItem(String withinCode);

	/**
	 * @Description 删除 zd_location
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdLocation(String withinCode);

	/**
	 * @Description 查询 zd_truck_relate 的 truck_no
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	List<String> queryZdTruckRelateCode(String c);


	/**
	 * @Description 删除 zd_truck
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdTruck(List<String> list);

	/**
	 * @Description 删除 zd_truck_relate
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdTruckRelate(String withinCode);

	/**
	 * @Description 删除 zd_trucktype
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdTruckType(String withinCode);

	/**
	 * @Description 删除 zd_unit
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdUnit(String withinCode);

	/**
	 * @Description 删除 zd_ywlocation
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdYwLocation(String withinCode);

	/**
	 * @Description 删除 zd_ywlocation_base
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void deleteZdYwLocationBase(String withinCode);

	/**
	 * @Description 修改 sys_within
	 * @param
	 * @Author lao li
	 * @Date
	 * @return
	*/
	void updateSysWithin(SysWithinCodeVO VO);

	SysWithinCodeVO querySysWithin(String sn);

    void copySet(RegisterVO vo);
}

