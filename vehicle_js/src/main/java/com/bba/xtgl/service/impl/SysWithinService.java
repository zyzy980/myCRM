package com.bba.xtgl.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import com.bba.common.vo.PageVO;
import com.bba.util.JSONUtils;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandlers;
import com.bba.xtgl.vo.SysWithinCodeVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bba.common.vo.ResultVO;
import com.bba.util.SessionUtils;
import com.bba.xtgl.dao.ISysWithinDao;
import com.bba.xtgl.dao.IWsys_SetDao;
import com.bba.xtgl.service.api.ISysWithinService;
import com.bba.xtgl.vo.SysUserVO;
import com.bba.xtgl.vo.SysWithinVO;
import com.bba.xtgl.vo.Wsys_SetVO;
import sun.misc.BASE64Decoder;

@Service
@Transactional
public class SysWithinService implements ISysWithinService {

	@Resource
	private ISysWithinDao sysWithinDao;
	@Resource
	private IWsys_SetDao wsys_SetDao;

	@Override
	public SysWithinVO getDetailBySessionWithinCode(String withinCode) {
		 return sysWithinDao.getByWithinCode(withinCode);
	}

	@Override
	public ResultVO updateWithin(SysWithinVO sysWithinVO,Wsys_SetVO wsys_SetVO,HttpServletRequest request) {
		SysUserVO sysUserVO = SessionUtils.currentSession();
		List<SysWithinVO> list = new ArrayList<SysWithinVO>();
		List<Wsys_SetVO> setVOs = new ArrayList<Wsys_SetVO>();
		setVOs.add(wsys_SetVO);
		list.add(sysWithinVO);
		//修改
		String within_code = sysUserVO.getWithin_code();
		SysWithinVO data_SysWithinCodeVO = this.getDetailBySessionWithinCode(within_code);
		if(!org.apache.commons.lang.StringUtils.startsWith(sysWithinVO.getLogo_path(), data_SysWithinCodeVO.getLogo_path())){
			//改变图片
			String rootPath = SessionUtils.getServerAbsoluteUrl(request);
			System.out.println(rootPath);
			rootPath = rootPath + "/WMS_Image/within_code/";
			BASE64Decoder decoder = new BASE64Decoder();
			String fileData = sysWithinVO.getLogo_path().substring(sysWithinVO.getLogo_path().indexOf(",") + 1);
			byte[] bytes = new byte[0];
			try {
				bytes = decoder.decodeBuffer(fileData);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("上传公司图片失败", e);
			}
			String fileName = sysWithinVO.getCode() + ".jpg";
			File targetFile = new File(rootPath, "");
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			File targetFile2 = new File(rootPath, fileName);
			try {
				OutputStream out = new FileOutputStream(targetFile2);
				out.write(bytes);
				out.flush();
				out.close();
			} catch (IllegalStateException | IOException e) {
				throw new RuntimeException("上传公司图片失败", e);
			}
			sysWithinVO.setLogo_path("/WMS_Image/within_code/" + fileName);
		}else{
			sysWithinVO.setLogo_path(data_SysWithinCodeVO.getLogo_path());
		}
		Wsys_SetVO vo =wsys_SetDao.getByWithinWhcenter(wsys_SetVO);
		if(vo!=null) {
			if(!vo.getOutstock().equals(wsys_SetVO.getOutstock())) {
				int count = wsys_SetDao.isOutStock(wsys_SetVO);
				if(count>0) {
					return ResultVO.failResult("有出库单没有扣减库存,不能变更该属性");
				} 
			}
			wsys_SetDao.batchUpdate(setVOs);
		}
		sysWithinDao.batchUpdate(list);
		return ResultVO.successResult("修改成功");
	}
	@Override
	public PageVO findList(JqGridParamModel jqGridParamModel, String customSearchFilters) {
		String filters = JqGridSearchParamHandlers.processSql(customSearchFilters, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		List<SysWithinCodeVO> list = sysWithinDao.findList(jqGridParamModel);
		return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, list.size());
	}

	@Override
	public ResultVO delete(String withinCode) {
		ResultVO resultVO = ResultVO.successResult();
		// 删除 sys_authorith
		sysWithinDao.deleteSysAuthority(withinCode);
		//删除 sys_module
		sysWithinDao.deleteSysModule(withinCode);
		// 删除 sys_role
		sysWithinDao.deleteSysRole(withinCode);
		// 删除 sys_rolebuttons
		sysWithinDao.deleteSysRoleButtons(withinCode);
		// 删除 sys_sheetid
		sysWithinDao.deleteSysSheetId(withinCode);
		// 删除 sys_users
		sysWithinDao.deleteSysUsers(withinCode);
		// 删除 sys_users_token
		sysWithinDao.deleteSysUsersToken(withinCode);
		// 删除 sys_within
		sysWithinDao.deleteSysWithin(withinCode);
		// 删除 sys_within_set
		sysWithinDao.deleteSysWithinSet(withinCode);
		// 删除 yw_fare_get
		sysWithinDao.deleteYwFareGet(withinCode);
		// 删除 yw_fare_get_qtylist
		sysWithinDao.deleteYwFareGetQtyList(withinCode);
		// 删除 yw_fare_get_sign
		sysWithinDao.deleteYwFareGetSign(withinCode);
		// 删除 yw_fare_pay
		sysWithinDao.deleteYwFarePay(withinCode);
		// 删除 yw_fare_pay_qtylist
		sysWithinDao.deleteYwFarePayQtyList(withinCode);
		// 删除 yw_get
		sysWithinDao.deleteYwGet(withinCode);
		// 删除 yw_invoice
		sysWithinDao.deleteYwInvoice(withinCode);
		// 删除 yw_invoice_item
		sysWithinDao.deleteYwInvoiceBillItem(withinCode);
		// 删除 yw_invoice_pay
		sysWithinDao.deleteInvoicePay(withinCode);
		// 删除 yw_message_app
		sysWithinDao.deleteYwMessageApp(withinCode);
		// 删除 yw_message_app_his
		sysWithinDao.deleteYwMessageAppHis(withinCode);
		// 删除 yw_message_sms
		sysWithinDao.deleteYwMessageSms(withinCode);
		// 删除 yw_message_sms_his
		sysWithinDao.deleteYwMessageSmsHis(withinCode);
		// 删除 yw_message_wechat
		sysWithinDao.deleteYwMessageWechat(withinCode);
		// 删除 yw_message_wechat_his
		sysWithinDao.deleteYwMessageWechatHis(withinCode);
		// 删除 yw_oil_card
		sysWithinDao.deleteYwOilCard(withinCode);
		// 删除 yw_order_detail
		sysWithinDao.deleteYwOrderDetail(withinCode);
		// 删除 yw_order_detail_exec
		sysWithinDao.deleteYwOrderDetailExec(withinCode);
		// 删除 yw_order_mostly
		sysWithinDao.deleteYwOrderMostly(withinCode);
		// 删除 yw_order_mostly_local
		sysWithinDao.deleteYwOrderMostlyLocal(withinCode);
		// 删除 yw_pay_billitem
		sysWithinDao.deleteYwPayBillItem(withinCode);
		// 删除 yw_pay_fee
		sysWithinDao.deleteYwPayFee(withinCode);
		// 删除 yw_pay_feeitem
		sysWithinDao.deleteYwPayFeeItem(withinCode);
		// 删除 yw_plan_exec
		sysWithinDao.deleteYwPlanExec(withinCode);
		// 删除 yw_plan_trans_recorder
		sysWithinDao.deleteYwPlanTransRecorder(withinCode);
		// 删除 yw_position
		sysWithinDao.deleteYwPosition(withinCode);
		// 删除 yw_position_backup
		sysWithinDao.deleteYwPositionBackUp(withinCode);
		// 删除 yw_position_last
		sysWithinDao.deleteYwPositionLast(withinCode);
		// 删除 yw_position_space
		sysWithinDao.deleteYwPostionSpace(withinCode);
		// 删除 yw_position_space_backup
		sysWithinDao.deleteYwPositionSpaceBackup(withinCode);
		// 删除 yw_rcv_billitem
		sysWithinDao.deleteYwRcvBillItem(withinCode);
		// 删除 yw_rcv_fee
		sysWithinDao.deleteYwRcvFee(withinCode);
		// 删除 yw_rcv_feeitem
		sysWithinDao.deleteYwFeeItem(withinCode);
		// 删除 zd_business_state
		sysWithinDao.deleteZdBusinessState(withinCode);
		// 删除 zd_contractor
		sysWithinDao.deleteZdContractor(withinCode);
		// 删除 zd_currency
		sysWithinDao.deleteZdCurrency(withinCode);
		// 删除 zd_cus
		sysWithinDao.deleteZdCus(withinCode);
		// 删除 zd_cus_address
		sysWithinDao.deleteZdCusAddress(withinCode);
		// 删除 zd_dictionary
		sysWithinDao.deleteZdDictionary(withinCode);
		// 删除 zd_dictionary_data
		sysWithinDao.deleteZdDictionaryData(withinCode);

		// 删除 zd_driver 相关表
		// 先通过内码去 zd_driver_relate 查看zd_driver的code
		List<String> zdDriverCode = sysWithinDao.queryZdDriverRelateCode(withinCode);
		if(zdDriverCode.size() > 0 ) {
			// 批量删除 zd_driver
			sysWithinDao.deleteZdDriver(zdDriverCode);
		}
		// 删除 zd_driver_relate
		sysWithinDao.deleteZdDriverRelate(withinCode);
		// 删除 zd_errorkind
		sysWithinDao.deleteZdErrorKind(withinCode);
		// 删除 zd_fare
		sysWithinDao.deleteZdFare(withinCode);
		// 删除 zd_fare_fangan
		sysWithinDao.deleteZdFareFangAn(withinCode);
		// 删除 zd_fare_fangan_rule
		sysWithinDao.deleteZdFangAnRule(withinCode);
		// 删除 zd_fare_fangan_rule_site
		sysWithinDao.deleteZdFareFangAnRuleSite(withinCode);
		// 删除 zd_fare_qtyitem
		sysWithinDao.deleteZdFareQtyItem(withinCode);
		// 删除 zd_location
		sysWithinDao.deleteZdLocation(withinCode);
		// 删除 zd_truck 相关表
		// 通过内码去 zd_truck_relate
		List<String> truckList = sysWithinDao.queryZdTruckRelateCode(withinCode);
		if(truckList.size() > 0) {
			sysWithinDao.deleteZdTruck(truckList);
		}

		sysWithinDao.deleteZdTruckRelate(withinCode);

		// 删除 zd_trucktype
		sysWithinDao.deleteZdTruckType(withinCode);
		// 删除 zd_unit
		sysWithinDao.deleteZdUnit(withinCode);
		// 删除 zd_ywlocation
		sysWithinDao.deleteZdYwLocation(withinCode);
		// 删除 zd_ywlocation_base
		sysWithinDao.deleteZdYwLocationBase(withinCode);
		return resultVO;
	}

    @Override
    public ResultVO save(String jsonData) {
        ResultVO resultVO = ResultVO.successResult();
        SysUserVO sysUserVO = SessionUtils.currentSession();
		SysWithinCodeVO VO = JSONUtils.toJSONObject(SysWithinCodeVO.class, jsonData);
		VO.setUpdateBy(sysUserVO.getUpdate_by());
        sysWithinDao.updateSysWithin(VO);
        return resultVO;
    }

	@Override
	public ResultVO query(String sn) {
		ResultVO resultVO = ResultVO.successResult();
		SysWithinCodeVO VO = sysWithinDao.querySysWithin(sn);
		resultVO.setResultDataFull(VO);
		return VO == null ? ResultVO.failResult("暂无数据") : resultVO;
	}

}
